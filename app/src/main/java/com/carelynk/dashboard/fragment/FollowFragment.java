package com.carelynk.dashboard.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.carelynk.dashboard.adapter.HighlightRecyclerAdapter;
import com.carelynk.dashboard.adapter.RequestRecyclerAdapter;
import com.carelynk.dashboard.model.ReuestGroupModel;
import com.carelynk.databinding.FragmentFollowBinding;
import com.carelynk.databinding.FragmentTimelineBinding;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskDeleteCommon;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.utilz.DialogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
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
    private static final String TAG = "FollowFragment";
    private FollowingRecyclerAdapter followingRecyclerAdapter;
    private FollowRecyclerAdapter followRecyclerAdapter;
    private FriendRequestRecyclerAdapter mFriendRequestRecyclerAdapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((HomeActivity)getActivity()).hideFab();
                    setFollowRecyclerAdapter(new ArrayList<String>());
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

                    setFollowingRecyclerAdapter(new ArrayList<String>());
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

                    setFollowRecyclerAdapter(new ArrayList<String>());
                }
            }
        });

    }

    private void setFollowRecyclerAdapter(List<String> mList) {
        followRecyclerAdapter = new FollowRecyclerAdapter(getActivity(), mList, FollowFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(followRecyclerAdapter);
    }

    private void setFollowingRecyclerAdapter(List<String> mList) {
        followingRecyclerAdapter = new FollowingRecyclerAdapter(getActivity(), mList, FollowFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(followingRecyclerAdapter);
    }

    private void setRequestRecyclerAdapter() {
        binding.recyclerViewRequest.setVisibility(View.VISIBLE);
        mFriendRequestRecyclerAdapter = new FriendRequestRecyclerAdapter(getActivity(), null, FollowFragment.this);
        binding.recyclerViewRequest.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerViewRequest.setLayoutManager(mLayoutManager);
        binding.recyclerViewRequest.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewRequest.setAdapter(mFriendRequestRecyclerAdapter);
    }

    public void onItemFollowingClick(int position) {
        FollowUserDetailFragment newFragment = new FollowUserDetailFragment();
        //newFragment.setTargetFragment(this, Constants.REQUEST_CODE_SEND_FRIEND);
        newFragment.show(getChildFragmentManager().beginTransaction(), "dialog");
    }

    public void onFollowItemClick(int position) {
        FollowUserDetailFragment newFragment = new FollowUserDetailFragment();
        //newFragment.setTargetFragment(this, Constants.REQUEST_CODE_SEND_FRIEND);
        newFragment.show(getChildFragmentManager().beginTransaction(), "dialog");
    }

    private void getFriendRequestOfGroup() {
        if(isOnline(getActivity())){
            DialogUtils.showProgressDialog(getActivity());
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    DialogUtils.stopProgressDialog();
                    try {

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_USER_FOLLOW+"?id=");
        }else{
            DialogUtils.stopProgressDialog();
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    public void onAcceptRequestClick(int position, ReuestGroupModel.Result result) {

    }

    public void onRejectRequestClick(int position, ReuestGroupModel.Result result) {

    }
}
