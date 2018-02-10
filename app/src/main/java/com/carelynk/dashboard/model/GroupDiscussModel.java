package com.carelynk.dashboard.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class GroupDiscussModel {

    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Detail {

        @SerializedName("SelfSupport")
        @Expose
        private String selfSupport;
        @SerializedName("RequestPending")
        @Expose
        private String requestPending;

        public String getRequestCount() {
            return requestCount;
        }

        public void setRequestCount(String requestCount) {
            this.requestCount = requestCount;
        }

        @SerializedName("RequestCount")
        @Expose
        private String requestCount;
        @SerializedName("GroupName")
        @Expose
        private String groupName;
        @SerializedName("Description")
        @Expose
        private String description;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;
        @SerializedName("GroupUser")
        @Expose
        private String groupUser;
        @SerializedName("GroupPhotoURL")
        @Expose
        private String groupPhotoURL;
        @SerializedName("Admin")
        @Expose
        private String admin;
        @SerializedName("GroupId")
        @Expose
        private String groupId;

        public String getOwnerUserId() {
            return OwnerUserId;
        }

        public void setOwnerUserId(String ownerUserId) {
            OwnerUserId = ownerUserId;
        }

        @SerializedName("OwnerUserId")
        @Expose
        private String OwnerUserId;
        @SerializedName("OwnerProfilePic")
        @Expose
        private String ownerProfilePic;

        public String getOwnerProfilePic() {
            return ownerProfilePic;
        }

        public void setOwnerProfilePic(String ownerProfilePic) {
            this.ownerProfilePic = ownerProfilePic;
        }

        public String getGroupGoalId() {
            return GroupGoalId;
        }

        public void setGroupGoalId(String groupGoalId) {
            GroupGoalId = groupGoalId;
        }

        @SerializedName("GroupGoalId")
        @Expose
        private String GroupGoalId;


        public String getGroupUserId() {
            return GroupUserId;
        }

        public void setGroupUserId(String groupUserId) {
            GroupUserId = groupUserId;
        }

        @SerializedName("GroupUserId")
        @Expose
        private String GroupUserId;

        public String getSelfSupport() {
            return selfSupport;
        }

        public void setSelfSupport(String selfSupport) {
            this.selfSupport = selfSupport;
        }

        public String getRequestPending() {
            return requestPending;
        }

        public void setRequestPending(String requestPending) {
            this.requestPending = requestPending;
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

        public String getGroupUser() {
            return groupUser;
        }

        public void setGroupUser(String groupUser) {
            this.groupUser = groupUser;
        }

        public String getGroupPhotoURL() {
            return groupPhotoURL;
        }

        public void setGroupPhotoURL(String groupPhotoURL) {
            this.groupPhotoURL = groupPhotoURL;
        }

        public String getAdmin() {
            return admin;
        }

        public void setAdmin(String admin) {
            this.admin = admin;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

    }

    public class GroupDatum {

        @SerializedName("GroupName")
        @Expose
        private String groupName;
        @SerializedName("Description")
        @Expose
        private String description;
        @SerializedName("MainGroupName")
        @Expose
        private String mainGroupName;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;
        @SerializedName("GroupUser")
        @Expose
        private String groupUser;
        @SerializedName("GroupPhotoURL")
        @Expose
        private String groupPhotoURL;
        @SerializedName("Admin")
        @Expose
        private String admin;
        @SerializedName("GroupUpdateId")
        @Expose
        private String groupUpdateId;
        @SerializedName("Updatemsg")
        @Expose
        private String updatemsg;
        @SerializedName("UpdateDate")
        @Expose
        private String updateDate;
        @SerializedName("UpdatePhotoURL")
        @Expose
        private String updatePhotoURL;
        @SerializedName("GroupId")
        @Expose
        private String groupId;
        @SerializedName("Comments")
        @Expose
        private String comments;
        @SerializedName("Supports")
        @Expose
        private String supports;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("GroupUserId")
        @Expose
        private String groupUserId;
        @SerializedName("SelfSupport")
        @Expose
        private String selfSupport;
        @SerializedName("RequestPending")
        @Expose
        private String requestPending;

        public String getOwnerProfilePic() {
            return ownerProfilePic;
        }

        public void setOwnerProfilePic(String ownerProfilePic) {
            this.ownerProfilePic = ownerProfilePic;
        }

        public String getOwnerUserId() {
            return ownerUserId;
        }

        public void setOwnerUserId(String ownerUserId) {
            this.ownerUserId = ownerUserId;
        }

        @SerializedName("OwnerProfilePic")
        @Expose
        private String ownerProfilePic;
        @SerializedName("GroupUserProfilePic")
        @Expose
        private String groupUserProfilePic;

        public String getGroupUserProfilePic() {
            return groupUserProfilePic;
        }

        public void setGroupUserProfilePic(String groupUserProfilePic) {
            this.groupUserProfilePic = groupUserProfilePic;
        }

        public String getGroupUsersId() {
            return groupUsersId;
        }

        public void setGroupUsersId(String groupUsersId) {
            this.groupUsersId = groupUsersId;
        }

        @SerializedName("GroupUsersId")
        @Expose
        private String groupUsersId;
        @SerializedName("OwnerUserId")
        @Expose
        private String ownerUserId;
        public String comment="";

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

        public String getMainGroupName() {
            return mainGroupName;
        }

        public void setMainGroupName(String mainGroupName) {
            this.mainGroupName = mainGroupName;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getGroupUser() {
            return groupUser;
        }

        public void setGroupUser(String groupUser) {
            this.groupUser = groupUser;
        }

        public String getGroupPhotoURL() {
            return groupPhotoURL;
        }

        public void setGroupPhotoURL(String groupPhotoURL) {
            this.groupPhotoURL = groupPhotoURL;
        }

        public String getAdmin() {
            return admin;
        }

        public void setAdmin(String admin) {
            this.admin = admin;
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

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getUpdatePhotoURL() {
            return updatePhotoURL;
        }

        public void setUpdatePhotoURL(String updatePhotoURL) {
            this.updatePhotoURL = updatePhotoURL;
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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getGroupUserId() {
            return groupUserId;
        }

        public void setGroupUserId(String groupUserId) {
            this.groupUserId = groupUserId;
        }

        public String getSelfSupport() {
            return selfSupport;
        }

        public void setSelfSupport(String selfSupport) {
            this.selfSupport = selfSupport;
        }

        public String getRequestPending() {
            return requestPending;
        }

        public void setRequestPending(String requestPending) {
            this.requestPending = requestPending;
        }

    }

    public class Result {

        @SerializedName("GroupData")
        @Expose
        private List<GroupDatum> groupData = null;
        @SerializedName("Detail")
        @Expose
        private Detail detail;

        public List<GroupDatum> getGroupData() {
            return groupData;
        }

        public void setGroupData(List<GroupDatum> groupData) {
            this.groupData = groupData;
        }

        public Detail getDetail() {
            return detail;
        }

        public void setDetail(Detail detail) {
            this.detail = detail;
        }

    }
}



