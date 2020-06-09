package in.rajpusht.pc;

import android.content.Context;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.DispatchingAndroidInjector;
import in.rajpusht.pc.di.components.AppComponent;
import in.rajpusht.pc.di.components.DaggerAppComponent;
import in.rajpusht.pc.utils.ContextWrapper;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class RajpushtApp extends DaggerApplication {
    @Inject
    DispatchingAndroidInjector<RajpushtApp> rajpushtAppDispatchingAndroidInjector;

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

}


