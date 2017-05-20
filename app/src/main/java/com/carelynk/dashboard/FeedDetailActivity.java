package com.carelynk.dashboard;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.adapter.FeedDetailRecyclerAdapter;
import com.carelynk.dashboard.fragment.CommentDialogFragment;
import com.carelynk.dashboard.fragment.CommentDialogGoalFragment;
import com.carelynk.dashboard.model.GroupDiscussModel;
import com.carelynk.dashboard.model.HealthFeedModel;
import com.carelynk.dashboard.model.HighlightModel;
import com.carelynk.databinding.ActivityFeedDetailBinding;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 06-Mar-17.
 */

public class FeedDetailActivity extends BaseActivity {

    ActivityFeedDetailBinding binding;
    private List<String> mFeedDetail;
    private FeedDetailRecyclerAdapter feedDetailRecyclerAdapter;
    private HighlightModel highlightModel;
    private EditText edtComment;
    private static final String TAG = "FeedDetailActivity";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feed_detail);
        init();
        setRecyclerAdapter();

        getGoalPostDetails();
    }

    public void showCommnetDialog(String updateId) {
        DialogFragment newFragment = new CommentDialogGoalFragment(updateId, ""+highlightModel.GoalId);
        //newFragment.setTargetFragment(this, Constants.REQUEST_CODE_SEND_FRIEND);
        newFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }


    private void init() {
        //setSupportActionBar(binding.includeToolbar.toolbar);
        binding.includeToolbar.toolbarTitle.setText("Feed Details");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        highlightModel = (HighlightModel) getIntent().getSerializableExtra(AppUtils.Extra_Goal_Detail);
    }

    private void setRecyclerAdapter() {
        mFeedDetail = new ArrayList<>();

        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        /**
         * Set Header
         */
        View header = LayoutInflater.from(this).inflate(R.layout.header_feed_detail, binding.recyclerView, false);
        LinearLayout linFooter = (LinearLayout) header.findViewById(R.id.linFooter);
        LinearLayout linSupport = (LinearLayout) header.findViewById(R.id.linSupport);
        TextView txtTitle = (TextView) header.findViewById(R.id.txtTitle);
        TextView txtName = (TextView) header.findViewById(R.id.txtName);
        TextView txtDesc = (TextView) header.findViewById(R.id.txtDesc);
        TextView txtPostTime = (TextView) header.findViewById(R.id.txtPostTime);
        ImageView imgUser = (ImageView) header.findViewById(R.id.imgUser);
        edtComment = (EditText) header.findViewById(R.id.edtComment);
        AppCompatButton btnSend = (AppCompatButton) header.findViewById(R.id.btnSend);
        CheckBox chkSupport = (CheckBox) header.findViewById(R.id.chkSupport);
        progressBar = (ProgressBar) header.findViewById(R.id.progressBar);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtComment.getText().toString())){
                    showSnackbar(null, "Please Enter Comment");
                }else{
                    insertGroupPost(edtComment.getText().toString());
                }
            }
        });

        chkSupport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }else{

                }
            }
        });

        txtTitle.setText(highlightModel.GoalName);
        txtDesc.setText(highlightModel.Desc);
        txtPostTime.setText(highlightModel.CreatedDate);
        txtName.setText(highlightModel.UserName);


        feedDetailRecyclerAdapter = new FeedDetailRecyclerAdapter(header, this, getDummyList(), FeedDetailActivity.this);
        binding.recyclerView.setAdapter(feedDetailRecyclerAdapter);
    }

    private List<HealthFeedModel> getDummyList() {
        List<HealthFeedModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HealthFeedModel healthFeedModel = new HealthFeedModel();
            healthFeedModel.Desc = "Lorem ipsum detail is the. Demo text will be used";
            healthFeedModel.GoalId = "10";
            healthFeedModel.UserName = "jason";
            healthFeedModel.GoalName = "GOal Name";
            healthFeedModel.GoalType = false;
            list.add(healthFeedModel);
        }
        return list;
    }

    private void getGoalPostDetails() {
        if(isOnline(this)){
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            Log.e(TAG, "getGoalPostDetails: "+ApiFactory.API_BASE_URL+""+ Urls.GET_GOAL_POST_LIST+"?Goal_id="+highlightModel.GoalId);
            Call<JsonObject> call = apiInterface.getGoalDetailList(ApiFactory.API_BASE_URL+""+ Urls.GET_GOAL_POST_LIST+"?Goal_id="+highlightModel.GoalId);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "get group: "+new Gson().toJson(response.body()));
//                            if(response.body().getResult() != null && response.body().getResult().size() > 0){
//
//                            }else{
//
//                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject>call, Throwable t) {
                    Log.e(TAG, t.toString());
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else{
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }


    private void insertGroupPost(String msg) {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("Updatemsg", msg);
            payerReg.addProperty("GoalId", highlightModel.GoalId);
            payerReg.addProperty("PhotoURL", "");
            //payerReg.addProperty("user_id", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            Log.e(TAG, "create goal detail: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.insertGoalPost(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "insert group: "+new Gson().toJson(response.body()));
                            edtComment.setText("");
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                AppUtils.closeKeyBoard(FeedDetailActivity.this);
                                getGoalPostDetails();
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

    public void addCommentToGoalDiscuss(String comment, String updateId){
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("CommentText", comment);
            payerReg.addProperty("UpdateId", updateId);
            payerReg.addProperty("PhotoURL", "");
            payerReg.addProperty("UserID", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            Log.e(TAG, "addCommentToGoalDiscuss: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.insertGoalPostComment(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "addCommentToGoalDiscuss: "+new Gson().toJson(response.body()));
                            edtComment.setText("");
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                AppUtils.closeKeyBoard(FeedDetailActivity.this);
                                showSnackbar(binding.getRoot(), "Comment Post Success");
                                getGoalPostDetails();
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


}
