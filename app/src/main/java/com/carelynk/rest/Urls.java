package com.carelynk.rest;

import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.PrefUtils;

/**
 * Created by Admin on 06-Dec-16.
 */

public class Urls {

    public static final String GET_GROUP_CATEGORY = "Group/GetCategories";
    public static final String GET_HELTH_FEED = "Goal/Gethightlights";
    public static final String GET_REQUEST_COUNT = "Home/GetRequestCount?UserId=";
    public static final String GET_PROFILE = "UserProfile/Get?id=";
    public static final String GET_OWN_GROUP = "Group/GetOwnedGroups?UserId="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "")+"&PUserId="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "");
    public static final String GET_ALL_GROUP = "Group/GetOwnedGroups?UserId=0&PUserId="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "");
    public static final String GET_FOLLOW_GROUP = "Group/GetUserFollowGroups?UserId="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "");
    public static final String GET_AREA_OF_INTEREST = "UserProfile/GetListInterestOfAreas";
    public static final String GET_GROUP_LIST = "Group/Get_Group_List?UserId=";
    public static final String GET_MESSAGE_COUNT = "Goal/Get_MessageCountList?FromUserId=";
    public static final String SEND_IP = "Home/InsertIpUser?FromUserId=";

    public static final String CREATE_GOAL = "Create_Goal";
    public static final String CREATE_POST = "Home/CreatePost";

    public static final String CREATE_GROUP = "Group/CreateGroup";
    public static final String GET_DELETE_GROUP = "Group/GroupDelete";
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
    public static final String GET_GROUP_JOIN_REQUEST= "Group/GetGroupsJoinRequest";
    public static final String SUPPORT_GROUP_DETAIL = "Group/Insert_Group_Update_Comment_Support";
    public static final String UNSUPPORT_GROUP_DETAIL = "Group/Unsupport_Group_Update_Comment";
    public static final String ACCEPT_GROUP_REQUEST = "Group/Approve_Group_JoinRequest";
    public static final String REJECT_GROUP_REQUEST = "Group/RejectGroupRequest";
    public static final String GET_USER_FOLLOW = "Home/GetFollowerList";
    public static final String GET_USER_FOLLOWING = "Home/GetFollowingList";
    public static final String GET_USER_FOLLOW_REQUEST = "Home/GetMember_FollowRequest";
    public static final String GET_GOAL_POST_LIST = "Home/Get_Goal_Post";
    public static final String INSERT_GOAL_POST = "Home/InsertGoalPost_Detail";
    public static final String INSERT_GOAL_POST_COMMENT = "Home/Insert_Goal_Post_Comments";
    public static final String GET_GOAL_COMMENT_LIST = "Home/Get_Goal_Post_Comment";
    public static final String INSERT_SUPPORT_GOAL_MOBILE = "Home/Insert_Support_Goal_Mobile";
    public static final String SUPPORT_GOAL_LIST_DETAIL = "Home/Insert_Support_Goal_Post_Mobile";
    public static final String UNSUPPORT_GOAL_LIST_DETAIL = "Home/Insert_Support_Goal_Post_Mobile";
    public static final String GET_SEARCH = "Home/GetSearchTextResult";
    public static final String GET_USER_DASHBOARD = "Home/Dashboard_GetDetail";
    public static final String GET_FOTGET_PASS = "Home/GetForgetPasswordUserDetailByEmail";
    public static final String ACCEPT_FOLLOW_REQUEST = "Home/Approve_Member_FollowUsers";
    public static final String REJECT_FOLLOW_REQUEST = "Home/RejectMemberFollowRequest";
    public static final String REJECT_GROUP_INVITATION = "Group/Reject_GroupInvitation";
    public static final String URL_TRENDING = "Home/GetTrendingList";
    public static final String URL_GET_VIEW_MEMBER_GROUP = "Group/GetGroupPostViewMember";
    public static final String URL_GET_INVITE_USER = "Group/GetInviteListGroup?UserId=";
    public static final String URL_GET_CHAT_LIST_USER = "Goal/Get_FriendUserList?FromUserId=";
    public static final String FOLLOW_FRINED = "Home/Insert_Member_FollowRequest";
    public static final String UNFOLLOW_FRIEND = "Home/RejectMemberFollowusers";
    public static final String INVITE_GROUP = "Group/Insert_Invite_Group";
    public static final String ACCEPT_EVENT_REQUEST = "Event/Approve_Event_JoinInvitation";
    public static final String REJECT_EVENT_REQUEST = "Event/Reject_Event_JoinInvitation";
    public static final String GET_HIGHLIGHT_FEED = "Home/Gethightlights";
    public static final String ACCEPT_GROUP_INVITATION = "Group/Approve_Group_JoinInvitation";
    public static final String CREATE_GOAL_POST = "Goal/CreatePost";
    public static final String DELETE_GOAL = "Goal/GoalDelete";
    public static final String UPDATE_GOAL_POST = "Goal/UpdateGoal";
    public static final String SPAM_GOAL = "Goal/GoalReportabuse";
    public static final String GET_SEARCH_ARTICLE = "Goal/GetSearchGoal?searchText=";
    public static final String PEOPLE_MAY_KNOW = "Goal/GetPeopleMayKnow?FromUserId=";
    public static final String URL_REJECT_GROUP_REQUEST = "Group/Reject_Member?GroupId=";
    public static final String GET_UPDATE_APP = "UserProfile/GetUpdateApp";
    public static String INVITE_EVENT = "Event/Insert_Invite_Event";
    public static String VIEW_EVENT_USER="Event/GetEventViewMember?event_id=";
    public static String URL_GET_CHAT_DETAIL = "Goal/Get_Message";
    public static String URL_LOGOUT = "Goal/Update_Register?FromUserId=";
}
