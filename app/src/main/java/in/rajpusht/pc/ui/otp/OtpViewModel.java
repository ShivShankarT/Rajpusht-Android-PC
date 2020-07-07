package in.rajpusht.pc.ui.otp;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.db.entity.InstitutionPlaceEntity;
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.Consumer;

public class OtpViewModel extends BaseViewModel {
    public MutableLiveData<Event<Pair<Boolean, String>>> _navigateToHome = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> progressDialog = new MutableLiveData<>();

    public OtpViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
    // TODO: Implement the ViewModel

    public void verifyOtp(String opt) {
        getCompositeDisposable().add(getDataManager().verifyOtp(opt)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui()).subscribe((jsonObjectApiResponse, throwable) -> {
                    if (jsonObjectApiResponse.isStatus()) {
                        download();
                        // _navigateToHome.setValue(new Event<>(new Pair<>(true, jsonObjectApiResponse.getMessage())));
                    } else {
                        _navigateToHome.setValue(new Event<>(new Pair<>(false, jsonObjectApiResponse.getMessage())));
                    }
                }));
    }

    public void download() {
        progressDialog.setValue(Event.data(true));
        getCompositeDisposable().add(getDataManager().profileAndBulkDownload().andThen(getDataManager().fetchInsertFacilityData())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<ApiResponse<List<InstitutionPlaceEntity>>>() {
                    @Override
                    public void accept(ApiResponse<List<InstitutionPlaceEntity>> listApiResponse) throws Exception {
                        getDataManager().setLogin(true);
                        _navigateToHome.setValue(new Event<>(new Pair<>(true, "")));
                        progressDialog.setValue(Event.data(false));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        _navigateToHome.setValue(new Event<>(new Pair<>(false, "sync failed")));
                        progressDialog.setValue(Event.data(false));
                    }
                }));

    }
}
