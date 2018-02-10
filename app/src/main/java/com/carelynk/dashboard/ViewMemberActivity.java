package com.carelynk.dashboard;

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
import com.carelynk.dashboard.adapter.ViewMemberListAdapter;
import com.carelynk.dashboard.model.ViewMemberModel;
import com.carelynk.profile.DashboardActivity;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Admin on 24-Dec-16.
 */

public class ViewMemberActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private Toolbar toolbar;
    private TextView txtToolbar;
    private ProgressBar progressBar;
    private ViewMemberListAdapter viewMemberListAdapter;
    private static final String TAG = "TrendingListActivity";
    private String mGroupId;
    private ViewMemberModel viewMemberModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        mGroupId = getIntent().getExtras().getString(Constants.EXTRA_GROUP_ID);
        initView();
        getHealthFeedDetail();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        txtToolbar.setText("View Members");
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
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.addFab);
        mFloatingActionButton.setVisibility(View.GONE);
    }

    private void setRecyclerAdapter(List<ViewMemberModel.Result> mList) {
        progressBar.setVisibility(View.GONE);
        viewMemberListAdapter = new ViewMemberListAdapter(this, mList, ViewMemberActivity.this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(viewMemberListAdapter);
    }

    void getHealthFeedDetail() {
        if (isOnline(this)) {
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "getFollowList: "+result);
                    progressBar.setVisibility(View.GONE);
                    try {
                        viewMemberModel = new Gson().fromJson(result, ViewMemberModel.class);
                        if (viewMemberModel.getResult().size() > 0) {
                            setRecyclerAdapter(viewMemberModel.getResult());
                        }else{
                            showSnackbar(mRecyclerView, "No Trending Data Available");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.URL_GET_VIEW_MEMBER_GROUP+"?group_id="+mGroupId);
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

    public void onUnfollow(final ViewMemberModel.Result data) {
        showAlertDialog(new BaseActivity.OnDialogClick() {
            @Override
            public void onPositiveBtnClick() {
                if (isOnline(ViewMemberActivity.this)) {
                    DialogUtils.showProgressDialog(ViewMemberActivity.this);
                    AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(ViewMemberActivity.this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                        @Override
                        public void onTaskComplete(String result) {
                            Log.e(TAG, "getFollowList: "+result);
                            DialogUtils.stopProgressDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("IsSuccess")) {
                                    getHealthFeedDetail();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                    asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.URL_REJECT_GROUP_REQUEST+mGroupId+"&MemberUserId="+data.getUserId());
                } else {
                    showSnackbar(mRecyclerView, getString(R.string.no_internet));
                }
            }

            @Override
            public void onNegativeBtnClick() {

            }
        }, "Unfollow", "You are sure to Unfollow this user", true);

    }
}
