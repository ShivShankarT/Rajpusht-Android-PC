package in.rajpusht.pc.di.module;


import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import in.rajpusht.pc.data.local.db.AppDatabase;
import in.rajpusht.pc.data.remote.ApiConstants;
import in.rajpusht.pc.data.remote.ApiService;
import in.rajpusht.pc.data.remote.RequestInterceptor;
import in.rajpusht.pc.di.DatabaseInfo;
import in.rajpusht.pc.di.PreferenceInfo;
import in.rajpusht.pc.utils.rx.AppSchedulerProvider;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {


    @Provides
    SchedulerProvider appSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @DatabaseInfo
    String dbName() {
        return "pc1.db";
    }


    @Provides
    @PreferenceInfo
    String prefName() {
        return "app-ref.db";
    }

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(@DatabaseInfo String dbName, Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, dbName).
                allowMainThreadQueries().
                fallbackToDestructiveMigration()//todo remove main , nig
                .build();
    }


    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.writeTimeout(ApiConstants.WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.addInterceptor(new RequestInterceptor());
        okHttpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        return okHttpClient.build();
    }


    @Provides
    @Singleton
    ApiService provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(ApiService.class);
    }


}
