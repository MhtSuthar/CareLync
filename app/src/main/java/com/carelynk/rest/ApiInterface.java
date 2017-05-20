package com.carelynk.rest;

import com.carelynk.dashboard.model.CommentModel;
import com.carelynk.dashboard.model.GroupDiscussModel;
import com.carelynk.dashboard.model.ReuestGroupModel;
import com.carelynk.event.model.EventList;
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
import retrofit2.http.Url;

public interface ApiInterface {

    @POST("UserProfile/login")
    Call<JsonObject> login(@Body JsonObject bean);

    @POST("UserProfile/Registration")
    Call<JsonObject> registrationOne(@Body RegisterStepOne registerStepOne);

    @POST("UserProfile/Registration2")
    Call<JsonObject> registrationTwo(@Body RegisterStepTwo registerStepTwo);

    @POST("UserProfile/ProfileUpdate")
    Call<JsonObject> profileUpdate(@Body JsonObject bean);

    @POST(Urls.CREATE_POST)
    Call<JsonObject> createPost(@Body JsonObject bean);

    @POST(Urls.INSERT_GOAL_POST)
    Call<JsonObject> insertGoalPost(@Body JsonObject bean);

    @POST(Urls.INSERT_GOAL_POST_COMMENT)
    Call<JsonObject> insertGoalPostComment(@Body JsonObject bean);

    @POST(Urls.INSERT_GROUP_POST)
    Call<JsonObject> insertGroupPost(@Body JsonObject bean);

    @POST(Urls.INSERT_COMMENT_GROUP_POST)
    Call<JsonObject> insertCommentGroupPost(@Body JsonObject bean);

    @POST(Urls.JOIN_GROUP)
    Call<JsonObject> joinGroup(@Body JsonObject bean);

    @POST(Urls.ACCEPT_GROUP_REQUEST)
    Call<JsonObject> acceptGroupRequest(@Body JsonObject bean);

    @POST(Urls.REJECT_GROUP_REQUEST)
    Call<JsonObject> rejectGroupRequest(@Body JsonObject bean);

    @POST(Urls.SUPPORT_GROUP_DETAIL)
    Call<JsonObject> supportGroupDetail(@Body JsonObject bean);

    @POST(Urls.UNSUPPORT_GROUP_DETAIL)
    Call<JsonObject> unSupportGroupDetail(@Body JsonObject bean);

    @GET(Urls.GET_GROUP_CATEGORY)
    Call<JsonObject> getGroupCategory();

    @GET(Urls.GET_HELTH_FEED)
    Call<JsonObject> getHealthFeed();

    @GET
    Call<CommentModel> getCommentList(@Url String url);

    @GET
    Call<CommentModel> getGoalCommentList(@Url String url);

    @GET
    Call<GroupDiscussModel> getGroupDiscussList(@Url String url);

    @GET
    Call<JsonObject> getGoalDetailList(@Url String url);

    @GET
    Call<ReuestGroupModel> getRequestGroup(@Url String url);

    @GET
    Call<EventList> getEventList(@Url String url);

    @POST(Urls.CREATE_GROUP)
    Call<JsonObject> createGroup(@Body JsonObject bean);

    @POST(Urls.UPDATE_GROUP)
    Call<JsonObject> updateGroup(@Body JsonObject bean);

    @POST(Urls.CREATE_EVENT)
    Call<JsonObject> createEvent(@Body JsonObject bean);

    @POST(Urls.UPDATE_EVENT)
    Call<JsonObject> updateEvent(@Body JsonObject bean);

    @Multipart
    @POST("api/editUserProfile")
    Call<JsonObject> editProfile(@Part("userid") RequestBody user_id,
                                 @Part("security_token") RequestBody seq,
                                 @Part("name") RequestBody name,
                                 @Part("status") RequestBody status,
                                 @Part MultipartBody.Part file);

}