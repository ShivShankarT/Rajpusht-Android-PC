package in.rajpusht.pc.data.remote;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.model.ProfileDetail;
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

    public Single<ApiResponse<JsonElement>> verifyOtp(String otp) {
        return apiService.verifyOtp(otp);
    }


    Single<ApiResponse<JsonObject>> changePassword(String oldPassword,
                                                   String newPassword) {
        return apiService.changePassword(oldPassword, newPassword);
    }


    Single<ApiResponse<JsonObject>> forgotPassword(String oldPassword) {
        return apiService.forgotPassword(oldPassword);
    }


    Single<ApiResponse<JsonObject>> setPassword(String resetOtp, String newPassword) {
        return apiService.setPassword(resetOtp, newPassword);
    }


    Single<ApiResponse<JsonObject>> resendOtp() {
        return apiService.resendOtp();
    }


    public Single<ApiResponse<ProfileDetail>> profileDetail() {
        return apiService.profileDetail();
    }


    Single<ApiResponse<JsonObject>> logout() {
        return apiService.logout();
    }


    public Single<ApiResponse<JsonObject>> bulkDownload() {
        return apiService.bulkDownload();
    }

    public Single<ApiResponse<JsonObject>> bulkUpload(JsonArray jsonArray) {
        return apiService.bulkUpload(jsonArray);
    }


}