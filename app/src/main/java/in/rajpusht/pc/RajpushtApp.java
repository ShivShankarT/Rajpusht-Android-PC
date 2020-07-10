package in.rajpusht.pc;

import android.content.Context;

import androidx.work.Configuration;
import androidx.work.WorkManager;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.di.components.AppComponent;
import in.rajpusht.pc.di.components.DaggerAppComponent;
import in.rajpusht.pc.utils.ContextWrapper;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import in.rajpusht.pc.utils.timber.DebugLogTree;
import in.rajpusht.pc.utils.timber.FileLoggingTree;
import in.rajpusht.pc.utils.timber.ReleaseTree;
import in.rajpusht.pc.worker.SimpleWorkerFactory;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import timber.log.Timber;

public class RajpushtApp extends DaggerApplication {

    @Inject
    SimpleWorkerFactory simpleWorkerFactory;

    private AppComponent applicationInjector = DaggerAppComponent.builder().application(this).build();



    @Override
    public void onCreate() {


       /*  AppComponent applicationInjector = DaggerAppComponent.builder()
                .application(this)
                .build();*/
        super.onCreate();
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("---------------------------");
                throwable.printStackTrace();
            }
        });
        configureWorkManager();
        if (BuildConfig.DEBUG)
            Timber.plant(new DebugLogTree());
        else {
            Timber.plant(new ReleaseTree());
        }
        Timber.plant(new FileLoggingTree());
        ContextWrapper.setLocale(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        applicationInjector.inject(this);
        return applicationInjector;
    }

    private void configureWorkManager() {
        Configuration config = new Configuration.Builder()
                .setWorkerFactory(simpleWorkerFactory)
                .build();

        WorkManager.initialize(this, config);
    }

    public DataRepository getDataRepository() {
        return applicationInjector.getDataRepository();
    }


    public SchedulerProvider getSchedulerProvider() {
        return applicationInjector.getSchedulerProvider();
    }
}


