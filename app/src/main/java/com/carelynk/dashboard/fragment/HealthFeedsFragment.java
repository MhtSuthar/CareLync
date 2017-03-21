package com.carelynk.dashboard.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.dashboard.FeedDetailActivity;
import com.carelynk.dashboard.adapter.HealthFeedRecyclerAdapter;
import com.carelynk.dashboard.model.HealthFeedModel;
import com.carelynk.dashboard.model.HighlightModel;
import com.carelynk.databinding.FragmentMyGroupBinding;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.databinding.FragmentTimelineBinding;
import com.carelynk.event.EventListActivity;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.AsyncTaskPostCommon;
import com.carelynk.rest.Urls;
import com.carelynk.search.MySearchActivity;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.trending.TrendingListActivity;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
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
public class HealthFeedsFragment extends BaseFragment {

    FragmentTimelineBinding binding;
    private List<HighlightModel> mHealthFeedList = new ArrayList<>();;
    private HealthFeedRecyclerAdapter myHealthFeedRecyclerAdapter;
    public AsyncTaskGetCommon asyncTaskGetCommon;
    private static final String TAG = "HealthFeedsFragment";
    private ProgressBar mProgressBarHeader;
    private CardView askAQuestion, writeArticle;
    private EditText edtTopic;
    private LinearLayout linAskQuestions;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mHealthFeedList == null || mHealthFeedList.size() == 0)
                        getHealthFeed();
                    ((HomeActivity) getActivity()).hideFab();
                }
            }, 500);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerAdapter();
    }

    void getHealthFeed() {
        if (isOnline(getContext())) {
                mProgressBarHeader.setVisibility(View.VISIBLE);
                ApiInterface apiInterface = ApiFactory.provideInterface();
                Call<JsonArray> call = apiInterface.getHealthFeed();
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray>call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            try{
                                mHealthFeedList.clear();
                                Log.e(TAG, "onResponse: "+response.body().toString());
                                JSONArray jsonArray = new JSONArray(response.body().toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //[{"mMainGroupID":1,"MainGroupName":"Heath"},
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    HighlightModel highlightModel = new HighlightModel();
                                    highlightModel.AnswerCount = object.getInt("AnswerCount");
                                    highlightModel.CreatedDate = object.getString("CreatedDate");
                                    highlightModel.Desc = object.getString("Desc");
                                    highlightModel.GoalId = object.getInt("GoalId");
                                    highlightModel.GoalName = object.getString("GoalName");
                                    highlightModel.GoalStatusId = object.getInt("GoalStatusId");
                                    highlightModel.PhotoURL = object.getString("PhotoURL");
                                    highlightModel.PostType = object.getString("PostType");
                                    highlightModel.SupportCount = object.getInt("SupportCount");
                                    highlightModel.UserId = object.getString("UserId");
                                    highlightModel.UserName = object.getString("UserName");
                                    mHealthFeedList.add(highlightModel);
                                }
                                Log.e(TAG, "Size: "+mHealthFeedList.size());
                                if(myHealthFeedRecyclerAdapter != null){
                                    mProgressBarHeader.setVisibility(View.GONE);
                                    myHealthFeedRecyclerAdapter.notifyDataSetChanged();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray>call, Throwable t) {
                        Log.e(TAG, t.toString());
                        mProgressBarHeader.setVisibility(View.GONE);
                    }
                });
        } else {
            mProgressBarHeader.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }


    private void setRecyclerAdapter() {
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        /**
         * Set Header
         */
        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_health_feed, binding.recyclerView, false);

        mProgressBarHeader = (ProgressBar) header.findViewById(R.id.progressBar);
        mProgressBarHeader.setVisibility(View.GONE);
        askAQuestion = (CardView) header.findViewById(R.id.cardViewAskQuestion);
        writeArticle = (CardView) header.findViewById(R.id.cardViewWriteArticle);
        linAskQuestions = (LinearLayout) header.findViewById(R.id.linQuestions);
        edtTopic = (EditText) header.findViewById(R.id.edtTopic);
        askAQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linAskQuestions.setVisibility(View.VISIBLE);
            }
        });
        header.findViewById(R.id.btnPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edtTopic.getText().toString())){
                    createPost(edtTopic.getText().toString());
                }else{
                    showSnackbar(binding.getRoot(), "Please Enter Share Text");
                }
            }
        });

        header.findViewById(R.id.txtGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(new Intent(getActivity(), MySearchActivity.class), getActivity(), false);
            }
        });

        header.findViewById(R.id.txtEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(new Intent(getActivity(), EventListActivity.class), getActivity(), false);
            }
        });

        header.findViewById(R.id.txtTrending).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveActivity(new Intent(getActivity(), TrendingListActivity.class), getActivity(), false);
            }
        });

        writeArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        myHealthFeedRecyclerAdapter = new HealthFeedRecyclerAdapter(header, getActivity(), mHealthFeedList, HealthFeedsFragment.this);
        binding.recyclerView.setAdapter(myHealthFeedRecyclerAdapter);
    }

    void createPost(String title){
        if(isOnline(getContext())){
            DialogUtils.showProgressDialog(getContext());
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("GoalName", title);
            payerReg.addProperty("Desc", "");
            payerReg.addProperty("UserId", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            Log.e(TAG, "create post: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.createPost(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONObject object = jsonObject.getJSONObject("result");
                            if (object.getBoolean("IsSuccess")) {
                                edtTopic.setText("");
                                AppUtils.closeKeyBoard(getActivity());
                                getHealthFeed();
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
        }else
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
        intent.putExtra(AppUtils.Extra_Goal_Id, mHealthFeedList.get(position).GoalId);
        moveActivity(intent, getActivity(), false);
    }

    void attemptCreateGoal(){
        if(isOnline(getActivity())){
            if(isValid()) {
                AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(getActivity(), new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
                    @Override
                    public void onTaskComplete(String result) {
                        if (result.length() > 0) {

                        } else
                            showSnackbar(binding.getRoot(), getString(R.string.error_server));
                    }
                });
                asyncTaskCommon.execute(Urls.CREATE_GOAL, getPostValue());
            }
        }else
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
    }

    private String getPostValue() {
        JSONObject jsonObject = new JSONObject();
        try{
            //jsonObject.put("GoalName", edtTopic.getText().toString());
            //jsonObject.put("Desc", edtDescription.getText().toString());
            jsonObject.put("Photourl", "");
            jsonObject.put("user_id", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private boolean isValid() {
        if(TextUtils.isEmpty(edtTopic.getText().toString())){
            showSnackbar(binding.getRoot(), "Please enter text");
            return false;
        }
        return true;
    }
}
