package com.carelynk.chat.model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatDetailModel {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public static class Result {

        @SerializedName("ID")
        @Expose
        private String iD;
        @SerializedName("Sender")
        @Expose
        private String sender;
        @SerializedName("Reciever")
        @Expose
        private String reciever;
        @SerializedName("Message")
        @Expose
        private String message;
        @SerializedName("Date")
        @Expose
        private String date;
        @SerializedName("Time")
        @Expose
        private String time;
        @SerializedName("Image")
        @Expose
        private String image;
        @SerializedName("FromUserId")
        @Expose
        private String fromUserId;
        @SerializedName("ToUserId")
        @Expose
        private String toUserId;
        @SerializedName("Fname")
        @Expose
        private String fname;
        @SerializedName("Accept")
        @Expose
        private String accept;

        public String getID() {
            return iD;
        }

        public void setID(String iD) {
            this.iD = iD;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getReciever() {
            return reciever;
        }

        public void setReciever(String reciever) {
            this.reciever = reciever;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(String fromUserId) {
            this.fromUserId = fromUserId;
        }

        public String getToUserId() {
            return toUserId;
        }

        public void setToUserId(String toUserId) {
            this.toUserId = toUserId;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getAccept() {
            return accept;
        }

        public void setAccept(String accept) {
            this.accept = accept;
        }

    }

}

