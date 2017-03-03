package com.carelynk.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.recent.HealthFeedDetailActivity;
import com.carelynk.recent.model.HealthFeedDetailModel;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Admin on 24-Dec-16.
 */

public class EventListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private Toolbar toolbar;
    private TextView txtToolbar;
    private AsyncTaskGetCommon asyncTaskGetCommon;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);

        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        txtToolbar.setText(getString(R.string.my_event));
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

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventListActivity.this, EventCreateActivity.class);
                moveActivity(intent, EventListActivity.this);
            }
        });
    }

    void getHealthFeedDetail() {
        if (isOnline(this)) {
            asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    /*try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            HealthFeedDetailModel helthFeedModel = new HealthFeedDetailModel();
                            helthFeedModel.GoalId = ""+object.getInt("GoalId");
                            helthFeedModel.PhotoURL = object.getString("PhotoURL");
                            helthFeedModel.Updatemsg = object.getString("Updatemsg");
                            helthFeedModel.status = ""+object.getInt("status");
                            helthFeedModel.UpdateDate = object.getString("UpdateDate");
                            helthFeedModel.UpdateId = ""+object.getInt("UpdateId");
                            mGroupList.add(helthFeedModel);
                        }
                        mProgressBarHeader.setVisibility(View.GONE);
                        healthFeedDetailRecyclerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }
            });
            asyncTaskGetCommon.execute(Urls.GET_EVENT_LIST + "/0");
        } else {
            progressBar.setVisibility(View.GONE);
           // showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }
}
