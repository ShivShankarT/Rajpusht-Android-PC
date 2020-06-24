package in.rajpusht.pc;

import android.content.Context;

import androidx.work.Configuration;
import androidx.work.WorkManager;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.DispatchingAndroidInjector;
import in.rajpusht.pc.di.components.AppComponent;
import in.rajpusht.pc.di.components.DaggerAppComponent;
import in.rajpusht.pc.utils.ContextWrapper;
import in.rajpusht.pc.worker.SimpleWorkerFactory;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class RajpushtApp extends DaggerApplication {
    @Inject
    DispatchingAndroidInjector<RajpushtApp> rajpushtAppDispatchingAndroidInjector;
    @Inject
    SimpleWorkerFactory simpleWorkerFactory;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate() {
       /*  AppComponent applicationInjector = DaggerAppComponent.builder()
                .application(this)
                .build();*/
        super.onCreate();
        configureWorkManager();

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("---------------------------");
                throwable.printStackTrace();
            }
        });
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    private void configureWorkManager() {
        Configuration config = new Configuration.Builder()
                .setWorkerFactory(simpleWorkerFactory)
                .build();

        WorkManager.initialize(this, config);
    }

}


