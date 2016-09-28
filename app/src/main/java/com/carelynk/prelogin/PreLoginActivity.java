package com.carelynk.prelogin;

import android.support.v4.content.ContextCompat;
import android.os.Bundle;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.prelogin.fragment.LoginFragment;

public class PreLoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setContentView(R.layout.activity_pre_login);
        replaceFragmentWithoutAnim(new LoginFragment());
    }
}
