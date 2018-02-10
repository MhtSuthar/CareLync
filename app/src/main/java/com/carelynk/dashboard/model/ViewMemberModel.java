package com.carelynk.dashboard.model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewMemberModel {

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

        @SerializedName("GroupUserId")
        @Expose
        private String groupUserId;
        @SerializedName("GroupId")
        @Expose
        private String groupId;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("Admin")
        @Expose
        private String admin;
        @SerializedName("AddedDate")
        @Expose
        private String addedDate;
        @SerializedName("UserName")
        @Expose
        private String userName;

        public String getGroupUserId() {
            return groupUserId;
        }

        public void setGroupUserId(String groupUserId) {
            this.groupUserId = groupUserId;
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

        public String getAdmin() {
            return admin;
        }

        public void setAdmin(String admin) {
            this.admin = admin;
        }

        public String getAddedDate() {
            return addedDate;
        }

        public void setAddedDate(String addedDate) {
            this.addedDate = addedDate;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

    }

}

