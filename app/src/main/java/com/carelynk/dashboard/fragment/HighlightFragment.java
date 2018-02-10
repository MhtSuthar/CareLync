package com.carelynk.dashboard.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.dashboard.FeedDetailActivity;
import com.carelynk.dashboard.GroupDetailActivity;
import com.carelynk.dashboard.PreviewImageActivity;
import com.carelynk.dashboard.model.HighlightAllDataModel;
import com.carelynk.dashboard.model.HighlightModel;
import com.carelynk.dashboard.model.HighliteCommonModel;
import com.carelynk.databinding.FragmentTimelineBinding;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.dashboard.adapter.HighlightRecyclerAdapter;
import com.carelynk.event.EventDetailActivity;
import com.carelynk.event.EventListActivity;
import com.carelynk.event.model.EventList;
import com.carelynk.profile.DashboardActivity;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12-Sep-16.
 */
public class HighlightFragment extends BaseFragment {

    FragmentTimelineBinding binding;
    private List<HighliteCommonModel> mTimelineList  = new ArrayList<>();;
    private HighlightRecyclerAdapter timelineRecyclerAdapter;
    private static final String TAG = "HighlightFragment";

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((HomeActivity)getActivity()).hideFab();
                    if (mTimelineList == null || mTimelineList.size() == 0)
                        getHighlights(true);
                    binding.txtNoData.setVisibility(View.GONE);
                }
            }, 500);
        }
    }

    void getHighlights(boolean b) {
        if (isOnline(getContext())) {
            if(b)
                binding.progressBar.setVisibility(View.VISIBLE);


            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);

                    try {
                        if(result != null) {
                            HighlightAllDataModel highlightAllDataModel = new Gson().fromJson(result, HighlightAllDataModel.class);
                            if(highlightAllDataModel.getResult().getGoalArray().size() > 0){
                                mTimelineList.clear();
                                List<HighlightAllDataModel.GoalArray> mGoalList = highlightAllDataModel.getResult().getGoalArray();
                                for (int i = 0; i < mGoalList.size(); i++) {
                                    HighliteCommonModel highliteCommonModel = new HighliteCommonModel();
                                    highliteCommonModel.title = mGoalList.get(i).getGoalName();
                                    highliteCommonModel.id = mGoalList.get(i).getGoalId();
                                    highliteCommonModel.created_date = mGoalList.get(i).getCreatedDate();
                                    highliteCommonModel.image = mGoalList.get(i).getPhotoURL();
                                    highliteCommonModel.description = "";
                                    highliteCommonModel.userName = mGoalList.get(i).getUserName();
                                    highliteCommonModel.profilePic = mGoalList.get(i).getProfilePicUrl();
                                    highliteCommonModel.date = AppUtils.convertStringToDate(mGoalList.get(i).getCreatedDate());
                                    highliteCommonModel.type = 0;
                                    highliteCommonModel.userId = mGoalList.get(i).getUserId();
                                    mTimelineList.add(highliteCommonModel);
                                }

                                List<HighlightAllDataModel.GoalUpdateArray> mGoalUpdateList = highlightAllDataModel.getResult().getGoalUpdateArray();
                                for (int i = 0; i < mGoalUpdateList.size(); i++) {
                                    HighliteCommonModel highliteCommonModel = new HighliteCommonModel();
                                    highliteCommonModel.title = mGoalUpdateList.get(i).getGoalName();
                                    highliteCommonModel.id = mGoalUpdateList.get(i).getGoalId();
                                    highliteCommonModel.created_date = mGoalUpdateList.get(i).getUpdateDate();
                                    highliteCommonModel.image = mGoalUpdateList.get(i).getPhotoURL();
                                    highliteCommonModel.description = mGoalUpdateList.get(i).getUpdatemsg();
                                    highliteCommonModel.profilePic = mGoalUpdateList.get(i).getProfilePicUrl();
                                    highliteCommonModel.userName = mGoalUpdateList.get(i).getUserName();
                                    highliteCommonModel.date = AppUtils.convertStringToDate(mGoalUpdateList.get(i).getUpdateDate());
                                    highliteCommonModel.type = 1;
                                    highliteCommonModel.userId = mGoalUpdateList.get(i).getUserId();
                                    mTimelineList.add(highliteCommonModel);
                                }

                                List<HighlightAllDataModel.GroupArray> mGroupList = highlightAllDataModel.getResult().getGroupArray();
                                for (int i = 0; i < mGroupList.size(); i++) {
                                    HighliteCommonModel highliteCommonModel = new HighliteCommonModel();
                                    highliteCommonModel.title = mGroupList.get(i).getGroupName();
                                    highliteCommonModel.id = mGroupList.get(i).getGroupId();
                                    highliteCommonModel.created_date = mGroupList.get(i).getCreatedDate();
                                    highliteCommonModel.image = mGroupList.get(i).getPhotoURL();
                                    highliteCommonModel.description = "";
                                    highliteCommonModel.userName = mGroupList.get(i).getUserName();
                                    highliteCommonModel.profilePic = mGroupList.get(i).getProfilePicUrl();
                                    highliteCommonModel.date = AppUtils.convertStringToDate(mGroupList.get(i).getCreatedDate());
                                    highliteCommonModel.type = 2;
                                    highliteCommonModel.userId = mGroupList.get(i).getUserId();
                                    mTimelineList.add(highliteCommonModel);
                                }

                                List<HighlightAllDataModel.GroupUpdateArray> mGroupUpdateList = highlightAllDataModel.getResult().getGroupUpdateArray();
                                for (int i = 0; i < mGroupUpdateList.size(); i++) {
                                    HighliteCommonModel highliteCommonModel = new HighliteCommonModel();
                                    highliteCommonModel.title = mGroupUpdateList.get(i).getGroupName();
                                    highliteCommonModel.id = mGroupUpdateList.get(i).getGroupId();
                                    highliteCommonModel.created_date = mGroupUpdateList.get(i).getUpdateDate();
                                    highliteCommonModel.image = mGroupUpdateList.get(i).getPhotoURL();
                                    highliteCommonModel.description = mGroupUpdateList.get(i).getUpdatemsg();
                                    highliteCommonModel.userName = mGroupUpdateList.get(i).getUserName();
                                    highliteCommonModel.profilePic = mGroupUpdateList.get(i).getProfilePicUrl();
                                    highliteCommonModel.date = AppUtils.convertStringToDate(mGroupUpdateList.get(i).getUpdateDate());
                                    highliteCommonModel.type = 3;
                                    highliteCommonModel.userId = mGroupUpdateList.get(i).getUserId();
                                    mTimelineList.add(highliteCommonModel);
                                }

                                List<HighlightAllDataModel.EventArray> mEventList = highlightAllDataModel.getResult().getEventArray();
                                for (int i = 0; i < mEventList.size(); i++) {
                                    HighliteCommonModel highliteCommonModel = new HighliteCommonModel();
                                    highliteCommonModel.title = mEventList.get(i).getEventName();
                                    highliteCommonModel.id = mEventList.get(i).getEventID();
                                    highliteCommonModel.created_date = mEventList.get(i).getCreatedDatetime();
                                    highliteCommonModel.image = mEventList.get(i).getPhotoURL();
                                    highliteCommonModel.profilePic = mEventList.get(i).getProfilePicUrl();
                                    if(mEventList.get(i).getEventDateFrom().equalsIgnoreCase(mEventList.get(i).getEventDateTo())) {
                                        if(mEventList.get(i).getEventTimeFrom().equalsIgnoreCase(mEventList.get(i).getEventTimeTo()))
                                            highliteCommonModel.description = mEventList.get(i).getEventDesc() +
                                                    "@<br/>Date: " + mEventList.get(i).getEventDateFrom() +
                                                    "<br/>Time:" + mEventList.get(i).getEventTimeFrom() +
                                                    "<br/>Location:" + mEventList.get(i).getLocation();
                                        else
                                            highliteCommonModel.description = mEventList.get(i).getEventDesc() +
                                                    "@<br/>Date: " + mEventList.get(i).getEventDateFrom() +
                                                    "<br/>Time: " + mEventList.get(i).getEventTimeFrom() + " To " + mEventList.get(i).getEventTimeTo() +
                                                    "<br/>Location: " + mEventList.get(i).getLocation();
                                    }else
                                        highliteCommonModel.description = mEventList.get(i).getEventDesc()+
                                            "@<br/>Date: "+mEventList.get(i).getEventDateFrom()+" To "+mEventList.get(i).getEventDateTo()+
                                            "<br/>Time: "+mEventList.get(i).getEventTimeFrom()+" To "+mEventList.get(i).getEventTimeTo()+
                                            "<br/>Location: "+mEventList.get(i).getLocation();
                                    highliteCommonModel.userName = mEventList.get(i).getUserName();
                                    highliteCommonModel.date = AppUtils.convertStringToDate(mEventList.get(i).getCreatedDatetime());
                                    highliteCommonModel.dateTo = mEventList.get(i).getEventDateTo();
                                    highliteCommonModel.dateFrom = mEventList.get(i).getEventDateFrom();
                                    highliteCommonModel.timeTo = mEventList.get(i).getEventTimeTo();
                                    highliteCommonModel.timeFrom = mEventList.get(i).getEventTimeFrom();
                                    highliteCommonModel.location = mEventList.get(i).getLocation();
                                    highliteCommonModel.isPrivate = mEventList.get(i).getIsPrivate();
                                    highliteCommonModel.userId = mEventList.get(i).getUserID();
                                    highliteCommonModel.type = 4;
                                    mTimelineList.add(highliteCommonModel);
                                }
                                binding.progressBar.setVisibility(View.GONE);
                                binding.swipeRefreshLayout.setRefreshing(false);
                                setRecyclerAdapter();
                            }
                        }else{
                            binding.progressBar.setVisibility(View.GONE);
                            binding.swipeRefreshLayout.setRefreshing(false);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_HIGHLIGHT_FEED+"?UserId="+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        } else {
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.progressBar.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false);
        return binding.getRoot();
    }

    public void onClickUserDetail(String userid) {
        Intent intentDash = new Intent(getActivity(), DashboardActivity.class);
        intentDash.putExtra(Constants.EXTRA_USERID, userid);
        startActivity(intentDash);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorTrasparentLightRed,
                R.color.colorTrasparentYellow);
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(true);
                getHighlights(false);
            }
        });
    }

    private void setRecyclerAdapter() {
        timelineRecyclerAdapter = new HighlightRecyclerAdapter(getActivity(), getFilterList(mTimelineList), HighlightFragment.this, binding.recyclerView);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(timelineRecyclerAdapter);
    }

    private List<HighliteCommonModel> getFilterList(List<HighliteCommonModel> mTimelineList) {
        Collections.sort(mTimelineList, new Comparator<HighliteCommonModel>() {
            public int compare(HighliteCommonModel o1, HighliteCommonModel o2) {
                return o1.date.compareTo(o2.date);
            }
        });
        Collections.reverse(mTimelineList);
        return mTimelineList;
    }

    public void onItemClickImage(String imgPath, ImageView imgView) {
        Intent intent = new Intent(getActivity(), PreviewImageActivity.class);
        intent.putExtra(Constants.EXTRA_IMAGE, imgPath);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), imgView, "profile");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
        }else{
            startActivity(intent);
        }
    }

    public void onItemClick(HighliteCommonModel feedModel) {
        Intent intent;
        switch (feedModel.type){
            case 0:
                intent = new Intent(getActivity(), FeedDetailActivity.class);
                intent.putExtra(AppUtils.Extra_Goal_Id, ""+feedModel.id);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(getActivity(), FeedDetailActivity.class);
                intent.putExtra(AppUtils.Extra_Goal_Id, ""+feedModel.id);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(getActivity(), GroupDetailActivity.class);
                intent.putExtra(AppUtils.Extra_Is_From_Which_Group, 2);
                intent.putExtra(Constants.EXTRA_GROUP_ID, feedModel.id);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(getActivity(), GroupDetailActivity.class);
                intent.putExtra(AppUtils.Extra_Is_From_Which_Group, 2);
                intent.putExtra(Constants.EXTRA_GROUP_ID, feedModel.id);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra(Constants.EXTRA_EVENT, getEventModel(feedModel));
                startActivity(intent);
                break;
        }

    }


    private EventList.Result getEventModel(HighliteCommonModel feedModel) {
        EventList.Result result = new EventList.Result();
        result.setAddress(feedModel.location);
        result.setEventID(feedModel.id);
        result.setEventName(feedModel.title);
        result.setEventDateFrom(feedModel.dateFrom);
        result.setEventTimeFrom(feedModel.timeFrom);
        result.setEventTimeTo(feedModel.timeTo);
        result.setEventDateTo(feedModel.dateTo);
        result.setCreatedDate(feedModel.created_date);
        result.setEventDesc(feedModel.description.split("@")[0]);
        result.setPhotoURL(feedModel.image);
        result.setIsPrivate(feedModel.isPrivate);
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Constants.REQUEST_CODE_CHANGE_FEED){
                getHighlights(true);
            }
        }
    }
}
