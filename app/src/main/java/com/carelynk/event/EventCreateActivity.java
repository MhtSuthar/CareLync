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
import com.carelynk.databinding.ActivityEventCreateBinding;
import com.carelynk.rest.AsyncTaskPostCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DatePickerDialogFragment;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.carelynk.utilz.TimePickerDialogFragment;
import com.carelynk.utilz.camera.BitmapHelper;
import com.carelynk.utilz.camera.CameraIntentHelper;
import com.carelynk.utilz.camera.CameraIntentHelperCallback;
import com.carelynk.utilz.camera.ImageFilePath;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 15-Sep-16.
 */
public class EventCreateActivity extends BaseActivity {

    private ActivityEventCreateBinding binding;
    private CameraIntentHelper mCameraIntentHelper;
    private String imagePath = "";
    private static final String TAG = "EventCreateActivity";

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
        /**
         * For image draw over status bar
         */
      /*  if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            binding.imgBack.setPadding(getStatusBarHeight()-10, getStatusBarHeight()+10, getStatusBarHeight(), getStatusBarHeight());
        }*/

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



        binding.edtEventFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(new DatePickerDialogFragment.OnDateSelection() {
                    @Override
                    public void onDateSelect(String date) {
                        binding.edtEventFromDate.setText(date);
                    }
                });
                datePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        binding.edtEventToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(new DatePickerDialogFragment.OnDateSelection() {
                    @Override
                    public void onDateSelect(String date) {
                        binding.edtEventToDate.setText(date);
                    }
                });
                datePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        binding.edtEventFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EventCreateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        binding.edtEventFromTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();*/
                TimePickerDialogFragment datePickerDialogFragment = new TimePickerDialogFragment(new TimePickerDialogFragment.OnTimeSelection() {
                    @Override
                    public void onTimeSelect(String time) {
                        binding.edtEventFromTime.setText(time);
                    }
                });
                datePickerDialogFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });

        binding.edtEventToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogFragment datePickerDialogFragment = new TimePickerDialogFragment(new TimePickerDialogFragment.OnTimeSelection() {
                    @Override
                    public void onTimeSelect(String time) {
                        binding.edtEventToTime.setText(time);
                    }
                });
                datePickerDialogFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
    }

    public void attemptInsertEvent(View view){
        if(isOnline(this)){
            if(isValid()) {
                AsyncTaskPostCommon asyncTaskCommon = new AsyncTaskPostCommon(getApplicationContext(), new AsyncTaskPostCommon.AsyncTaskCompleteListener() {
                    @Override
                    public void onTaskComplete(String result) {
                        if (result.length() > 0) {
                            showSnackbar(binding.getRoot(), "Success");
                        } else
                            showSnackbar(binding.getRoot(), getString(R.string.error_server));
                    }
                });
                asyncTaskCommon.execute(Urls.INSERT_EVENT, getAllValues());
            }
        }else
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
    }

    private String getAllValues() {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("EventName", binding.edtEventName.getText().toString());
            jsonObject.put("EventDesc", binding.edtDesc.getText().toString());
            jsonObject.put("EventDateFrom ", binding.edtEventFromDate.getText().toString());
            jsonObject.put("EventDateTo", binding.edtEventToDate.getText().toString());
            jsonObject.put("EventTimeFrom ", binding.edtEventFromTime.getText().toString());
            jsonObject.put("EventTimeTo", binding.edtEventToTime.getText().toString());
            jsonObject.put("IsPrivate", false);
            jsonObject.put("User_ID", SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            jsonObject.put("PhotoURL", "");
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private boolean isValid() {
        if(TextUtils.isEmpty(binding.edtEventName.getText().toString())) {
            showSnackbar(binding.getRoot(), "Please enter event name");
            return false;
        }else  if(TextUtils.isEmpty(binding.edtEventFromDate.getText().toString())) {
            showSnackbar(binding.getRoot(), "Please select from date");
            return false;
        }else if(TextUtils.isEmpty(binding.edtEventToDate.getText().toString())) {
            showSnackbar(binding.getRoot(), "Please select to date");
            return false;
        }else if(TextUtils.isEmpty(binding.edtEventFromTime.getText().toString())) {
            showSnackbar(binding.getRoot(), "Please select from time");
            return false;
        }else if(TextUtils.isEmpty(binding.edtEventToTime.getText().toString())) {
            showSnackbar(binding.getRoot(), "Please select to time");
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
