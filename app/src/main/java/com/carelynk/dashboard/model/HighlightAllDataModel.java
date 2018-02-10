package com.carelynk.dashboard.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;


public class HighlightAllDataModel {

    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class GoalArray {

        @SerializedName("GoalId")
        @Expose
        private String goalId;
        @SerializedName("GoalName")
        @Expose
        private String goalName;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("ProfilePicUrl")
        @Expose
        private String profilePicUrl;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;

        public String getGoalId() {
            return goalId;
        }

        public void setGoalId(String goalId) {
            this.goalId = goalId;
        }

        public String getGoalName() {
            return goalName;
        }

        public void setGoalName(String goalName) {
            this.goalName = goalName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhotoURL() {
            return photoURL;
        }

        public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

    }

    public class GoalUpdateArray {

        @SerializedName("GoalId")
        @Expose
        private String goalId;
        @SerializedName("GoalName")
        @Expose
        private String goalName;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("ProfilePicUrl")
        @Expose
        private String profilePicUrl;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("UpdateId")
        @Expose
        private String updateId;
        @SerializedName("Updatemsg")
        @Expose
        private String updatemsg;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;
        @SerializedName("UpdateDate")
        @Expose
        private String updateDate;

        public String getGoalId() {
            return goalId;
        }

        public void setGoalId(String goalId) {
            this.goalId = goalId;
        }

        public String getGoalName() {
            return goalName;
        }

        public void setGoalName(String goalName) {
            this.goalName = goalName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUpdateId() {
            return updateId;
        }

        public void setUpdateId(String updateId) {
            this.updateId = updateId;
        }

        public String getUpdatemsg() {
            return updatemsg;
        }

        public void setUpdatemsg(String updatemsg) {
            this.updatemsg = updatemsg;
        }

        public String getPhotoURL() {
            return photoURL;
        }

        public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

    }

    public class GroupArray {

        @SerializedName("GroupId")
        @Expose
        private String groupId;
        @SerializedName("GroupName")
        @Expose
        private String groupName;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("ProfilePicUrl")
        @Expose
        private String profilePicUrl;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhotoURL() {
            return photoURL;
        }

        public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

    }

    public class GroupUpdateArray {

        @SerializedName("GroupId")
        @Expose
        private String groupId;
        @SerializedName("GroupName")
        @Expose
        private String groupName;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("ProfilePicUrl")
        @Expose
        private String profilePicUrl;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("GroupUpdateId")
        @Expose
        private String groupUpdateId;
        @SerializedName("Updatemsg")
        @Expose
        private String updatemsg;
        @SerializedName("GroupGoalId")
        @Expose
        private String groupGoalId;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;
        @SerializedName("UpdateDate")
        @Expose
        private String updateDate;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getGroupUpdateId() {
            return groupUpdateId;
        }

        public void setGroupUpdateId(String groupUpdateId) {
            this.groupUpdateId = groupUpdateId;
        }

        public String getUpdatemsg() {
            return updatemsg;
        }

        public void setUpdatemsg(String updatemsg) {
            this.updatemsg = updatemsg;
        }

        public String getGroupGoalId() {
            return groupGoalId;
        }

        public void setGroupGoalId(String groupGoalId) {
            this.groupGoalId = groupGoalId;
        }

        public String getPhotoURL() {
            return photoURL;
        }

        public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

    }

    public class Result {

        @SerializedName("ConnectedArray")
        @Expose
        private List<ConnectedArray> connectedArray = null;
        @SerializedName("GoalArray")
        @Expose
        private List<GoalArray> goalArray = null;
        @SerializedName("GoalUpdateArray")
        @Expose
        private List<GoalUpdateArray> goalUpdateArray = null;
        @SerializedName("GroupArray")
        @Expose
        private List<GroupArray> groupArray = null;
        @SerializedName("GroupUpdateArray")
        @Expose
        private List<GroupUpdateArray> groupUpdateArray = null;
        @SerializedName("EventArray")
        @Expose
        private List<EventArray> eventArray = null;

        public List<ConnectedArray> getConnectedArray() {
            return connectedArray;
        }

        public void setConnectedArray(List<ConnectedArray> connectedArray) {
            this.connectedArray = connectedArray;
        }

        public List<GoalArray> getGoalArray() {
            return goalArray;
        }

        public void setGoalArray(List<GoalArray> goalArray) {
            this.goalArray = goalArray;
        }

        public List<GoalUpdateArray> getGoalUpdateArray() {
            return goalUpdateArray;
        }

        public void setGoalUpdateArray(List<GoalUpdateArray> goalUpdateArray) {
            this.goalUpdateArray = goalUpdateArray;
        }

        public List<GroupArray> getGroupArray() {
            return groupArray;
        }

        public void setGroupArray(List<GroupArray> groupArray) {
            this.groupArray = groupArray;
        }

        public List<GroupUpdateArray> getGroupUpdateArray() {
            return groupUpdateArray;
        }

        public void setGroupUpdateArray(List<GroupUpdateArray> groupUpdateArray) {
            this.groupUpdateArray = groupUpdateArray;
        }

        public List<EventArray> getEventArray() {
            return eventArray;
        }

        public void setEventArray(List<EventArray> eventArray) {
            this.eventArray = eventArray;
        }

    }

    public class ConnectedArray {

        @SerializedName("ToUserId")
        @Expose
        private String toUserId;
        @SerializedName("ToUserName")
        @Expose
        private String toUserName;
        @SerializedName("caption")
        @Expose
        private String caption;
        @SerializedName("FromUserName")
        @Expose
        private String fromUserName;
        @SerializedName("FromUserId")
        @Expose
        private String fromUserId;
        @SerializedName("AddedDate")
        @Expose
        private String addedDate;

        public String getToUserId() {
            return toUserId;
        }

        public void setToUserId(String toUserId) {
            this.toUserId = toUserId;
        }

        public String getToUserName() {
            return toUserName;
        }

        public void setToUserName(String toUserName) {
            this.toUserName = toUserName;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getFromUserName() {
            return fromUserName;
        }

        public void setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
        }

        public String getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(String fromUserId) {
            this.fromUserId = fromUserId;
        }

        public String getAddedDate() {
            return addedDate;
        }

        public void setAddedDate(String addedDate) {
            this.addedDate = addedDate;
        }

    }

    public class EventArray {

        @SerializedName("Event_ID")
        @Expose
        private String eventID;
        @SerializedName("EventName")
        @Expose
        private String eventName;
        @SerializedName("User_ID")
        @Expose
        private String userID;
        @SerializedName("ProfilePicUrl")
        @Expose
        private String profilePicUrl;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("EventDesc")
        @Expose
        private String eventDesc;
        @SerializedName("EventDateFrom")
        @Expose
        private String eventDateFrom;
        @SerializedName("EventDateTo")
        @Expose
        private String eventDateTo;
        @SerializedName("EventTimeFrom")
        @Expose
        private String eventTimeFrom;
        @SerializedName("EventTimeTo")
        @Expose
        private String eventTimeTo;
        @SerializedName("IsPrivate")
        @Expose
        private String isPrivate;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;
        @SerializedName("Location")
        @Expose
        private String location;
        @SerializedName("Created_Datetime")
        @Expose
        private String createdDatetime;

        public String getEventID() {
            return eventID;
        }

        public void setEventID(String eventID) {
            this.eventID = eventID;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEventDesc() {
            return eventDesc;
        }

        public void setEventDesc(String eventDesc) {
            this.eventDesc = eventDesc;
        }

        public String getEventDateFrom() {
            return eventDateFrom;
        }

        public void setEventDateFrom(String eventDateFrom) {
            this.eventDateFrom = eventDateFrom;
        }

        public String getEventDateTo() {
            return eventDateTo;
        }

        public void setEventDateTo(String eventDateTo) {
            this.eventDateTo = eventDateTo;
        }

        public String getEventTimeFrom() {
            return eventTimeFrom;
        }

        public void setEventTimeFrom(String eventTimeFrom) {
            this.eventTimeFrom = eventTimeFrom;
        }

        public String getEventTimeTo() {
            return eventTimeTo;
        }

        public void setEventTimeTo(String eventTimeTo) {
            this.eventTimeTo = eventTimeTo;
        }

        public String getIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(String isPrivate) {
            this.isPrivate = isPrivate;
        }

        public String getPhotoURL() {
            return photoURL;
        }

        public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getCreatedDatetime() {
            return createdDatetime;
        }

        public void setCreatedDatetime(String createdDatetime) {
            this.createdDatetime = createdDatetime;
        }

    }
}

