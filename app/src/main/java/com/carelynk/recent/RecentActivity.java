package com.carelynk.recent;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.databinding.ActivityRecentBinding;
import com.carelynk.recent.fragment.MyForumFragment;
import com.carelynk.recent.fragment.MyGroupFragment;
import com.carelynk.recent.fragment.MyTimelineFragment;
import com.carelynk.utilz.AnimationUtils;
import com.carelynk.utilz.Constants;

public class RecentActivity extends BaseActivity {

    public ActivityRecentBinding binding;
    private MyTimelineFragment myTimelineFragment;
    private MyGroupFragment myGroupFragment;
    private MyForumFragment myForumFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recent);
        initToolbar();
        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.txtToolbar.setText(getString(R.string.my_timeline));
        setTitle("");
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(getIntent().getExtras().getInt(Constants.BUNDLE_WHICH_LINK));
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                myTimelineFragment = new MyTimelineFragment();
                return myTimelineFragment;
            }else if(position == 1){
                myGroupFragment = new MyGroupFragment();
                return myGroupFragment;
            }else if(position == 2){
                myForumFragment = new MyForumFragment();
                return myForumFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "My Timeline";
            }else if(position == 1){
                return "My Groups";
            } else if(position == 2){
                return "My Forums";
            }
            return "";
        }
    }

    public void hideFab(){
        if(binding.fabAdd.isShown())
            AnimationUtils.animateScaleIn(binding.fabAdd);
    }

    public void showFab(){
        if(!binding.fabAdd.isShown())
            AnimationUtils.animateScaleOut(binding.fabAdd);
    }
}
