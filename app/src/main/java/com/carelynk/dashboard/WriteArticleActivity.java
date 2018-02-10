package com.carelynk.dashboard;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.model.AutoCompliteArticle;
import com.carelynk.dashboard.model.SearchData;
import com.carelynk.invite.adapter.ContactInviteListAdapter;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.FileUtils;
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
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 20-Oct-16.
 */

public class WriteArticleActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView txtToolbar;
    private static final String TAG = "WriteArticleActivity";
    private RichEditor richEditor;
    private AutoCompleteTextView edtTopicArticle;
    private CameraIntentHelper mCameraIntentHelper;
    private String imagePath = "", videoPath = "";
    private ImageView imgSelection, imgCancel, imgVideoCancel, imgVideoSelection;
    private TextView txtYoutube;
    private RelativeLayout relVideo, relImage;
    private boolean mEditArticle;
    private AsyncTaskGetCommon asyncTaskGetCommon;
    private String mEditImagePath = "", mEditVideoPath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_article);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);
        initView();
        checkPermission();
        setupCameraIntentHelper();
    }

    void openGalleryDialog(){
        final Dialog dialog = new DialogUtils(this).setupCustomeDialogFromBottom(R.layout.dialog_gallery);
        ImageView imgCamera = (ImageView) dialog.findViewById(R.id.imgCamera);
        ImageView imgGallery = (ImageView) dialog.findViewById(R.id.imgGallery);
        ImageView imgVideo = (ImageView) dialog.findViewById(R.id.imgVideo);
        imgVideo.setVisibility(View.VISIBLE);
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
        imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openVideo();
            }
        });
        dialog.show();
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgSelection = (ImageView) findViewById(R.id.imgSelection);
        imgCancel = (ImageView) findViewById(R.id.imgCancel);
        imgVideoCancel = (ImageView) findViewById(R.id.imgVideoCancel);
        imgVideoSelection = (ImageView) findViewById(R.id.imgVideoSelection);

        setSupportActionBar(toolbar);
        txtToolbar = (TextView) toolbar.findViewById(R.id.txtToolbar);
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtYoutube = (TextView) findViewById(R.id.txtYoutube);
        relVideo = (RelativeLayout) findViewById(R.id.relVideo);
        relImage = (RelativeLayout) findViewById(R.id.relImage);
        richEditor = (RichEditor) findViewById(R.id.richEditor);
        //richEditor.setBackgroundResource(R.drawable.round_corner_grey);
        richEditor.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        richEditor.setEditorFontSize(22);
        richEditor.setEditorFontColor(Color.BLACK);
        richEditor.setPadding(10, 10, 10, 10);
        richEditor.setPlaceholder("Insert Article here...");
        edtTopicArticle = (AutoCompleteTextView) findViewById(R.id.edtTopicArticle);

        findViewById(R.id.img_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.undo();
            }
        });
        findViewById(R.id.img_redu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.redo();
            }
        });
        findViewById(R.id.img_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setBold();
            }
        });
        findViewById(R.id.img_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setItalic();
            }
        });
        findViewById(R.id.img_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setUnderline();
            }
        });
        findViewById(R.id.img_header_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(1);
            }
        });
        findViewById(R.id.img_header_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(2);
            }
        });
        findViewById(R.id.img_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignCenter();
            }
        });
        findViewById(R.id.img_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignLeft();
            }
        });
        findViewById(R.id.img_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignRight();
            }
        });
        findViewById(R.id.imgSelectArticle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryDialog();
            }
        });
        findViewById(R.id.img_tube).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogForTubeLink();
            }
        });

        findViewById(R.id.btnPostArticle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String mYoutube = TextUtils.isEmpty(txtYoutube.getText().toString()) ? "" :
                            AppUtils.getYoutubeUrl(txtYoutube.getText().toString());
                    if (!TextUtils.isEmpty(richEditor.getHtml().toString()) && !TextUtils.isEmpty(edtTopicArticle.getText().toString())) {
                        createPost(edtTopicArticle.getText().toString() ,richEditor.getHtml().toString()+" "+
                                mYoutube);
                    } else {
                        showSnackbar(toolbar, "Please Enter Share Text");
                    }
                }catch (Exception e){
                    showSnackbar(toolbar, "Please Enter Share Text");
                    e.printStackTrace();
                }
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relImage.setVisibility(View.GONE);
                imagePath = "";
                if(mEditArticle)
                    mEditImagePath = "";
            }
        });

        imgVideoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditArticle)
                    mEditVideoPath = "";
                relVideo.setVisibility(View.GONE);
                videoPath = "";
            }
        });

        if(getIntent().hasExtra(Constants.EXTRA_GOAL_ID)){
            mEditArticle = true;
            edtTopicArticle.setText(getIntent().getExtras().getString(Constants.EXTRA_TITLE));
            richEditor.setHtml(getIntent().getExtras().getString(Constants.EXTRA_DESCRIPTION));
            if(!TextUtils.isEmpty(getIntent().getExtras().getString(Constants.EXTRA_PROFILE))){
                relImage.setVisibility(View.VISIBLE);
                mEditImagePath = getIntent().getExtras().getString(Constants.EXTRA_PROFILE);
                Glide.with(this).load(AppUtils.getImagePath(getIntent().getExtras().getString(Constants.EXTRA_PROFILE))).apply(new RequestOptions()).into(imgSelection);
            }
            if(!TextUtils.isEmpty(getIntent().getExtras().getString(Constants.EXTRA_VIDEO_PATH))){
                mEditVideoPath = getIntent().getExtras().getString(Constants.EXTRA_VIDEO_PATH);
                relVideo.setVisibility(View.VISIBLE);
            }
        }

        edtTopicArticle.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isOnline(WriteArticleActivity.this)) {
                    if(asyncTaskGetCommon != null)
                        asyncTaskGetCommon.cancel(true);
                    onSearchAttempt();
                } else {
                    showSnackbar(relImage, getString(R.string.no_internet));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(asyncTaskGetCommon != null)
            asyncTaskGetCommon.cancel(true);
        super.onBackPressed();
    }

    private void onSearchAttempt() {
        if (edtTopicArticle.getText().length() > 0) {
            asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    AutoCompliteArticle autoCompliteArticle = new Gson().fromJson(result, AutoCompliteArticle.class);
                    if (autoCompliteArticle != null) {
                        edtTopicArticle.setAdapter(getList(autoCompliteArticle.getResult().getGoals()));
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL + "" + Urls.GET_SEARCH_ARTICLE + edtTopicArticle.getText().toString());
        }
    }

    private ArrayAdapter<String> getList(List<AutoCompliteArticle.Goal> mList) {
        String[] addresses = new String[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
            addresses[i] = mList.get(i).getName();
        }
        return new ArrayAdapter<String>(WriteArticleActivity.this, android.R.layout.simple_dropdown_item_1line, addresses);
    }

    private void openDialogForTubeLink() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage("URL (ex: https://www.youtube.com/embed/PlgSC4YeBjY)");
        alert.setTitle("Youtube Video Link");
        alert.setView(edittext);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String mStr = edittext.getText().toString();
                txtYoutube.setText(mStr);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    void createPost(String title, String desc){
        if(isOnline(this)){
            DialogUtils.showProgressDialog(this);
            ApiInterface apiInterface = ApiFactory.provideInterface();

            MultipartBody.Part bodyImage = null;
            if(!TextUtils.isEmpty(imagePath)) {
                File imageProfileFile = new File(imagePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageProfileFile);
                bodyImage = MultipartBody.Part.createFormData("fileUpload", imageProfileFile.getName(), requestFile);
            }

            MultipartBody.Part bodyVideo = null;
            if(!TextUtils.isEmpty(videoPath)) {
                File imageProfileFile = new File(videoPath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), imageProfileFile);
                bodyVideo = MultipartBody.Part.createFormData("fileUpload1", imageProfileFile.getName(), requestFile);
            }


            RequestBody goalD = RequestBody.create(MediaType.parse("text/plain"), mEditArticle ? getIntent().getExtras().getString(Constants.EXTRA_GOAL_ID) : "0");
            RequestBody goalName = RequestBody.create(MediaType.parse("text/plain"), title);
            RequestBody Description = RequestBody.create(MediaType.parse("text/plain"), desc);
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "False");
            RequestBody image = RequestBody.create(MediaType.parse("text/plain"), mEditImagePath);
            RequestBody video = RequestBody.create(MediaType.parse("text/plain"), mEditVideoPath);

            if(mEditArticle){
                Call<JsonObject> call = apiInterface.updatePostGoal(goalD, goalName, Description, type, image, video, bodyImage, bodyVideo);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                        DialogUtils.stopProgressDialog();
                        if (response.isSuccessful()) {
                            try{
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                if (jsonObject.getBoolean("IsSuccess")) {
                                    AppUtils.closeKeyBoard(WriteArticleActivity.this);
                                    setResult(RESULT_OK);
                                    finish();
                                }else{
                                    showSnackbar(toolbar, jsonObject.getString("ErrorMessage"));
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

            }else {
                Call<JsonObject> call = apiInterface.createPostGoal(goalD, goalName, Description, type, userId, bodyImage, bodyVideo);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        DialogUtils.stopProgressDialog();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONObject object = jsonObject.getJSONObject("result");
                                if (object.getBoolean("IsSuccess")) {
                                    AppUtils.closeKeyBoard(WriteArticleActivity.this);
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    showSnackbar(toolbar, jsonObject.getString("ErrorMessage"));
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
            }
        }else
            showSnackbar(toolbar, getString(R.string.no_internet));
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
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.REQUEST_PERMISSION_WRITE_STORAGE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});
        startActivityForResult(intent, Constants.REQUEST_OPEN_GALLERY);
    }

    void openVideo() {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/* video/*");
            startActivityForResult(Intent.createChooser(intent, "Select Video"), Constants.REQUEST_OPEN_VIDEO);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});
            startActivityForResult(intent, Constants.REQUEST_OPEN_VIDEO);
        }
    }

    void openCamera(){
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,this) &&
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
        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);
        }
    }

    /**
     *
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
                    imagePath = ImageFilePath.getPath(WriteArticleActivity.this, photoUri);
                    Log.e(TAG, "Image Camera" + imagePath);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, WriteArticleActivity.this);
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
     *
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
            }else if (requestCode == Constants.REQUEST_OPEN_VIDEO) {
                getVideoPath(data);
            }
        }
    }

    private void getVideoPath(Intent data) {
        Uri selectedImageUri = data.getData();
        videoPath =  FileUtils.getPath(this, selectedImageUri);
        if(!TextUtils.isEmpty(videoPath)) {
            File file = new File(videoPath);
            long sizeInBytes = file.length();
            long sizeInMb = sizeInBytes / (1024 * 1024);
            if (sizeInMb > 10) {
                videoPath = "";
                Snackbar.make(relImage, "Please Select Video below 10 MB", Snackbar.LENGTH_LONG).show();
            }else
                relVideo.setVisibility(View.VISIBLE);
        }
    }

    private Uri getGalleryImageUri(Intent data) {
        Uri uri = null;
        try {
            Uri imageUri = data.getData();
            if (imageUri.toString().contains("image")) {
                /*String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = getContentResolver().query(imageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);*/
                //imagePath = selectedImagePath;
                imagePath = ImageFilePath.getPath(WriteArticleActivity.this, imageUri);
                uri= Uri.fromFile(new File(imagePath));
                Log.e(TAG, "Image Gallery" + imagePath);
                //bitmap = galleryCameraDialog.decodeUri(imageUri);
                displayImage(uri);
            } else  if (imageUri.toString().contains("video")) {
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = getContentResolver().query(imageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                //uri= Uri.fromFile(new File(imagePath));
                Log.e(TAG, "Video Gallery" + selectedImagePath);
            }else{
                imagePath = ImageFilePath.getPath(WriteArticleActivity.this, imageUri);
                uri= Uri.fromFile(new File(imagePath));
                Log.e(TAG, "Else Image Gallery" + imagePath);
                //bitmap = galleryCameraDialog.decodeUri(imageUri);
                displayImage(uri);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void displayImage(Uri photoUri) {
        relImage.setVisibility(View.VISIBLE);
        Glide.with(this).load(photoUri).apply(new RequestOptions()).into(imgSelection);
    }

}
