package in.rajpusht.pc.di.builder;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import in.rajpusht.pc.di.WorkerKey;
import in.rajpusht.pc.worker.ChildWorkerFactory;
import in.rajpusht.pc.worker.SyncDataWorker;

@Module
public interface WorkerBindingModule {
    @Binds
    @IntoMap
    @WorkerKey(SyncDataWorker.class)
    ChildWorkerFactory bindWorker(SyncDataWorker.Factory factory);
}