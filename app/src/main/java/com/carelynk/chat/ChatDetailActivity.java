package com.carelynk.chat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.chat.adapter.ChatDetailAdapter;
import com.carelynk.chat.adapter.ChatListAdapter;
import com.carelynk.chat.model.ChatDetailModel;
import com.carelynk.dashboard.GroupCreateActivity;
import com.carelynk.dashboard.GroupDetailActivity;
import com.carelynk.dashboard.PreviewImageActivity;
import com.carelynk.dashboard.model.FollowersModel;
import com.carelynk.event.EventCreateActivity;
import com.carelynk.event.EventListActivity;
import com.carelynk.event.adapter.EventListAdapter;
import com.carelynk.event.model.EventList;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskGetCommon;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.carelynk.utilz.AppUtils.createImageFile;

/**
 * Created by Mohit-Anjali on 11-Oct-17.
 */

public class ChatDetailActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private Toolbar toolbar;
    private TextView txtToolbar, txtNoData;
    private ProgressBar progressBar, progressBarChat;
    private ChatDetailAdapter chatDetailAdapter;
    private static final String TAG = "ChatDetailActivity";
    private String mUserId;
    private CameraIntentHelper mCameraIntentHelper;
    private String imagePath = "";
    private EditText mEdtMsg;
    private RelativeLayout mRelImage;
    private ImageView imgSelect;
    private List<ChatDetailModel.Result> mListChat = new ArrayList<>();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        initView();
        setRecyclerAdapter();
        checkPermission();
        setupCameraIntentHelper();
        getChatDetailDetail(true);
    }

    private void initView() {
        mUserId = getIntent().getExtras().getString(Constants.EXTRA_USERID);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtNoData = (TextView) findViewById(R.id.txtNoData);
        setSupportActionBar(toolbar);
        txtToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
        txtToolbar.setText(getIntent().getExtras().getString(Constants.EXTRA_USER_NAME)+"'s Message");
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEdtMsg = (EditText) findViewById(R.id.edtMessage);
        mRelImage = (RelativeLayout) findViewById(R.id.relImage);
        imgSelect = (ImageView) findViewById(R.id.imgSelect);
        progressBarChat = (ProgressBar) findViewById(R.id.progressBarChat);
    }

    public void onClickClose(View view) {
        mRelImage.setVisibility(View.GONE);
        imagePath = "";
    }

    void getChatDetailDetail(final boolean isRecyclerBottom) {
        if (isOnline(this)) {
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        ChatDetailModel chatDetailModel = new Gson().fromJson(result, ChatDetailModel.class);
                        if (chatDetailModel != null) {
                            mListChat.clear();
                            mListChat.addAll(chatDetailModel.getResult());
                            if (chatDetailAdapter != null) {
                                chatDetailAdapter.notifyDataSetChanged();
                            } else
                                setRecyclerAdapter();

                            if (isRecyclerBottom)
                                mRecyclerView.scrollToPosition(mListChat.size() - 1);

                            handler.postDelayed(runnable, 8000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL + "" + Urls.URL_GET_CHAT_DETAIL + "?FromUserId=" +
                    SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "") + "&ToUserId=" + mUserId);
        } else {
            progressBar.setVisibility(View.GONE);
            showSnackbar(mRecyclerView, getString(R.string.no_internet));
        }
    }

    void getChatDetailDetail() {
        if (isOnline(this)) {
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        ChatDetailModel chatDetailModel = new Gson().fromJson(result, ChatDetailModel.class);
                        if (chatDetailModel != null) {
                            mListChat.clear();
                            mListChat.addAll(chatDetailModel.getResult());
                            if (chatDetailAdapter != null) {
                                chatDetailAdapter.notifyDataSetChanged();
                            } else
                                setRecyclerAdapter();

                            mRecyclerView.scrollToPosition(mListChat.size() - 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL + "" + Urls.URL_GET_CHAT_DETAIL + "?FromUserId=" +
                    SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "") + "&ToUserId=" + mUserId);
        } else {
            progressBar.setVisibility(View.GONE);
            showSnackbar(mRecyclerView, getString(R.string.no_internet));
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getChatDetailDetail(false);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private void setRecyclerAdapter() {
        chatDetailAdapter = new ChatDetailAdapter(mListChat, ChatDetailActivity.this);
        //mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(chatDetailAdapter);
    }

    public void openImage(ImageView imgView, String imagePath){
        Intent intent = new Intent(ChatDetailActivity.this, PreviewImageActivity.class);
        intent.putExtra(Constants.EXTRA_IMAGE, imagePath);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(ChatDetailActivity.this, imgView, "profile");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
        }else{
            startActivity(intent);
        }
    }

    public void onClickSend(View view) {
        if (!TextUtils.isEmpty(mEdtMsg.getText().toString())) {
            if (isOnline(this)) {
                progressBarChat.setVisibility(View.VISIBLE);
                ApiInterface apiInterface = ApiFactory.provideInterface();
                MultipartBody.Part bodyImage = null;
                if (!TextUtils.isEmpty(imagePath)) {
                    File imageProfileFile = new File(imagePath);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageProfileFile);
                    bodyImage = MultipartBody.Part.createFormData("fileUpload", imageProfileFile.getName(), requestFile);
                }

                RequestBody ID = RequestBody.create(MediaType.parse("text/plain"), "0");
                RequestBody sender = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceUtil.getString(PrefUtils.PREF_FIRST_NAME, ""));
                RequestBody receiver = RequestBody.create(MediaType.parse("text/plain"), getIntent().getExtras().getString(Constants.EXTRA_USER_NAME));
                RequestBody message = RequestBody.create(MediaType.parse("text/plain"), mEdtMsg.getText().toString());
                RequestBody fromUserId = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
                RequestBody toUserId = RequestBody.create(MediaType.parse("text/plain"), mUserId);
                RequestBody image = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceUtil.getString(PrefUtils.PREF_PROFILE_PIC, ""));

                Call<JsonObject> call = apiInterface.sendMessage(ID, sender, receiver, message, image, fromUserId, toUserId, bodyImage);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressBarChat.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            try {
                                mEdtMsg.setText("");
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                if (jsonObject.getJSONObject("result").getBoolean("IsSuccess")) {
                                    imagePath = "";
                                    AppUtils.closeKeyBoard(ChatDetailActivity.this);
                                    getChatDetailDetail();
                                    ChatDetailModel.Result chat = new ChatDetailModel.Result();
                                    mRelImage.setVisibility(View.GONE);
                                } else {
                                    showSnackbar(mRecyclerView, jsonObject.getString("ErrorMessage"));
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
                showSnackbar(mRecyclerView, getString(R.string.no_internet));
            }
        } else {
            showSnackbar(mRecyclerView, "Please Enter Send Message");
        }

    }

    public void onClickPic(View view) {
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
                File photoFile = createImageFile();
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
                    imagePath = ImageFilePath.getPath(ChatDetailActivity.this, photoUri);
                    Log.e(TAG, "Image Camera" + imagePath);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, ChatDetailActivity.this);
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

    /**
     * End  Camera Intent handler
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_OPEN_CAMERA) {
                mCameraIntentHelper.onActivityResult(requestCode, resultCode, data);
            } else if (requestCode == Constants.REQUEST_OPEN_GALLERY) {
                getGalleryImageUri(data);
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
        mRelImage.setVisibility(View.VISIBLE);
        Glide.with(this).load(photoUri).apply(new RequestOptions().centerCrop())
                .into(imgSelect);
    }

}
