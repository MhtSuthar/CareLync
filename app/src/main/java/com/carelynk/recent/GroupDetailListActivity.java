package com.carelynk.recent;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.databinding.ActivityGroupTopicListBinding;
import com.carelynk.recent.adapter.GroupDetailRecyclerAdapter;
import com.carelynk.recent.model.GroupPostModel;
import com.carelynk.recent.model.TimelineModel;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.AsyncTaskPostCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 19-Oct-16.
 */

public class GroupDetailListActivity extends BaseActivity {

    private ActivityGroupTopicListBinding binding;
    private GroupDetailRecyclerAdapter groupTopicTimelineRecyclerAdapter;
    private List<GroupPostModel> mGroupList = new ArrayList<>();
    private static final String TAG = "GroupDetailListActivity";
    private String mGroupId = "";
    private ProgressBar mProgressBarHeader;
    private EditText edtTopic, edtDescription;
    private ImageView imgSelect;
    private AppCompatButton btnPost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_topic_list);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupSlideWindowAnimationSlide(Gravity.BOTTOM);

        initToolbar();

        mGroupId = getIntent().getExtras().getString(AppUtils.Extra_Group_Id);

        setRecyclerAdapter();

        getGroupDetails();

       /* binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(new Intent(GroupDetailListActivity.this, GroupPostActivity.class), GroupDetailListActivity.this);
            }
        });*/
    }

    private void getGroupDetails() {
        if(isOnline(this)){
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    try{
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            GroupPostModel groupPostModel = new GroupPostModel();
                            groupPostModel.GroupGoalId = object.getInt("GroupGoalId");
                            groupPostModel.GroupId = object.getInt("GroupId");
                            groupPostModel.PhotoURL = object.getString("PhotoURL");
                            groupPostModel.GroupUpdateId = object.getInt("GroupUpdateId");
                            groupPostModel.UpdateDate = object.getString("UpdateDate");
                            groupPostModel.status = object.getInt("status");
                            groupPostModel.Updatemsg = object.getString("Updatemsg");
                            mGroupList.add(groupPostModel);
                        }
                        mProgressBarHeader.setVisibility(View.GONE);
                        groupTopicTimelineRecyclerAdapter.notifyDataSetChanged();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(Urls.GET_GROUP_POST + "/"+mGroupId);
        }else{
            mProgressBarHeader.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), "Error");
        }
    }

    private void initToolbar() {
        setSupportActionBar(binding.includeToolbar.toolbar);
        binding.includeToolbar.toolbarTitle.setText(getString(R.string.group_detail));
        setTitle("");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setRecyclerAdapter() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        /**
         * Set Header
         */
        View header = LayoutInflater.from(this).inflate(R.layout.header_health_feed, binding.recyclerView, false);
        mProgressBarHeader = (ProgressBar) header.findViewById(R.id.progressBar);
        edtTopic = (EditText) header.findViewById(R.id.edtTopic);
        TextInputLayout textLayout  = (TextInputLayout) header.findViewById(R.id.textLayout);
        textLayout.setHint("Add an update for this support group");
        edtTopic.setHint("Add an update for this support group");
        edtDescription = (EditText) header.findViewById(R.id.edtDesc);
        edtDescription.setVisibility(View.GONE);
        btnPost = (AppCompatButton) header.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    insertGroupPost();
            }
        });
        imgSelect = (ImageView) header.findViewById(R.id.imgSelect);
        imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        groupTopicTimelineRecyclerAdapter = new GroupDetailRecyclerAdapter(header, this, mGroupList);
        binding.recyclerView.setAdapter(groupTopicTimelineRecyclerAdapter);
    }


    void insertGroupPost(){
        if(isOnline(this)){
            if(isValid()) {
                AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(this, new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
                    @Override
                    public void onTaskComplete(String result) {
                        if (result.length() > 0) {

                        } else
                            showSnackbar(binding.getRoot(), getString(R.string.error_server));
                    }
                });
                asyncTaskCommon.execute(Urls.INSERT_GROUP_POST, getValue());
            }
        }else
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
    }

    private String getValue() {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("GroupGoalId", mGroupId);
            jsonObject.put("Updatemsg", edtTopic.getText().toString());
            jsonObject.put("Photourl", "");
            jsonObject.put("user_id", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private boolean isValid() {
        if(TextUtils.isEmpty(edtTopic.getText().toString())){
            showSnackbar(binding.getRoot(), "Please enter text");
            return false;
        }
        return true;
    }

}
