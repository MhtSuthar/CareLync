package com.carelynk.recent.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.databinding.FragmentTimelineBinding;
import com.carelynk.recent.RecentActivity;
import com.carelynk.recent.adapter.MyTimelineRecyclerAdapter;
import com.carelynk.recent.model.TimelineModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12-Sep-16.
 */
public class MyTimelineFragment extends BaseFragment {

    FragmentTimelineBinding binding;
    private List<TimelineModel> mTimelineList;
    private MyTimelineRecyclerAdapter timelineRecyclerAdapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            ((RecentActivity)getActivity()).hideFab();
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
       setRecyclerAdapter(getFeedList());

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                    ((RecentActivity)getActivity()).hideFab();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private List<TimelineModel> getFeedList() {
        List<TimelineModel> mFeedList = new ArrayList<>();
        TimelineModel feedModel = new TimelineModel();
        feedModel.setName("Jack");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Mac");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Sultan");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Green And Nature");
        mFeedList.add(feedModel);
        return mFeedList;
    }

    private void setRecyclerAdapter(List<TimelineModel> mList) {
        mTimelineList = new ArrayList<>();
        mTimelineList.addAll(mList);
        timelineRecyclerAdapter = new MyTimelineRecyclerAdapter(getActivity(), mList);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(timelineRecyclerAdapter);
    }

}
