package com.carelynk.dashboard;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.adapter.CommentRecyclerAdapter;
import com.carelynk.dashboard.model.CommentModel;
import com.carelynk.databinding.DialogCommentListBinding;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
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
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.carelynk.utilz.AppUtils.createImageFile;

/**
 * Created by Admin on 20-Oct-16.
 */

public class CommentListFeedActivity extends BaseActivity {

    private DialogCommentListBinding binding;
    private static final String TAG = "CommentListFeedActivity";
    private String groupUpdateId, goalId;
    private CommentRecyclerAdapter commentRecyclerAdapter;
    private CameraIntentHelper mCameraIntentHelper;
    private String imagePath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_comment_list);
        initView();
        checkPermission();
        setupCameraIntentHelper();
        getGroalCOmmentDetails();
    }

    public void onClickClose(View view) {
        binding.relImage.setVisibility(View.GONE);
        imagePath = "";
    }

    private void initView() {
        binding.includeToolbar.toolbarTitle.setText(getString(R.string.comment));
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        groupUpdateId = getIntent().getExtras().getString(Constants.EXTRA_GROUP_UPDATE_ID);
        goalId = getIntent().getExtras().getString(Constants.EXTRA_GOAL_ID);
    }

    private void setRecyclerAdapter(List<CommentModel.Result> mList) {
        commentRecyclerAdapter = new CommentRecyclerAdapter(this, mList);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(commentRecyclerAdapter);
    }

    private void getGroalCOmmentDetails() {
        if(AppUtils.isOnline(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            Call<CommentModel> call = apiInterface.getGoalCommentList(ApiFactory.API_BASE_URL+""+ Urls.GET_GOAL_COMMENT_LIST+"?UpdateID="+groupUpdateId+"&goalid="+goalId);
            Log.e(TAG, "getGroalCOmmentDetails: "+ApiFactory.API_BASE_URL+""+ Urls.GET_GOAL_COMMENT_LIST+"?UpdateID="+groupUpdateId+"&goalid="+goalId);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel>call, Response<CommentModel> response) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "getGroalCOmmentDetails: "+new Gson().toJson(response.body()));
                            if(response.body().getResult() != null && response.body().getResult().size() > 0){
                                setRecyclerAdapter(response.body().getResult());
                            }else{

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel>call, Throwable t) {
                    Log.e(TAG, t.toString());
                    binding.progressBar.setVisibility(View.GONE);
                }
            });
        }else{
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    public void onClickSend(View view) {
        if (!TextUtils.isEmpty(binding.edtMessage.getText().toString())) {
            if (isOnline(this)) {
                binding.progressBarChat.setVisibility(View.VISIBLE);
                ApiInterface apiInterface = ApiFactory.provideInterface();

                MultipartBody.Part bodyImage = null;
                if (!TextUtils.isEmpty(imagePath)) {
                    File imageProfileFile = new File(imagePath);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageProfileFile);
                    bodyImage = MultipartBody.Part.createFormData("fileUpload", imageProfileFile.getName(), requestFile);
                }

                RequestBody CommentText = RequestBody.create(MediaType.parse("text/plain"), binding.edtMessage.getText().toString());
                RequestBody UpdateId = RequestBody.create(MediaType.parse("text/plain"), groupUpdateId);
                RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));

                Call<JsonObject> call = apiInterface.insertGoalPostComment(CommentText, UpdateId, UserId, bodyImage);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                        binding.progressBarChat.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            try{
                                Log.e(TAG, "addCommentToGoalDiscuss: "+new Gson().toJson(response.body()));
                                binding.edtMessage.setText("");
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                if (jsonObject.getBoolean("IsSuccess")) {
                                    AppUtils.closeKeyBoard(CommentListFeedActivity.this);
                                    showSnackbar(binding.getRoot(), "Comment Post Success");
                                    getGroalCOmmentDetails();
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
        }else {
            showSnackbar(binding.getRoot(), "Please Enter Send Message");
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
                    imagePath = ImageFilePath.getPath(CommentListFeedActivity.this, photoUri);
                    Log.e(TAG, "Image Camera" + imagePath);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, CommentListFeedActivity.this);
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
        binding.relImage.setVisibility(View.VISIBLE);
        Glide.with(this).load(photoUri).apply(new RequestOptions().centerCrop())
                .into(binding.imgSelect);
    }

}
