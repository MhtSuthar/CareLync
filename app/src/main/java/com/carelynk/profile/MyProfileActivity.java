package com.carelynk.profile;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.databinding.ActivityHomeBinding;
import com.carelynk.databinding.ActivityMyProfileBinding;

/**
 * Created by Admin on 15-Sep-16.
 */
public class MyProfileActivity extends BaseActivity {

    private ActivityMyProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupSlideWindowAnimationSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);
        init();
    }

    void init(){
        /**
         * For image draw over status bar
         */
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            binding.imgBack.setPadding(getStatusBarHeight()-10, getStatusBarHeight()+10, getStatusBarHeight(), getStatusBarHeight());
        }

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
