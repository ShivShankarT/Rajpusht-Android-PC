package in.rajpusht.pc.data.remote;

import com.google.gson.JsonObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.rajpusht.pc.model.ApiResponse;
import io.reactivex.Single;

@Singleton
public class AppApiHelper {

    ApiService apiService;

    @Inject
    public AppApiHelper(ApiService apiService) {
        this.apiService = apiService;
    }


    public Single<ApiResponse<JsonObject>> login(String email, String password) {
        return apiService.login(email, password);
    }

    public Single<ApiResponse<JsonObject>> verifyOtp(String otp) {
        return apiService.verifyOtp(otp);
    }

    public Single<ApiResponse<JsonObject>> assignedLocation() {
        return apiService.assignedLocation();
    }


}