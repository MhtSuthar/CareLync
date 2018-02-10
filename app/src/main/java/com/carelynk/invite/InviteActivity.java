package com.carelynk.invite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.FeedDetailActivity;
import com.carelynk.invite.adapter.InviteListAdapter;
import com.carelynk.profile.DashboardActivity;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.AsyncTaskPostCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.PrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 20-Oct-16.
 */

public class InviteActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private Toolbar toolbar;
    private TextView txtToolbar, txtNoData;
    private ProgressBar progressBar;
    private InviteListAdapter inviteListAdapter;
    private static final String TAG = "InviteActivity";
    private String mGroupId, mEventId;
    private boolean isFromEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        if(getIntent().hasExtra(Constants.EXTRA_GROUP_ID))
            mGroupId = getIntent().getExtras().getString(Constants.EXTRA_GROUP_ID);
        else if(getIntent().hasExtra(Constants.EXTRA_EVENT_ID)){
            isFromEvent = true;
            mEventId = getIntent().getExtras().getString(Constants.EXTRA_EVENT_ID);
        }
        initView();
        getHealthFeedDetail();
    }

    public void onClickInvite(View view){
        if(inviteListAdapter != null){
            if(inviteListAdapter.getSelectedCount()){
                sendInvitation();
            }else{
                showSnackbar(mRecyclerView, "Please Select Users");
            }
        }else{
            showSnackbar(mRecyclerView, "Please Select Users");
        }
    }

    private void sendInvitation() {
        if(isOnline(this)){
            if(isFromEvent){
                AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(this, new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
                    @Override
                    public void onTaskComplete(String result) {
                        if (result.length() > 0) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("IsSuccess")) {
                                    setResult(RESULT_OK);
                                    finish();
                                    showSnackbar(mRecyclerView, "Invite Request Send");
                                } else {
                                    showSnackbar(mRecyclerView, jsonObject.getString("ErrorMessage"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else
                            showSnackbar(mRecyclerView, getString(R.string.error_server));
                    }
                });
                asyncTaskCommon.execute(ApiFactory.API_BASE_URL + "" + Urls.INVITE_EVENT, getPostValueEvent());
            }else {
                AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(this, new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
                    @Override
                    public void onTaskComplete(String result) {
                        if (result.length() > 0) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("IsSuccess")) {
                                    finish();
                                } else {
                                    showSnackbar(mRecyclerView, jsonObject.getString("ErrorMessage"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else
                            showSnackbar(mRecyclerView, getString(R.string.error_server));
                    }
                });
                asyncTaskCommon.execute(ApiFactory.API_BASE_URL + "" + Urls.INVITE_GROUP, getPostValue());
            }
        }else
            showSnackbar(mRecyclerView, getString(R.string.no_internet));
    }

    private String getPostValue() {
        JSONObject jsonObject = new JSONObject();
        try{
            JSONArray jsonArray = new JSONArray();
            List<HashMap<String, String>> mList = inviteListAdapter.getAllData();
            for (int i = 0; i < mList.size(); i++) {
                if(mList.get(i).get("IsSelect").equals("1")) {
                    JSONObject json = new JSONObject();
                    json.put("FromUserId", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
                    json.put("GroupId", mGroupId);
                    json.put("ToUserId", mList.get(i).get("UserId"));
                    jsonArray.put(json);
                }
            }
            jsonObject.put("GroupInviteUsers", jsonArray);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private String getPostValueEvent() {
        JSONObject jsonObject = new JSONObject();
        try{
            JSONArray jsonArray = new JSONArray();
            List<HashMap<String, String>> mList = inviteListAdapter.getAllData();
            for (int i = 0; i < mList.size(); i++) {
                if(mList.get(i).get("IsSelect").equals("1")) {
                    JSONObject json = new JSONObject();
                    json.put("FromUserId", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
                    json.put("EventId", mEventId);
                    json.put("ToUserId", mList.get(i).get("UserId"));
                    jsonArray.put(json);
                }
            }
            jsonObject.put("EventInviteUsers", jsonArray);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtToolbar = (TextView) toolbar.findViewById(R.id.txtToolbar);
        txtNoData = (TextView) findViewById(R.id.txtNoData);
        txtToolbar.setText("Invite Users");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void setRecyclerAdapter(List<HashMap<String, String>> mList) {
        progressBar.setVisibility(View.GONE);
        inviteListAdapter = new InviteListAdapter(this, mList, InviteActivity.this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(inviteListAdapter);
    }

    void getHealthFeedDetail() {
        if (isOnline(this)) {
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "getFollowList: "+result);
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray object = jsonObject.getJSONArray("result");
                        List<HashMap<String, String>> mList = new ArrayList<>();
                        for (int i = 0; i < object.length(); i++) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            JSONObject jsonObj = object.getJSONObject(i);
                            hashMap.put("UserId", jsonObj.getString("UserId"));
                            hashMap.put("UserName", jsonObj.getString("UserName"));
                            hashMap.put("ProfilePicUrl", jsonObj.getString("ProfilePicUrl"));
                            hashMap.put("IsSelect", "0");
                            mList.add(hashMap);
                        }
                        if (mList.size() > 0) {
                            mRecyclerView.setVisibility(View.VISIBLE);
                            txtNoData.setVisibility(View.GONE);
                            setRecyclerAdapter(mList);
                        }else{
                            mRecyclerView.setVisibility(View.GONE);
                            txtNoData.setVisibility(View.VISIBLE);
                            showSnackbar(mRecyclerView, "No Invite List Available");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.URL_GET_INVITE_USER+""+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        } else {
            progressBar.setVisibility(View.GONE);
            showSnackbar(mRecyclerView, getString(R.string.no_internet));
        }
    }



    public void onClickDetail(String userid) {
        Intent intentDash = new Intent(this, DashboardActivity.class);
        intentDash.putExtra(Constants.EXTRA_USERID, userid);
        startActivity(intentDash);
    }

}
