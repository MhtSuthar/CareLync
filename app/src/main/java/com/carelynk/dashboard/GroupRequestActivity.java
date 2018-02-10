package com.carelynk.dashboard;

import android.app.Activity;
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
import com.carelynk.dashboard.adapter.GroupRequestRecyclerAdapter;
import com.carelynk.dashboard.model.GroupModelGson;
import com.carelynk.dashboard.model.ReuestGroupModel;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskDeleteCommon;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 24-Dec-16.
 */

public class GroupRequestActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private Toolbar toolbar;
    private TextView txtToolbar, txtNoData;
    private ProgressBar progressBar;
    private static final String TAG = "TrendingListActivity";
    private String mGroupId;
    private GroupRequestRecyclerAdapter mReqestRecyclerAdapter;
    boolean isChangeRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        mGroupId = getIntent().getExtras().getString(Constants.EXTRA_GROUP_ID);
        initView();
        getRequestOfGroup();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        txtNoData = (TextView) findViewById(R.id.txtNoData);
        txtToolbar.setText("Pending Requests");
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



    private void getRequestOfGroup() {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    DialogUtils.stopProgressDialog();
                    try{
                        Log.e(TAG, "get group: "+new Gson().toJson(result));
                        ReuestGroupModel mGroupModelGson = new Gson().fromJson(result, ReuestGroupModel.class);
                        if(mGroupModelGson.getResult().size() > 0){
                            txtNoData.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            setRequestRecyclerAdapter(mGroupModelGson.getResult());
                        }else{
                            txtNoData.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                            txtNoData.setText("No Requests Available!");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_GROUP_JOIN_REQUEST+"?GroupId="+mGroupId+"&UserId="+SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        }else{
            showSnackbar(mRecyclerView, getString(R.string.no_internet));
        }
    }

    private void setRequestRecyclerAdapter(List<ReuestGroupModel.Result> result) {
        progressBar.setVisibility(View.GONE);
        mReqestRecyclerAdapter = new GroupRequestRecyclerAdapter(this, result, GroupRequestActivity.this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mReqestRecyclerAdapter);
    }


    public void onAcceptRequestClick(int position, ReuestGroupModel.Result result) {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("GroupId", Integer.parseInt(result.getGroupId()));
            payerReg.addProperty("RequestUserID", result.getUserId());
            Log.e(TAG, "Accept update: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.acceptGroupRequest(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            isChangeRequest = true;
                            Log.e(TAG, "create update: "+new Gson().toJson(response.body()));
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                getRequestOfGroup();
                            }else{
                                showSnackbar(mRecyclerView, jsonObject.getString("ErrorMessage"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject>call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        }else{
            showSnackbar(mRecyclerView, getString(R.string.no_internet));
        }
    }

    public void onRejectRequestClick(int position, ReuestGroupModel.Result result) {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    DialogUtils.stopProgressDialog();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        if (jsonObject.getJSONObject("result").getBoolean("IsSuccess")) {
                            isChangeRequest = true;
                            getRequestOfGroup();
                        }else{
                            showSnackbar(mRecyclerView, jsonObject.getJSONObject("result").getString("ErrorMessage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.REJECT_GROUP_REQUEST+"?group_id="+mGroupId+"&UserId="+result.getUserId());
        }else{
            showSnackbar(mRecyclerView, getString(R.string.no_internet));
        }
    }

    @Override
    public void onBackPressed() {
        if(isChangeRequest){
            setResult(Activity.RESULT_OK);
            finish();
        }else{
            super.onBackPressed();
        }
    }
}
