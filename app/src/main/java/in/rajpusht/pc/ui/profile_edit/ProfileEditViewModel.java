package in.rajpusht.pc.ui.profile_edit;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import in.rajpusht.pc.R;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

public class ProfileEditViewModel extends BaseViewModel {

    public static final int FNAME_ERROR = 1;
    public static final int LNAME_ERROR = 2;
    public static final int MOBILE_ERROR = 3;
    public static final int SUCCESS = 4;
    public static final int ERROR = 5;

    public MutableLiveData<Event<Pair<Integer, String>>> statusLiveData = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> progressDialog = new MutableLiveData<>();


    public ProfileEditViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void updateProfile(String fname, String lastname, String mobile) {

        if (fname.length() < 4) {
            statusLiveData.setValue(Event.data(new Pair<>(FNAME_ERROR, getDataManager().getString(R.string.error_firstname))));
        } else if (lastname.length() < 4) {
            statusLiveData.setValue(Event.data(new Pair<>(LNAME_ERROR, getDataManager().getString(R.string.error_lastname))));
        } else if (mobile.length() != 10) {
            statusLiveData.setValue(Event.data(new Pair<>(MOBILE_ERROR, getDataManager().getString(R.string.error_phoneno))));
        } else {
            progressDialog.setValue(Event.data(true));
            getCompositeDisposable().add(getDataManager().profileUpdate(fname, lastname, mobile)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui()).subscribe((jsonObjectApiResponse, throwable) -> {
                        progressDialog.setValue(Event.data(false));
                        if (throwable != null)
                            throwable.printStackTrace();
                        if (jsonObjectApiResponse.isStatus()) {
                            statusLiveData.setValue(new Event<>(new Pair<>(SUCCESS, jsonObjectApiResponse.getMessage())));
                        } else {
                            statusLiveData.setValue(new Event<>(new Pair<>(ERROR, jsonObjectApiResponse.getMessage())));
                        }
                    }));
        }
    }
}
