package in.rajpusht.pc.ui.login;

import android.app.Activity;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import in.rajpusht.pc.R;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

public class LoginViewModel extends BaseViewModel {

    public MutableLiveData<Event<Pair<Boolean, String>>> _navigateToHome = new MutableLiveData<>();

    public MutableLiveData<Event<Boolean>> progressDialog = new MutableLiveData<>();
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();
    public MutableLiveData<String> errorPassword = new MutableLiveData<>();

    public LoginViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
    // TODO: Implement the ViewModel

    public void logind(String email, String password) {
        _navigateToHome.setValue(new Event<>(new Pair<>(true, "success")));
    }

    public void login(String email, String password) {
        errorEmail.setValue(null);
        errorPassword.setValue(null);
        if (email.isEmpty()) {
            errorEmail.setValue(getDataManager().getString(R.string.enter_email));
        } else if (password.isEmpty()) {
            errorPassword.setValue(getDataManager().getString(R.string.invalid_password));
        } else if (!HUtil.isEmail(email)) {
            errorEmail.setValue(getDataManager().getString(R.string.error_email));
        } else if (password.length() < 8) {
            errorEmail.setValue(getDataManager().getString(R.string.invalid_password));
        } else {
            progressDialog.setValue(Event.data(true));
            getCompositeDisposable().add(getDataManager().login(email, password)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui()).subscribe((jsonObjectApiResponse, throwable) -> {
                        progressDialog.setValue(Event.data(false));
                        if (throwable != null)
                            throwable.printStackTrace();
                        if (jsonObjectApiResponse.isStatus()) {
                            _navigateToHome.setValue(new Event<>(new Pair<>(true, jsonObjectApiResponse.getMessage())));
                        } else {
                            _navigateToHome.setValue(new Event<>(new Pair<>(false, jsonObjectApiResponse.getMessage())));
                        }

                    }));
        }
    }

}
