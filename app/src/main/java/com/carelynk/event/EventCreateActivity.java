package com.carelynk.event;

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
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.GroupCreateActivity;
import com.carelynk.databinding.ActivityEventCreateBinding;
import com.carelynk.event.model.EventList;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskPostCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DatePickerDialogFragment;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.carelynk.utilz.TimePickerDialogFragment;
import com.carelynk.utilz.camera.BitmapHelper;
import com.carelynk.utilz.camera.CameraIntentHelper;
import com.carelynk.utilz.camera.CameraIntentHelperCallback;
import com.carelynk.utilz.camera.ImageFilePath;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 15-Sep-16.
 */
public class EventCreateActivity extends BaseActivity {

    private ActivityEventCreateBinding binding;
    private CameraIntentHelper mCameraIntentHelper;
    private String imagePath = "";
    private static final String TAG = "EventCreateActivity";
    private boolean mIsFromEdit;
    private EventList.Result mEventData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupSlideWindowAnimationSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_create);
        init();

        checkPermission();

        setupCameraIntentHelper();
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

    void init(){
        if(getIntent().hasExtra(Constants.EXTRA_IS_FOR_EDIT_EVENT)){
            mIsFromEdit = getIntent().getExtras().getBoolean(Constants.EXTRA_IS_FOR_EDIT_EVENT);
            binding.txtToolbar.setText(getString(R.string.edit_event));
            mEventData = (EventList.Result) getIntent().getSerializableExtra(Constants.EXTRA_EVENT);
            binding.edtEventAddress.setText(mEventData.getAddress());
            binding.edtDesc.setText(mEventData.getEventDesc());
            binding.edtEventName.setText(mEventData.getEventName());

            binding.edtTimeTo.setText(mEventData.getEventTimeTo());
            binding.edtDateTo.setText(AppUtils.formattedDate("dd/MM/yyyy", "dd MMMM yyyy", mEventData.getEventDateTo()));
            binding.edtEventDate.setText(AppUtils.formattedDate("dd/MM/yyyy", "dd MMMM yyyy", mEventData.getEventDateFrom()));
            binding.edtEventTime.setText(mEventData.getEventTimeFrom());
            binding.checkPrivate.setChecked(mEventData.getIsPrivate().equalsIgnoreCase("true") ? true : false);
        }
        binding.toolbar.setNavigationIcon(R.drawable.ic_cancel_grey);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryDialog();
            }
        });

        binding.imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryDialog();
            }
        });

        binding.edtEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(new DatePickerDialogFragment.OnDateSelection() {
                    @Override
                    public void onDateSelect(String date) {
                        binding.edtEventDate.setText(date);
                    }
                });
                datePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        binding.edtEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogFragment datePickerDialogFragment = new TimePickerDialogFragment(new TimePickerDialogFragment.OnTimeSelection() {
                    @Override
                    public void onTimeSelect(String time) {
                        binding.edtEventTime.setText(AppUtils.get12HourTime(time));
                    }
                });
                datePickerDialogFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        binding.edtDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(new DatePickerDialogFragment.OnDateSelection() {
                    @Override
                    public void onDateSelect(String date) {
                        binding.edtDateTo.setText(date);
                    }
                });
                datePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        binding.edtTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogFragment datePickerDialogFragment = new TimePickerDialogFragment(new TimePickerDialogFragment.OnTimeSelection() {
                    @Override
                    public void onTimeSelect(String time) {
                        binding.edtTimeTo.setText(AppUtils.get12HourTime(time));
                    }
                });
                datePickerDialogFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
    }


    public void attemptInsertEvent(View view) {
        DialogUtils.showProgressDialog(this);
        ApiInterface apiInterface = ApiFactory.provideInterface();
        Call<JsonObject> call;
        JsonObject payerReg = new JsonObject();

        payerReg.addProperty("Event_ID", mIsFromEdit ? Integer.parseInt(mEventData.getEventID()) : 0);
        payerReg.addProperty("EventDesc", binding.edtDesc.getText().toString());
        payerReg.addProperty("User_ID", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        payerReg.addProperty("IsPrivate", binding.checkPrivate.isChecked() ? 1 : 0);
        payerReg.addProperty("EventName", binding.edtEventName.getText().toString());
        payerReg.addProperty("EventDateFrom", getDateFrom());
        payerReg.addProperty("EventDateTo", getDateTo());
        payerReg.addProperty("EventTimeTo", getDateTo());
        payerReg.addProperty("Location", binding.edtEventAddress.getText().toString());
        payerReg.addProperty("EventTimeFrom", getDateFrom());

        Log.e(TAG, "create: " + payerReg.toString());
        if(mIsFromEdit) {
            call = apiInterface.updateEvent(payerReg);
        }else{
            call = apiInterface.createEvent(payerReg);
        }
        call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try {
                            Log.e(TAG, "onResponse: " + response.body().toString());
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONObject object = jsonObject.getJSONObject("result");
                            if (object.getBoolean("IsSuccess")) {
                                AppUtils.closeKeyBoard(EventCreateActivity.this);
                                showSnackbar(binding.getRoot(), "Event Done Successfully");
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

    private String getDateFrom() {
        String date = AppUtils.formattedDate("dd MMM yyyy", "MM-dd-yyyy", binding.edtEventDate.getText().toString());
        String time = binding.edtEventTime.getText().toString()+":00";
        return date+" "+time;
    }

    private String getDateTo() {
        String date = "", time="";
        if(!TextUtils.isEmpty(binding.edtDateTo.getText().toString())) {
            date = AppUtils.formattedDate("dd MMM yyyy", "MM-dd-yyyy", binding.edtDateTo.getText().toString());
            time = binding.edtTimeTo.getText().toString() + ":00";
        }
        return date+" "+time;
    }

    private boolean isValid() {
        if(TextUtils.isEmpty(binding.edtEventName.getText().toString())) {
            showSnackbar(binding.getRoot(), "Please enter event name");
            return false;
        }else  if(TextUtils.isEmpty(binding.edtEventDate.getText().toString())) {
            showSnackbar(binding.getRoot(), "Please select date");
            return false;
        }else if(TextUtils.isEmpty(binding.edtEventTime.getText().toString())) {
            showSnackbar(binding.getRoot(), "Please select time");
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                    imagePath = ImageFilePath.getPath(EventCreateActivity.this, photoUri);
                    Log.e(TAG, "Image Camera" + imagePath);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, EventCreateActivity.this);
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
        Glide.with(this).load(photoUri).into(binding.imgPreview);
    }


}
