package com.carelynk.recent.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainGroupModel {

    @SerializedName("MainGroupID")
    @Expose
    private Integer mainGroupID;
    @SerializedName("MainGroupName")
    @Expose
    private String mainGroupName;

    /**
     * @return The mainGroupID
     */
    public Integer getMainGroupID() {
        return mainGroupID;
    }

    /**
     * @param mainGroupID The MainGroupID
     */
    public void setMainGroupID(Integer mainGroupID) {
        this.mainGroupID = mainGroupID;
    }

    /**
     * @return The mainGroupName
     */
    public String getMainGroupName() {
        return mainGroupName;
    }

    /**
     * @param mainGroupName The MainGroupName
     */
    public void setMainGroupName(String mainGroupName) {
        this.mainGroupName = mainGroupName;
    }

}