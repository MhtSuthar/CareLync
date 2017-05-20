package com.carelynk.dashboard.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentModel {

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

        @SerializedName("GroupCommentId")
        @Expose
        private String groupCommentId;
        @SerializedName("CommentText")
        @Expose
        private String commentText;
        @SerializedName("GroupUpdateId")
        @Expose
        private String groupUpdateId;
        @SerializedName("CommentDate")
        @Expose
        private String commentDate;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("UserName")
        @Expose
        private String userName;

        public String getGroupCommentId() {
            return groupCommentId;
        }

        public void setGroupCommentId(String groupCommentId) {
            this.groupCommentId = groupCommentId;
        }

        public String getCommentText() {
            return commentText;
        }

        public void setCommentText(String commentText) {
            this.commentText = commentText;
        }

        public String getGroupUpdateId() {
            return groupUpdateId;
        }

        public void setGroupUpdateId(String groupUpdateId) {
            this.groupUpdateId = groupUpdateId;
        }

        public String getCommentDate() {
            return commentDate;
        }

        public void setCommentDate(String commentDate) {
            this.commentDate = commentDate;
        }

        public String getPhotoURL() {
            return photoURL;
        }

        public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
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

    }

}

