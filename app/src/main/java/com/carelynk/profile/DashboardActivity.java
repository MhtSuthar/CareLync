package com.carelynk.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.databinding.ActivityDashboardBinding;
import com.carelynk.profile.fragment.AnswersFragment;
import com.carelynk.profile.fragment.ArticleFragment;
import com.carelynk.profile.fragment.FollowersFragment;
import com.carelynk.profile.fragment.FollowingFragment;
import com.carelynk.profile.fragment.ProfileFragment;
import com.carelynk.profile.fragment.QuestionFragment;
import com.carelynk.profile.model.DashboardModel;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends BaseActivity {

    private ActivityDashboardBinding binding;

    private static final String TAG = "DashboardActivity";
    private String mUserId;
    private ArticleFragment articleFragment;
    private AnswersFragment answersFragment;
    private QuestionFragment questionFragment;
    private FollowersFragment followersFragment;
    private FollowingFragment followingFragment;
    private ProfileFragment profileFragment;
    private DashboardModel mDashboardModel;
    private String mOwnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        init();
        getUserAllData();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(6);
        viewPager.setCurrentItem(0);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                profileFragment = new ProfileFragment();
                profileFragment.setmList(mDashboardModel.getResult().getProfileDetail());
                return profileFragment;
            } else if(position == 1){
                articleFragment = new ArticleFragment();
                articleFragment.setmList(mDashboardModel.getResult().getArticleArray());
                return articleFragment;
            }else if(position == 2){
                questionFragment = new QuestionFragment();
                questionFragment.setmList(mDashboardModel.getResult().getQuestionArray());
                return questionFragment;
            }else if(position == 3){
                answersFragment = new AnswersFragment();
                answersFragment.setmList(mDashboardModel.getResult().getAnswerArray());
                return answersFragment;
            }else if(position == 4){
                followersFragment = new FollowersFragment();
                followersFragment.setmList(mDashboardModel.getResult().getFollowerArray());
                return followersFragment;
            }else if(position == 5){
                followingFragment = new FollowingFragment();
                followingFragment.setmList(mDashboardModel.getResult().getFollowingArray());
                return followingFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "Profile";
            } else if(position == 1){
                return "Article ("+mDashboardModel.getResult().getArticleArray().size()+")";
            }else if(position == 2){
                return "Question ("+mDashboardModel.getResult().getQuestionArray().size()+")";
            }else if(position == 3){
                return "Answers ("+mDashboardModel.getResult().getAnswerArray().size()+")";
            }else if(position == 4){
                return "Followers ("+mDashboardModel.getResult().getFollowerArray().size()+")";
            }else if(position == 5){
                return "Following ("+mDashboardModel.getResult().getFollowingArray().size()+")";
            }
            return "";
        }
    }

    private void init() {
        //binding.txtToolbar.setText("Dashboard");
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mOwnerId = SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "");
        if(getIntent().hasExtra(Constants.EXTRA_USERID)){
            mUserId = getIntent().getExtras().getString(Constants.EXTRA_USERID);
            if(mUserId.equalsIgnoreCase(SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "")))
                binding.txtFollow.setVisibility(View.INVISIBLE);
            else
                binding.txtFollow.setVisibility(View.VISIBLE);
        }else{
            mUserId = SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "");
        }

        binding.txtFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.txtFollow.getText().toString().equalsIgnoreCase("Follow")){
                    followUser();
                }else if(binding.txtFollow.getText().toString().equalsIgnoreCase("UnFollow")){
                    unFollowUser();
                }
            }
        });
    }

    private void unFollowUser() {
        if(isOnline(this)){
            showProgressDialog();
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    stopProgressDialog();
                    try{
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getJSONObject("result").getBoolean("IsSuccess")) {
                            getUserAllData();
                        }else{
                            showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.UNFOLLOW_FRIEND+"?FromUserId="+mOwnerId+"&ToUserId="+mUserId);

        }else{
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    private void followUser() {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("FromUserId", mOwnerId);
            payerReg.addProperty("ToUserId", mUserId);
            Log.e(TAG, "create update: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.followFriends(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getJSONObject("result").getBoolean("IsSuccess")) {
                                getUserAllData();
                            }else{
                                showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject>call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        }else{
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    public void getUserAllData() {
        if(isOnline(this)){
            showProgressDialog();
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    stopProgressDialog();
                    try {
                        mDashboardModel = new Gson().fromJson(result, DashboardModel.class);
                        if (mDashboardModel.getResult() != null) {
                            binding.txtName.setText("" + mDashboardModel.getResult().getDashboardDetail().getName());
                            binding.txtDesc.setText("" + mDashboardModel.getResult().getDashboardDetail().getExpertise());
                            Glide.with(DashboardActivity.this).
                                    load(AppUtils.getImagePath(mDashboardModel.getResult().getDashboardDetail().getProfilePicUrl())).
                                    apply(new RequestOptions().placeholder(R.drawable.ic_placeholder))
                                    .into(binding.backdrop);
                            setupViewPager(binding.viewpager);
                            binding.tabs.setupWithViewPager(binding.viewpager);
                            if (mDashboardModel.getResult().getDashboardDetail().getRequestSent().equals("0") &&
                                    !mDashboardModel.getResult().getDashboardDetail().getUserId().equals(mDashboardModel.getResult().getDashboardDetail().getOwnUserId())
                                    && mDashboardModel.getResult().getDashboardDetail().getFollowings().equals("0")) {
                                binding.txtFollow.setText("Follow");
                            } else if (mDashboardModel.getResult().getDashboardDetail().getRequestSent().equals("1") &&
                                    !mDashboardModel.getResult().getDashboardDetail().getUserId().equals(mDashboardModel.getResult().getDashboardDetail().getOwnUserId())
                                    && mDashboardModel.getResult().getDashboardDetail().getFollowings().equals("0")) {
                                binding.txtFollow.setText("Pending");
                            } else if (mDashboardModel.getResult().getDashboardDetail().getRequestSent().equals("0") &&
                                    !mDashboardModel.getResult().getDashboardDetail().getUserId().equals(mDashboardModel.getResult().getDashboardDetail().getOwnUserId())
                                    && mDashboardModel.getResult().getDashboardDetail().getFollowings().equals("1")) {
                                binding.txtFollow.setText("UnFollow");
                            }
                        } else {

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_USER_DASHBOARD+"?UserId="+mUserId+"&OwnuserId="+mOwnerId);
        }else{
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }
}
