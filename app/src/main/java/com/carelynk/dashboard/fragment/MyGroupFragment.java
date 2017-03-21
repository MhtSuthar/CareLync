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
import com.carelynk.dashboard.model.GroupModel;
import com.carelynk.databinding.FragmentMyGroupBinding;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.utilz.AppUtils;

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
    private GroupFollowedRecyclerAdapter mFollowedRecyclerAdapter;
    private AllGroupRecyclerAdapter mAllGroupRecyclerAdapter;

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
        setOwnGroupRecyclerAdapter();
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

                    setOwnGroupRecyclerAdapter();
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

                    setFollowRecyclerAdapter();
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

                    setAllGroupRecyclerAdapter();
                }
            }
        });

    }

   /* void getGroupList() {
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
                        setOwnGroupRecyclerAdapter();
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
    }*/

    private void setOwnGroupRecyclerAdapter() {
        mMyGroupRecyclerAdapter = new MyGroupRecyclerAdapter(getActivity(), getDummyList(), MyGroupFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mMyGroupRecyclerAdapter);
    }

    private void setAllGroupRecyclerAdapter() {
        mAllGroupRecyclerAdapter = new AllGroupRecyclerAdapter(getActivity(), new ArrayList<String>(), MyGroupFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mAllGroupRecyclerAdapter);
    }

    private void setFollowRecyclerAdapter() {
        mFollowedRecyclerAdapter = new GroupFollowedRecyclerAdapter(getActivity(), new ArrayList<String>(), MyGroupFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mFollowedRecyclerAdapter);
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



    public void onDeleteItemClick(int position) {
        showAlertDialog(new BaseActivity.OnDialogClick() {
            @Override
            public void onPositiveBtnClick() {

            }

            @Override
            public void onNegativeBtnClick() {

            }
        }, "Delete", getString(R.string.are_you_sure_delete), true);
    }

    public void onEditItemClick(int position) {
        moveActivity(new Intent(getActivity(), GroupCreateActivity.class), getActivity(), false);
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
        //intent.putExtra(AppUtils.Extra_Group_Id, mGroupList.get(position).GroupId);
        intent.putExtra(AppUtils.Extra_Is_From_Which_Group, 0);
        moveActivity(intent, getActivity(), false);
    }

    public void onItemFollowClick(int position) {
        Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Is_From_Which_Group, 1);
        moveActivity(intent, getActivity(), false);
    }

    public void onItemAllGroupClick(int position) {
        Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Is_From_Which_Group, 2);
        moveActivity(intent, getActivity(), false);
    }


}
