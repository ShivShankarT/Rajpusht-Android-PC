package in.rajpusht.pc.ui.home;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;

import in.rajpusht.pc.R;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class HomeViewModel extends BaseViewModel {

    public MutableLiveData<Event<Pair<Boolean, String>>> progressLive = new MutableLiveData<>();

    public HomeViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void syncData(boolean isLogOut) {
        progressLive.postValue(Event.data(new Pair<>(true, "")));
        getCompositeDisposable().add(getDataManager().uploadDataToServer()
                .flatMap((Function<ApiResponse<JsonObject>, SingleSource<ApiResponse<JsonObject>>>) jsonObjectApiResponse -> {
                    if (jsonObjectApiResponse.isStatus() && !isLogOut) {
                        ApiResponse<JsonObject> completionValue = new ApiResponse<>();
                        completionValue.setStatus(true);
                        return getDataManager().profileAndBulkDownload().toSingleDefault(completionValue);
                    } else
                        return Single.just(jsonObjectApiResponse);
                })
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui()).subscribe((jsonObjectApiResponse, throwable) -> {
                    if (throwable != null)
                        throwable.printStackTrace();
                    if (jsonObjectApiResponse != null) {

                        if (isLogOut && (jsonObjectApiResponse.isStatus() || jsonObjectApiResponse.getInternalErrorCode() == ApiResponse.NO_DATA_SYNC))
                            getDataManager().logout();

                        if (jsonObjectApiResponse.isStatus()) {
                            progressLive.postValue(Event.data(new Pair<>(false, getString(R.string.sync_successfully))));
                        } else {
                            progressLive.postValue(Event.data(new Pair<>(false, jsonObjectApiResponse.getMessage())));
                        }
                    } else {
                        progressLive.postValue(Event.data(new Pair<>(false, "")));
                    }

                }));
    }


}
