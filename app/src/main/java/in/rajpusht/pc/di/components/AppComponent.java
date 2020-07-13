package in.rajpusht.pc.di.components;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import in.rajpusht.pc.RajpushtApp;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.di.builder.ActivityBuilderModule;
import in.rajpusht.pc.di.builder.WorkerBindingModule;
import in.rajpusht.pc.di.module.AppModule;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import in.rajpusht.pc.utils.timber.FileLoggingTree;

@Singleton
@Component(modules = {
        AppModule.class,
        AndroidInjectionModule.class,
        ActivityBuilderModule.class, WorkerBindingModule.class})
public interface AppComponent extends AndroidInjector<RajpushtApp> {

    DataRepository getDataRepository();

    SchedulerProvider getSchedulerProvider();

    FileLoggingTree getFileLoggingTree();


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