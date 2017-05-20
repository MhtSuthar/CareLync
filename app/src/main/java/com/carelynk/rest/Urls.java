package com.carelynk.rest;

import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.PrefUtils;

/**
 * Created by Admin on 06-Dec-16.
 */

public class Urls {

    public static final String GET_GROUP_CATEGORY = "Group/GetCategories";
    public static final String GET_HELTH_FEED = "Home/Gethightlights";
    public static final String GET_PROFILE = "UserProfile/Get?id="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "");
    public static final String GET_OWN_GROUP = "Group/GetOwnedGroups?UserId="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "")+"&PUserId="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "");
    public static final String GET_ALL_GROUP = "Group/GetOwnedGroups?UserId=0&PUserId="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "");
    public static final String GET_FOLLOW_GROUP = "Group/GetUserFollowGroups?UserId="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "");


    public static final String CREATE_GOAL = "Create_Goal";
    public static final String CREATE_POST = "Home/CreatePost";

    public static final String CREATE_GROUP = "Group/CreateGroup";
    public static final String GET_DELETE_GROUP = "Group/DeleteGroup";
    public static final String UPDATE_GROUP = "Group/UpdateGroup";

    public static final String CREATE_EVENT = "Event/CreateEvent";
    public static final String UPDATE_EVENT = "Event/UpdateEvent";
    public static final String GET_EVENT_LIST = "Event/GetEvents";
    public static final String DELETE_EVENT = "Event/DeleteEvent";

    public static final String INSERT_GROUP_POST = "Group/InsertGroupPost";
    public static final String GET_GROUP_DISCUSS = "Group/GetGroupPost";
    public static final String INSERT_COMMENT_GROUP_POST = "Group/Insert_Group_Post_Comment";
    public static final String GET_COMMENT_LIST = "Group/Get_Group_Post_Comment";
    public static final String JOIN_GROUP = "Group/Insert_Group_JoinRequest";
    public static final String GET_GROUP_JOIN_REQUEST= "Group/GetGroupsJoinRequest?UserId="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "");
    public static final String SUPPORT_GROUP_DETAIL = "Group/Insert_Group_Update_Comment_Support";
    public static final String UNSUPPORT_GROUP_DETAIL = "Group/Unsupport_Group_Update_Comment";
    public static final String ACCEPT_GROUP_REQUEST = "Approve_Group_JoinRequest";
    public static final String REJECT_GROUP_REQUEST = "DeleteGroup_JoinRequest?id=";
    public static final String GET_USER_FOLLOW = "GT";
    public static final String GET_GOAL_POST_LIST = "GET_GOAL_POST";
    public static final String INSERT_GOAL_POST = "Home/InsertGoalPost_Detail";
    public static final String INSERT_GOAL_POST_COMMENT = "Home/Insert_Goal_Post_Comments";
    public static final String GET_GOAL_COMMENT_LIST = "Home/Get_Goal_Post_Comment";
}
