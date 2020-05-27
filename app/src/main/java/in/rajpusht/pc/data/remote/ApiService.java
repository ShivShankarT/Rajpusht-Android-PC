package in.rajpusht.pc.data.remote;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import in.rajpusht.pc.model.ApiResponse;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    @FormUrlEncoded
    Single<ApiResponse<JsonObject>> login(@Field("user") String email, @Field("password") String password);

    @POST("password/reset/otp")
    @FormUrlEncoded
    Call<ApiResponse<List<JsonObject>>> forgotPassword(@Field("user") String user);


    @POST("/password")
    @FormUrlEncoded
    Call<ApiResponse<Object>> changePassword(@Field("curr_password") String curr_password,
                                             @Field("password") String password,
                                             @Field("cnfrm_password") String cnfrm_password);

    @POST("login/otp")
    @FormUrlEncoded
    Single<ApiResponse<JsonObject>> verifyOtp(@Field("otp") String opt);

    @POST("password/reset")
    @FormUrlEncoded
    Call<ApiResponse<Object>> setPassword(@Field("cnfrm_password") String cnfrm_password,
                                          @Field("otp") String otp, @Field("password") String password,
                                          @Field("user") String user);


    @POST("report/milestone/update")
    @FormUrlEncoded
    Call<ApiResponse<JsonElement>> updateMilestoneComment(@Field("plan_id") int planId,
                                                          @Field("progress") int planProgress,
                                                          @Field("report_comment") String cmd,
                                                          @Field("submit_date") String submitDate);

    @GET("assigned-location")
    Single<ApiResponse<JsonObject>> assignedLocation();
}
