package in.rajpusht.pc.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import javax.inject.Inject;
import javax.inject.Provider;

import in.rajpusht.pc.data.DataRepository;

public class SimpleWorker extends androidx.work.Worker {

    private final String TAG = getClass().getSimpleName();
    //dagger (what we want to Inject into worker) U CAN ADD WHATEVER NEEDED
    private DataRepository dataRepository;
    //not dagger (just some fields)
    private String someField;

    private SimpleWorker(@NonNull Context context,
                         @NonNull WorkerParameters workerParams,
                         DataRepository dataRepository) {
        super(context, workerParams);
        this.dataRepository = dataRepository;

        someField = "just some work";
    }


    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Worker starts");
        Log.d(TAG, dataRepository.getClass().getSimpleName() + " doing some work");
        Log.d(TAG, "Job done!");
        return ListenableWorker.Result.success();
    }


    public static class Factory implements ChildWorkerFactory {

        private final Provider<DataRepository> dataRepositoryProvider;

        @Inject
        public Factory(Provider<DataRepository> modelProvider) {
            this.dataRepositoryProvider = modelProvider;
        }

        @Override
        public ListenableWorker create(Context context, WorkerParameters workerParameters) {
            return new SimpleWorker(context,
                    workerParameters,
                    dataRepositoryProvider.get());
        }
    }
}