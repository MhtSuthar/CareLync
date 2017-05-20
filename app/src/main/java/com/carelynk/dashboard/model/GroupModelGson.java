package com.carelynk.dashboard.model;


import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupModelGson {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result implements Serializable{

        @SerializedName("GroupId")
        @Expose
        private String groupId;
        @SerializedName("GroupName")
        @Expose
        private String groupName;
        @SerializedName("Description")
        @Expose
        private String description;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;
        @SerializedName("MainGroupId")
        @Expose
        private String mainGroupId;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;

        public String getGroupGoalId() {
            return groupGoalId;
        }

        public void setGroupGoalId(String groupGoalId) {
            this.groupGoalId = groupGoalId;
        }

        @SerializedName("GroupGoalId")
        @Expose
        private String groupGoalId;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
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

        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("ProfilePicUrl")
        @Expose
        private String profilePicUrl;

        public String getRequestStatus() {
            return requestStatus;
        }

        public void setRequestStatus(String requestStatus) {
            this.requestStatus = requestStatus;
        }

        @SerializedName("RequestStatus")
        @Expose
        private String requestStatus;

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }

        @SerializedName("TitleName")
        @Expose
        private String titleName;
        @SerializedName("PublicPrivate")
        @Expose
        private String publicPrivate;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getMainGroupId() {
            return mainGroupId;
        }

        public void setMainGroupId(String mainGroupId) {
            this.mainGroupId = mainGroupId;
        }

        public String getPhotoURL() {
            return photoURL;
        }

        public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
        }

        public String getPublicPrivate() {
            return publicPrivate;
        }

        public void setPublicPrivate(String publicPrivate) {
            this.publicPrivate = publicPrivate;
        }

    }
}

