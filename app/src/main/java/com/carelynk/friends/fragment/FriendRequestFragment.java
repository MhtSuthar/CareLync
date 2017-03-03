package com.carelynk.friends.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.databinding.FragmentMyGroupBinding;
import com.carelynk.friends.adapter.FriendRequestAdapter;
import com.carelynk.recent.model.TimelineModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12-Sep-16.
 */
public class FriendRequestFragment extends BaseFragment {

    FragmentMyGroupBinding binding;
    private List<TimelineModel> mForumList;
    private FriendRequestAdapter friendRequestAdapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
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
        friendRequestAdapter = new FriendRequestAdapter(getActivity(), mList);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(friendRequestAdapter);
    }

    private List<TimelineModel> getForum() {
        List<TimelineModel> mFeedList = new ArrayList<>();
        TimelineModel feedModel = new TimelineModel();
        feedModel.setName("John");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Johny");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Janardhan");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Gabbar");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Join Us");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Teja");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Anand");
        mFeedList.add(feedModel);

        return mFeedList;
    }

}
