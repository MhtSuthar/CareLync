package com.carelynk.dashboard.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoalListDetail {

    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("GoalArray")
        @Expose
        private List<GoalArray> goalArray = null;
        @SerializedName("GoalDetail")
        @Expose
        private GoalDetail goalDetail;

        public List<GoalArray> getGoalArray() {
            return goalArray;
        }

        public void setGoalArray(List<GoalArray> goalArray) {
            this.goalArray = goalArray;
        }

        public GoalDetail getGoalDetail() {
            return goalDetail;
        }

        public void setGoalDetail(GoalDetail goalDetail) {
            this.goalDetail = goalDetail;
        }

    }

    public class GoalArray {

        @SerializedName("UpdateId")
        @Expose
        private String updateId;
        @SerializedName("Updatemsg")
        @Expose
        private String updatemsg;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("GoalId")
        @Expose
        private String goalId;
        @SerializedName("UpdateDate")
        @Expose
        private String updateDate;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("SupportsCount")
        @Expose
        private String supportsCount;
        @SerializedName("CommentsCount")
        @Expose
        private String commentsCount;
        @SerializedName("SelfSupportCount")
        @Expose
        private String selfSupportCount;

        public String getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

        @SerializedName("ProfilePicUrl")
        @Expose
        private String profilePicUrl;
        public String comment = "";

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGoalId() {
            return goalId;
        }

        public void setGoalId(String goalId) {
            this.goalId = goalId;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getPhotoURL() {
            return photoURL;
        }

        public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
        }

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

        public String getSupportsCount() {
            return supportsCount;
        }

        public void setSupportsCount(String supportsCount) {
            this.supportsCount = supportsCount;
        }

        public String getCommentsCount() {
            return commentsCount;
        }

        public void setCommentsCount(String commentsCount) {
            this.commentsCount = commentsCount;
        }

        public String getSelfSupportCount() {
            return selfSupportCount;
        }

        public void setSelfSupportCount(String selfSupportCount) {
            this.selfSupportCount = selfSupportCount;
        }

    }

    public class GoalDetail {

        @SerializedName("GoalId")
        @Expose
        private String goalId;
        @SerializedName("GoalName")
        @Expose
        private String goalName;
        @SerializedName("Descrptn")
        @Expose
        private String descrptn;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("CreatedName")
        @Expose
        private String createdName;
        @SerializedName("ProfilePicUrl")
        @Expose
        private String profilePicUrl;
        @SerializedName("SelfSupportFlag")
        @Expose
        private String selfSupportFlag;
        @SerializedName("CreatedDate")
        @Expose
        private String CreatedDate;

        public String getPhotoURL() {
            return photoURL;
        }

        public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
        }

        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;

        public String getVideoURL() {
            return VideoURL;
        }

        public void setVideoURL(String videoURL) {
            VideoURL = videoURL;
        }

        @SerializedName("VideoURL")
        @Expose
        private String VideoURL;
        public String getExpertise() {
            return expertise;
        }

        public void setExpertise(String expertise) {
            this.expertise = expertise;
        }

        @SerializedName("Expertise")
        @Expose
        private String expertise;

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String createdDate) {
            CreatedDate = createdDate;
        }

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

        public String getDescrptn() {
            return descrptn;
        }

        public void setDescrptn(String descrptn) {
            this.descrptn = descrptn;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCreatedName() {
            return createdName;
        }

        public void setCreatedName(String createdName) {
            this.createdName = createdName;
        }

        public String getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

        public String getSelfSupportFlag() {
            return selfSupportFlag;
        }

        public void setSelfSupportFlag(String selfSupportFlag) {
            this.selfSupportFlag = selfSupportFlag;
        }

    }
}





