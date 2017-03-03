package com.carelynk.dashboard.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.dashboard.adapter.HealthFeedRecyclerAdapter;
import com.carelynk.databinding.FragmentMyGroupBinding;
import com.carelynk.recent.HealthFeedDetailActivity;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.recent.model.HealthFeedModel;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.AsyncTaskPostCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.PrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12-Sep-16.
 */
public class HealthFeedsFragment extends BaseFragment {

    FragmentMyGroupBinding binding;
    private List<HealthFeedModel> mHealthFeedList;
    private HealthFeedRecyclerAdapter myHealthFeedRecyclerAdapter;
    public AsyncTaskGetCommon asyncTaskGetCommon;
    private static final String TAG = "HealthFeedsFragment";
    private ProgressBar mProgressBarHeader;
    private EditText edtTopic, edtDescription;
    private ImageView imgSelect;
    private AppCompatButton btnPost;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mHealthFeedList == null || mHealthFeedList.size() == 0)
                        getHealthFeed();
                    ((HomeActivity) getActivity()).hideFab();
                }
            }, 500);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_group, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.progressBar.setVisibility(View.GONE);
        setRecyclerAdapter();
    }

    void getHealthFeed() {
        if (isOnline(getContext())) {
            asyncTaskGetCommon = new AsyncTaskGetCommon(getContext(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            HealthFeedModel helthFeedModel = new HealthFeedModel();
                            helthFeedModel.CreatedDate = object.getString("CreatedDate");
                            helthFeedModel.Desc = object.getString("Desc");
                            helthFeedModel.GoalId = "" + object.getInt("GoalId");
                            helthFeedModel.GoalName = object.getString("GoalName");
                            helthFeedModel.PhotoURL = object.getString("PhotoURL");
                            helthFeedModel.UserId = object.getString("UserId");
                            helthFeedModel.UserName = object.getString("UserName");
                            helthFeedModel.GoalStatusId = "" + object.getInt("GoalStatusId");
                            helthFeedModel.GoalType = object.getBoolean("GoalType");
                            mHealthFeedList.add(helthFeedModel);
                        }
                        mProgressBarHeader.setVisibility(View.GONE);
                        myHealthFeedRecyclerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(Urls.GET_HELTH_FEED + "/0");
        } else {
            mProgressBarHeader.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }


    private void setRecyclerAdapter() {
        mHealthFeedList = new ArrayList<>();

        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        /**
         * Set Header
         */
        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_health_feed, binding.recyclerView, false);

        mProgressBarHeader = (ProgressBar) header.findViewById(R.id.progressBar);
        edtTopic = (EditText) header.findViewById(R.id.edtTopic);
        edtDescription = (EditText) header.findViewById(R.id.edtDesc);
        btnPost = (AppCompatButton) header.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptCreateGoal();
            }
        });
        imgSelect = (ImageView) header.findViewById(R.id.imgSelect);
        imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        myHealthFeedRecyclerAdapter = new HealthFeedRecyclerAdapter(header, getActivity(), getDummyList(), HealthFeedsFragment.this);
        binding.recyclerView.setAdapter(myHealthFeedRecyclerAdapter);
    }

    private List<HealthFeedModel> getDummyList() {
        List<HealthFeedModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HealthFeedModel healthFeedModel = new HealthFeedModel();
            healthFeedModel.Desc = "Lorem ipsum detail is the. Demo text will be used";
            healthFeedModel.GoalId = "10";
            healthFeedModel.UserName = "jason";
            healthFeedModel.GoalName = "GOal Name";
            healthFeedModel.GoalType = false;
            list.add(healthFeedModel);
        }
        return list;
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), HealthFeedDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Goal_Id, mHealthFeedList.get(position).GoalId);
        moveActivity(intent, getActivity(), false);
    }

    void attemptCreateGoal(){
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
                asyncTaskCommon.execute(Urls.CREATE_GOAL, getPostValue());
            }
        }else
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
    }

    private String getPostValue() {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("GoalName", edtTopic.getText().toString());
            jsonObject.put("Desc", edtDescription.getText().toString());
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
