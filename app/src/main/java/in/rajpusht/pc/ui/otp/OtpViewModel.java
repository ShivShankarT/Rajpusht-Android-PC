package in.rajpusht.pc.ui.otp;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import in.rajpusht.pc.R;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.db.entity.InstitutionPlaceEntity;
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.Consumer;

public class OtpViewModel extends BaseViewModel {

    public static final int LAUNCH_HOME = 1;
    public static final int LIMIT_REACHED = 2;
    public static final int ERROR = 3;


    public MutableLiveData<Event<Pair<Integer, String>>> _navigateToHome = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> progressDialog = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> resendOtp = new MutableLiveData<>();

    public OtpViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
    // TODO: Implement the ViewModel

    public void verifyOtp(String opt) {
        if (opt.length() != 6) {
            _navigateToHome.setValue(new Event<>(new Pair<>(ERROR, getDataManager().getString(R.string.invalid_otp))));
            return;
        }
        progressDialog.setValue(Event.data(true));
        getCompositeDisposable().add(getDataManager().verifyOtp(opt)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui()).subscribe((jsonObjectApiResponse, throwable) -> {
                    if (jsonObjectApiResponse.isStatus()) {
                        download();
                    } else {

                        if (jsonObjectApiResponse.getData()!=null&&
                                jsonObjectApiResponse.getData().getAsJsonObject().get("isLimitReached") != null &&
                                jsonObjectApiResponse.getData().getAsJsonObject().get("isLimitReached").getAsBoolean()) {
                            _navigateToHome.setValue(new Event<>(new Pair<>(LIMIT_REACHED, jsonObjectApiResponse.getMessage())));
                        } else
                            _navigateToHome.setValue(new Event<>(new Pair<>(ERROR, jsonObjectApiResponse.getMessage())));
                    }
                }));
    }

    public void resend() {
        progressDialog.setValue(Event.data(true));
        getCompositeDisposable().add(getDataManager().resendOtp()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui()).subscribe((jsonObjectApiResponse, throwable) -> {
                    resendOtp.setValue(Event.data(true));
                    if (jsonObjectApiResponse.isStatus()) {

                    } else {
                        _navigateToHome.setValue(new Event<>(new Pair<>(ERROR, jsonObjectApiResponse.getMessage())));
                    }
                    progressDialog.setValue(Event.data(false));

                }));
    }

    public void download() {
        getCompositeDisposable().add(getDataManager().profileAndBulkDownload().andThen(getDataManager().fetchInsertFacilityData())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<ApiResponse<List<InstitutionPlaceEntity>>>() {
                    @Override
                    public void accept(ApiResponse<List<InstitutionPlaceEntity>> listApiResponse) throws Exception {
                        getDataManager().setLogin(true);
                        _navigateToHome.setValue(new Event<>(new Pair<>(LAUNCH_HOME, "")));
                        progressDialog.setValue(Event.data(false));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        _navigateToHome.setValue(new Event<>(new Pair<>(ERROR, "sync failed")));
                        progressDialog.setValue(Event.data(false));
                    }
                }));

    }
}
