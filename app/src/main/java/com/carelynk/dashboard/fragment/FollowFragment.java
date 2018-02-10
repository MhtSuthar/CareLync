package com.carelynk.dashboard.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.dashboard.adapter.FollowRecyclerAdapter;
import com.carelynk.dashboard.adapter.FollowingRecyclerAdapter;
import com.carelynk.dashboard.adapter.FriendRequestRecyclerAdapter;
import com.carelynk.dashboard.model.FollowRequestModel;
import com.carelynk.dashboard.model.FollowersModel;
import com.carelynk.dashboard.model.FollowingModel;
import com.carelynk.dashboard.model.ReuestGroupModel;
import com.carelynk.databinding.FragmentFollowBinding;
import com.carelynk.profile.DashboardActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12-Sep-16.
 */
public class FollowFragment extends BaseFragment {

    FragmentFollowBinding binding;
    private List<String> mTimelineList;
    private static final String TAG = "FollowersFragment";
    private FollowingRecyclerAdapter followingRecyclerAdapter;
    private FollowRecyclerAdapter followRecyclerAdapter;
    private FriendRequestRecyclerAdapter mFriendRequestRecyclerAdapter;
    private List<FollowersModel.Result> mListFollow = new ArrayList<>();
    private List<FollowingModel.Result> mListFollowing = new ArrayList<>();
    private List<HashMap<String, String>> mListFollowRequest;
    private FollowersModel followersModel;
    private FollowingModel followingModel;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((HomeActivity)getActivity()).hideFab();
                    if(mListFollow.size() == 0)
                        getFollowList();
                    if(mListFollowRequest == null)
                        getFriendRequestOfGroup();
                    //binding.txtNoData.setVisibility(View.VISIBLE);
                }
            }, 500);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_follow, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getFriendRequestOfGroup();
        binding.txtFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag().toString().equals("0")){
                    v.setTag("1");
                    binding.txtFollow.setTag("0");

                    binding.txtFollow.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightWhite));
                    binding.txtFollowing.setBackgroundColor(Color.WHITE);

                    binding.txtFollow.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    binding.txtFollowing.setTextColor(Color.BLACK);

                    if(mListFollowing.size() == 0){
                        getFollowingList();
                    }else if(followingModel != null){
                        setFollowingRecyclerAdapter(followingModel.getResult());
                    }

                }
            }
        });

        binding.txtFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag().toString().equals("0")) {
                    v.setTag("1");
                    binding.txtFollowing.setTag("0");

                    binding.txtFollowing.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightWhite));
                    binding.txtFollow.setBackgroundColor(Color.WHITE);

                    binding.txtFollowing.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    binding.txtFollow.setTextColor(Color.BLACK);

                    if(mListFollow.size() == 0){
                        getFollowList();
                    }else if(followersModel != null){
                        setFollowRecyclerAdapter(followersModel.getResult());
                    }
                }
            }
        });

    }

    private void setFollowRecyclerAdapter(List<FollowersModel.Result> mList) {
        mListFollow = mList;
        followRecyclerAdapter = new FollowRecyclerAdapter(getActivity(), mList, FollowFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(followRecyclerAdapter);

        if(mList.size() == 0){
            binding.txtNoData.setVisibility(View.VISIBLE);
        }
    }

    private void setFollowingRecyclerAdapter(List<FollowingModel.Result> mList) {
        mListFollowing = mList;
        followingRecyclerAdapter = new FollowingRecyclerAdapter(getActivity(), mList, FollowFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(followingRecyclerAdapter);

        if(mList.size() == 0){
            binding.txtNoData.setVisibility(View.VISIBLE);
        }
    }

    private void setRequestRecyclerAdapter(List<HashMap<String, String>> result) {
        mListFollowRequest = result;
        binding.recyclerViewRequest.setVisibility(View.VISIBLE);
        mFriendRequestRecyclerAdapter = new FriendRequestRecyclerAdapter(getActivity(), mListFollowRequest, FollowFragment.this);
        binding.recyclerViewRequest.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        binding.recyclerViewRequest.setNestedScrollingEnabled(false);
        binding.recyclerViewRequest.setLayoutManager(mLayoutManager);
        binding.recyclerViewRequest.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewRequest.setAdapter(mFriendRequestRecyclerAdapter);

        if(result.size() != 0){
            binding.txtRequest.setText("You have a Request");
        }else{
            binding.txtRequest.setText("No Requests Available!");
        }
    }

    public void onItemFollowingClick(String position) {
        Intent intentDash = new Intent(getActivity(), DashboardActivity.class);
        intentDash.putExtra(Constants.EXTRA_USERID, position);
        startActivity(intentDash);
    }

    public void onFollowItemClick(String position) {
        /*FollowUserDetailFragment newFragment = new FollowUserDetailFragment();
        //newFragment.setTargetFragment(this, Constants.REQUEST_CODE_SEND_FRIEND);
        newFragment.show(getChildFragmentManager().beginTransaction(), "dialog");*/
        Intent intentDash = new Intent(getActivity(), DashboardActivity.class);
        intentDash.putExtra(Constants.EXTRA_USERID, position);
        startActivity(intentDash);
    }

    private void getFriendRequestOfGroup() {
        if(isOnline(getActivity())){
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    try {
                        FollowRequestModel followRequestModel = new Gson().fromJson(result, FollowRequestModel.class);
                        if(followRequestModel != null){
                            setRequestRecyclerAdapter(getMergeList(followRequestModel));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_USER_FOLLOW_REQUEST+"?UserId="+SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        }else{
            binding.progressBar.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    private List<HashMap<String, String>> getMergeList(FollowRequestModel followRequestModel) {
        List<HashMap<String, String>> mList = new ArrayList<>();
        for (int i = 0; i < followRequestModel.getResult().size(); i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("message", followRequestModel.getResult().get(i).getFromUserName());
            hashMap.put("isEvent", "0");
            hashMap.put("from_user_id", followRequestModel.getResult().get(i).getFromUserId());
            mList.add(hashMap);
        }

        for (int i = 0; i < followRequestModel.getGroupresults().size(); i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("message", followRequestModel.getGroupresults().get(i).getFromUserName());
            hashMap.put("isEvent", "1");
            hashMap.put("GroupId", ""+followRequestModel.getGroupresults().get(i).getGroupId());
            hashMap.put("from_user_id", followRequestModel.getGroupresults().get(i).getFromUserId());
            mList.add(hashMap);
        }

        for (int i = 0; i < followRequestModel.getEventResult().size(); i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("message", followRequestModel.getEventResult().get(i).getFromUserName());
            hashMap.put("isEvent", "2");
            hashMap.put("EventId", followRequestModel.getEventResult().get(i).getEvent_id());
            hashMap.put("from_user_id", followRequestModel.getEventResult().get(i).getFromUserId());
            mList.add(hashMap);
        }
        return mList;
    }

    private void getFollowList() {
        if(isOnline(getActivity())){
            binding.progressBar.setVisibility(View.VISIBLE);
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "getFollowList: "+result);
                    binding.progressBar.setVisibility(View.GONE);
                    try {
                        followersModel = new Gson().fromJson(result, FollowersModel.class);
                        setFollowRecyclerAdapter(followersModel.getResult());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_USER_FOLLOW+"?UserId="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        }else{
            binding.progressBar.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    private void getFollowingList() {
        if(isOnline(getActivity())){
            binding.progressBar.setVisibility(View.VISIBLE);
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "getFollowingList: "+result);
                    binding.progressBar.setVisibility(View.GONE);
                    try {
                        followingModel = new Gson().fromJson(result, FollowingModel.class);
                        setFollowingRecyclerAdapter(followingModel.getResult());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_USER_FOLLOWING+"?UserId="+SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        }else{
            binding.progressBar.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    public void onAcceptRequestClick(int position, String fromUserId, String isEvent) {
        if(isOnline(getContext())){
            DialogUtils.showProgressDialog(getContext());
            if(isEvent.equals("2")){
                /**
                 * Event Reject Here
                 */
                ApiInterface apiInterface = ApiFactory.provideInterface();
                JsonObject payerReg = new JsonObject();
                payerReg.addProperty("EventId", "" + mListFollowRequest.get(position).get("EventId"));
                payerReg.addProperty("FromUserId", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
                Log.e(TAG, "App Event: " + payerReg.toString());
                Call<JsonObject> call = apiInterface.acceptEventRequest(payerReg);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        DialogUtils.stopProgressDialog();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                if (jsonObject.getBoolean("IsSuccess")) {
                                    getFriendRequestOfGroup();
                                } else {
                                    showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });
            }else if(isEvent.equals("0")) {
                ApiInterface apiInterface = ApiFactory.provideInterface();
                JsonObject payerReg = new JsonObject();
                payerReg.addProperty("ToUserId", "" + fromUserId);
                payerReg.addProperty("FromUserId", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
                Log.e(TAG, "create post: " + payerReg.toString());
                Call<JsonObject> call = apiInterface.acceptFollowingRequest(payerReg);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        DialogUtils.stopProgressDialog();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONObject object = jsonObject.getJSONObject("result");
                                if (object.getBoolean("IsSuccess")) {
                                    getFriendRequestOfGroup();
                                    getFollowList();
                                } else {
                                    showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });
            }else if(isEvent.equals("1")){
                ApiInterface apiInterface = ApiFactory.provideInterface();
                JsonObject payerReg = new JsonObject();
                payerReg.addProperty("GroupId", Integer.parseInt(mFriendRequestRecyclerAdapter.getGroupId(position)));
                payerReg.addProperty("FromuserID", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
                Log.e(TAG, "Accept update: "+payerReg.toString());
                Call<JsonObject> call = apiInterface.acceptGroupInvitation(payerReg);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                        DialogUtils.stopProgressDialog();
                        if (response.isSuccessful()) {
                            try{
                                Log.e(TAG, "create update: "+new Gson().toJson(response.body()));
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                if (jsonObject.getBoolean("IsSuccess")) {
                                    getFriendRequestOfGroup();
                                    getFollowList();
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
            }
        }else
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
    }

    public void onRejectRequestClick(int position, String fromId, String isEvent) {
        if(isOnline(getContext())){
            DialogUtils.showProgressDialog(getContext());
            if(isEvent.equals("2")){
                /**
                 * Event Reject Request
                 */
                ApiInterface apiInterface = ApiFactory.provideInterface();
                JsonObject payerReg = new JsonObject();
                payerReg.addProperty("EventId", "" + mListFollowRequest.get(position).get("EventId"));
                payerReg.addProperty("FromUserId", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
                Log.e(TAG, "Rejec post: " + payerReg.toString());
                Call<JsonObject> call = apiInterface.rejectEventRequest(payerReg);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        DialogUtils.stopProgressDialog();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                if (jsonObject.getBoolean("IsSuccess")) {
                                    getFriendRequestOfGroup();
                                } else {
                                    showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });
            }else if(isEvent.equals("0")) {
                AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                    @Override
                    public void onTaskComplete(String result) {
                        Log.e(TAG, "getFollowList: " + result);
                        binding.progressBar.setVisibility(View.GONE);
                        DialogUtils.stopProgressDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject object = jsonObject.getJSONObject("result");
                            if (object.getBoolean("IsSuccess")) {
                                getFriendRequestOfGroup();
                            } else {
                                showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL + "" + Urls.REJECT_FOLLOW_REQUEST + "?Touser_id=" + fromId + "&fromuser_id=" + SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            }
            else if(isEvent.equals("1")) {
                AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                    @Override
                    public void onTaskComplete(String result) {
                        Log.e(TAG, "Reject GroupInvitation: " + result);
                        binding.progressBar.setVisibility(View.GONE);
                        DialogUtils.stopProgressDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject object = jsonObject.getJSONObject("result");
                            if (object.getBoolean("IsSuccess")) {
                                getFriendRequestOfGroup();
                            } else {
                                showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL + "" + Urls.REJECT_GROUP_INVITATION + "?GroupId="+mListFollowRequest.get(position).get("GroupId")+"&FromUserId=" + SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            }
        }else
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
    }
}
