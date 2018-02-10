package com.carelynk.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.adapter.GroupDetailRecyclerAdapter;
import com.carelynk.dashboard.model.GroupDiscussModel;
import com.carelynk.databinding.ActivityFeedDetailBinding;
import com.carelynk.invite.InviteActivity;
import com.carelynk.profile.DashboardActivity;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskDeleteCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.carelynk.utilz.camera.BitmapHelper;
import com.carelynk.utilz.camera.CameraIntentHelper;
import com.carelynk.utilz.camera.CameraIntentHelperCallback;
import com.carelynk.utilz.camera.ImageFilePath;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 06-Mar-17.
 */

public class GroupDetailActivity extends BaseActivity {

    public ActivityFeedDetailBinding binding;
    private List<GroupDiscussModel.GroupDatum> mGroupChatDetail;
    private GroupDetailRecyclerAdapter groupDetailRecyclerAdapter;
    private int mFromWhich = 0;
    private static final String TAG = "GroupDetailActivity";
    private ProgressBar progressBar;
    private EditText edtPost;
    private String mGroupId;
    private GroupDiscussModel.Result mGroupDetails;
    private boolean isLoadComment = true;
    private CameraIntentHelper mCameraIntentHelper;
    private String imagePath = "";
    private ImageView imgGroupUpdate;
    private RelativeLayout mRelGroupUpdate;
    private boolean isRequestChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        //setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feed_detail);
        mFromWhich = getIntent().getExtras().getInt(AppUtils.Extra_Is_From_Which_Group);
        mGroupId = getIntent().getExtras().getString(Constants.EXTRA_GROUP_ID);

        init();

        checkPermission();

        setupCameraIntentHelper();

