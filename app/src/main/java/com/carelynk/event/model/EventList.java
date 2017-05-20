package com.carelynk.event.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventList {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result implements Serializable {

        @SerializedName("Event_ID")
        @Expose
        private String eventID;
        @SerializedName("EventName")
        @Expose
        private String eventName;
        @SerializedName("EventDesc")
        @Expose
        private String eventDesc;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("PhotoURL")
        @Expose
        private String photoURL;

        public String getIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(String isPrivate) {
            this.isPrivate = isPrivate;
        }

        @SerializedName("IsPrivate")
        @Expose
        private String isPrivate;

        @SerializedName("EventDateFrom")
        @Expose
        private String eventDateFrom;

        public String getEventDateFrom() {
            return eventDateFrom;
        }

        public void setEventDateFrom(String eventDateFrom) {
            this.eventDateFrom = eventDateFrom;
        }

        public String getEventDateTo() {
            return eventDateTo;
        }

        public void setEventDateTo(String eventDateTo) {
            this.eventDateTo = eventDateTo;
        }

        public String getEventTimeFrom() {
            return eventTimeFrom;
        }

        public void setEventTimeFrom(String eventTimeFrom) {
            this.eventTimeFrom = eventTimeFrom;
        }

        public String getEventTimeTo() {
            return eventTimeTo;
        }

        public void setEventTimeTo(String eventTimeTo) {
            this.eventTimeTo = eventTimeTo;
        }

        @SerializedName("EventDateTo")
        @Expose
        private String eventDateTo;
        @SerializedName("EventTimeFrom")
        @Expose
        private String eventTimeFrom;
        @SerializedName("EventTimeTo")
        @Expose
        private String eventTimeTo;

        public String getEventID() {
            return eventID;
        }

        public void setEventID(String eventID) {
            this.eventID = eventID;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getEventDesc() {
            return eventDesc;
        }

        public void setEventDesc(String eventDesc) {
            this.eventDesc = eventDesc;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhotoURL() {
            return photoURL;
        }

        public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
        }

    }
}

