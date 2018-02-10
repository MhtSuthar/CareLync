package com.carelynk.dashboard;

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
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.chat.ChatListActivity;
import com.carelynk.chat.model.ChatDetailModel;
import com.carelynk.dashboard.fragment.FollowFragment;
import com.carelynk.dashboard.fragment.HighlightFragment;
import com.carelynk.databinding.ActivityRecentBinding;
import com.carelynk.dashboard.fragment.HealthFeedsFragment;
import com.carelynk.dashboard.fragment.MyGroupFragment;
import com.carelynk.invite.ContactInviteActivity;
import com.carelynk.invite.InviteActivity;
import com.carelynk.prelogin.PreLoginActivity;
import com.carelynk.profile.DashboardActivity;
import com.carelynk.profile.MyProfileActivity;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.search.DiscoverActivity;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AnimationUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    public ActivityRecentBinding binding;
    private HighlightFragment heighlightFragment;
    private MyGroupFragment myGroupFragment;
    private HealthFeedsFragment helthFeedsFragment;
    private FollowFragment followFragment;
    private SearchView.OnQueryTextListener queryTextListener;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recent);
        initToolbar();
        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);
        getRequestCount();
    }

    private void getRequestCount() {
        if(isOnline(this)){
            ApiInterface apiInterface = ApiFactory.provideInterface();
            Call<JsonObject> call = apiInterface.getHealthFeed(ApiFactory.API_BASE_URL+""+ Urls.GET_REQUEST_COUNT+""+SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject object = jsonArray.getJSONObject(0);
                            if(Integer.parseInt(object.getString("RequestCount")) > 0)
                                binding.tabs.getTabAt(2).setText("Request ("+object.getString("RequestCount")+")");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject>call, Throwable t) {
                    Log.e(TAG, t.toString());

                }
            });
        }
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        //binding.txtToolbar.setText(getString(R.string.app_name));
        setTitle("");
        setOverflowButtonColor(binding.toolbar, Color.WHITE);
        //binding.toolbar.setNavigationIcon(R.drawable.ic_launcher);
       /* binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivityResult(new Intent(HomeActivity.this, GroupCreateActivity.class), HomeActivity.this, Constants.REQUEST_ADD_GROUP);
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
        /*final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
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
        }*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_dashboard:
                moveActivity(new Intent(HomeActivity.this, DashboardActivity.class), HomeActivity.this);
                break;
            case R.id.menu_profile:
                moveActivity(new Intent(HomeActivity.this, MyProfileActivity.class), HomeActivity.this);
                break;
            case R.id.menu_chat:
                moveActivity(new Intent(HomeActivity.this, ChatListActivity.class), HomeActivity.this);
                break;
            case R.id.menu_invite:
                moveActivity(new Intent(HomeActivity.this, ContactInviteActivity.class), HomeActivity.this);
                break;
            case R.id.menu_logout:
                showAlertDialog(new OnDialogClick() {
                    @Override
                    public void onPositiveBtnClick() {
                        logoutApi();
                    }

                    @Override
                    public void onNegativeBtnClick() {

                    }
                }, getString(R.string.logout), getString(R.string.are_you_sure_logout), true);
                break;
            case R.id.menu_search:
                startActivity(new Intent(this, SearchActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.menu_discover:
                startActivity(new Intent(this, DiscoverActivity.class));
                overridePendingTransition(0,0);
                break;
        }
        return true;

    }

    void logoutApi() {
        if (isOnline(this)) {
            DialogUtils.showProgressDialog(this);
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    DialogUtils.stopProgressDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        SharedPreferenceUtil.putValue(PrefUtils.PREF_IS_LOGIN, false);
                        SharedPreferenceUtil.putValue(PrefUtils.PREF_USER_ID, "");
                        SharedPreferenceUtil.save();
                        startActivity(new Intent(HomeActivity.this, PreLoginActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL + "" + Urls.URL_LOGOUT +
                    SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        } else {
            DialogUtils.stopProgressDialog();
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
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
                heighlightFragment = new HighlightFragment();
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
                return "Request";
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.REQUEST_ADD_GROUP){
                if(myGroupFragment != null){
                    myGroupFragment.refresh();
                }
            }
        }
    }
}
