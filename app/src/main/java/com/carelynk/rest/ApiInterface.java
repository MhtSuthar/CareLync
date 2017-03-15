package com.carelynk.rest;

import com.carelynk.prelogin.model.RegisterStepOne;
import com.carelynk.prelogin.model.RegisterStepTwo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @POST("UserProfile/login")
    Call<JsonObject> login(@Body JsonObject bean);

    @POST("UserProfile/Registration")
    Call<JsonObject> registrationOne(@Body RegisterStepOne registerStepOne);

    @POST("UserProfile/Registration2")
    Call<JsonObject> registrationTwo(@Body RegisterStepTwo registerStepTwo);

    @GET(Urls.GET_MAIN_GROUP)
    Call<JsonArray> getMainGroupJs();

    @GET(Urls.GET_HELTH_FEED)
    Call<JsonArray> getHealthFeed();

    @GET(Urls.GET_HELTH_FEED)
    Call<JsonObject> getProfile();

    @Multipart
    @POST("api/editUserProfile")
    Call<JsonObject> editProfile(@Part("userid") RequestBody user_id,
                                 @Part("security_token") RequestBody seq,
                                 @Part("name") RequestBody name,
                                 @Part("status") RequestBody status,
                                 @Part MultipartBody.Part file);

}