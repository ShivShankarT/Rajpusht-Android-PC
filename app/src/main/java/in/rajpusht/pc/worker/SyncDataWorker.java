package in.rajpusht.pc.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import com.google.gson.JsonObject;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Provider;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.utils.AppDateTimeUtils;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class SyncDataWorker extends RxWorker {

    private final String TAG = getClass().getSimpleName();
    //dagger (what we want to Inject into worker) U CAN ADD WHATEVER NEEDED
    private DataRepository dataRepository;

    private SyncDataWorker(@NonNull Context context,
                           @NonNull WorkerParameters workerParams,
                           DataRepository dataRepository) {
        super(context, workerParams);
        this.dataRepository = dataRepository;
    }


    private Single<Result> syncData() {
        Timber.i("Auto sync init");
        DataRepository dataManager = dataRepository;
        dataManager.putPrefString("" + AppDateTimeUtils.convertLocalDateTime(new Date()), "---");
        return dataManager.uploadDataToServer()
                .flatMap((Function<ApiResponse<JsonObject>, SingleSource<ApiResponse<JsonObject>>>) jsonObjectApiResponse -> {
                    Log.i(TAG, "syncData: " + jsonObjectApiResponse.toString());
                    if (jsonObjectApiResponse.isStatus()) {
                        ApiResponse<JsonObject> completionValue = new ApiResponse<>();
                        completionValue.setStatus(true);
                        return dataManager.profileAndBulkDownload().toSingleDefault(completionValue);
                    } else
                        return Single.just(jsonObjectApiResponse);
                }).map(jsonObjectApiResponse -> {
                    if (jsonObjectApiResponse.getInternalErrorCode() == ApiResponse.NO_DATA_SYNC) {
                        Timber.i("Auto Sync no data to sync");
                        return Result.success();
                    } else if (jsonObjectApiResponse.isInternalError()) {
                        Timber.i("Auto Sync Failed; no nw ");
                        return Result.retry();
                    } else if (jsonObjectApiResponse.isStatus()) {
                        Timber.i("Auto Success");
                        return Result.success();
                    } else {
                        Timber.d("Auto Sync Failed" + jsonObjectApiResponse.getMessage());
                        return Result.failure();
                    }
                });
    }

    @NonNull
    @Override
    public Single<Result> createWork() {

        return syncData();
    }


    public static class Factory implements ChildWorkerFactory {

        private final Provider<DataRepository> dataRepositoryProvider;

        @Inject
        public Factory(Provider<DataRepository> modelProvider) {
            this.dataRepositoryProvider = modelProvider;
        }

        @Override
        public ListenableWorker create(Context context, WorkerParameters workerParameters) {
            return new SyncDataWorker(context,
                    workerParameters,
                    dataRepositoryProvider.get());
        }
    }
}