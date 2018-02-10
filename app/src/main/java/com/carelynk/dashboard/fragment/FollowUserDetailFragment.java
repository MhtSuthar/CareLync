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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.dashboard.adapter.CommentRecyclerAdapter;
import com.carelynk.databinding.DialogCommentListBinding;
import com.carelynk.databinding.DialogFollowUserDetailBinding;
import com.carelynk.utilz.CircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 19-Oct-16.
 */

public class FollowUserDetailFragment extends DialogFragment {

    private DialogFollowUserDetailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_follow_user_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog();

        initToolbar();

        Glide.with(this).load(R.drawable.ic_default_avatar).apply(RequestOptions.circleCropTransform()).into(binding.imgUser);
    }

    private void initToolbar() {
        binding.includeToolbar.toolbarTitle.setText("Name User");
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

}
