package com.carelynk.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.dashboard.fragment.CommentDialogFragment;
import com.carelynk.event.adapter.EventListAdapter;
import com.carelynk.event.fragment.EventDetailDialogFragment;
import com.carelynk.prelogin.PreLoginActivity;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 24-Dec-16.
 */

public class EventListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private Toolbar toolbar;
    private TextView txtToolbar;
    private ProgressBar progressBar;
    private EventListAdapter eventListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);

        initView();
        setRecyclerAdapter();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        txtToolbar.setText(getString(R.string.my_event));
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.addFab);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventListActivity.this, EventCreateActivity.class);
                moveActivity(intent, EventListActivity.this);
            }
        });
    }

    private void setRecyclerAdapter() {
        eventListAdapter = new EventListAdapter(this, getDummyList(), EventListActivity.this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(eventListAdapter);
    }

    private List<String> getDummyList() {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        return list;
    }

    void getHealthFeedDetail() {
        if (isOnline(this)) {

        } else {
            progressBar.setVisibility(View.GONE);
           // showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    public void onEditClick(int position) {
        Intent intent = new Intent(EventListActivity.this, EventCreateActivity.class);
        intent.putExtra(Constants.EXTRA_IS_FOR_EDIT_EVENT, true);
        moveActivity(intent, EventListActivity.this);
    }

    public void onDeleteClick(int position) {
        showAlertDialog(new OnDialogClick() {
            @Override
            public void onPositiveBtnClick() {

            }

            @Override
            public void onNegativeBtnClick() {

            }
        }, getString(R.string.delete), getString(R.string.are_you_sure_delete), true);
    }

    public void onClickDetail(int position) {
        DialogFragment newFragment = new EventDetailDialogFragment();
        //newFragment.setTargetFragment(this, Constants.REQUEST_CODE_SEND_FRIEND);
        newFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }
}
