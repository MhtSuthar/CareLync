package com.carelynk.dashboard.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.databinding.FragmentMyGroupBinding;
import com.carelynk.recent.GroupDetailListActivity;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.dashboard.adapter.MyGroupRecyclerAdapter;
import com.carelynk.recent.model.GroupModel;
import com.carelynk.recent.model.HealthFeedModel;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.utilz.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mGroupList == null || mGroupList.size() == 0) {
                //getGroupList();
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
        setRecyclerAdapter();
       /* binding.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(binding.recyclerView, new RecyclerTouchListener.OnRecyclerClickListener() {
            @Override
            public void onClick(View view, final int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));*/

    }

    void getGroupList() {
        if (isOnline(getContext())) {
            mGroupList = new ArrayList<>();
            asyncTaskGetCommon = new AsyncTaskGetCommon(getContext(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            GroupModel groupModel = new GroupModel();
                            groupModel.CreatedDate = object.getString("CreatedDate");
                            groupModel.Description = object.getString("Description");
                            groupModel.GroupName = object.getString("GroupName");
                            groupModel.GroupId = "" + object.getInt("GroupId");
                            groupModel.PhotoURL = object.getString("PhotoURL");
                            groupModel.MainGroupName = object.getString("MainGroupName");
                            groupModel.MainGroupId = "" + object.getInt("MainGroupId");
                            groupModel.PublicPrivate = object.getBoolean("PublicPrivate");
                            mGroupList.add(groupModel);
                        }
                        binding.progressBar.setVisibility(View.GONE);
                        setRecyclerAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(Urls.GET_GROUP_LIST + "/0");
        } else {
            binding.progressBar.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    private void setRecyclerAdapter() {
        mMyGroupRecyclerAdapter = new MyGroupRecyclerAdapter(getActivity(), getDummyList(), MyGroupFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mMyGroupRecyclerAdapter);
    }

    private List<GroupModel> getDummyList() {
        List<GroupModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GroupModel healthFeedModel = new GroupModel();
            healthFeedModel.GroupName = "Helth is welth";
            healthFeedModel.Description = "Lorem ipsum detail is the. Demo text will be used";
            healthFeedModel.GroupId = "10";
            list.add(healthFeedModel);
        }
        return list;
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), GroupDetailListActivity.class);
        intent.putExtra(AppUtils.Extra_Group_Id, mGroupList.get(position).GroupId);
        moveActivity(intent, getActivity(), false);
    }
}
