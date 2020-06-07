package in.rajpusht.pc.data.remote;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.IOException;

import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    private final AppPreferencesHelper appPreferencesHelper;

    public RequestInterceptor(AppPreferencesHelper appPreferencesHelper) {
        this.appPreferencesHelper = appPreferencesHelper;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl originalHttpUrl = originalRequest.url();

      /* originalHttpUrl.newBuilder()
                .addQueryParameter("app vers", "v1")
                .build();*/

        //todo add sharddPref
        Request.Builder builder = originalRequest.newBuilder();
        String accessToken = appPreferencesHelper.getAccessToken();
        if (!TextUtils.isEmpty(accessToken))
            builder.addHeader("Authorization", "Bearer " + accessToken);
        Request request = builder.build();
        return chain.proceed(request);
    }
}