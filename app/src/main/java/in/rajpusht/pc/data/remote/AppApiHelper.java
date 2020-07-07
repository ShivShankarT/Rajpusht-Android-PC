package in.rajpusht.pc.data.remote;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.rajpusht.pc.data.local.db.entity.InstitutionPlaceEntity;
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.model.ProfileDetail;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@Singleton
public class AppApiHelper {

    ApiService apiService;

    @Inject
    public AppApiHelper(ApiService apiService) {
        this.apiService = apiService;
    }

    private <T> Single<ApiResponse<T>> networkErrorWrapper(Single<ApiResponse<T>> apiResponseSingle) {
        return apiResponseSingle.onErrorReturn(new Function<Throwable, ApiResponse<T>>() {
            @Override
            public ApiResponse<T> apply(Throwable throwable) throws Exception {
                if (throwable instanceof HttpException) {
                    HttpException httpException = (HttpException) throwable;
                    ResponseBody json = httpException.response().errorBody();
                    boolean status = false;
                    String message = httpException.code() + " " + httpException.message();
                    if (httpException.code() == 422) {
                        JSONObject jsonObject = new JSONObject(json.string());
                        status = jsonObject.optBoolean("status", false);
                        message = jsonObject.optString("message", httpException.message());
                    }

                    ApiResponse jsonObjectApiResponse = new ApiResponse();
                    jsonObjectApiResponse.setStatus(status);
                    jsonObjectApiResponse.setMessage(message);
                    return jsonObjectApiResponse;
                }
                if (throwable != null)
                    throwable.printStackTrace();
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setStatus(false);
                apiResponse.setInternalError(true);
                apiResponse.setMessage("Check your internet");
                return apiResponse;
            }
        });
    }


    public Single<ApiResponse<JsonObject>> login(String email, String password) {
        return networkErrorWrapper(apiService.login(email, password));
    }

    public Single<ApiResponse<JsonElement>> verifyOtp(String otp) {
        return networkErrorWrapper(apiService.verifyOtp(otp));
    }


    public Single<ApiResponse<JsonElement>> changePassword(String oldPassword,
                                                           String newPassword) {
        return networkErrorWrapper(apiService.changePassword(oldPassword, newPassword));
    }


    public Single<ApiResponse<JsonObject>> forgotPassword(String email) {
        return networkErrorWrapper(apiService.forgotPassword(email));
    }


    public Single<ApiResponse<JsonElement>> setPassword(String resetOtp, String newPassword) {
        return networkErrorWrapper(apiService.setPassword(resetOtp, newPassword));
    }


    public Single<ApiResponse<JsonObject>> resendOtp() {
        return networkErrorWrapper(apiService.resendOtp());
    }


    public Single<ApiResponse<ProfileDetail>> profileDetail() {
        return networkErrorWrapper(apiService.profileDetail());
    }

    public Single<ApiResponse<JsonElement>> profileUpdate(String fName, String lname, String mob) {

        return networkErrorWrapper(apiService.profileUpdate(fName, lname, mob));
    }


    public Single<ApiResponse<JsonObject>> logout() {
        return networkErrorWrapper(apiService.logout());
    }


    public Single<ApiResponse<JsonObject>> bulkDownload() {
        return networkErrorWrapper(apiService.bulkDownload());
    }

    public Single<ApiResponse<List<InstitutionPlaceEntity>>> fetchInstitutionPlaceEntity() {
        return networkErrorWrapper(apiService.fetchInstitutionPlaceEntity());
    }

    public Single<ApiResponse<JsonObject>> bulkUpload(JsonArray jsonArray) {
        return networkErrorWrapper(apiService.bulkUpload(jsonArray));
    }


}