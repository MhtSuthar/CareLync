package com.carelynk.dashboard.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.dashboard.FeedDetailActivity;
import com.carelynk.dashboard.model.HighlightModel;
import com.carelynk.databinding.FragmentTimelineBinding;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.dashboard.adapter.HighlightRecyclerAdapter;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.utilz.AppUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12-Sep-16.
 */
public class HighlightFragment extends BaseFragment {

    FragmentTimelineBinding binding;
    private List<HighlightModel> mTimelineList  = new ArrayList<>();;
    private HighlightRecyclerAdapter timelineRecyclerAdapter;
    private static final String TAG = "HighlightFragment";

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((HomeActivity)getActivity()).hideFab();
                    if (mTimelineList == null || mTimelineList.size() == 0)
                        getHighlights();
                    binding.txtNoData.setVisibility(View.GONE);
                }
            }, 500);
        }
    }

    void getHighlights() {
        if (isOnline(getContext())) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            Call<JsonObject> call = apiInterface.getHealthFeed();
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try{
                              JSONObject jsonObject = new JSONObject(response.body().toString());
                              JSONArray jsonArray = jsonObject.getJSONArray("result");
                              //JSONArray jsonArray = new JSONArray(response.body().toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //[{"mMainGroupID":1,"MainGroupName":"Heath"},
                                JSONObject object = jsonArray.getJSONObject(i);
                                HighlightModel highlightModel = new HighlightModel();
                                highlightModel.AnswerCount = object.getInt("AnswerCount");
                                highlightModel.CreatedDate = object.getString("CreatedDate");
                                highlightModel.Desc = object.getString("Desc");
                                highlightModel.GoalId = object.getInt("GoalId");
                                highlightModel.GoalName = object.getString("GoalName");
                                highlightModel.GoalStatusId = object.getInt("GoalStatusId");
                                highlightModel.PhotoURL = object.getString("PhotoURL");
                                highlightModel.PostType = object.getString("PostType");
                                highlightModel.SupportCount = object.getInt("SupportCount");
                                highlightModel.UserId = object.getString("UserId");
                                highlightModel.UserName = object.getString("UserName");
                                mTimelineList.add(highlightModel);
                            }
                            binding.progressBar.setVisibility(View.GONE);
                            setRecyclerAdapter();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject>call, Throwable t) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.txtNoData.setVisibility(View.VISIBLE);
                    Log.e(TAG, t.toString());
                }
            });
        } else {
            binding.progressBar.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setRecyclerAdapter() {
        timelineRecyclerAdapter = new HighlightRecyclerAdapter(getActivity(), mTimelineList, HighlightFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(timelineRecyclerAdapter);
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Goal_Detail, mTimelineList.get(position));
        moveActivity(intent, getActivity(), false);
    }
}
