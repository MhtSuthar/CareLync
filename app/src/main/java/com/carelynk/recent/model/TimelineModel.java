package com.carelynk.recent.model;

/**
 * Created by Admin on 13-Sep-16.
 */
public class TimelineModel {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public boolean isFollowing;
}
