package com.carelynk.dashboard;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.model.GroupModelGson;
import com.carelynk.databinding.ActivityGroupCreateBinding;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.CircleTransform;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.carelynk.utilz.camera.BitmapHelper;
import com.carelynk.utilz.camera.CameraIntentHelper;
import com.carelynk.utilz.camera.CameraIntentHelperCallback;
import com.carelynk.utilz.camera.ImageFilePath;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

/**
 * Created by Admin on 19-Oct-16.
 */

public class GroupCreateActivity extends BaseActivity {

    private ActivityGroupCreateBinding binding;
    private CameraIntentHelper mCameraIntentHelper;
    private String imagePath = "";
    private static final String TAG = "GroupCreateActivity";
    private String mMainGroupID = "MainGroupID";
    private String mMainGroupName = "MainGroupName";
    private List<HashMap<String, String>> mGroupCatList = new ArrayList<>();
    private boolean mIsEditGroup;
    private GroupModelGson.OwnGroupDet mGroupDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_create);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupWindowAnimationsExplodeSlide(Gravity.BOTTOM);

        checkPermission();

        if(getIntent().hasExtra(Constants.EXTRA_IS_EDIT_GROUP)){
            mIsEditGroup = getIntent().getExtras().getBoolean(Constants.EXTRA_IS_EDIT_GROUP);
            binding.txtCreate.setText("Update");
        }
        if(mIsEditGroup){
            binding.txtToolbar.setText(getString(R.string.edit_group));
            mGroupDetail = (GroupModelGson.OwnGroupDet) getIntent().getExtras().getSerializable(Constants.EXTRA_GROUP_DETAIL);
            binding.edtDescription.setText(mGroupDetail.getDescription());
            binding.edtGroupName.setText(mGroupDetail.getGroupName());
            binding.checkPrivate.setChecked(mGroupDetail.getPublicPrivate().equalsIgnoreCase("false") ? false : true);
            Glide.with(this).load(AppUtils.getImagePath(mGroupDetail.getPhotoURL())).apply(RequestOptions.circleCropTransform()).into(binding.imgPreview);
            if(!TextUtils.isEmpty(mGroupDetail.getPhotoURL())){
                binding.imgSelect.setVisibility(View.GONE);
                binding.imgPreview.setVisibility(View.VISIBLE);
            }
        }

        setupCameraIntentHelper();

        getGroupCategory();

        binding.toolbar.setNavigationIcon(R.drawable.ic_close);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //binding.toolbar.inflateMenu(R.menu.menu_create);

        binding.imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryDialog();
            }
        });

        binding.imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryDialog();
            }
        });
    }

    public void onClickCancel(View view){
        onBackPressed();
    }

    public void onClickDone(View view){
        attemptCreateGroup();
    }

    void attemptCreateGroup(){
        if(isOnline(this)){
            if(isValid()) {
               createGroup();
            }
        }else
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
    }

    private void createGroup() {
        DialogUtils.showProgressDialog(this);
        ApiInterface apiInterface = ApiFactory.provideInterface();
        JsonObject payerReg = new JsonObject();

        payerReg.addProperty("GroupName", binding.edtGroupName.getText().toString());
        payerReg.addProperty("Description", binding.edtDescription.getText().toString());
        payerReg.addProperty("UserId", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        payerReg.addProperty("PublicPrivate", binding.checkPrivate.isChecked() ? 1 : 0);
        payerReg.addProperty("PhotoURL", "");
        payerReg.addProperty("MainGroupId", Integer.parseInt(getSelectedGroupCatId()));

        MultipartBody.Part bodyImage = null;
        RequestBody PhotoUrl = null;
        if(!TextUtils.isEmpty(imagePath)) {
            File imageProfileFile = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageProfileFile);
            bodyImage = MultipartBody.Part.createFormData("fileUpload", imageProfileFile.getName(), requestFile);
        }

        if(mIsEditGroup)
            PhotoUrl = RequestBody.create(MediaType.parse("text/plain"), mGroupDetail.getPhotoURL());

        RequestBody User_ID = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        RequestBody GroupName = RequestBody.create(MediaType.parse("text/plain"), binding.edtGroupName.getText().toString());
        RequestBody Description = RequestBody.create(MediaType.parse("text/plain"), binding.edtDescription.getText().toString());
        RequestBody PublicPrivate = RequestBody.create(MediaType.parse("text/plain"), binding.checkPrivate.isChecked() ? "true" : "false");
        RequestBody MainGroupId = RequestBody.create(MediaType.parse("text/plain"), getSelectedGroupCatId());


        if(mIsEditGroup){
            payerReg.addProperty("GroupId", Integer.parseInt(mGroupDetail.getGroupId()));
            RequestBody GroupId = RequestBody.create(MediaType.parse("text/plain"), mGroupDetail.getGroupId());
            Log.e(TAG, "edit group: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.updateGroup(User_ID, GroupId, GroupName, Description, PublicPrivate, MainGroupId,
                    PhotoUrl, bodyImage);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "onResponse: " + response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                AppUtils.closeKeyBoard(GroupCreateActivity.this);
                                showSnackbar(binding.getRoot(), "Group Update Successfully");
                                setResult(RESULT_OK);
                                finish();
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
        }else {
            Log.e(TAG, "create group: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.createGroup(User_ID, GroupName, Description, PublicPrivate, MainGroupId, bodyImage);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "onResponse: " + response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                AppUtils.closeKeyBoard(GroupCreateActivity.this);
                                showSnackbar(binding.getRoot(), "Group Create Successfully");
                                setResult(RESULT_OK);
                                finish();
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
        }
    }

    private boolean isValid() {
        if(TextUtils.isEmpty(binding.edtGroupName.getText().toString())){
            showSnackbar(binding.getRoot(), "Please enter group name");
            binding.edtGroupName.requestFocus();
            return false;
        }
        return true;
    }

    private String getSelectedGroupCatId() {
        return mGroupCatList.get(binding.spnrGroupCategory.getSelectedItemPosition()).get(mMainGroupID);
    }


    private void getGroupCategory() {
        if(isOnline(this)){
            ApiInterface apiInterface = ApiFactory.provideInterface();
            Call<JsonObject> call = apiInterface.getGroupCategory();
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try{
                            binding.progressBar.setVisibility(View.GONE);
                            Log.e(TAG, "onResponse: "+response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(mMainGroupID, ""+object.getInt("MainGroupID"));
                                map.put(mMainGroupName, ""+object.getString("MainGroupName"));
                                mGroupCatList.add(map);
                            }
                            setSpinnerAdapter(getGroupCatName());
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
            binding.progressBar.setVisibility(View.GONE);
            showSnackbar(binding.getRoot(), "Error");
        }
    }

    private List<String> getGroupCatName() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < mGroupCatList.size(); i++) {
            list.add(mGroupCatList.get(i).get(mMainGroupName));
        }
        return list;
    }

    void setSpinnerAdapter(List<String> country){
        if(country.size() > 0) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, country);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spnrGroupCategory.setAdapter(spinnerArrayAdapter);
        }

        if(mIsEditGroup){
            binding.spnrGroupCategory.setSelection(getSelectedGroupPos());
        }
    }

    private int getSelectedGroupPos() {
        for (int i = 0; i < mGroupCatList.size(); i++) {
            Log.e(TAG, "getSelectedGroupPos: "+mGroupCatList.get(i).get(mMainGroupID)+"  ==  "+mGroupDetail.getMainGroupId());
            if(mGroupCatList.get(i).get(mMainGroupID).equals(mGroupDetail.getMainGroupId()))
                return i;
        }
        return 0;
    }

    void openGalleryDialog(){
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return true;
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
        startActivityForResult(intent, Constants.REQUEST_OPEN_GALLERY);
    }

    void openCamera(){
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,this) &&
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
        }else{
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Carelync_" + timeStamp + "_";
        File sdCard = new File(Environment.getExternalStorageDirectory()+"/Carelync/Images");
        if(!sdCard.exists())
            sdCard.mkdirs();
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    sdCard      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
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
                    imagePath = ImageFilePath.getPath(GroupCreateActivity.this, photoUri);
                    Log.e(TAG, "Image Camera" + imagePath);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, GroupCreateActivity.this);
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
            uri= Uri.fromFile(new File(imagePath));
            Log.e(TAG, "Image Gallery" + imagePath);
            //bitmap = galleryCameraDialog.decodeUri(imageUri);
            displayImage(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void displayImage(Uri photoUri) {
        binding.imgSelect.setVisibility(View.GONE);
        binding.imgPreview.setVisibility(View.VISIBLE);
        Glide.with(this).load(photoUri).apply(RequestOptions.circleCropTransform())
                .into(binding.imgPreview);
    }
}
