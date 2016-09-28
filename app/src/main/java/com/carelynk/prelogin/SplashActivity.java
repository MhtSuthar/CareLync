package com.carelynk.prelogin;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.databinding.ActivitySplashBinding;

/**
 * Created by Admin on 15-Sep-16.
 */
public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        //setContentView(R.layout.activity_splash);
        animScaleOut();
        startTimer();
    }

    private void animScaleOut() {
        ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(1000);
        fade_in.setFillAfter(true);
        findViewById(R.id.txtAppName).startAnimation(fade_in);
    }

    void startTimer(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, PreLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
