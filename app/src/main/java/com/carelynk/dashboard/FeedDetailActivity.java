package com.carelynk.dashboard;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.adapter.FeedDetailRecyclerAdapter;
import com.carelynk.dashboard.fragment.CommentDialogFragment;
import com.carelynk.dashboard.model.HealthFeedModel;
import com.carelynk.databinding.ActivityFeedDetailBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 06-Mar-17.
 */

public class FeedDetailActivity extends BaseActivity {

    ActivityFeedDetailBinding binding;
    private List<String> mFeedDetail;
    private FeedDetailRecyclerAdapter feedDetailRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feed_detail);
        init();
        setRecyclerAdapter();
    }

    public void showCommnetDialog() {
        DialogFragment newFragment = new CommentDialogFragment();
        //newFragment.setTargetFragment(this, Constants.REQUEST_CODE_SEND_FRIEND);
        newFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }


    private void init() {
        //setSupportActionBar(binding.includeToolbar.toolbar);
        binding.includeToolbar.toolbarTitle.setText("Feed Details");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setRecyclerAdapter() {
        mFeedDetail = new ArrayList<>();

        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        /**
         * Set Header
         */
        View header = LayoutInflater.from(this).inflate(R.layout.header_feed_detail, binding.recyclerView, false);
        LinearLayout linFooter = (LinearLayout) header.findViewById(R.id.linFooter);
        LinearLayout linSupport = (LinearLayout) header.findViewById(R.id.linSupport);


        feedDetailRecyclerAdapter = new FeedDetailRecyclerAdapter(header, this, getDummyList(), FeedDetailActivity.this);
        binding.recyclerView.setAdapter(feedDetailRecyclerAdapter);
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

}
