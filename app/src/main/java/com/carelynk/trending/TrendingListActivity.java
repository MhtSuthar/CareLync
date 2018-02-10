package com.carelynk.trending;

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
import com.carelynk.event.EventCreateActivity;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.trending.adapter.TrendingListAdapter;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 24-Dec-16.
 */

public class TrendingListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private Toolbar toolbar;
    private TextView txtToolbar;
    private ProgressBar progressBar;
    private TrendingListAdapter trendingListAdapter;
    private static final String TAG = "TrendingListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);

        initView();
        getHealthFeedDetail();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        txtToolbar.setText(getString(R.string.trending));
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

    private void setRecyclerAdapter(List<HashMap<String, String>> mList) {
        progressBar.setVisibility(View.GONE);
        trendingListAdapter = new TrendingListAdapter(this, mList, TrendingListActivity.this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(trendingListAdapter);
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
                            hashMap.put("GoalId", jsonObj.getString("GoalId"));
                            hashMap.put("GoalName", jsonObj.getString("GoalName"));
                            mList.add(hashMap);
                        }
                        if (mList.size() > 0) {
                            setRecyclerAdapter(mList);
                        }else{
                            showSnackbar(mRecyclerView, "No Trending Data Available");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.URL_TRENDING);
        } else {
            progressBar.setVisibility(View.GONE);
            showSnackbar(mRecyclerView, getString(R.string.no_internet));
        }
    }



    public void onClickDetail(String goalId) {
        Intent intent = new Intent(this, FeedDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Goal_Id, ""+goalId);
        startActivity(intent);
    }
}
