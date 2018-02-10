package com.carelynk.rest;

import com.carelynk.dashboard.model.CommentModel;
import com.carelynk.dashboard.model.GoalListDetail;
import com.carelynk.dashboard.model.GroupDiscussModel;
import com.carelynk.dashboard.model.ReuestGroupModel;
import com.carelynk.event.model.EventList;
import com.carelynk.prelogin.model.RegisterStepOne;
import com.carelynk.prelogin.model.RegisterStepTwo;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ApiInterface {

    @POST("UserProfile/login")
    Call<JsonObject> login(@Body JsonObject bean);

    @Multipart
    @POST("UserProfile/Registration")
    Call<JsonObject> registrationOne(@Part("Email") RequestBody email,
                                     @Part("PasswordHash") RequestBody pass,
                                     @Part("UserProfileId") RequestBody userProfileId,
                                     @Part("AboutMe") RequestBody aboutMe,
                                     @Part("FirstName") RequestBody firstName,
                                     @Part("LastName") RequestBody lastName,
                                     @Part("DateOfBirth") RequestBody dateOfBirth,
                                     @Part("Gender") RequestBody gender,
                                     @Part("ContactNo") RequestBody contactNo,
                                     @Part MultipartBody.Part file);

    @POST("UserProfile/Registration2")
    Call<JsonObject> registrationTwo(@Body RegisterStepTwo registerStepTwo);

    @Multipart
    @POST("UserProfile/ProfileUpdate")
    Call<JsonObject> profileUpdate(@Part("UserProfileId") RequestBody UserProfileId,
                                   @Part("Email") RequestBody Email,
                                   @Part("FirstName") RequestBody FirstName,
                                   @Part("LastName") RequestBody LastName,
                                   @Part("DateOfBirth") RequestBody DateOfBirth,
                                   @Part("Gender") RequestBody Gender,
                                   @Part("Address") RequestBody Address,
                                   @Part("City") RequestBody City,
                                   @Part("State") RequestBody State,
                                   @Part("Country") RequestBody Country,
                                   @Part("ZipCode") RequestBody ZipCode,
                                   @Part("ContactNo") RequestBody ContactNo,
                                   @Part("DisplayName") RequestBody DisplayName,
                                   @Part("AboutMe") RequestBody AboutMe,
                                   @Part("Alternate_EmailID") RequestBody Alternate_EmailID,
                                   @Part("Marital_Status") RequestBody Marital_Status,
                                   @Part("Occupation") RequestBody Occupation,
                                   @Part("Food_Habits") RequestBody Food_Habits,
                                   @Part("Someone_Message_Email") RequestBody Someone_Message_Email,
                                   @Part("Friend_Request_Email") RequestBody Friend_Request_Email,
                                   @Part("News_Updates_Portal_Email") RequestBody News_Updates_Portal_Email,
                                   @Part("Recd_notif_Email") RequestBody Recd_notif_Email,
                                   @Part("IsPrivate_Profile") RequestBody IsPrivate_Profile,
                                   @Part("ProfilePicUrl") RequestBody ProfilePicUrl,
                                   @Part("Expertise") RequestBody expertise,
                                   @Part("Certification") RequestBody certification,
                                   @Part("Education") RequestBody education,
                                   @Part("AboutMeText") RequestBody aboutMeText,
                                   @Part MultipartBody.Part file);

    @POST(Urls.CREATE_POST)
    Call<JsonObject> createPost(@Body JsonObject bean);

    @Multipart
    @POST(Urls.CREATE_GOAL_POST)
    Call<JsonObject> createPostGoal(@Part("GoalId") RequestBody GoalId,
                                    @Part("GoalName") RequestBody goalName,
                                    @Part("Desc") RequestBody desc,
                                    @Part("GoalType") RequestBody type,
                                    @Part("UserId") RequestBody userId,
                                    @Part MultipartBody.Part file,
                                    @Part MultipartBody.Part fileVideo);

    @Multipart
    @POST(Urls.UPDATE_GOAL_POST)
    Call<JsonObject> updatePostGoal(@Part("GoalId") RequestBody GoalId,
                                    @Part("GoalName") RequestBody goalName,
                                    @Part("Desc") RequestBody desc,
                                    @Part("GoalType") RequestBody type,
                                    @Part("PhotoURL") RequestBody image,
                                    @Part("VideoURL") RequestBody video,
                                    @Part MultipartBody.Part file,
                                    @Part MultipartBody.Part fileVideo);

    @Multipart
    @POST(Urls.INSERT_GOAL_POST)
    Call<JsonObject> insertGoalPost(@Part("Updatemsg") RequestBody updateMsg,
                                    @Part("GoalId") RequestBody GoalId,
                                    @Part("UserId") RequestBody userId,
                                    @Part MultipartBody.Part file);

    @Multipart
    @POST(Urls.INSERT_GOAL_POST_COMMENT)
    Call<JsonObject> insertGoalPostComment(@Part("CommentText") RequestBody CommentText,
                                           @Part("UpdateId") RequestBody UpdateId,
                                           @Part("UserID") RequestBody userId,
                                           @Part MultipartBody.Part file);

    @Multipart
    @POST(Urls.INSERT_GROUP_POST)
    Call<JsonObject> insertGroupPost(@Part("Updatemsg") RequestBody msg,
                                     @Part("GroupId") RequestBody groupId,
                                     @Part("GroupGoalId") RequestBody groupGoalId,
                                     @Part("user_id") RequestBody userId,
                                     @Part MultipartBody.Part file);

    @POST(Urls.FOLLOW_FRINED)
    Call<JsonObject> followFriends(@Body JsonObject bean);

    @Multipart
    @POST(Urls.INSERT_COMMENT_GROUP_POST)
    Call<JsonObject> insertCommentGroupPost(@Part("CommentText") RequestBody commentText,
                                            @Part("GroupUpdateId") RequestBody groupUpdateId,
                                            @Part("user_id") RequestBody userId,
                                            @Part MultipartBody.Part file);

    @POST(Urls.JOIN_GROUP)
    Call<JsonObject> joinGroup(@Body JsonObject bean);

    @POST(Urls.GET_GROUP_JOIN_REQUEST)
    Call<ReuestGroupModel> getJoinGroupRequest(@Body JsonObject bean);

    @POST(Urls.ACCEPT_GROUP_REQUEST)
    Call<JsonObject> acceptGroupRequest(@Body JsonObject bean);

    @POST(Urls.ACCEPT_GROUP_INVITATION)
    Call<JsonObject> acceptGroupInvitation(@Body JsonObject bean);

    @POST(Urls.REJECT_GROUP_REQUEST)
    Call<JsonObject> rejectGroupRequest(@Body JsonObject bean);

    @POST(Urls.SUPPORT_GROUP_DETAIL)
    Call<JsonObject> supportGroupDetail(@Body JsonObject bean);

    @POST(Urls.UNSUPPORT_GROUP_DETAIL)
    Call<JsonObject> unSupportGroupDetail(@Body JsonObject bean);

    @POST(Urls.SUPPORT_GOAL_LIST_DETAIL)
    Call<JsonObject> supportGoalListDetail(@Body JsonObject bean);

    @POST(Urls.UNSUPPORT_GOAL_LIST_DETAIL)
    Call<JsonObject> unSupportGoalListDetail(@Body JsonObject bean);

    @GET(Urls.GET_GROUP_CATEGORY)
    Call<JsonObject> getGroupCategory();

    @GET(Urls.GET_AREA_OF_INTEREST)
    Call<JsonObject> getAreaOfIntrest();

    @GET
    Call<JsonObject> getHealthFeed(@Url String url);

    @GET
    Call<CommentModel> getCommentList(@Url String url);

    @GET
    Call<CommentModel> getGoalCommentList(@Url String url);

    @GET
    Call<GroupDiscussModel> getGroupDiscussList(@Url String url);

    @GET
    Call<GoalListDetail> getGoalDetailList(@Url String url);

    @GET
    Call<ReuestGroupModel> getRequestGroup(@Url String url);

    @GET
    Call<EventList> getEventList(@Url String url);

    @POST(Urls.CREATE_GROUP)
    Call<JsonObject> createGroup(@Body JsonObject bean);

    @POST(Urls.ACCEPT_FOLLOW_REQUEST)
    Call<JsonObject> acceptFollowingRequest(@Body JsonObject payerReg);

    @POST(Urls.ACCEPT_EVENT_REQUEST)
    Call<JsonObject> acceptEventRequest(@Body JsonObject payerReg);

    @POST(Urls.REJECT_EVENT_REQUEST)
    Call<JsonObject> rejectEventRequest(@Body JsonObject payerReg);

    @POST(Urls.UPDATE_GROUP)
    Call<JsonObject> updateGroup(@Body JsonObject bean);

    @POST(Urls.CREATE_EVENT)
    Call<JsonObject> createEvent(@Body JsonObject bean);

    @POST(Urls.UPDATE_EVENT)
    Call<JsonObject> updateEvent(@Body JsonObject bean);

    @POST(Urls.INSERT_SUPPORT_GOAL_MOBILE)
    Call<JsonObject> addSupportGoal(@Body JsonObject bean);

    @Multipart
    @POST("Goal/Insert_Message")
    Call<JsonObject> sendMessage(@Part("ID") RequestBody id,
                                 @Part("Sender") RequestBody sender,
                                 @Part("Reciever") RequestBody receiver,
                                 @Part("Message") RequestBody message,
                                 @Part("Image") RequestBody image,
                                 @Part("FromUserId") RequestBody fromUserId,
                                 @Part("ToUserId") RequestBody toUserId,
                                 @Part MultipartBody.Part file);

    @Multipart
    @POST(Urls.CREATE_EVENT)
    Call<JsonObject> createEventPhoto(@Part("Event_ID") RequestBody event_id,
                                      @Part("EventDesc") RequestBody desc,
                                      @Part("User_ID") RequestBody user_id,
                                      @Part("IsPrivate") RequestBody is_private,
                                      @Part("EventName") RequestBody event_name,
                                      @Part("EventDateFrom") RequestBody event_date_from,
                                      @Part("EventDateTo") RequestBody event_date_to,
                                      @Part("EventTimeTo") RequestBody event_time_to,
                                      @Part("Location") RequestBody location,
                                      @Part("EventTimeFrom") RequestBody event_time_from,
                                      @Part MultipartBody.Part file);

    @Multipart
    @POST(Urls.UPDATE_EVENT)
    Call<JsonObject> updateEventPhoto(@Part("Event_ID") RequestBody event_id,
                                      @Part("EventDesc") RequestBody desc,
                                      @Part("IsPrivate") RequestBody is_private,
                                      @Part("EventName") RequestBody event_name,
                                      @Part("EventDateFrom") RequestBody event_date_from,
                                      @Part("EventDateTo") RequestBody event_date_to,
                                      @Part("EventTimeTo") RequestBody event_time_to,
                                      @Part("Location") RequestBody location,
                                      @Part("EventTimeFrom") RequestBody event_time_from,
                                      @Part("PhotoURL") RequestBody photo,
                                      @Part MultipartBody.Part file);

    @Multipart
    @POST(Urls.CREATE_GROUP)
    Call<JsonObject> createGroup(@Part("UserId") RequestBody userId,
                                 @Part("GroupName") RequestBody groupName,
                                 @Part("Description") RequestBody desc,
                                 @Part("PublicPrivate") RequestBody is_private,
                                 @Part("MainGroupId") RequestBody mainGroupId,
                                 @Part MultipartBody.Part file);

    @Multipart
    @POST(Urls.UPDATE_GROUP)
    Call<JsonObject> updateGroup(@Part("UserId") RequestBody userId,
                                @Part("GroupId") RequestBody groupId,
                                @Part("GroupName") RequestBody groupName,
                                @Part("Description") RequestBody desc,
                                @Part("PublicPrivate") RequestBody is_private,
                                @Part("MainGroupId") RequestBody mainGroupId,
                                @Part("PhotoURL") RequestBody photoUrl,
                                @Part MultipartBody.Part file);


}