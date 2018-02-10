package com.carelynk.dashboard.model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowRequestModel {

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

        @SerializedName("FollowRequestId")
        @Expose
        private String followRequestId;
        @SerializedName("FromUserId")
        @Expose
        private String fromUserId;
        @SerializedName("FromUserName")
        @Expose
        private String fromUserName;
        @SerializedName("ToUserId")
        @Expose
        private String toUserId;
        @SerializedName("ToUserName")
        @Expose
        private String toUserName;
        @SerializedName("Accepted")
        @Expose
        private String accepted;

        public String getFollowRequestId() {
            return followRequestId;
        }

        public void setFollowRequestId(String followRequestId) {
            this.followRequestId = followRequestId;
        }

        public String getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(String fromUserId) {
            this.fromUserId = fromUserId;
        }

        public String getFromUserName() {
            return fromUserName;
        }

        public void setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
        }

        public String getToUserId() {
            return toUserId;
        }

        public void setToUserId(String toUserId) {
            this.toUserId = toUserId;
        }

        public String getToUserName() {
            return toUserName;
        }

        public void setToUserName(String toUserName) {
            this.toUserName = toUserName;
        }

        public String getAccepted() {
            return accepted;
        }

        public void setAccepted(String accepted) {
            this.accepted = accepted;
        }

    }

    public List<EventResult> getEventResult() {
        return eventResult;
    }

    public void setEventResult(List<EventResult> eventResult) {
        this.eventResult = eventResult;
    }

    @SerializedName("EventResult")
    @Expose
    private List<EventResult> eventResult = null;

    public class EventResult {

        @SerializedName("Event_Member_id")
        @Expose
        private String Event_Member_id;
        @SerializedName("FromUserId")
        @Expose
        private String fromUserId;

        public String getEvent_Member_id() {
            return Event_Member_id;
        }

        public void setEvent_Member_id(String event_Member_id) {
            Event_Member_id = event_Member_id;
        }

        public String getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(String fromUserId) {
            this.fromUserId = fromUserId;
        }

        public String getFromUserName() {
            return fromUserName;
        }

        public void setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
        }

        public String getUser_id() {
            return User_id;
        }

        public void setUser_id(String user_id) {
            User_id = user_id;
        }

        public String getEvent_id() {
            return Event_id;
        }

        public void setEvent_id(String event_id) {
            Event_id = event_id;
        }

        public String getAccepted() {
            return accepted;
        }

        public void setAccepted(String accepted) {
            this.accepted = accepted;
        }

        @SerializedName("FromUserName")
        @Expose
        private String fromUserName;
        @SerializedName("User_id")
        @Expose
        private String User_id;
        @SerializedName("Event_id")
        @Expose
        private String Event_id;
        @SerializedName("Accepted")
        @Expose
        private String accepted;



    }


    public List<Groupresult> getGroupresults() {
        return groupresults;
    }

    public void setGroupresults(List<Groupresult> groupresults) {
        this.groupresults = groupresults;
    }

    @SerializedName("Groupresult")
    @Expose
    private List<Groupresult> groupresults = null;

    public class Groupresult {

        public String getGroupInvitationId() {
            return GroupInvitationId;
        }

        public void setGroupInvitationId(String groupInvitationId) {
            GroupInvitationId = groupInvitationId;
        }

        public String getFromUserId() {
            return FromUserId;
        }

        public void setFromUserId(String fromUserId) {
            FromUserId = fromUserId;
        }

        public String getFromUserName() {
            return FromUserName;
        }

        public void setFromUserName(String fromUserName) {
            FromUserName = fromUserName;
        }

        public String getToUserId() {
            return ToUserId;
        }

        public void setToUserId(String toUserId) {
            ToUserId = toUserId;
        }

        public String getGroupId() {
            return GroupId;
        }

        public void setGroupId(String groupId) {
            GroupId = groupId;
        }

        public String getAccepted() {
            return accepted;
        }

        public void setAccepted(String accepted) {
            this.accepted = accepted;
        }

        @SerializedName("GroupInvitationId")
        @Expose
        private String GroupInvitationId;
        @SerializedName("FromUserId")
        @Expose
        private String FromUserId;
        @SerializedName("FromUserName")
        @Expose
        private String FromUserName;
        @SerializedName("ToUserId")
        @Expose
        private String ToUserId;
        @SerializedName("GroupId")
        @Expose
        private String GroupId;
        @SerializedName("Accepted")
        @Expose
        private String accepted;



    }
}

