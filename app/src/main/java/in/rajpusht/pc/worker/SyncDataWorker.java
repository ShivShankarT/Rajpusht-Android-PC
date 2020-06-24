package in.rajpusht.pc.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SyncDataWorker extends Worker {


    public SyncDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        uploadImages();

        return Result.success();
    }

    private void uploadImages() {

    }
}
