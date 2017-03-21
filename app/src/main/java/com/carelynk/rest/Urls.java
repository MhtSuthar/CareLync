package com.carelynk.rest;

import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.PrefUtils;

/**
 * Created by Admin on 06-Dec-16.
 */

public class Urls {

    public static final String BaseUrl = "http://wcfcarelynk.shauryatech.co.in.204-11-58-75.bhus-pp-wb8.webhostbox.net/Service1.svc/";

    public static final String GET_MAIN_GROUP = "Get_MainGroup";
    public static final String GET_HELTH_FEED = "Home/Gethightlights";
    public static final String GET_PROFILE = "UserProfile/Get?id="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "");


    public static final String INSERT_CREATE_GROUP = "Insert_Create_Group";
    public static final String CREATE_GOAL = "Create_Goal";
    public static final String INSERT_EVENT = "Insert_Event";
    public static final String CREATE_POST = "Home/CreatePost";
}
