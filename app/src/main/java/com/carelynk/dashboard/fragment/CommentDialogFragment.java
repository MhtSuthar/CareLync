package com.carelynk.dashboard.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.carelynk.R;
import com.carelynk.dashboard.adapter.CommentRecyclerAdapter;
import com.carelynk.dashboard.model.GroupModel;
import com.carelynk.databinding.DialogCommentListBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 19-Oct-16.
 */

public class CommentDialogFragment extends DialogFragment {

    private DialogCommentListBinding binding;
    private List<String> mGroupList;
    private CommentRecyclerAdapter commentRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_comment_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog();

        initToolbar();

       setRecyclerAdapter(getGroupList());
    }

    private void initToolbar() {
        binding.includeToolbar.toolbarTitle.setText(getString(R.string.comment));
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void setRecyclerAdapter(List<String> mList) {
        commentRecyclerAdapter = new CommentRecyclerAdapter(getContext(), mList);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(commentRecyclerAdapter);
    }

    private List<String> getGroupList() {
        List<String> mFeedList = new ArrayList<>();
        mFeedList.add("Kishan");
        mFeedList.add("Radha");
        mFeedList.add("Ram..");
        mFeedList.add("Sita");
        mFeedList.add("Geeta");
        mFeedList.add("Jay");
        mFeedList.add("Mohit");
        return mFeedList;
    }
}
