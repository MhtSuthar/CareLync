package com.carelynk.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.TouchImageView;

/**
 * Created by Admin on 29-Mar-17.
 */

public class PreviewImageActivity extends BaseActivity {

    private static final String TAG = "PreviewImageActivity";
    private TouchImageView previewImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //statusBarColor(Color.BLACK);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setContentView(R.layout.activity_preview_image);
        fullScreen();
        init();
    }

    private void fullScreen() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    private void init() {
        previewImage = (TouchImageView) findViewById(R.id.previewImage);
        Glide.with(this).load(getIntent().getExtras().getString(Constants.EXTRA_IMAGE)).into(previewImage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
