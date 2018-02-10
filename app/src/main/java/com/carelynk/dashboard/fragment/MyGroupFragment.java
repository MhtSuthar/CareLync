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
import com.carelynk.dashboard.adapter.GroupRequestRecyclerAdapter;
import com.carelynk.dashboard.model.GroupModel;
import com.carelynk.dashboard.model.GroupModelGson;
import com.carelynk.databinding.FragmentMyGroupBinding;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

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
    private GroupModelGson mGroupModelGson;
    private GroupRequestRecyclerAdapter mReqestRecyclerAdapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mGroupModelGson == null) {
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
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.txtEmpty.setVisibility(View.GONE);
        mMyGroupRecyclerAdapter = new MyGroupRecyclerAdapter(getActivity(), mGroupModelGson.getResult().getOwnGroupDet(), MyGroupFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mMyGroupRecyclerAdapter);
    }

    private void setAllGroupRecyclerAdapter() {
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.txtEmpty.setVisibility(View.GONE);
        mAllGroupRecyclerAdapter = new AllGroupRecyclerAdapter(getActivity(), mGroupModelGson.getResult().getAllGroupDet(), MyGroupFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mAllGroupRecyclerAdapter);
    }

    private void setFollowRecyclerAdapter() {
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.txtEmpty.setVisibility(View.GONE);
        mFollowedRecyclerAdapter = new GroupFollowedRecyclerAdapter(getActivity(), mGroupModelGson.getResult().getFollowedGroupDet(), MyGroupFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mFollowedRecyclerAdapter);
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
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getContext(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
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

    public void onEditItemClick(GroupModelGson.OwnGroupDet group) {
        Intent intent = new Intent(getActivity(), GroupCreateActivity.class);
        intent.putExtra(Constants.EXTRA_IS_EDIT_GROUP, true);
        intent.putExtra(Constants.EXTRA_GROUP_DETAIL, group);
        moveActivityResult(intent, getActivity(), Constants.REQUEST_ADD_GROUP);
    }

    public void onItemClick(int position, GroupModelGson.OwnGroupDet groupDetail) {
        Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Is_From_Which_Group, 0);
        intent.putExtra(Constants.EXTRA_GROUP_ID, groupDetail.getGroupId());
        startActivityForResult(intent, Constants.REQUEST_GROUP_DETAIL);
    }

    public void onItemFollowClick(int position, GroupModelGson.FollowedGroupDet groupDetail) {
        Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Is_From_Which_Group, 1);
        intent.putExtra(Constants.EXTRA_GROUP_ID, groupDetail.getGroupId());
        startActivityForResult(intent, Constants.REQUEST_GROUP_DETAIL);
    }

    public void onItemAllGroupClick(int position, GroupModelGson.AllGroupDet groupDetail) {
        Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Is_From_Which_Group, 2);
        intent.putExtra(Constants.EXTRA_GROUP_ID, groupDetail.getGroupId());
        startActivityForResult(intent, Constants.REQUEST_GROUP_DETAIL);
    }



    public void getOwnGroup() {
        if(mGroupModelGson != null && mGroupModelGson.getResult().getOwnGroupDet().size() > 0) {
            setOwnGroupRecyclerAdapter();
        }else{
            binding.recyclerView.setVisibility(View.GONE);
            binding.txtEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void getAllGroup() {
        if(mGroupModelGson != null && mGroupModelGson.getResult().getAllGroupDet().size() > 0) {
            setAllGroupRecyclerAdapter();
        }else{
            binding.recyclerView.setVisibility(View.GONE);
            binding.txtEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void getFollowGroup() {
        if(mGroupModelGson != null && mGroupModelGson.getResult().getFollowedGroupDet().size() > 0) {
            setFollowRecyclerAdapter();
        }else{
            binding.recyclerView.setVisibility(View.GONE);
            binding.txtEmpty.setVisibility(View.VISIBLE);
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

        if(isOnline(getContext())){
            binding.progressBar.setVisibility(View.VISIBLE);
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getContext(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    binding.progressBar.setVisibility(View.GONE);
                    mGroupModelGson = new Gson().fromJson(result, GroupModelGson.class);
                    if(mGroupModelGson != null && mGroupModelGson.getResult().getOwnGroupDet().size() > 0) {
                        setOwnGroupRecyclerAdapter();
                    }else{
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.txtEmpty.setVisibility(View.VISIBLE);
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_GROUP_LIST+""+SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        }else{
            binding.progressBar.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }

    }


}
