package com.carelynk.recent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.recent.fragment.HealthFeedDetailFragment;
import com.carelynk.utilz.AppUtils;

/**
 * Created by Admin on 20-Oct-16.
 */

public class HealthFeedDetailActivity extends BaseActivity {

    public String mGoalId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fram);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupSlideWindowAnimationSlide(Gravity.BOTTOM);

        mGoalId = getIntent().getExtras().getString(AppUtils.Extra_Goal_Id);

        replaceFragmentWithoutAnim(new HealthFeedDetailFragment());
    }
}
