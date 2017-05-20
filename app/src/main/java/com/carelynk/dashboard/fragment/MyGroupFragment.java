package com.carelynk.dashboard.fragment;

import android.app.Activity;
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
import com.carelynk.base.BaseActivity;
import com.carelynk.base.BaseFragment;
import com.carelynk.dashboard.GroupCreateActivity;
import com.carelynk.dashboard.GroupDetailActivity;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.dashboard.adapter.AllGroupRecyclerAdapter;
import com.carelynk.dashboard.adapter.GroupFollowedRecyclerAdapter;
import com.carelynk.dashboard.adapter.MyGroupRecyclerAdapter;
import com.carelynk.dashboard.adapter.RequestRecyclerAdapter;
import com.carelynk.dashboard.model.GroupModel;
import com.carelynk.dashboard.model.GroupModelGson;
import com.carelynk.dashboard.model.ReuestGroupModel;
import com.carelynk.databinding.FragmentMyGroupBinding;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskDeleteCommon;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12-Sep-16.
 */
public class MyGroupFragment extends BaseFragment {

    FragmentMyGroupBinding binding;
    private List<GroupModel> mGroupList;
    private MyGroupRecyclerAdapter mMyGroupRecyclerAdapter;
    public AsyncTaskGetCommon asyncTaskGetCommon;
    private static final String TAG = "MyGroupFragment";
    private GroupFollowedRecyclerAdapter mFollowedRecyclerAdapter;
    private AllGroupRecyclerAdapter mAllGroupRecyclerAdapter;
    private GroupModelGson mOwnGroupModelGson;
    private RequestRecyclerAdapter mReqestRecyclerAdapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mOwnGroupModelGson == null || mOwnGroupModelGson.getResult().size() == 0) {
                refresh();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((HomeActivity) getActivity()).showFab();
                }
            }, 500);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_group, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setOwnGroupRecyclerAdapter();
       /* binding.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(binding.recyclerView, new RecyclerTouchListener.OnRecyclerClickListener() {
            @Override
            public void onClick(View view, final int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));*/


        getRequestOfGroup();

        binding.txtOwned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag().toString().equals("0")){
                    v.setTag("1");
                    binding.txtAllGrp.setTag("0");
                    binding.txtFollowed.setTag("0");

                    binding.txtAllGrp.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightWhite));
                    binding.txtFollowed.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightWhite));
                    binding.txtOwned.setBackgroundColor(Color.WHITE);

                    binding.txtAllGrp.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    binding.txtFollowed.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    binding.txtOwned.setTextColor(Color.BLACK);

                    getOwnGroup();
                }
            }
        });

        binding.txtFollowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag().toString().equals("0")) {
                    v.setTag("1");
                    binding.txtAllGrp.setTag("0");
                    binding.txtOwned.setTag("0");

                    binding.txtOwned.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightWhite));
                    binding.txtAllGrp.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightWhite));
                    binding.txtFollowed.setBackgroundColor(Color.WHITE);

                    binding.txtOwned.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    binding.txtAllGrp.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    binding.txtFollowed.setTextColor(Color.BLACK);

                    getFollowGroup();
                }
            }
        });

        binding.txtAllGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag().toString().equals("0")) {
                    v.setTag("1");
                    binding.txtOwned.setTag("0");
                    binding.txtFollowed.setTag("0");

                    binding.txtOwned.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightWhite));
                    binding.txtFollowed.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightWhite));
                    binding.txtAllGrp.setBackgroundColor(Color.WHITE);

                    binding.txtOwned.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    binding.txtFollowed.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    binding.txtAllGrp.setTextColor(Color.BLACK);

                    getAllGroup();
                }
            }
        });

    }

    private void setOwnGroupRecyclerAdapter() {
        mMyGroupRecyclerAdapter = new MyGroupRecyclerAdapter(getActivity(), mOwnGroupModelGson.getResult(), MyGroupFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mMyGroupRecyclerAdapter);
    }

    private void setAllGroupRecyclerAdapter() {
        mAllGroupRecyclerAdapter = new AllGroupRecyclerAdapter(getActivity(), mOwnGroupModelGson.getResult(), MyGroupFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mAllGroupRecyclerAdapter);
    }

    private void setFollowRecyclerAdapter() {
        mFollowedRecyclerAdapter = new GroupFollowedRecyclerAdapter(getActivity(),mOwnGroupModelGson.getResult(), MyGroupFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mFollowedRecyclerAdapter);
    }

    private void setRequestRecyclerAdapter(List<ReuestGroupModel.Result> result) {
        binding.recyclerViewRequest.setVisibility(View.VISIBLE);
        mReqestRecyclerAdapter = new RequestRecyclerAdapter(getActivity(), result, MyGroupFragment.this);
        binding.recyclerViewRequest.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerViewRequest.setLayoutManager(mLayoutManager);
        binding.recyclerViewRequest.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewRequest.setAdapter(mReqestRecyclerAdapter);
    }


    public void onDeleteItemClick(final String  groupId) {
        showAlertDialog(new BaseActivity.OnDialogClick() {
            @Override
            public void onPositiveBtnClick() {
                deleteGroup(groupId);
            }

            @Override
            public void onNegativeBtnClick() {

            }
        }, "Delete", getString(R.string.are_you_sure_delete), true);
    }

    private void deleteGroup(String groupId) {
        if(isOnline(getContext())){
            DialogUtils.showProgressDialog(getActivity());
            AsyncTaskDeleteCommon asyncTaskGetCommon = new AsyncTaskDeleteCommon(getContext(), new AsyncTaskDeleteCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    DialogUtils.stopProgressDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("IsSuccess")) {
                            refresh();
                        } else {
                            showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_DELETE_GROUP+"?id="+groupId);
        }else{
            DialogUtils.stopProgressDialog();
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    public void onEditItemClick(GroupModelGson.Result group) {
        Intent intent = new Intent(getActivity(), GroupCreateActivity.class);
        intent.putExtra(Constants.EXTRA_IS_EDIT_GROUP, true);
        intent.putExtra(Constants.EXTRA_GROUP_DETAIL, group);
        moveActivityResult(intent, getActivity(), Constants.REQUEST_ADD_GROUP);
    }

    public void onItemClick(int position, GroupModelGson.Result groupDetail) {
        Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Is_From_Which_Group, 0);
        intent.putExtra(Constants.EXTRA_GROUP_DETAIL, groupDetail);
        moveActivityResult(intent, getActivity(), Constants.REQUEST_GROUP_DETAIL);
    }

    public void onItemFollowClick(int position, GroupModelGson.Result groupDetail) {
        Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Is_From_Which_Group, 1);
        intent.putExtra(Constants.EXTRA_GROUP_DETAIL, groupDetail);
        moveActivityResult(intent, getActivity(), Constants.REQUEST_GROUP_DETAIL);
    }

    public void onItemAllGroupClick(int position, GroupModelGson.Result groupDetail) {
        Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Is_From_Which_Group, 2);
        intent.putExtra(Constants.EXTRA_GROUP_DETAIL, groupDetail);
        moveActivityResult(intent, getActivity(), Constants.REQUEST_GROUP_DETAIL);
    }

    private void getRequestOfGroup() {
        if(isOnline(getActivity())){
            ApiInterface apiInterface = ApiFactory.provideInterface();
            Call<ReuestGroupModel> call = apiInterface.getRequestGroup(ApiFactory.API_BASE_URL+""+ Urls.GET_GROUP_JOIN_REQUEST);
            call.enqueue(new Callback<ReuestGroupModel>() {
                @Override
                public void onResponse(Call<ReuestGroupModel>call, Response<ReuestGroupModel> response) {
                    if (response.isSuccessful()) {
                        try{
                            //{"result":[{"GroupRequestId":"1003","Message":"kittu want to join your group -food is good","GroupId":"68","UserId":"08facb07-2161-4c9f-9905-d6cdafcb2c10","Accepted":"False","UserName":"kittu"}]}
                            Log.e(TAG, "get group: "+new Gson().toJson(response.body()));
                            //showSnackbar(binding.getRoot(), new Gson().toJson(response.body()));
                            if(response.body().getResult() != null && response.body().getResult().size() > 0){
                                binding.txtRequest.setText("Group Request");
                                setRequestRecyclerAdapter(response.body().getResult());
                            }else{
                                binding.txtRequest.setText("No Requests Available!");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ReuestGroupModel>call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        }else{
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    public void getOwnGroup() {
        if(isOnline(getContext())){
            binding.progressBar.setVisibility(View.VISIBLE);
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getContext(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);

                    binding.progressBar.setVisibility(View.GONE);
                    mOwnGroupModelGson = new Gson().fromJson(result, GroupModelGson.class);
                    if(mOwnGroupModelGson.getResult().size() > 0) {
                        setOwnGroupRecyclerAdapter();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_OWN_GROUP);
        }else{
            binding.progressBar.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    public void getAllGroup() {
        if(isOnline(getContext())){
            binding.progressBar.setVisibility(View.VISIBLE);
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getContext(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    binding.progressBar.setVisibility(View.GONE);
                    mOwnGroupModelGson = new Gson().fromJson(result, GroupModelGson.class);
                    if(mOwnGroupModelGson.getResult().size() > 0) {
                        setAllGroupRecyclerAdapter();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_ALL_GROUP);
        }else{
            binding.progressBar.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    public void getFollowGroup() {
        if(isOnline(getContext())){
            binding.progressBar.setVisibility(View.VISIBLE);
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getContext(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    binding.progressBar.setVisibility(View.GONE);
                    mOwnGroupModelGson = new Gson().fromJson(result, GroupModelGson.class);
                    if(mOwnGroupModelGson.getResult().size() > 0) {
                        setFollowRecyclerAdapter();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_FOLLOW_GROUP);
        }else{
            binding.progressBar.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Constants.REQUEST_ADD_GROUP){
                refresh();
            }else if(requestCode == Constants.REQUEST_GROUP_DETAIL){
                refresh();
            }
        }
    }

    /**
     * Refresh after create group
     */
    public void refresh() {
        binding.txtOwned.setTag("1");
        binding.txtAllGrp.setTag("0");
        binding.txtFollowed.setTag("0");

        binding.txtAllGrp.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightWhite));
        binding.txtFollowed.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightWhite));
        binding.txtOwned.setBackgroundColor(Color.WHITE);

        binding.txtAllGrp.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        binding.txtFollowed.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        binding.txtOwned.setTextColor(Color.BLACK);

        getOwnGroup();
    }

    public void onAcceptRequestClick(int position, ReuestGroupModel.Result result) {
        if(isOnline(getActivity())){
            DialogUtils.showProgressDialog(getActivity());
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("GroupId", Integer.parseInt(result.getGroupId()));
            payerReg.addProperty("RequestUserID", Integer.parseInt(result.getGroupRequestId()));
            Log.e(TAG, "Accept update: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.acceptGroupRequest(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "create update: "+new Gson().toJson(response.body()));
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                getRequestOfGroup();
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

    public void onRejectRequestClick(int position, ReuestGroupModel.Result result) {
        if(isOnline(getActivity())){
            DialogUtils.showProgressDialog(getActivity());
            AsyncTaskDeleteCommon asyncTaskGetCommon = new AsyncTaskDeleteCommon(getContext(), new AsyncTaskDeleteCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    DialogUtils.stopProgressDialog();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("IsSuccess")) {
                            getRequestOfGroup();
                        }else{
                            showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.REJECT_GROUP_REQUEST+""+result.getGroupRequestId());
        }else{
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }
}
