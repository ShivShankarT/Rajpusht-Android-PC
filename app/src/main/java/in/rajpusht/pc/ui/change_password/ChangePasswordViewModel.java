package in.rajpusht.pc.ui.change_password;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import in.rajpusht.pc.R;
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
            statusLiveData.setValue(Event.data(new Pair<>(1, getDataManager().getString(R.string.Invalid_password))));
        } else if (newPassword.length() < 8) {
            statusLiveData.setValue(Event.data(new Pair<>(2,getDataManager().getString(R.string.Invalid_password))));
        } else if (confirmPassword.length() < 8) {
            statusLiveData.setValue(Event.data(new Pair<>(3,getDataManager().getString(R.string.Invalid_password))));
        } else if (!newPassword.equals(confirmPassword)) {
            statusLiveData.setValue(Event.data(new Pair<>(3, getDataManager().getString(R.string.confirm_password_incorrect))));
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
