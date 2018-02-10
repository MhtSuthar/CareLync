package com.carelynk.dashboard.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutoCompliteArticle {

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

        public List<Goal> getGoals() {
            return goals;
        }

        public void setGoals(List<Goal> goals) {
            this.goals = goals;
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

}

