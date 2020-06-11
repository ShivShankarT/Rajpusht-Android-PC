package in.rajpusht.pc.ui.change_password;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

public class ChangePasswordViewModel extends BaseViewModel {

    public MutableLiveData<Event<Pair<Integer, String>>> statusLiveData = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> progressDialog = new MutableLiveData<>();

    public ChangePasswordViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {

        if (oldPassword.length() < 8) {
            statusLiveData.setValue(Event.data(new Pair<>(1, "Invalid Password")));
        } else if (newPassword.length() < 8) {
            statusLiveData.setValue(Event.data(new Pair<>(2, "Invalid Password")));
        } else if (confirmPassword.length() < 8) {
            statusLiveData.setValue(Event.data(new Pair<>(3, "Invalid Password")));
        } else if (!newPassword.equalsIgnoreCase(confirmPassword)) {
            statusLiveData.setValue(Event.data(new Pair<>(3, "Confirm Password Incorrect")));
        } else {
            progressDialog.setValue(Event.data(true));
            getCompositeDisposable().add(getDataManager().changePassword(oldPassword, newPassword)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui()).subscribe((jsonObjectApiResponse, throwable) -> {
                        progressDialog.setValue(Event.data(false));
                        if (throwable != null)
                            throwable.printStackTrace();
                        if (jsonObjectApiResponse.isStatus()) {
                            statusLiveData.setValue(new Event<>(new Pair<>(4, jsonObjectApiResponse.getMessage())));
                        } else {
                            statusLiveData.setValue(new Event<>(new Pair<>(5, jsonObjectApiResponse.getMessage())));
                        }

                    }));
        }
    }


}
