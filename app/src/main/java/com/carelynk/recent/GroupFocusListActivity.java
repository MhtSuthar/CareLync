package com.carelynk.recent;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.databinding.ActivityGroupTopicListBinding;
import com.carelynk.recent.adapter.GroupTopicListAdapter;
import com.carelynk.recent.model.TimelineModel;
import com.carelynk.utilz.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 19-Oct-16.
 */

public class GroupFocusListActivity extends BaseActivity {

    private ActivityGroupTopicListBinding binding;
    private GroupTopicListAdapter groupTopicListAdapter;
    private List<TimelineModel> mGroupList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_topic_list);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupSlideWindowAnimationFadeSlide(Gravity.TOP);

        initToolbar();

        setRecyclerAdapter(getGroupList());

        binding.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(binding.recyclerView, new RecyclerTouchListener.OnRecyclerClickListener() {
            @Override
            public void onClick(View view, final int position) {
                moveActivity(new Intent(GroupFocusListActivity.this, GroupDetailListActivity.class), GroupFocusListActivity.this);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void initToolbar() {
        setSupportActionBar(binding.includeToolbar.toolbar);
        binding.includeToolbar.toolbarTitle.setText(getString(R.string.group_focus));
        setTitle("");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setRecyclerAdapter(List<TimelineModel> mList) {
        mGroupList = new ArrayList<>();
        mGroupList.addAll(mList);
        groupTopicListAdapter = new GroupTopicListAdapter(this, mList);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(groupTopicListAdapter);
    }

    private List<TimelineModel> getGroupList() {
        List<TimelineModel> mFeedList = new ArrayList<>();
        TimelineModel feedModel = new TimelineModel();
        feedModel.setName("Nature and Suns");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Helth is welth");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Meditation..");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Green And Nature");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Green And Nature--");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Green And Nature0");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Green And Nature1");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Green And Nature2");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Green And Nature3");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Green And Nature4");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Green And Nature5");
        mFeedList.add(feedModel);
        return mFeedList;
    }
}
