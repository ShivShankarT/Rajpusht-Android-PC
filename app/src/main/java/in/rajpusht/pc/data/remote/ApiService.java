package in.rajpusht.pc.data.remote;


import com.google.gson.JsonObject;

import in.rajpusht.pc.model.ApiResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    @FormUrlEncoded
    Single<ApiResponse<JsonObject>> login(@Field("email") String email, @Field("password") String password);


    @POST("verifyOtp")
    @FormUrlEncoded
    Single<ApiResponse<JsonObject>> verifyOtp(@Field("otp") String opt);


    @POST("/changePassword")
    @FormUrlEncoded
    Single<ApiResponse<JsonObject>> changePassword(@Field("oldPassword") String oldPassword,
                                                   @Field("newPassword") String newPassword);


    @POST("/forgotPassword")
    @FormUrlEncoded
    Single<ApiResponse<JsonObject>> forgotPassword(@Field("email") String oldPassword);


    @POST("/setPassword")
    @FormUrlEncoded
    Single<ApiResponse<JsonObject>> setPassword(@Field("resetOtp") String resetOtp,
                                                @Field("newPassword") String newPassword);


    @POST("/resendOtp")
    @FormUrlEncoded
    Single<ApiResponse<JsonObject>> resendOtp();


    @GET("/profileDetail")
    Single<ApiResponse<JsonObject>> profileDetail();


    @POST("/logout")
    Single<ApiResponse<JsonObject>> logout();

    @GET("/bulkDownload")
    Single<ApiResponse<JsonObject>> bulkDownload();

    @POST("/bulkUpload")
    Single<ApiResponse<JsonObject>> bulkUpload(@Body JsonObject jsonObject);


}
