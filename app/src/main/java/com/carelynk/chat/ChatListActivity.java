package com.carelynk.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.carelynk.chat.adapter.ChatListAdapter;
import com.carelynk.chat.model.ChatDetailModel;
import com.carelynk.chat.model.ChatListModel;
import com.carelynk.profile.DashboardActivity;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.AsyncTaskPostCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 20-Oct-16.
 */

public class ChatListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private Toolbar toolbar;
    private TextView txtToolbar, txtNoData;
    private ProgressBar progressBar;
    private ChatListAdapter chatListAdapter;
    private static final String TAG = "ChatListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupSlideWindowAnimationSlide(Gravity.RIGHT);
        initView();
        getHealthFeedDetail();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtToolbar = (TextView) toolbar.findViewById(R.id.txtToolbar);
        toolbar.findViewById(R.id.txtInvite).setVisibility(View.INVISIBLE);
        txtNoData = (TextView) findViewById(R.id.txtNoData);
        findViewById(R.id.relParent).setBackgroundColor(Color.WHITE);
        txtToolbar.setText(getString(R.string.chat));
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

    private void setRecyclerAdapter(List<ChatListModel.Result> mList) {
        progressBar.setVisibility(View.GONE);
        chatListAdapter = new ChatListAdapter(this, mList, ChatListActivity.this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(chatListAdapter);
    }

    void getHealthFeedDetail() {
        if (isOnline(this)) {
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "getFollowList: "+result);
                    progressBar.setVisibility(View.GONE);
                    try {
                        ChatListModel chatListModel = new Gson().fromJson(result, ChatListModel.class);
                        if(chatListModel != null) {
                            if (chatListModel.getResult().size() > 0) {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                txtNoData.setVisibility(View.GONE);
                                setRecyclerAdapter(chatListModel.getResult());
                            } else {
                                mRecyclerView.setVisibility(View.GONE);
                                txtNoData.setVisibility(View.VISIBLE);
                                showSnackbar(mRecyclerView, "No Invite List Available");
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.URL_GET_CHAT_LIST_USER+""+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        } else {
            progressBar.setVisibility(View.GONE);
            showSnackbar(mRecyclerView, getString(R.string.no_internet));
        }
    }


    public void onClickDetail(ChatListModel.Result map) {
        Intent intentDash = new Intent(this, ChatDetailActivity.class);
        intentDash.putExtra(Constants.EXTRA_USERID, map.getUserId());
        intentDash.putExtra(Constants.EXTRA_USER_NAME, map.getName());
        intentDash.putExtra(Constants.EXTRA_USER_IMAGE, map.getImage());
        startActivity(intentDash);
    }

}
