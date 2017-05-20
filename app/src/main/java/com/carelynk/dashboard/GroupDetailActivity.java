package com.carelynk.dashboard;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.adapter.GroupDetailRecyclerAdapter;
import com.carelynk.dashboard.fragment.CommentDialogFragment;
import com.carelynk.dashboard.model.GroupDiscussModel;
import com.carelynk.dashboard.model.GroupModelGson;
import com.carelynk.databinding.ActivityFeedDetailBinding;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskDeleteCommon;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 06-Mar-17.
 */

public class GroupDetailActivity extends BaseActivity {

    ActivityFeedDetailBinding binding;
    private List<GroupDiscussModel.Result> mGroupChatDetail;
    private GroupDetailRecyclerAdapter groupDetailRecyclerAdapter;
    private int mFromWhich = 0;
    private GroupModelGson.Result groupDetail;
    private static final String TAG = "GroupDetailActivity";
    private ProgressBar progressBar;
    private EditText edtPost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feed_detail);
        mFromWhich = getIntent().getExtras().getInt(AppUtils.Extra_Is_From_Which_Group);
        groupDetail = (GroupModelGson.Result) getIntent().getSerializableExtra(Constants.EXTRA_GROUP_DETAIL);

        init();

        setRecyclerAdapter();

        getGroupDiscussDetails();
    }

    public void showCommnetDialog(String groupUpdateId) {
        DialogFragment newFragment = new CommentDialogFragment(groupUpdateId);
        //newFragment.setTargetFragment(this, Constants.REQUEST_CODE_SEND_FRIEND);
        newFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }


    private void init() {
        //setSupportActionBar(binding.includeToolbar.toolbar);
        binding.includeToolbar.toolbarTitle.setText("Group Details");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setRecyclerAdapter() {
        mGroupChatDetail = new ArrayList<>();

        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        /**
         * Set Header
         * 0 : My Group
         * 1 : Follow Group
         * 2 : All Group
         */
        View header = LayoutInflater.from(this).inflate(R.layout.header_group_detail, binding.recyclerView, false);
        progressBar = (ProgressBar) header.findViewById(R.id.progressBar);
        final TextView txtJoinNow = (TextView) header.findViewById(R.id.txtJoinNow);
        CardView cardViewEditor = (CardView) header.findViewById(R.id.cardViewEditor);
        ImageView imgEdit = (ImageView) header.findViewById(R.id.imgEdit);
        ImageView imgDelete = (ImageView) header.findViewById(R.id.imgDelete);
        TextView txtGroupName = (TextView) header.findViewById(R.id.txtGroupName);
        TextView txtDesc = (TextView) header.findViewById(R.id.txtDesc);
        TextView txtUserName = (TextView) header.findViewById(R.id.txtName);
        TextView txtTime = (TextView) header.findViewById(R.id.txtPostTime);
        TextView txtGroupCategory = (TextView) header.findViewById(R.id.txtGroupCategory);
        LinearLayout linRequestAll = (LinearLayout) header.findViewById(R.id.linRequestAll);
        LinearLayout linEditable = (LinearLayout) header.findViewById(R.id.linSupport);
        AppCompatButton btnSend = (AppCompatButton) header.findViewById(R.id.btnSend);
        edtPost = (EditText) header.findViewById(R.id.edtPost);

        txtGroupName.setText(groupDetail.getGroupName());
        txtDesc.setText(groupDetail.getDescription());
        txtGroupCategory.setText("Main Group : "+groupDetail.getTitleName());
        txtTime.setText(groupDetail.getCreatedDate());
        txtUserName.setText(groupDetail.getGroupName());


        switch (mFromWhich){
            case 0:
                //linEditable.setVisibility(View.VISIBLE);
                break;
            case 1:
                //linEditable.setVisibility(View.GONE);
                break;
            case 2:
                if(groupDetail.getRequestStatus().equals("")){
                    txtJoinNow.setVisibility(View.VISIBLE);
                    cardViewEditor.setVisibility(View.GONE);
                }else if(groupDetail.getRequestStatus().equalsIgnoreCase("false")){
                    txtJoinNow.setVisibility(View.VISIBLE);
                    txtJoinNow.setText("Your request is pending");
                    cardViewEditor.setVisibility(View.GONE);
                    //// TODO: 21-Apr-17  Hide Recycler view Item
                }else{
                    txtJoinNow.setVisibility(View.GONE);
                }
                linRequestAll.setVisibility(View.GONE);
                break;
        }

        if(SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "").equals(groupDetail.getUserId())){
            linEditable.setVisibility(View.VISIBLE);
            linRequestAll.setVisibility(View.VISIBLE);
            txtJoinNow.setVisibility(View.GONE);
            cardViewEditor.setVisibility(View.VISIBLE);
        }else{
            linEditable.setVisibility(View.GONE);
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edtPost.getText().toString())){
                    insertGroupPost(edtPost.getText().toString());
                }else{
                    showSnackbar(binding.getRoot(), "Please Enter");
                }
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(new BaseActivity.OnDialogClick() {
                    @Override
                    public void onPositiveBtnClick() {
                        deleteGroup();
                    }

                    @Override
                    public void onNegativeBtnClick() {

                    }
                }, "Delete", getString(R.string.are_you_sure_delete), true);
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupDetailActivity.this, GroupCreateActivity.class);
                intent.putExtra(Constants.EXTRA_IS_EDIT_GROUP, true);
                intent.putExtra(Constants.EXTRA_GROUP_DETAIL, groupDetail);
                moveActivityResult(intent, GroupDetailActivity.this, Constants.REQUEST_ADD_GROUP);
            }
        });

        txtJoinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtJoinNow.getText().toString().equalsIgnoreCase("Join Now"))
                    joinGroup(txtJoinNow);
            }
        });


        groupDetailRecyclerAdapter = new GroupDetailRecyclerAdapter(header, this, mGroupChatDetail, GroupDetailActivity.this, mFromWhich, SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "").equals(groupDetail.getUserId()));
        binding.recyclerView.setAdapter(groupDetailRecyclerAdapter);
    }

    private void deleteGroup() {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            AsyncTaskDeleteCommon asyncTaskGetCommon = new AsyncTaskDeleteCommon(this, new AsyncTaskDeleteCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    DialogUtils.stopProgressDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("IsSuccess")) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_DELETE_GROUP+"?id="+groupDetail.getGroupId());
        }else{
            DialogUtils.stopProgressDialog();
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    private void insertGroupPost(String msg) {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("Updatemsg", msg);
            payerReg.addProperty("GroupGoalId", Integer.parseInt(groupDetail.getGroupGoalId()));
            payerReg.addProperty("PhotoURL", "");
            payerReg.addProperty("user_id", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            Log.e(TAG, "create update: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.insertGroupPost(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            edtPost.setText("");
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                AppUtils.closeKeyBoard(GroupDetailActivity.this);
                                getGroupDiscussDetails();
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

    private void getGroupDiscussDetails() {
        if(isOnline(this)){
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            Call<GroupDiscussModel> call = apiInterface.getGroupDiscussList(ApiFactory.API_BASE_URL+""+ Urls.GET_GROUP_DISCUSS+"?group_id="+groupDetail.getGroupId());
            call.enqueue(new Callback<GroupDiscussModel>() {
                @Override
                public void onResponse(Call<GroupDiscussModel>call, Response<GroupDiscussModel> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "get group: "+new Gson().toJson(response.body()));
                            if(response.body().getResult() != null && response.body().getResult().size() > 0){
                                mGroupChatDetail.clear();
                                mGroupChatDetail.addAll(response.body().getResult());
                                groupDetailRecyclerAdapter.notifyDataSetChanged();
                            }else{

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GroupDiscussModel>call, Throwable t) {
                    Log.e(TAG, t.toString());
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else{
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }


    public void addCommentToGroupDiscuss(String msg, String groupUpdateId) {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("CommentText", msg);
            payerReg.addProperty("GroupUpdateId", Integer.parseInt(groupUpdateId));
            payerReg.addProperty("PhotoURL", "");
            payerReg.addProperty("user_id", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            Log.e(TAG, "create update: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.insertCommentGroupPost(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "create update: "+new Gson().toJson(response.body()));
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                AppUtils.closeKeyBoard(GroupDetailActivity.this);
                                getGroupDiscussDetails();
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

    public void joinGroup(final TextView txtJoinNow) {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("GroupId", Integer.parseInt(groupDetail.getGroupId()));
            payerReg.addProperty("UserId", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            Log.e(TAG, "join update: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.joinGroup(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "create update: "+new Gson().toJson(response.body()));
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                AppUtils.closeKeyBoard(GroupDetailActivity.this);
                                txtJoinNow.setText("Your Request has been send!");
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

    public void supportGroupDiscuss(String groupUpdateId, String groupId) {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("GroupUpdateId", Integer.parseInt(groupUpdateId));
            payerReg.addProperty("GroupUserId", groupId);
            Log.e(TAG, "join support: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.supportGroupDetail(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "create update: "+new Gson().toJson(response.body()));
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                AppUtils.closeKeyBoard(GroupDetailActivity.this);
                                getGroupDiscussDetails();
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

    public void unSupportGroupDiscuss(String groupUpdateId, String groupId) {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("GroupUpdateId", Integer.parseInt(groupUpdateId));
            payerReg.addProperty("GroupUserId", groupId);
            Log.e(TAG, "join unsupport: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.unSupportGroupDetail(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "create update: "+new Gson().toJson(response.body()));
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                AppUtils.closeKeyBoard(GroupDetailActivity.this);
                                getGroupDiscussDetails();
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
