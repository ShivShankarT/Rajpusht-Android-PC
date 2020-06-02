package in.rajpusht.pc.ui.login;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.BiConsumer;

public class LoginViewModel extends BaseViewModel {

    public MutableLiveData<Event<Pair<Boolean, String>>> _navigateToHome = new MutableLiveData<>();

    public LoginViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
    // TODO: Implement the ViewModel

    public void login(String email, String password) {
        _navigateToHome.setValue(new Event<>(new Pair<>(true, "success")));
    }

    private void loginAppi(String email, String password) {
        getCompositeDisposable().add(getDataManager().login(email, password)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui()).subscribe(new BiConsumer<ApiResponse<JsonObject>, Throwable>() {
                    @Override
                    public void accept(ApiResponse<JsonObject> jsonObjectApiResponse, Throwable throwable) throws Exception {
                        if (jsonObjectApiResponse.isStatus()) {
                            _navigateToHome.setValue(new Event<>(new Pair<>(true, jsonObjectApiResponse.getMessage())));
                        } else {
                            _navigateToHome.setValue(new Event<>(new Pair<>(true, jsonObjectApiResponse.getMessage())));
                        }

                    }
                }));
    }

}
