package in.rajpusht.pc.ui.forgot_password;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.BiConsumer;

public class ForgotPasswordViewModel extends BaseViewModel {

    public static final int ERROR = 0;
    public static final int ERROR_EMAIL = 1;
    public static final int ERROR_NEW_PASSWORD = -1;
    public static final int ERROR_CONFIRM_PASSWORD = 2;
    public static final int ERROR_OTP = 3;
    public static final int OPT_SEND = 4;
    public static final int PASSWORD_CHANGED = 5;

    public MutableLiveData<Event<Boolean>> progressDialog = new MutableLiveData<>();
    public MutableLiveData<Event<Pair<Integer, String>>> statusLiveData = new MutableLiveData<>();


    public ForgotPasswordViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void setOtp(String email) {

        if (email.isEmpty()) {
            statusLiveData.setValue(Event.data(new Pair<>(ERROR_EMAIL, "Please enter your email")));
        } else if (!HUtil.isEmail(email)) {
            statusLiveData.setValue(Event.data(new Pair<>(ERROR_EMAIL, "Invalid email")));
        } else {
            progressDialog.setValue(Event.data(true));
            getCompositeDisposable().add(getDataManager().forgotPassword(email)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe((jsonObjectApiResponse, throwable) -> {
                        progressDialog.setValue(Event.data(false));
                        if (jsonObjectApiResponse.isStatus()) {
                            statusLiveData.setValue(Event.data(new Pair<>(OPT_SEND, "OPT Send Successfully")));
                        } else if (jsonObjectApiResponse.isInternalError()) {
                            statusLiveData.setValue(Event.data(new Pair<>(ERROR, jsonObjectApiResponse.getMessage())));
                        } else {
                            statusLiveData.setValue(Event.data(new Pair<>(ERROR_EMAIL, jsonObjectApiResponse.getMessage())));
                        }

                    }));

        }


    }


    public void setPassword(String otp, String newPassword, String confirmPassword) {

        if (otp.isEmpty()) {
            statusLiveData.setValue(Event.data(new Pair<>(ERROR_OTP, "Please enter OTP")));
        } else if (otp.length() < 4) {
            statusLiveData.setValue(Event.data(new Pair<>(ERROR_OTP, "Invalid OTP")));
        } else if (newPassword.isEmpty()) {
            statusLiveData.setValue(Event.data(new Pair<>(ERROR_NEW_PASSWORD, "Please new password")));
        } else if (newPassword.length() < 8) {
            statusLiveData.setValue(Event.data(new Pair<>(ERROR_NEW_PASSWORD, "Invalid new password")));
        } else if (confirmPassword.isEmpty()) {
            statusLiveData.setValue(Event.data(new Pair<>(ERROR_CONFIRM_PASSWORD, "Please new password")));
        } else if (confirmPassword.length() < 8) {
            statusLiveData.setValue(Event.data(new Pair<>(ERROR_CONFIRM_PASSWORD, "Invalid new password")));
        } else if (!confirmPassword.equals(newPassword)) {
            statusLiveData.setValue(Event.data(new Pair<>(ERROR_CONFIRM_PASSWORD, "Confirm Password is not same")));
        } else {
            progressDialog.setValue(Event.data(true));
            getCompositeDisposable().add(getDataManager().setPassword(otp, newPassword)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe((jsonObjectApiResponse, throwable) -> {
                        progressDialog.setValue(Event.data(false));
                        if (jsonObjectApiResponse.isStatus()) {
                            statusLiveData.setValue(Event.data(new Pair<>(PASSWORD_CHANGED, jsonObjectApiResponse.getMessage())));
                        } else if (jsonObjectApiResponse.isInternalError()) {
                            statusLiveData.setValue(Event.data(new Pair<>(ERROR, jsonObjectApiResponse.getMessage())));
                        } else {
                            statusLiveData.setValue(Event.data(new Pair<>(ERROR_OTP, jsonObjectApiResponse.getMessage())));
                        }

                    }));

        }

    }
}
