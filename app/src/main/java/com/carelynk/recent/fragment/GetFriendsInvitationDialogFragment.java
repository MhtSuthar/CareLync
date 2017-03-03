package com.carelynk.recent.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.carelynk.R;
import com.carelynk.databinding.DialogFragmentGetFriendInviteBinding;
import com.carelynk.recent.adapter.FriendInvitationListAdapter;
import com.carelynk.recent.model.TimelineModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 19-Oct-16.
 */

public class GetFriendsInvitationDialogFragment extends DialogFragment {

    private DialogFragmentGetFriendInviteBinding binding;
    private FriendInvitationListAdapter friendInvitationListAdapter;
    private List<TimelineModel> mGroupList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_get_friend_invite, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog();

        initToolbar();

        setRecyclerAdapter(getGroupList());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    private void initToolbar() {
        binding.includeToolbar.toolbarTitle.setText(getString(R.string.frirnds_list));
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initDialog() {
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ThemeDialogFit;
        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_HORIZONTAL;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void setRecyclerAdapter(List<TimelineModel> mList) {
        mGroupList = new ArrayList<>();
        mGroupList.addAll(mList);
        friendInvitationListAdapter = new FriendInvitationListAdapter(getContext(), mList);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(friendInvitationListAdapter);
    }

    private List<TimelineModel> getGroupList() {
        List<TimelineModel> mFeedList = new ArrayList<>();
        TimelineModel feedModel = new TimelineModel();
        feedModel.setName("Kishan");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Radha");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Ram..");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Sita");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Geeta");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Mohan");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Jay");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Mohit");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Jay kIshan");
        mFeedList.add(feedModel);

        feedModel = new TimelineModel();
        feedModel.setName("Amar");
        mFeedList.add(feedModel);

        return mFeedList;
    }
}
