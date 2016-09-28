package com.carelynk.dashboard;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.carelynk.R;
import com.carelynk.base.BaseActivityWithSidebar;
import com.carelynk.databinding.ActivityHomeBinding;
import com.carelynk.recent.RecentActivity;
import com.carelynk.profile.MyProfileActivity;
import com.carelynk.utilz.AnimationUtils;
import com.carelynk.utilz.Constants;

/**
 * Created by Admin on 15-Sep-16.
 */
public class HomeActivity extends BaseActivityWithSidebar {

    private ActivityHomeBinding binding;
    private LayoutInflater inflater;
    private AnimationUtils animationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        inflater = LayoutInflater.from(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_home, getMiddleContent(), true);
        init();
    }

    void init(){
        initTabAnim();
        binding.viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(new Intent(HomeActivity.this, MyProfileActivity.class), HomeActivity.this);
            }
        });

        binding.viewTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RecentActivity.class);
                intent.putExtra(Constants.BUNDLE_WHICH_LINK, 0);
                moveActivity(intent, HomeActivity.this);
            }
        });

        binding.viewMyGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RecentActivity.class);
                intent.putExtra(Constants.BUNDLE_WHICH_LINK, 1);
                moveActivity(intent, HomeActivity.this);
            }
        });

        binding.viewMyForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RecentActivity.class);
                intent.putExtra(Constants.BUNDLE_WHICH_LINK, 2);
                moveActivity(intent, HomeActivity.this);
            }
        });
    }

    private void initTabAnim() {
        animationUtils = new AnimationUtils();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animationUtils.animateIn(binding.linDashboard);
            }
        }, ANIM_TIME);
    }
}
