package com.carelynk.dashboard;

import android.app.SearchManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.fragment.FollowFragment;
import com.carelynk.databinding.ActivityRecentBinding;
import com.carelynk.dashboard.fragment.HeighlightFragment;
import com.carelynk.dashboard.fragment.HealthFeedsFragment;
import com.carelynk.dashboard.fragment.MyGroupFragment;
import com.carelynk.event.EventListActivity;
import com.carelynk.profile.MyProfileActivity;
import com.carelynk.search.MySearchActivity;
import com.carelynk.utilz.AnimationUtils;

public class HomeActivity extends BaseActivity {

    public ActivityRecentBinding binding;
    private HeighlightFragment heighlightFragment;
    private MyGroupFragment myGroupFragment;
    private HealthFeedsFragment helthFeedsFragment;
    private FollowFragment followFragment;
    private SearchView.OnQueryTextListener queryTextListener;

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
        binding.txtToolbar.setText(getString(R.string.app_name));
        setTitle("");
        setOverflowButtonColor(binding.toolbar, ContextCompat.getColor(this, R.color.colorAccent));
        binding.toolbar.setNavigationIcon(R.drawable.ic_comment);
       /* binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(new Intent(HomeActivity.this, GroupCreateActivity.class), HomeActivity.this);
            }
        });
    }

    public void setOverflowButtonColor(final Toolbar toolbar, final int color) {
        Drawable drawable = toolbar.getOverflowIcon();
        if(drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), color);
            toolbar.setOverflowIcon(drawable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.base, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    if (TextUtils.isEmpty(newText)){
                        searchAttempt("");
                    }else{
                        searchAttempt(newText);
                    }
                    return false;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.e("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_event:
                moveActivity(new Intent(HomeActivity.this, EventListActivity.class), HomeActivity.this);
                break;
            case R.id.menu_search_google:
                moveActivity(new Intent(HomeActivity.this, MySearchActivity.class), HomeActivity.this);
                break;
            case R.id.menu_profile:
                moveActivity(new Intent(HomeActivity.this, MyProfileActivity.class), HomeActivity.this);
                break;
            case R.id.menu_invite:
                moveActivity(new Intent(HomeActivity.this, MyProfileActivity.class), HomeActivity.this);
                break;
        }
        return true;

    }

    private void searchAttempt(String newText) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        //viewPager.setCurrentItem(getIntent().getExtras().getInt(Constants.BUNDLE_WHICH_LINK));
        viewPager.setCurrentItem(0);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 3){
                heighlightFragment = new HeighlightFragment();
                return heighlightFragment;
            }else if(position == 1){
                myGroupFragment = new MyGroupFragment();
                return myGroupFragment;
            }else if(position == 0){
                helthFeedsFragment = new HealthFeedsFragment();
                return helthFeedsFragment;
            }else if(position == 2){
                followFragment = new FollowFragment();
                return followFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 3){
                return "Highlights";
            }else if(position == 1){
                return "Care Group";
            } else if(position == 0){
                return "Health Feed";
            }else if(position == 2){
                return "Follow";
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
