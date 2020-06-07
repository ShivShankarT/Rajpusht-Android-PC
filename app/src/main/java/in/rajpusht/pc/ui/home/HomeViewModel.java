package in.rajpusht.pc.ui.home;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.BiConsumer;

public class HomeViewModel extends BaseViewModel {

    public MutableLiveData<Event<Pair<Boolean, String>>> progressLive = new MutableLiveData<>();

    public HomeViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void syncData() {
        progressLive.postValue(Event.data(new Pair<>(true, "")));
        getCompositeDisposable().add(getDataManager().uploadDataToServer()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui()).subscribe(new BiConsumer<ApiResponse<JsonObject>, Throwable>() {
                    @Override
                    public void accept(ApiResponse<JsonObject> jsonObjectApiResponse, Throwable throwable) throws Exception {
                        if (throwable != null)
                            throwable.printStackTrace();

                        if (jsonObjectApiResponse != null) {
                            if (jsonObjectApiResponse.isStatus()) {
                                getDataManager().logout();
                                progressLive.postValue(Event.data(new Pair<>(false, "")));
                            } else {
                                progressLive.postValue(Event.data(new Pair<>(false, jsonObjectApiResponse.getMessage())));
                            }
                        } else {
                            progressLive.postValue(Event.data(new Pair<>(false, "")));
                        }

                    }
                }));
    }

    ;
}
