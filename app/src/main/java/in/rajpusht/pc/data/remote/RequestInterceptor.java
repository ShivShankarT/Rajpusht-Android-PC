package in.rajpusht.pc.data.remote;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl originalHttpUrl = originalRequest.url();

      /* originalHttpUrl.newBuilder()
                .addQueryParameter("app vers", "v1")
                .build();*/

        //todo add sharddPref
        Request request = originalRequest.newBuilder().addHeader("token", "zzz").build();
        return chain.proceed(request);
    }
}