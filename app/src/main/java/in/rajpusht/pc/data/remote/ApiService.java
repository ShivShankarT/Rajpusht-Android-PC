package in.rajpusht.pc.data.remote;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.InstitutionPlaceEntity;
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.model.ProfileDetail;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    @FormUrlEncoded
    Single<ApiResponse<JsonObject>> login(@Field("email") String email, @Field("password") String password);


    @POST("verifyOtp")
    @FormUrlEncoded
    Single<ApiResponse<JsonElement>> verifyOtp(@Field("otp") String opt);


    @POST("changePassword")
    @FormUrlEncoded
    Single<ApiResponse<JsonElement>> changePassword(@Field("oldPassword") String oldPassword,
                                                    @Field("newPassword") String newPassword);


    @POST("forgotPassword")
    @FormUrlEncoded
    Single<ApiResponse<JsonObject>> forgotPassword(@Field("email") String email);


    @POST("setPassword")
    @FormUrlEncoded
    Single<ApiResponse<JsonElement>> setPassword(@Field("resetOtp") String resetOtp,
                                                 @Field("newPassword") String newPassword);


    @POST("resendOtp")
    @FormUrlEncoded
    Single<ApiResponse<JsonObject>> resendOtp();


    @GET("profileDetail")
    Single<ApiResponse<ProfileDetail>> profileDetail();


    @PATCH("profileUpdate")
    @FormUrlEncoded
    Single<ApiResponse<JsonElement>> profileUpdate(@Field("firstName") String fName, @Field("lastName") String LName, @Field("mobileNumber") String mob);


    @POST("logout")
    Single<ApiResponse<JsonElement>> logout();

    @GET("bulkDownload")
    Single<ApiResponse<JsonObject>> bulkDownload();

    @GET("getAllDeliveryInstitute")
    Single<ApiResponse<List<InstitutionPlaceEntity>>> fetchInstitutionPlaceEntity();

    @POST("bulkUpload")
    Single<ApiResponse<JsonObject>> bulkUpload(@Body JsonArray jsonArray);


}
