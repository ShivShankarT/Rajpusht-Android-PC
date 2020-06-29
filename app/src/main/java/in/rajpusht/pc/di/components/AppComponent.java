package in.rajpusht.pc.di.components;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import in.rajpusht.pc.RajpushtApp;
import in.rajpusht.pc.di.builder.ActivityBuilderModule;
import in.rajpusht.pc.di.builder.WorkerBindingModule;
import in.rajpusht.pc.di.module.AppModule;

@Singleton
@Component(modules = {
        AppModule.class,
        AndroidInjectionModule.class,
        ActivityBuilderModule.class, WorkerBindingModule.class})
public interface AppComponent extends AndroidInjector<RajpushtApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

   /* @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<RajpushtApp> {}*/
   /* @Component.Factory
    abstract class Factory {

        public abstract AppComponent application(@BindsInstance Application application);
    }*/
   /* @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }*/
}