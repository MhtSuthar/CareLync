package com.carelynk.dashboard.model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupDiscussModel {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("GroupUpdateId")
        @Expose
        private String groupUpdateId;
        @SerializedName("Updatemsg")
        @Expose
        private String updatemsg;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("GroupGoalId")
        @Expose
        private String groupGoalId;
        @SerializedName("UpdateDate")
        @Expose
        private String updateDate;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;
        @SerializedName("GroupId")
        @Expose
        private String groupId;
        @SerializedName("Comments")
        @Expose
        private String comments = "";
        @SerializedName("Supports")
        @Expose
        private String supports;
        @SerializedName("UserId")
        @Expose
        private String userId;

        public String getGroupUserId() {
            return groupUserId;
        }

        public void setGroupUserId(String groupUserId) {
            this.groupUserId = groupUserId;
        }

        @SerializedName("GroupUserId")
        @Expose
        private String groupUserId;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;

        public String getSelfSupport() {
            return selfSupport;
        }

        public void setSelfSupport(String selfSupport) {
            this.selfSupport = selfSupport;
        }

        @SerializedName("SelfSupport")
        @Expose
        private String selfSupport;

        public boolean isSupport;
        public String comment = "";

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGroupGoalId() {
            return groupGoalId;
        }

        public void setGroupGoalId(String groupGoalId) {
            this.groupGoalId = groupGoalId;
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

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getSupports() {
            return supports;
        }

        public void setSupports(String supports) {
            this.supports = supports;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

    }

}

