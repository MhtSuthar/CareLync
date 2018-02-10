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
import com.carelynk.profile.adapter.ArticleDashboardRecyclerAdapter;
import com.carelynk.databinding.FragmentTimelineBinding;
import com.carelynk.profile.model.DashboardModel;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;

import java.util.List;

/**
 * Created by admin on 09/02/17.
 */

public class ArticleFragment extends Fragment {

    FragmentTimelineBinding binding;
    private List<DashboardModel.ArticleArray> mList;
    private ArticleDashboardRecyclerAdapter articleDashboardRecyclerAdapter;

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
        setArticleAdapter();
        if(mList.size() == 0){
            binding.txtNoData.setVisibility(View.VISIBLE);
        }
    }

    void setArticleAdapter(){
        articleDashboardRecyclerAdapter = new ArticleDashboardRecyclerAdapter(ArticleFragment.this, mList);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(articleDashboardRecyclerAdapter);
    }

    public void setmList(List<DashboardModel.ArticleArray> mList) {
        this.mList = mList;
    }

    public void onClick(String goalId) {
        Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Goal_Id, ""+goalId);
        startActivity(intent);
    }
}
