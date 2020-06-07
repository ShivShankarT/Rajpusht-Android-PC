package in.rajpusht.pc.ui.login;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

public class LoginViewModel extends BaseViewModel {

    public MutableLiveData<Event<Pair<Boolean, String>>> _navigateToHome = new MutableLiveData<>();

    public LoginViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
    // TODO: Implement the ViewModel

    public void logind(String email, String password) {
        _navigateToHome.setValue(new Event<>(new Pair<>(true, "success")));
    }

    public void login(String email, String password) {
        getCompositeDisposable().add(getDataManager().login(email, password)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui()).subscribe((jsonObjectApiResponse, throwable) -> {
                    if (throwable != null)
                        throwable.printStackTrace();
                    Log.i("ss", "login: "+jsonObjectApiResponse);
                    if (jsonObjectApiResponse.isStatus()) {
                        _navigateToHome.setValue(new Event<>(new Pair<>(true, jsonObjectApiResponse.getMessage())));
                    } else {
                        _navigateToHome.setValue(new Event<>(new Pair<>(false, jsonObjectApiResponse.getMessage())));
                    }

                }));
    }

}
