package com.carelynk.recent.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.databinding.ActivityGroupTopicListBinding;
import com.carelynk.recent.HealthFeedDetailActivity;
import com.carelynk.recent.adapter.HealthFeedDetailRecyclerAdapter;
import com.carelynk.recent.model.HealthFeedDetailModel;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.AsyncTaskPostCommon;
import com.carelynk.rest.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12-Sep-16.
 */
public class HealthFeedDetailFragment extends BaseFragment {

    private ActivityGroupTopicListBinding binding;
    private HealthFeedDetailRecyclerAdapter healthFeedDetailRecyclerAdapter;
    private List<HealthFeedDetailModel> mGroupList;
    private AsyncTaskGetCommon asyncTaskGetCommon;
    private ProgressBar mProgressBarHeader;
    private  EditText edtDesc, edtTopic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_group_topic_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();

        setRecyclerAdapter();

        getHealthFeedDetail();
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.includeToolbar.toolbar);
        binding.includeToolbar.toolbarTitle.setText(getString(R.string.goal_detail));
        ((AppCompatActivity) getActivity()).setTitle("");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HealthFeedDetailActivity) getActivity()).onBackPressed();
            }
        });
    }

    void getHealthFeedDetail() {
        if (isOnline(getContext())) {
            asyncTaskGetCommon = new AsyncTaskGetCommon(getContext(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    try {
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
                    }
                }
            });
            asyncTaskGetCommon.execute(Urls.GET_GOAL_POST + "/"+((HealthFeedDetailActivity)getActivity()).mGoalId);
        } else {
            mProgressBarHeader.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    private void setRecyclerAdapter() {
        mGroupList = new ArrayList<>();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        /**
         * Set Header
         */
        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_health_feed, binding.recyclerView, false);

        mProgressBarHeader = (ProgressBar) header.findViewById(R.id.progressBar);
        edtTopic = (EditText) header.findViewById(R.id.edtTopic);
        TextInputLayout textLayout  = (TextInputLayout) header.findViewById(R.id.textLayout);
        textLayout.setHint("Add an update for this Health Feed");
        edtTopic.setHint("Add an update for this Health Feed");
        edtDesc = (EditText) header.findViewById(R.id.edtDesc);
        edtDesc.setVisibility(View.GONE);
        AppCompatButton btnPost = (AppCompatButton) header.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertGoalPost();
            }
        });

        healthFeedDetailRecyclerAdapter = new HealthFeedDetailRecyclerAdapter(header, getContext(), mGroupList);
        binding.recyclerView.setAdapter(healthFeedDetailRecyclerAdapter);
    }

    void insertGoalPost(){
        if(isOnline(getActivity())){
            if(isValid()) {
                AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(getActivity(), new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
                    @Override
                    public void onTaskComplete(String result) {
                        if (result.length() > 0) {

                        } else
                            showSnackbar(binding.getRoot(), getString(R.string.error_server));
                    }
                });
                asyncTaskCommon.execute(Urls.INSERT_GOAL_POST, getValue());
            }
        }else
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
    }

    private String getValue() {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("GoalId ", ((HealthFeedDetailActivity)getActivity()).mGoalId);
            jsonObject.put("Updatemsg", edtTopic.getText().toString());
            jsonObject.put("Photourl", "");
            //jsonObject.put("user_id", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
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
