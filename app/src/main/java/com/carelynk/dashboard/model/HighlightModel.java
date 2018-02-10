package com.carelynk.dashboard.model;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by Admin on 06-Mar-17.
 */

public class HighlightModel implements Serializable{
    public int GoalId;
    public String GoalName;
    public String Desc;
    public int GoalType;
    public int GoalStatusId;
    public String UserId;
    public String CreatedDate;
    public String PhotoURL;
    public String UserName;
    public String PostType;
    public int SupportCount;
    public int AnswerCount;
    public String ProfilePicUrl;
    public String Expertise;
    public String VideoUrl;
    public JSONArray PepPleMayKnow;
}
