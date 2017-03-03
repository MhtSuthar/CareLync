package com.carelynk.friends;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.databinding.ActivityMyFriendsBinding;
import com.carelynk.friends.fragment.FriendRequestFragment;
import com.carelynk.friends.fragment.MyFriendsFragment;
import com.carelynk.friends.fragment.SendFriendsRequestDialogFragment;

/**
 * Created by Admin on 20-Oct-16.
 */

public class MyFriendsActivity extends BaseActivity {

    private ActivityMyFriendsBinding binding;
    private MyFriendsFragment myFriendsFragment;
    private FriendRequestFragment friendRequestFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_friends);

        init();

        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);

        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    void showDialog() {
        DialogFragment newFragment = new SendFriendsRequestDialogFragment();
        //newFragment.setTargetFragment(this, Constants.REQUEST_CODE_SEND_FRIEND);
        newFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }

    void init(){
        setSupportActionBar(binding.toolbar);
        binding.txtToolbar.setText(getString(R.string.my_friends));
        setTitle("");
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                myFriendsFragment = new MyFriendsFragment();
                return myFriendsFragment;
            }else if(position == 1){
                friendRequestFragment = new FriendRequestFragment();
                return friendRequestFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "My Friends";
            }else if(position == 1){
                return "Friends Request";
            }
            return "";
        }
    }
}
