package com.carelynk.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.carelynk.dashboard.adapter.FeedDetailRecyclerAdapter;
import com.carelynk.dashboard.model.GoalListDetail;
import com.carelynk.databinding.ActivityFeedDetailBinding;
import com.carelynk.profile.DashboardActivity;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.ImageGetterAsyncTask;
import com.carelynk.utilz.MyTagHandler;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.carelynk.utilz.Constants.REQUEST_CODE_UPDATE_GOAL;

/**
 * Created by Admin on 06-Mar-17.
 */

public class FeedDetailActivity extends BaseActivity {

    public ActivityFeedDetailBinding binding;
    private List<GoalListDetail.GoalArray> mFeedDetail = new ArrayList<>();
    private FeedDetailRecyclerAdapter feedDetailRecyclerAdapter;
    private EditText edtComment;
    private static final String TAG = "FeedDetailActivity";
    private ProgressBar progressBar;
    private String mGoalID;
    private GoalListDetail.GoalDetail mGoalDetails;
    private boolean isChangeInFeed;
    private CameraIntentHelper mCameraIntentHelper;
    private String imagePath = "";
    private ImageView imgGroupUpdate;
    private RelativeLayout mRelGroupUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        //setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feed_detail);
        init();
        getGoalPostDetails(false);
        checkPermission();

