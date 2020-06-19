package in.rajpusht.pc.di.module;


import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import in.rajpusht.pc.data.local.db.AppDatabase;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
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
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
        return "pc-test1.1.3.db";
    }


    @Provides
    @PreferenceInfo
    String prefName() {
        return AppPreferencesHelper.PREF_NAME;
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
    OkHttpClient provideOkHttpClient(AppPreferencesHelper appPreferencesHelper) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.writeTimeout(ApiConstants.WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.addInterceptor(new RequestInterceptor(appPreferencesHelper));
        okHttpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        return okHttpClient.build();
    }

    @Provides
    @Singleton
    Gson gson(){
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        return gson;
    }


    @Provides
    @Singleton
    ApiService provideRetrofit(OkHttpClient okHttpClient,Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(ApiService.class);
    }


}
