package com.carelynk.profile.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.dashboard.FeedDetailActivity;
import com.carelynk.databinding.FragmentTimelineBinding;
import com.carelynk.profile.adapter.FollowingDashboardRecyclerAdapter;
import com.carelynk.profile.adapter.QuestionDashboardRecyclerAdapter;
import com.carelynk.profile.model.DashboardModel;
import com.carelynk.utilz.AppUtils;

import java.util.List;

/**
 * Created by admin on 09/02/17.
 */

public class QuestionFragment extends Fragment {

    FragmentTimelineBinding binding;
    private List<DashboardModel.QuestionArray> mList;
    private QuestionDashboardRecyclerAdapter questionDashboardRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipeRefreshLayout.setRefreshing( false );
        binding.swipeRefreshLayout.setEnabled( false );
        setAdapter();
        if(mList.size() == 0){
            binding.txtNoData.setVisibility(View.VISIBLE);
        }
    }

    void setAdapter(){
        questionDashboardRecyclerAdapter = new QuestionDashboardRecyclerAdapter(QuestionFragment.this, mList);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(questionDashboardRecyclerAdapter);
    }

    public void setmList(List<DashboardModel.QuestionArray> mList) {
        this.mList = mList;
    }

    public void onClick(String goalId) {
        Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Goal_Id, ""+goalId);
        startActivity(intent);
    }
}
