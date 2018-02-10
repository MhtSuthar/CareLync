package com.carelynk.event;

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
import com.carelynk.event.adapter.EventListAdapter;
import com.carelynk.event.model.EventList;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskDeleteCommon;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 24-Dec-16.
 */

public class EventListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private Toolbar toolbar;
    private TextView txtToolbar, txtNoData;
    private ProgressBar progressBar;
    private EventListAdapter eventListAdapter;
    private static final String TAG = "EventListActivity";
    private List<EventList.Result> mEventList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);

        initView();

        getEventList();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtNoData = (TextView) findViewById(R.id.txtNoData);
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
                startActivityForResult(intent, Constants.REQUEST_CODE_ADD_EVENT);
                //moveActivityResult(intent, EventListActivity.this, Constants.REQUEST_CODE_ADD_EVENT);
            }
        });
    }

    private void setRecyclerAdapter(List<EventList.Result> result) {
        mEventList.clear();
        mEventList.addAll(result);
        eventListAdapter = new EventListAdapter(this, mEventList, EventListActivity.this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(eventListAdapter);
    }


    void getEventList() {
        if (isOnline(this)) {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            Call<EventList> call = apiInterface.getEventList(ApiFactory.API_BASE_URL+""+ Urls.GET_EVENT_LIST+"?userId="+SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            call.enqueue(new Callback<EventList>() {
                @Override
                public void onResponse(Call<EventList>call, Response<EventList> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "onResponse: "+response.body().toString());
                            if(response.body().getResult() != null && response.body().getResult().size() > 0){
                                txtNoData.setVisibility(View.GONE);
                                setRecyclerAdapter(response.body().getResult());
                            }else{
                                txtNoData.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<EventList>call, Throwable t) {
                    Log.e(TAG, t.toString());
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            showToast(getString(R.string.no_internet));
        }
    }

    public void onEditClick(int position) {
        Intent intent = new Intent(EventListActivity.this, EventCreateActivity.class);
        intent.putExtra(Constants.EXTRA_IS_FOR_EDIT_EVENT, true);
        intent.putExtra(Constants.EXTRA_EVENT, mEventList.get(position));
        startActivityForResult(intent, Constants.REQUEST_CODE_ADD_EVENT);
        //moveActivityResult(intent, EventListActivity.this, Constants.REQUEST_CODE_ADD_EVENT);
    }

    public void onDeleteClick(final int position) {
        showAlertDialog(new OnDialogClick() {
            @Override
            public void onPositiveBtnClick() {
                deleteEvent(mEventList.get(position).getEventID(), position);
            }

            @Override
            public void onNegativeBtnClick() {

            }
        }, getString(R.string.delete), getString(R.string.are_you_sure_delete), true);
    }

    private void deleteEvent(String eventID, final int position) {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    DialogUtils.stopProgressDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("IsSuccess")) {
                            mEventList.remove(position);
                            eventListAdapter.notifyDataSetChanged();
                            if(mEventList.size() == 0){
                                txtNoData.setVisibility(View.VISIBLE);
                            }
                        } else {
                            showSnackbar(mRecyclerView, jsonObject.getString("ErrorMessage"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.DELETE_EVENT+"?id="+eventID);
        }else{
            showSnackbar(mRecyclerView, getString(R.string.no_internet));
        }
    }

    public void onClickDetail(int position) {
        Intent intent = new Intent(EventListActivity.this, EventDetailActivity.class);
        intent.putExtra(Constants.EXTRA_IS_FOR_EDIT_EVENT, true);
        intent.putExtra(Constants.EXTRA_EVENT, mEventList.get(position));
        startActivityForResult(intent, Constants.REQUEST_CODE_ADD_EVENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.REQUEST_CODE_ADD_EVENT){
                getEventList();
            }
        }
    }
}