        setupCameraIntentHelper();
    }

    public void showCommnetDialog(String updateId) {
        Intent intent = new Intent(this, CommentListFeedActivity.class);
        intent.putExtra(Constants.EXTRA_GOAL_ID, mGoalID);
        intent.putExtra(Constants.EXTRA_GROUP_UPDATE_ID, updateId);
        startActivity(intent);
        //DialogFragment newFragment = new CommentDialogGoalFragment(mGoalID, updateId);
        //newFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
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
        mGoalID = getIntent().getExtras().getString(AppUtils.Extra_Goal_Id);
    }

    private void setRecyclerAdapter() {
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        /**
         * Set Header
         */
        View header = LayoutInflater.from(this).inflate(R.layout.header_feed_detail, binding.recyclerView, false);
        RelativeLayout relEdit = (RelativeLayout) header.findViewById(R.id.relEdit);
        LinearLayout linSupport = (LinearLayout) header.findViewById(R.id.linSupport);
        ImageView imgEdit = (ImageView) header.findViewById(R.id.imgEdit);
        ImageView imgDelete = (ImageView) header.findViewById(R.id.imgDelete);
        TextView txtTitle = (TextView) header.findViewById(R.id.txtTitle);
        TextView txtName = (TextView) header.findViewById(R.id.txtName);
        final TextView txtDesc = (TextView) header.findViewById(R.id.txtDesc);
        TextView txtPostTime = (TextView) header.findViewById(R.id.txtPostTime);
        TextView txtPlayVideo = (TextView) header.findViewById(R.id.txtPlayVideo);
        final ImageView imgCover = (ImageView) header.findViewById(R.id.imgCover);
        ImageView imgUser = (ImageView) header.findViewById(R.id.imgUser);
        LinearLayout linWeb = (LinearLayout) header.findViewById(R.id.linWebView);
        WebView webView = (WebView) header.findViewById(R.id.webView);
        imgGroupUpdate = (ImageView) header.findViewById(R.id.img_group_update);
        mRelGroupUpdate = (RelativeLayout) header.findViewById(R.id.rel_group_update);

        edtComment = (EditText) header.findViewById(R.id.edtComment);
        ImageView btnSend = (ImageView) header.findViewById(R.id.btnSend);
        CheckBox chkSupport = (CheckBox) header.findViewById(R.id.chkSupport);
        chkSupport.setChecked(mGoalDetails.getSelfSupportFlag().equalsIgnoreCase("0") ? false : true);
        chkSupport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    addSupport(mGoalID);
                }else{
                    addSupport(mGoalID);
                }
            }
        });
        progressBar = (ProgressBar) header.findViewById(R.id.progressBar);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtComment.getText().toString())){
                    showSnackbar(binding.getRoot(), "Please Enter Comment");
                }else{
                    insertGroupPost(edtComment.getText().toString());
                }
            }
        });
        header.findViewById(R.id.imgCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryDialog();
            }
        });
        header.findViewById(R.id.img_clear_group_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePath = "";
                mRelGroupUpdate.setVisibility(View.GONE);
            }
        });
        txtPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(AppUtils.getImagePath(mGoalDetails.getVideoURL())), "video/mp4");
                startActivity(intent);
            }
        });

        if (!TextUtils.isEmpty(mGoalDetails.getProfilePicUrl())) {
            Glide.with(this).load(AppUtils.getImagePath(mGoalDetails.getProfilePicUrl())).
                    apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy)).into(imgUser);
        }


        if (!TextUtils.isEmpty(mGoalDetails.getPhotoURL())) {
            imgCover.setVisibility(View.VISIBLE);
            Glide.with(this).load(AppUtils.getImagePath(mGoalDetails.getPhotoURL())).
                    apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop()).into(imgCover);
        }else
            imgCover.setVisibility(View.GONE);

        imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedDetailActivity.this, PreviewImageActivity.class);
                intent.putExtra(Constants.EXTRA_IMAGE, AppUtils.getImagePath(mGoalDetails.getPhotoURL()));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(FeedDetailActivity.this, imgCover, "profile");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, options.toBundle());
                }else{
                    startActivity(intent);
                }
            }
        });

        txtTitle.setText(Html.fromHtml(mGoalDetails.getGoalName()));
        Spanned spanned = Html.fromHtml(mGoalDetails.getDescrptn(),
                new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        LevelListDrawable d = new LevelListDrawable();
                        Drawable empty = getResources().getDrawable(R.drawable.ic_placeholder);
                        d.addLevel(0, 0, empty);
                        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                        new ImageGetterAsyncTask(FeedDetailActivity.this, source, d).execute(txtDesc);
                        return d;
                    }
                }, new MyTagHandler());
        txtDesc.setText(spanned);
        txtDesc.setMovementMethod(LinkMovementMethod.getInstance());
        txtPostTime.setText(mGoalDetails.getCreatedDate());
        txtName.setText(mGoalDetails.getCreatedName());
        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDashboard(mGoalDetails.getUserId());
            }
        });
        if(TextUtils.isEmpty(mGoalDetails.getVideoURL()))
            txtPlayVideo.setVisibility(View.GONE);
        else
            txtPlayVideo.setVisibility(View.VISIBLE);
        if(mGoalDetails.getDescrptn().contains("<iframe")){
            linWeb.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient() {
            } );
            String mLink = mGoalDetails.getDescrptn().split("<iframe")[1].split("</iframe>")[0];
            String mFull = "<iframe "+mLink+"</iframe>";
            Matcher matcher = Pattern.compile("src=\"([^\"]+)\"").matcher(mFull);
            matcher.find();
            String src = matcher.group(1);
            webView.loadData(AppUtils.getYoutubeUrl(src), "text/html" , "utf-8" );
        }

        if(SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "").equalsIgnoreCase(mGoalDetails.getUserId())){
            relEdit.setVisibility(View.VISIBLE);
        }

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedDetailActivity.this, WriteArticleActivity.class);
                intent.putExtra(Constants.EXTRA_TITLE, mGoalDetails.getGoalName());
                intent.putExtra(Constants.EXTRA_DESCRIPTION, mGoalDetails.getDescrptn());
                intent.putExtra(Constants.EXTRA_GOAL_ID, mGoalDetails.getGoalId());
                intent.putExtra(Constants.EXTRA_PROFILE, mGoalDetails.getPhotoURL());
                intent.putExtra(Constants.EXTRA_VIDEO_PATH, mGoalDetails.getVideoURL());
                startActivityForResult(intent, REQUEST_CODE_UPDATE_GOAL);
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(new OnDialogClick() {
                    @Override
                    public void onPositiveBtnClick() {
                        deleteGoal(mGoalDetails.getGoalId());
                    }

                    @Override
                    public void onNegativeBtnClick() {

                    }
                }, getString(R.string.delete), getString(R.string.are_you_sure_delete), true);
            }
        });

        feedDetailRecyclerAdapter = new FeedDetailRecyclerAdapter(header, this, mFeedDetail, FeedDetailActivity.this, binding.recyclerView);
        binding.recyclerView.setAdapter(feedDetailRecyclerAdapter);
    }

    public void openDashboard(String userId) {
        Intent intentDash = new Intent(FeedDetailActivity.this, DashboardActivity.class);
        intentDash.putExtra(Constants.EXTRA_USERID, userId);
        startActivity(intentDash);
    }

    public void openImage(ImageView imgView, String imagePath){
        Intent intent = new Intent(FeedDetailActivity.this, PreviewImageActivity.class);
        intent.putExtra(Constants.EXTRA_IMAGE, imagePath);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(FeedDetailActivity.this, imgView, "profile");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
        }else{
            startActivity(intent);
        }
    }

    private void deleteGoal(String goalId) {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    DialogUtils.stopProgressDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getJSONObject("result").getBoolean("IsSuccess")) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            showSnackbar(binding.getRoot(), jsonObject.getJSONObject("result").getString("ErrorMessage"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.DELETE_GOAL+"?id="+goalId);
        }else{
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }


    private void addSupport(String goalId) {
        if(isOnline(this)){
            showProgressDialog();
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("UserId", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            payerReg.addProperty("SupportedDate", "2016-08-25");
            payerReg.addProperty("GoalId", goalId);
            Log.e(TAG, "addSupport: " + payerReg.toString());
            Call<JsonObject> call = apiInterface.addSupportGoal(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            isChangeInFeed = true;
                            Log.e(TAG, "get group: "+new Gson().toJson(response.body()));
                            getGoalPostDetails(false);
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


    private void getGoalPostDetails(final boolean isRefresh) {
        if(isOnline(this)){
            showProgressDialog();
            ApiInterface apiInterface = ApiFactory.provideInterface();
            Log.e(TAG, "getGoalPostDetails: "+ApiFactory.API_BASE_URL+""+ Urls.GET_GOAL_POST_LIST+"?Goal_id="+mGoalID+"&UserId="+SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            Call<GoalListDetail> call = apiInterface.getGoalDetailList(ApiFactory.API_BASE_URL+""+ Urls.GET_GOAL_POST_LIST+"?Goal_id="+mGoalID+"&UserId="+SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            call.enqueue(new Callback<GoalListDetail>() {
                @Override
                public void onResponse(Call<GoalListDetail>call, Response<GoalListDetail> response) {
                    stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "get group: "+new Gson().toJson(response.body()));
                           if(response.body().getResult() != null){
                               mGoalDetails = response.body().getResult().getGoalDetail();
                               mFeedDetail.clear();
                               mFeedDetail.addAll(response.body().getResult().getGoalArray());
                               if(isRefresh)
                                    feedDetailRecyclerAdapter.notifyDataSetChanged();
                               else
                                    setRecyclerAdapter();
                           }else{

                           }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GoalListDetail>call, Throwable t) {
                    Log.e(TAG, t.toString());
                    stopProgressDialog();
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
            payerReg.addProperty("GoalId", mGoalID);
            payerReg.addProperty("PhotoURL", "");
            payerReg.addProperty("UserId", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            Log.e(TAG, "create goal detail: "+payerReg.toString());

            MultipartBody.Part bodyImage = null;
            if (!TextUtils.isEmpty(imagePath)) {
                File imageProfileFile = new File(imagePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageProfileFile);
                bodyImage = MultipartBody.Part.createFormData("fileUpload", imageProfileFile.getName(), requestFile);
            }

            RequestBody Updatemsg = RequestBody.create(MediaType.parse("text/plain"), msg);
            RequestBody GoalId = RequestBody.create(MediaType.parse("text/plain"), mGoalID);
            RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));

            Call<JsonObject> call = apiInterface.insertGoalPost(Updatemsg, GoalId, UserId, bodyImage);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            isChangeInFeed = true;
                            Log.e(TAG, "insert group: "+new Gson().toJson(response.body()));
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                mRelGroupUpdate.setVisibility(View.GONE);
                                imagePath = "";
                                edtComment.setText("");
                                AppUtils.closeKeyBoard(FeedDetailActivity.this);
                                getGoalPostDetails(true);
                            }else{
                                getGoalPostDetails(true);
                                //showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
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

            MultipartBody.Part bodyImage = null;
            if (!TextUtils.isEmpty(imagePath)) {
                File imageProfileFile = new File(imagePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageProfileFile);
                bodyImage = MultipartBody.Part.createFormData("fileUpload", imageProfileFile.getName(), requestFile);
            }

            RequestBody CommentText = RequestBody.create(MediaType.parse("text/plain"), comment);
            RequestBody UpdateId = RequestBody.create(MediaType.parse("text/plain"), updateId);
            RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));

            Call<JsonObject> call = apiInterface.insertGoalPostComment(CommentText, UpdateId, UserId, bodyImage);
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
                                getGoalPostDetails(true);
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


    public void supportGroupDiscuss(String updateId) {
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("UpdateSupportedDate", "2016-08-25");
            payerReg.addProperty("UpdateId", updateId);
            payerReg.addProperty("UserID", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            Log.e(TAG, "supportGoalDiscuss: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.supportGoalListDetail(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "onResponse: "+response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                AppUtils.closeKeyBoard(FeedDetailActivity.this);
                                showSnackbar(binding.getRoot(), "Support Success");
                                getGoalPostDetails(true);
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

    @Override
    public void onBackPressed() {
        if(isChangeInFeed){
            setResult(Activity.RESULT_OK);
            finish();
        }else{
            super.onBackPressed();
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
                    imagePath = ImageFilePath.getPath(FeedDetailActivity.this, photoUri);
                    Log.e(TAG, "Image Camera" + imagePath);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, FeedDetailActivity.this);
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
            }else if(requestCode == REQUEST_CODE_UPDATE_GOAL){
                isChangeInFeed = true;
                getGoalPostDetails(false);
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