        getGroupDiscussDetails(false);
    }

    public void checkPermission() {
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this) &&
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, this) && checkPermission(Manifest.permission.CAMERA, this)) {
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_PERMISSION_WRITE_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constants.REQUEST_OPEN_GALLERY);
    }

    void openCamera() {
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this) &&
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, this) && checkPermission(Manifest.permission.CAMERA, this)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                File photoFile = AppUtils.createImageFile();
                imagePath = photoFile.getAbsolutePath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mCameraIntentHelper != null) {
                mCameraIntentHelper.startCameraIntent();
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);
        }
    }

    public void openGalleryDialog() {
        final Dialog dialog = new DialogUtils(this).setupCustomeDialogFromBottom(R.layout.dialog_gallery);
        ImageView imgCamera = (ImageView) dialog.findViewById(R.id.imgCamera);
        ImageView imgGallery = (ImageView) dialog.findViewById(R.id.imgGallery);
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openCamera();
            }
        });
        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openGallery();
            }
        });
        dialog.show();
    }

    public void showCommnetDialog(String groupUpdateId) {
        Intent intent = new Intent(this, CommentListGroupActivity.class);
        intent.putExtra(Constants.EXTRA_GROUP_UPDATE_ID, groupUpdateId);
        startActivity(intent);
        //DialogFragment newFragment = new CommentDialogFragment(groupUpdateId);
        //newFragment.setTargetFragment(this, Constants.REQUEST_CODE_SEND_FRIEND);
        //newFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }


    private void init() {
        //setSupportActionBar(binding.includeToolbar.toolbar);
        binding.includeToolbar.toolbarTitle.setText("Group");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(isRequestChange){
            setResult(Activity.RESULT_OK);
            finish();
        }else{
            super.onBackPressed();
        }
    }

    private void setRecyclerAdapter() {
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());


        View header = LayoutInflater.from(this).inflate(R.layout.header_group_detail, binding.recyclerView, false);
        progressBar = (ProgressBar) header.findViewById(R.id.progressBar);
        final TextView txtJoinNow = (TextView) header.findViewById(R.id.txtJoinNow);
        final ImageView imgCover = (ImageView) header.findViewById(R.id.imgCover);
        imgGroupUpdate = (ImageView) header.findViewById(R.id.img_group_update);
        ImageView imgGroupClear = (ImageView) header.findViewById(R.id.img_clear_group_update);
        ImageView imgUser = (ImageView) header.findViewById(R.id.imgUser);
        TextView txtGroupName = (TextView) header.findViewById(R.id.txtGroupName);
        TextView txtDesc = (TextView) header.findViewById(R.id.txtDesc);
        LinearLayout linEditor = (LinearLayout) header.findViewById(R.id.linEditor);
        TextView txtInviteUsers = (TextView) header.findViewById(R.id.txtInviteUsers);
        TextView txtUserName = (TextView) header.findViewById(R.id.txtName);
        TextView txtTime = (TextView) header.findViewById(R.id.txtPostTime);
        TextView txtGroupCategory = (TextView) header.findViewById(R.id.txtGroupCategory);
        TextView txtCheckRequest = (TextView) header.findViewById(R.id.txtCheckRequest);
        LinearLayout linRequestAll = (LinearLayout) header.findViewById(R.id.linRequestAll);
        mRelGroupUpdate = (RelativeLayout) header.findViewById(R.id.rel_group_update);
        ImageView btnSend = (ImageView) header.findViewById(R.id.btnSend);
        edtPost = (EditText) header.findViewById(R.id.edtPost);
        header.findViewById(R.id.txtViewMembers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupDetailActivity.this, ViewMemberActivity.class);
                intent.putExtra(Constants.EXTRA_GROUP_ID, mGroupId);
                startActivity(intent);

            }
        });
        txtInviteUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupDetailActivity.this, InviteActivity.class);
                intent.putExtra(Constants.EXTRA_GROUP_ID, mGroupId);
                startActivity(intent);

            }
        });

        imgGroupClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePath = "";
                mRelGroupUpdate.setVisibility(View.GONE);
            }
        });

        header.findViewById(R.id.imgCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryDialog();
            }
        });
        txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDashboard(mGroupDetails.getDetail().getOwnerUserId());
            }
        });

        if (!TextUtils.isEmpty(mGroupDetails.getDetail().getGroupPhotoURL())) {
            imgCover.setVisibility(View.VISIBLE);
            Glide.with(this).load(AppUtils.getImagePath(mGroupDetails.getDetail().getGroupPhotoURL())).
                    apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop()).into(imgCover);
        }else
            imgCover.setVisibility(View.GONE);
        imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupDetailActivity.this, PreviewImageActivity.class);
                intent.putExtra(Constants.EXTRA_IMAGE, AppUtils.getImagePath(mGroupDetails.getDetail().getGroupPhotoURL()));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(GroupDetailActivity.this, imgCover, "profile");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, options.toBundle());
                }else{
                    startActivity(intent);
                }
            }
        });

        txtGroupName.setText(mGroupDetails.getDetail().getGroupName());
        txtDesc.setText(mGroupDetails.getDetail().getDescription());
        txtGroupCategory.setText("Main Group : ");
        txtTime.setText(mGroupDetails.getDetail().getCreatedDate());
        txtUserName.setText(mGroupDetails.getDetail().getGroupUser());
        txtCheckRequest.setText(mGroupDetails.getDetail().getRequestCount() + " Request");
        Glide.with(this).load(AppUtils.getImagePath(mGroupDetails.getDetail().getOwnerProfilePic())).
                apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_default_avatar)).into(imgUser);


        /**
         * Set Header
         * 0 : My Group
         * 1 : Follow Group
         * 2 : All Group
         */
        switch (mFromWhich) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }

        if (mGroupDetails.getDetail().getAdmin().equalsIgnoreCase("True")) {
            txtJoinNow.setVisibility(View.GONE);
            txtInviteUsers.setVisibility(View.VISIBLE);
            linEditor.setVisibility(View.VISIBLE);
            if (Integer.parseInt(mGroupDetails.getDetail().getRequestCount()) > 0)
                txtCheckRequest.setVisibility(View.VISIBLE);
        } else if (mGroupDetails.getDetail().getAdmin().equalsIgnoreCase("False") && mGroupDetails.getDetail().getGroupUserId().equalsIgnoreCase("") && mGroupDetails.getDetail().getRequestPending().equalsIgnoreCase("1")) {
            isLoadComment = false;
            txtJoinNow.setText("Request is Pending");
            txtJoinNow.setVisibility(View.VISIBLE);
            txtInviteUsers.setVisibility(View.GONE);
            linEditor.setVisibility(View.GONE);
        } else if (mGroupDetails.getDetail().getAdmin().equalsIgnoreCase("False") && mGroupDetails.getDetail().getGroupUserId().equalsIgnoreCase("")) {
            isLoadComment = false;
            txtJoinNow.setVisibility(View.VISIBLE);
            txtInviteUsers.setVisibility(View.GONE);
            linEditor.setVisibility(View.GONE);
        } else if (mGroupDetails.getDetail().getAdmin().equalsIgnoreCase("False") && !mGroupDetails.getDetail().getGroupUserId().equalsIgnoreCase("")) {
            txtJoinNow.setVisibility(View.GONE);
            txtInviteUsers.setVisibility(View.GONE);
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtPost.getText().toString())) {
                    insertGroupPost(edtPost.getText().toString());
                } else {
                    showSnackbar(binding.getRoot(), "Please Enter");
                }
            }
        });

        txtCheckRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupDetailActivity.this, GroupRequestActivity.class);
                intent.putExtra(Constants.EXTRA_GROUP_ID, mGroupId);
                startActivityForResult(intent, 2001);
            }
        });

        /*imgDelete.setOnClickListener(new View.OnClickListener() {
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
        });*/

        txtJoinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtJoinNow.getText().toString().equalsIgnoreCase("Join Now"))
                    joinGroup(txtJoinNow);
            }
        });


        Log.e(TAG, "setRecyclerAdapter: LOad "+isLoadComment+"  size "+mGroupChatDetail.size());
        groupDetailRecyclerAdapter = new GroupDetailRecyclerAdapter(header, this, isLoadComment ? mGroupChatDetail : new ArrayList<GroupDiscussModel.GroupDatum>(), GroupDetailActivity.this, mFromWhich);
        binding.recyclerView.setAdapter(groupDetailRecyclerAdapter);
    }

    public void openImage(ImageView imgView, String imagePath){
        Intent intent = new Intent(GroupDetailActivity.this, PreviewImageActivity.class);
        intent.putExtra(Constants.EXTRA_IMAGE, imagePath);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(GroupDetailActivity.this, imgView, "profile");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
        }else{
            startActivity(intent);
        }
    }

    public void openDashboard(String ownerUserId) {
        Intent intentDash = new Intent(GroupDetailActivity.this, DashboardActivity.class);
        intentDash.putExtra(Constants.EXTRA_USERID, ownerUserId);
        startActivity(intentDash);
    }

    private void deleteGroup() {
        if (isOnline(this)) {
            DialogUtils.showProgressDialog(this);
            AsyncTaskDeleteCommon asyncTaskGetCommon = new AsyncTaskDeleteCommon(this, new AsyncTaskDeleteCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: " + result);
                    DialogUtils.stopProgressDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("IsSuccess")) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL + "" + Urls.GET_DELETE_GROUP + "?id=" + mGroupId);
        } else {
            DialogUtils.stopProgressDialog();
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    private void insertGroupPost(String msg) {
        if (isOnline(this)) {
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();


            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("Updatemsg", msg);
            payerReg.addProperty("GroupId", mGroupId);
            payerReg.addProperty("GroupGoalId", mGroupDetails.getDetail().getGroupGoalId());
            payerReg.addProperty("PhotoURL", "");
            payerReg.addProperty("user_id", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));


            MultipartBody.Part bodyImage = null;
            if (!TextUtils.isEmpty(imagePath)) {
                File imageProfileFile = new File(imagePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageProfileFile);
                bodyImage = MultipartBody.Part.createFormData("fileUpload", imageProfileFile.getName(), requestFile);
            }

            RequestBody User_ID = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            RequestBody Updatemsg = RequestBody.create(MediaType.parse("text/plain"), msg);
            RequestBody GroupId = RequestBody.create(MediaType.parse("text/plain"), mGroupId);
            RequestBody GroupGoalId = RequestBody.create(MediaType.parse("text/plain"), mGroupDetails.getDetail().getGroupGoalId());


            Log.e(TAG, "create update: " + payerReg.toString());
            Call<JsonObject> call = apiInterface.insertGroupPost(Updatemsg, GroupId, GroupGoalId, User_ID, bodyImage);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try {
                            edtPost.setText("");
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                mRelGroupUpdate.setVisibility(View.GONE);
                                imagePath = "";
                                AppUtils.closeKeyBoard(GroupDetailActivity.this);
                                getGroupDiscussDetails(false);
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
        } else {
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    private void getGroupDiscussDetails(final boolean isRefresh) {
        if (isOnline(this)) {
            showProgressDialog();
            ApiInterface apiInterface = ApiFactory.provideInterface();
            //Log.e(TAG, "APi: "+ApiFactory.API_BASE_URL + "" + Urls.GET_GROUP_DISCUSS + "?group_id=" + mGroupId + "&UserId=" + SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            Call<GroupDiscussModel> call = apiInterface.getGroupDiscussList(ApiFactory.API_BASE_URL + "" + Urls.GET_GROUP_DISCUSS + "?group_id=" + mGroupId + "&UserId=" + SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            call.enqueue(new Callback<GroupDiscussModel>() {
                @Override
                public void onResponse(Call<GroupDiscussModel> call, Response<GroupDiscussModel> response) {
                    stopProgressDialog();
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "get group: " + new Gson().toJson(response.body()));
                            if (response.body().getResult() != null) {
                                mGroupDetails = response.body().getResult();
                                mGroupChatDetail = new ArrayList<>();
                                mGroupChatDetail.addAll(response.body().getResult().getGroupData());
                                if (isRefresh) {
                                    groupDetailRecyclerAdapter.notifyDataSetChanged();
                                } else
                                    setRecyclerAdapter();
                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GroupDiscussModel> call, Throwable t) {
                    Log.e(TAG, t.toString());
                    stopProgressDialog();
                }
            });
        } else {
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }


    public void addCommentToGroupDiscuss(String msg, String groupUpdateId) {
        if (isOnline(this)) {
            if (!TextUtils.isEmpty(groupUpdateId)) {
                DialogUtils.showProgressDialog(this);
                ApiInterface apiInterface = ApiFactory.provideInterface();

                JsonObject payerReg = new JsonObject();
                payerReg.addProperty("CommentText", msg);
                payerReg.addProperty("GroupUpdateId", Integer.parseInt(groupUpdateId));
                payerReg.addProperty("PhotoURL", "");
                payerReg.addProperty("user_id", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));

                MultipartBody.Part bodyImage = null;
                if (!TextUtils.isEmpty(imagePath)) {
                    File imageProfileFile = new File(imagePath);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageProfileFile);
                    bodyImage = MultipartBody.Part.createFormData("fileUpload", imageProfileFile.getName(), requestFile);
                }

                RequestBody User_ID = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
                RequestBody CommentText = RequestBody.create(MediaType.parse("text/plain"), msg);
                RequestBody GroupUpdateId = RequestBody.create(MediaType.parse("text/plain"), groupUpdateId);


                Log.e(TAG, "create update: " + payerReg.toString());
                Call<JsonObject> call = apiInterface.insertCommentGroupPost(CommentText, GroupUpdateId, User_ID, bodyImage);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        DialogUtils.stopProgressDialog();
                        if (response.isSuccessful()) {
                            try {
                                Log.e(TAG, "create update: " + new Gson().toJson(response.body()));
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                if (jsonObject.getBoolean("IsSuccess")) {
                                    mRelGroupUpdate.setVisibility(View.GONE);
                                    imagePath = "";
                                    AppUtils.closeKeyBoard(GroupDetailActivity.this);
                                    getGroupDiscussDetails(false);
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
            } else
                showSnackbar(binding.getRoot(), "You Cant Comment, Try Later");
        } else {
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    public void joinGroup(final TextView txtJoinNow) {
        if (isOnline(this)) {
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("GroupId", Integer.parseInt(mGroupId));
            payerReg.addProperty("UserId", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            Log.e(TAG, "join update: " + payerReg.toString());
            Call<JsonObject> call = apiInterface.joinGroup(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "create update: " + new Gson().toJson(response.body()));
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                getGroupDiscussDetails(false);
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
        } else {
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    public void supportGroupDiscuss(String groupUpdateId, String groupId) {
        if (isOnline(this)) {
            if (!TextUtils.isEmpty(groupUpdateId) && !TextUtils.isEmpty(groupId)) {
                DialogUtils.showProgressDialog(this);
                ApiInterface apiInterface = ApiFactory.provideInterface();
                JsonObject payerReg = new JsonObject();
                payerReg.addProperty("GroupUpdateId", Integer.parseInt(groupUpdateId));
                payerReg.addProperty("GroupUserId", Integer.parseInt(groupId));
                Log.e(TAG, "join support: " + payerReg.toString());
                Call<JsonObject> call = apiInterface.supportGroupDetail(payerReg);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        DialogUtils.stopProgressDialog();
                        if (response.isSuccessful()) {
                            try {
                                Log.e(TAG, "create update: " + new Gson().toJson(response.body()));
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                if (jsonObject.getBoolean("IsSuccess")) {
                                    AppUtils.closeKeyBoard(GroupDetailActivity.this);
                                    getGroupDiscussDetails(false);
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
            } else
                showSnackbar(binding.getRoot(), "You Cant Support, Try Later");
        } else {
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }


    /**
     * @param savedInstanceState Start Camera Intent handler
     */

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mCameraIntentHelper.onSaveInstanceState(savedInstanceState);
    }

    private void setupCameraIntentHelper() {
        mCameraIntentHelper = new CameraIntentHelper(this, new CameraIntentHelperCallback() {
            @Override
            public void onPhotoUriFound(Date dateCameraIntentStarted, Uri photoUri, int rotateXDegrees) {
                if (photoUri != null) {
                    displayImage(photoUri);
                    imagePath = ImageFilePath.getPath(GroupDetailActivity.this, photoUri);
                    Log.e(TAG, "Image Camera" + imagePath);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, GroupDetailActivity.this);
            }

            @Override
            public void onSdCardNotMounted() {
            }

            @Override
            public void onCanceled() {
            }

            @Override
            public void onCouldNotTakePhoto() {
            }

            @Override
            public void onPhotoUriNotFound() {
            }

            @Override
            public void logException(Exception e) {
                Log.e(getClass().getName(), e.getMessage());
            }

            @Override
            public void onActivityResult(Intent intent, int requestCode) {
                startActivityForResult(intent, requestCode);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_OPEN_CAMERA) {
                mCameraIntentHelper.onActivityResult(requestCode, resultCode, data);
            } else if (requestCode == Constants.REQUEST_OPEN_GALLERY) {
                getGalleryImageUri(data);
            }else if(requestCode == 2001){
                isRequestChange = true;
                getGroupDiscussDetails(false);
            }
        }
    }

    private Uri getGalleryImageUri(Intent data) {
        Uri uri = null;
        try {
            Uri imageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(imageUri, projection, null, null,
                    null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            imagePath = selectedImagePath;
            uri = Uri.fromFile(new File(imagePath));
            Log.e(TAG, "Image Gallery" + imagePath);
            //bitmap = galleryCameraDialog.decodeUri(imageUri);
            displayImage(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void displayImage(Uri photoUri) {
        mRelGroupUpdate.setVisibility(View.VISIBLE);
        //binding.imgSelect.setVisibility(View.GONE);
        //binding.imgPreview.setVisibility(View.VISIBLE);
        // Glide.with(this).load(photoUri).transform(new CircleTransform(this)).into(binding.imgPreview);
        Glide.with(this).load(photoUri).
                apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop()).into(imgGroupUpdate);
    }

    /**
     *
     * End  Camera Intent handler
     */

}
