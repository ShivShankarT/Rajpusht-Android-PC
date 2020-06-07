package in.rajpusht.pc.ui.otp;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class OtpViewModel extends BaseViewModel {
    public MutableLiveData<Event<Pair<Boolean, String>>> _navigateToHome = new MutableLiveData<>();

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
        getCompositeDisposable().add(getDataManager().profileAndBulkDownload()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui()).subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getDataManager().setLogin(true);
                        _navigateToHome.setValue(new Event<>(new Pair<>(true, "")));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        _navigateToHome.setValue(new Event<>(new Pair<>(false, "sync failed")));
                    }
                }));

    }
}
