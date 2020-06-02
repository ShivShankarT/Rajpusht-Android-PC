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


    Single<ApiResponse<JsonObject>> changePassword(String oldPassword,
                                                   String newPassword) {
        return apiService.changePassword(oldPassword, newPassword);
    }

    ;


    Single<ApiResponse<JsonObject>> forgotPassword(String oldPassword) {
        return apiService.forgotPassword(oldPassword);
    }

    ;


    Single<ApiResponse<JsonObject>> setPassword(String resetOtp, String newPassword) {
        return apiService.setPassword(resetOtp, newPassword);
    }

    ;


    Single<ApiResponse<JsonObject>> resendOtp() {
        return apiService.resendOtp();
    }

    ;


    Single<ApiResponse<JsonObject>> profileDetail() {
        return apiService.profileDetail();
    }

    ;


    Single<ApiResponse<JsonObject>> logout() {
        return apiService.logout();
    }

    ;


    Single<ApiResponse<JsonObject>> bulkDownload() {
        return apiService.bulkDownload();
    }

    ;

    Single<ApiResponse<JsonObject>> bulkUpload(JsonObject jsonObject) {
        return apiService.bulkUpload(jsonObject);
    }

    ;


}