package com.carelynk.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;



public class GroupModelGson {

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

        @SerializedName("OwnGroupDet")
        @Expose
        private List<OwnGroupDet> ownGroupDet = null;
        @SerializedName("FollowedGroupDet")
        @Expose
        private List<FollowedGroupDet> followedGroupDet = null;
        @SerializedName("AllGroupDet")
        @Expose
        private List<AllGroupDet> allGroupDet = null;

        public List<OwnGroupDet> getOwnGroupDet() {
            return ownGroupDet;
        }

        public void setOwnGroupDet(List<OwnGroupDet> ownGroupDet) {
            this.ownGroupDet = ownGroupDet;
        }

        public List<FollowedGroupDet> getFollowedGroupDet() {
            return followedGroupDet;
        }

        public void setFollowedGroupDet(List<FollowedGroupDet> followedGroupDet) {
            this.followedGroupDet = followedGroupDet;
        }

        public List<AllGroupDet> getAllGroupDet() {
            return allGroupDet;
        }

        public void setAllGroupDet(List<AllGroupDet> allGroupDet) {
            this.allGroupDet = allGroupDet;
        }

    }

    public class AllGroupDet {

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
        @SerializedName("PublicPrivate")
        @Expose
        private String publicPrivate;
        @SerializedName("MainGroupId")
        @Expose
        private String mainGroupId;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;
        @SerializedName("MainGroupName")
        @Expose
        private String mainGroupName;

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

        public String getPublicPrivate() {
            return publicPrivate;
        }

        public void setPublicPrivate(String publicPrivate) {
            this.publicPrivate = publicPrivate;
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

        public String getMainGroupName() {
            return mainGroupName;
        }

        public void setMainGroupName(String mainGroupName) {
            this.mainGroupName = mainGroupName;
        }

    }

    public class FollowedGroupDet {

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
        @SerializedName("PublicPrivate")
        @Expose
        private String publicPrivate;
        @SerializedName("MainGroupId")
        @Expose
        private String mainGroupId;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;
        @SerializedName("MainGroupName")
        @Expose
        private String mainGroupName;

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

        public String getPublicPrivate() {
            return publicPrivate;
        }

        public void setPublicPrivate(String publicPrivate) {
            this.publicPrivate = publicPrivate;
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

        public String getMainGroupName() {
            return mainGroupName;
        }

        public void setMainGroupName(String mainGroupName) {
            this.mainGroupName = mainGroupName;
        }

    }

    public class OwnGroupDet implements Serializable{

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
        @SerializedName("PublicPrivate")
        @Expose
        private String publicPrivate;
        @SerializedName("MainGroupId")
        @Expose
        private String mainGroupId;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;
        @SerializedName("MainGroupName")
        @Expose
        private String mainGroupName;

        public String getRequestCount() {
            return requestCount;
        }

        public void setRequestCount(String requestCount) {
            this.requestCount = requestCount;
        }

        @SerializedName("RequestCount")
        @Expose
        private String requestCount;

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

        public String getPublicPrivate() {
            return publicPrivate;
        }

        public void setPublicPrivate(String publicPrivate) {
            this.publicPrivate = publicPrivate;
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

        public String getMainGroupName() {
            return mainGroupName;
        }

        public void setMainGroupName(String mainGroupName) {
            this.mainGroupName = mainGroupName;
        }

    }
}




