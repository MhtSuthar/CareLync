package com.carelynk.dashboard.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchData {

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

        @SerializedName("Goals")
        @Expose
        private List<Goal> goals = null;
        @SerializedName("UserProfiles")
        @Expose
        private List<UserProfile> userProfiles = null;
        @SerializedName("Groups")
        @Expose
        private List<Group> groups = null;

        public List<Goal> getGoals() {
            return goals;
        }

        public void setGoals(List<Goal> goals) {
            this.goals = goals;
        }

        public List<UserProfile> getUserProfiles() {
            return userProfiles;
        }

        public void setUserProfiles(List<UserProfile> userProfiles) {
            this.userProfiles = userProfiles;
        }

        public List<Group> getGroups() {
            return groups;
        }

        public void setGroups(List<Group> groups) {
            this.groups = groups;
        }

    }

    public class Goal {

        @SerializedName("ID")
        @Expose
        private String iD;
        @SerializedName("Name")
        @Expose
        private String name;

        public String getID() {
            return iD;
        }

        public void setID(String iD) {
            this.iD = iD;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public class Group {

        @SerializedName("ID")
        @Expose
        private String iD;
        @SerializedName("Name")
        @Expose
        private String name;

        public String getID() {
            return iD;
        }

        public void setID(String iD) {
            this.iD = iD;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public class UserProfile {

        @SerializedName("ID")
        @Expose
        private String iD;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("ProfilePicUrl")
        @Expose
        private String ProfilePicUrl;

        public String getID() {
            return iD;
        }

        public void setID(String iD) {
            this.iD = iD;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePicUrl() {
            return ProfilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            ProfilePicUrl = profilePicUrl;
        }

    }

}







