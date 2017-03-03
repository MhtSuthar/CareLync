package com.carelynk.dashboard.fragment;

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
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.databinding.FragmentTimelineBinding;
import com.carelynk.dashboard.adapter.HighlightRecyclerAdapter;
import com.carelynk.recent.model.TimelineModel;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.google.gson.JsonArray;

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
public class FollowFragment extends BaseFragment {

    FragmentTimelineBinding binding;
    private List<TimelineModel> mTimelineList;
    private HighlightRecyclerAdapter timelineRecyclerAdapter;
    private static final String TAG = "FollowFragment";

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((HomeActivity)getActivity()).hideFab();
                    binding.txtNoData.setVisibility(View.VISIBLE);
                }
            }, 500);
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
       setRecyclerAdapter(new ArrayList<TimelineModel>());
    }

    private void setRecyclerAdapter(List<TimelineModel> mList) {
        mTimelineList = new ArrayList<>();
        mTimelineList.addAll(mList);
        timelineRecyclerAdapter = new HighlightRecyclerAdapter(getActivity(), mList);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(timelineRecyclerAdapter);
    }

}
