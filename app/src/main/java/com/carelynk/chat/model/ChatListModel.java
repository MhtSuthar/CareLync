package com.carelynk.chat.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatListModel {

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

        @SerializedName("Image")
        @Expose
        private String image;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("Livesta")
        @Expose
        private String livesta;
        @SerializedName("Accepted")
        @Expose
        private String accepted;
        @SerializedName("Counting")
        @Expose
        private String counting;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLivesta() {
            return livesta;
        }

        public void setLivesta(String livesta) {
            this.livesta = livesta;
        }

        public String getAccepted() {
            return accepted;
        }

        public void setAccepted(String accepted) {
            this.accepted = accepted;
        }

        public String getCounting() {
            return counting;
        }

        public void setCounting(String counting) {
            this.counting = counting;
        }

    }
}

