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
import com.carelynk.databinding.FragmentMyGroupBinding;
import com.carelynk.recent.RecentActivity;
import com.carelynk.recent.adapter.MyForumRecyclerAdapter;
import com.carelynk.recent.model.TimelineModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12-Sep-16.
 */
public class MyForumFragment extends BaseFragment {

    FragmentMyGroupBinding binding;
    private List<TimelineModel> mForumList;
    private MyForumRecyclerAdapter mMyForumRecyclerAdapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            ((RecentActivity)getActivity()).showFab();
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
        setRecyclerAdapter(getForum());


    }

    private void setRecyclerAdapter(List<TimelineModel> mList) {
        mForumList = new ArrayList<>();
        mForumList.addAll(mList);
        mMyForumRecyclerAdapter = new MyForumRecyclerAdapter(getActivity(), mList);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mMyForumRecyclerAdapter);
    }

    private List<TimelineModel> getForum() {
        List<TimelineModel> mFeedList = new ArrayList<>();
        TimelineModel feedModel = new TimelineModel();
        feedModel.setName("Sunlight Bad or Good?");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Meditation takes you high");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Yoga");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Life and Nature");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Join Us");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Do or Die");
        mFeedList.add(feedModel);
        return mFeedList;
    }

}
