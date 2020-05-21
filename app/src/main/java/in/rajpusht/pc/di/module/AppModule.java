package in.rajpusht.pc.di.module;


import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import in.rajpusht.pc.data.local.AppDatabase;
import in.rajpusht.pc.di.DatabaseInfo;
import in.rajpusht.pc.utils.rx.AppSchedulerProvider;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

@Module
public class AppModule {


    @Provides
    SchedulerProvider appSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @DatabaseInfo
    String dbName() {
        return "pc.db";
    }

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(@DatabaseInfo String dbName, Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, dbName).fallbackToDestructiveMigration()
                .build();
    }


}
