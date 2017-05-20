package com.carelynk.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReuestGroupModel {

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

        @SerializedName("GroupRequestId")
        @Expose
        private String groupRequestId;
        @SerializedName("Message")
        @Expose
        private String message;
        @SerializedName("GroupId")
        @Expose
        private String groupId;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("Accepted")
        @Expose
        private String accepted;
        @SerializedName("UserName")
        @Expose
        private String userName;

        public String getGroupRequestId() {
            return groupRequestId;
        }

        public void setGroupRequestId(String groupRequestId) {
            this.groupRequestId = groupRequestId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAccepted() {
            return accepted;
        }

        public void setAccepted(String accepted) {
            this.accepted = accepted;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

}

