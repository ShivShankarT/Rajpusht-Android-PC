package in.rajpusht.pc.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.SingleLiveEvent;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

public class LoginViewModel extends BaseViewModel {

    public MutableLiveData<Event<Boolean>> _navigateToHome = new MutableLiveData<>();

    public LoginViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
    // TODO: Implement the ViewModel

    public void login(String email, String password) {
        _navigateToHome.setValue(new Event<>(true));
    }

}
