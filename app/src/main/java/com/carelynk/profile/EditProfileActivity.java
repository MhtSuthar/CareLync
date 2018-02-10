package com.carelynk.profile;

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
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.databinding.ActivityEditProfileBinding;
import com.carelynk.profile.model.ProfileModel;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DatePickerDialogFragment;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 15-Sep-16.
 */
public class EditProfileActivity extends BaseActivity {

    private ActivityEditProfileBinding binding;
    private static final String TAG = "EditProfileActivity";
    private CameraIntentHelper mCameraIntentHelper;
    private String imagePath = "";
    private ProfileModel profileModel;
    private boolean isEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorBluePrimaryDark));
        setupSlideWindowAnimationSlide(Gravity.RIGHT);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);

        init();

        setData();

        checkPermission();

        setupCameraIntentHelper();
    }

    private void setData() {
        if(profileModel != null){
            binding.edtName.setText(profileModel.FirstName);
            binding.edtLastName.setText(profileModel.LastName);
            binding.edtAboutMe.setText(profileModel.AboutMeText);
            binding.edtEmail.setText(profileModel.Email);
            binding.edtAboutMe.setText(profileModel.AboutMeText);
            if(!TextUtils.isEmpty(profileModel.Address))
                binding.edtAddress.setText(profileModel.Address);
            if(!TextUtils.isEmpty(profileModel.City))
                binding.edtCity.setText(profileModel.City);
            binding.edtContactNo.setText(profileModel.ContactNo);
            binding.edtCountry.setText(profileModel.Country);
            binding.edtDateOfBirth.setText(AppUtils.formattedDate("MM/dd/yyyy", "dd MMMM yyyy", profileModel.DateOfBirth));
            binding.edtMarital.setText(profileModel.Marital_Status);
            binding.edtState.setText(profileModel.State);
            binding.edtZipcode.setText(profileModel.ZipCode);
            if(profileModel.AboutMe.equalsIgnoreCase("Individual")){
                binding.spnrAboutMe.setSelection(1);
                isEntity=false;
            }else{
                isEntity=true;
                binding.edtLastName.setVisibility(View.GONE);
                binding.spnrGender.setVisibility(View.GONE);
                binding.edtDateOfBirth.setVisibility(View.GONE);
                binding.edtMarital.setVisibility(View.GONE);
                binding.edtOccupation.setVisibility(View.GONE);
                binding.edtFoodHabit.setVisibility(View.GONE);
                binding.edtEducation.setVisibility(View.GONE);
                binding.spnrAboutMe.setSelection(2);
            }
            if(!profileModel.Gender)
                binding.spnrGender.setSelection(1);
            else
                binding.spnrGender.setSelection(2);
            binding.switchAutoFollow.setChecked(profileModel.AutoFollow);
            binding.switchEmailFriendRequest.setChecked(profileModel.EmailFriendRequest);
            binding.switchNotificationEmail.setChecked(profileModel.NotificationEmail);
            binding.switchProfilePublic.setChecked(profileModel.ProfilePublic);
            binding.switchSendEmail.setChecked(profileModel.SendEmail);
            binding.edtEducation.setText(profileModel.Education);
            binding.edtCertification.setText(profileModel.Certification);
            binding.edtExpertise.setText(profileModel.Expertise);
            if(!TextUtils.isEmpty(profileModel.ProfilePicUrl)){
                binding.imgPhotoSelection.setVisibility(View.GONE);
                binding.imgPhoto.setVisibility(View.VISIBLE);
                Glide.with(this).load(AppUtils.getImagePath(profileModel.ProfilePicUrl)).
                        apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy))
                        .into(binding.imgPhoto);
            }
        }
    }

    private boolean isValid() {
        if(!isOnline(this)){
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
            return false;
        } else if(TextUtils.isEmpty(binding.edtName.getText().toString())){
            showSnackbar(binding.getRoot(), getString(R.string.enter_name));
            return false;
        }else if(TextUtils.isEmpty(binding.edtDateOfBirth.getText().toString()) && !isEntity){
            showSnackbar(binding.getRoot(), getString(R.string.enter_date_of_birth));
            return false;
        }else if(binding.spnrAboutMe.getSelectedItem().toString().equalsIgnoreCase("Who am i")){
            showSnackbar(binding.getRoot(), getString(R.string.select_who_am_i));
            return false;
        }else if(binding.spnrGender.getSelectedItem().toString().equalsIgnoreCase("Gender") && !isEntity){
            showSnackbar(binding.getRoot(), getString(R.string.select_gender));
            return false;
        }else if(TextUtils.isEmpty(binding.edtEmail.getText().toString())){
            showSnackbar(binding.getRoot(), getString(R.string.enter_email));
            return false;
        }else if(!AppUtils.isValidEmail(binding.edtEmail.getText().toString())){
            showSnackbar(binding.getRoot(), getString(R.string.enter_correcr_email));
            return false;
        }else if(TextUtils.isEmpty(binding.edtContactNo.getText().toString())){
            showSnackbar(binding.getRoot(), getString(R.string.enter_contact_no));
            return false;
        }else if(binding.edtContactNo.getText().toString().length() < 9){
            showSnackbar(binding.getRoot(), getString(R.string.enter_correct_contact_no));
            return false;
        }
        return true;
    }

    void init(){
        /**
         * For image draw over status bar
         */
        profileModel = (ProfileModel) getIntent().getSerializableExtra(Constants.EXTRA_PROFILE);
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    attemptUpdateProfile();
                }
            }
        });

        binding.edtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(new DatePickerDialogFragment.OnDateSelection() {
                    @Override
                    public void onDateSelect(String date) {
                        binding.edtDateOfBirth.setText(date);
                    }
                });
                datePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        binding.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryDialog();
            }
        });

        binding.imgPhotoSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryDialog();
            }
        });

        binding.spnrAboutMe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(binding.spnrAboutMe.getSelectedItem().toString().equalsIgnoreCase("Individual")){
                    isEntity=false;
                    binding.edtLastName.setVisibility(View.VISIBLE);
                    binding.textInput.setHint("First Name");
                    binding.edtDateOfBirth.setVisibility(View.VISIBLE);
                    binding.spnrGender.setVisibility(View.VISIBLE);
                    binding.edtMarital.setVisibility(View.VISIBLE);
                    binding.edtOccupation.setVisibility(View.VISIBLE);
                    binding.edtFoodHabit.setVisibility(View.VISIBLE);
                    binding.edtEducation.setVisibility(View.VISIBLE);
                }else if(binding.spnrAboutMe.getSelectedItem().toString().equalsIgnoreCase("Entity")){
                    isEntity=true;
                    binding.textInput.setHint("Entity Name");
                    binding.edtLastName.setVisibility(View.GONE);
                    binding.spnrGender.setVisibility(View.GONE);
                    binding.edtDateOfBirth.setVisibility(View.GONE);
                    binding.edtMarital.setVisibility(View.GONE);
                    binding.edtOccupation.setVisibility(View.GONE);
                    binding.edtFoodHabit.setVisibility(View.GONE);
                    binding.edtEducation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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


    private void attemptUpdateProfile() {
        DialogUtils.showProgressDialog(this);
        ApiInterface apiInterface = ApiFactory.provideInterface();

        MultipartBody.Part bodyImage = null;
        if (!TextUtils.isEmpty(imagePath)) {
            File imageProfileFile = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageProfileFile);
            bodyImage = MultipartBody.Part.createFormData("fileUpload", imageProfileFile.getName(), requestFile);
        }

        RequestBody UserProfileId = RequestBody.create(MediaType.parse("text/plain"), ""+profileModel.UserProfileId);
        RequestBody Email = RequestBody.create(MediaType.parse("text/plain"), binding.edtEmail.getText().toString());
        RequestBody FirstName = RequestBody.create(MediaType.parse("text/plain"), binding.edtName.getText().toString());
        RequestBody LastName = RequestBody.create(MediaType.parse("text/plain"), binding.edtLastName.getText().toString());
        RequestBody DateOfBirth = RequestBody.create(MediaType.parse("text/plain"), isEntity ? "" : AppUtils.formattedDate("dd MMMM yyyy", "MM/dd/yyyy", binding.edtDateOfBirth.getText().toString()));
        RequestBody Gender = RequestBody.create(MediaType.parse("text/plain"), isEntity ? "" : binding.spnrGender.getSelectedItem().toString().equalsIgnoreCase("Male") ? "False" : "True");
        RequestBody AboutMe = RequestBody.create(MediaType.parse("text/plain"), binding.spnrAboutMe.getSelectedItem().toString());
        RequestBody Address = RequestBody.create(MediaType.parse("text/plain"), binding.edtAddress.getText().toString());
        RequestBody City = RequestBody.create(MediaType.parse("text/plain"), binding.edtCity.getText().toString());
        RequestBody State = RequestBody.create(MediaType.parse("text/plain"), binding.edtState.getText().toString());
        RequestBody Country = RequestBody.create(MediaType.parse("text/plain"), binding.edtCountry.getText().toString());
        RequestBody ZipCode = RequestBody.create(MediaType.parse("text/plain"), binding.edtZipcode.getText().toString());
        RequestBody ContactNo = RequestBody.create(MediaType.parse("text/plain"), binding.edtContactNo.getText().toString());
        RequestBody DisplayName = RequestBody.create(MediaType.parse("text/plain"), binding.edtName.getText().toString());
        RequestBody Alternate_EmailID = RequestBody.create(MediaType.parse("text/plain"), binding.edtAlternateId.getText().toString());
        RequestBody Marital_Status = RequestBody.create(MediaType.parse("text/plain"), binding.edtMarital.getText().toString());
        RequestBody Occupation = RequestBody.create(MediaType.parse("text/plain"), binding.edtOccupation.getText().toString());
        RequestBody Food_Habits = RequestBody.create(MediaType.parse("text/plain"), binding.edtFoodHabit.getText().toString());
        RequestBody Someone_Message_Email = RequestBody.create(MediaType.parse("text/plain"), ""+!binding.switchAutoFollow.isChecked());
        RequestBody Friend_Request_Email = RequestBody.create(MediaType.parse("text/plain"), ""+!binding.switchEmailFriendRequest.isChecked());
        RequestBody News_Updates_Portal_Email = RequestBody.create(MediaType.parse("text/plain"), ""+!binding.switchSendEmail.isChecked());
        RequestBody Recd_notif_Email = RequestBody.create(MediaType.parse("text/plain"), ""+!binding.switchNotificationEmail.isChecked());
        RequestBody IsPrivate_Profile = RequestBody.create(MediaType.parse("text/plain"), ""+!binding.switchProfilePublic.isChecked());
        RequestBody ProfilePicUrl = RequestBody.create(MediaType.parse("text/plain"), profileModel.ProfilePicUrl);
        RequestBody Expertise = RequestBody.create(MediaType.parse("text/plain"), binding.edtExpertise.getText().toString());
        RequestBody Certification = RequestBody.create(MediaType.parse("text/plain"), binding.edtCertification.getText().toString());
        RequestBody Education = RequestBody.create(MediaType.parse("text/plain"), binding.edtEducation.getText().toString());
        RequestBody AboutMeText = RequestBody.create(MediaType.parse("text/plain"), binding.edtAboutMe.getText().toString());

        Call<JsonObject> call = apiInterface.profileUpdate(UserProfileId, Email, FirstName, LastName, DateOfBirth,
                Gender,Address, City, State, Country, ZipCode, ContactNo, DisplayName, AboutMe, Alternate_EmailID, Marital_Status,
                Occupation, Food_Habits, Someone_Message_Email,Friend_Request_Email, News_Updates_Portal_Email,Recd_notif_Email,
                IsPrivate_Profile,ProfilePicUrl, Expertise, Certification, Education, AboutMeText, bodyImage);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                DialogUtils.stopProgressDialog();
                if (response.isSuccessful()) {
                    try{
                        Log.e(TAG, "onTaskComplete: "+response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getBoolean("IsSuccess")) {
                            showSnackbar(binding.getRoot(), "Profile Update Successful");
                            setResult(RESULT_OK);
                            finish();
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
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                    imagePath = ImageFilePath.getPath(EditProfileActivity.this, photoUri);
                    Log.e(TAG, "Image Camera" + imagePath);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, EditProfileActivity.this);
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
        binding.imgPhotoSelection.setVisibility(View.GONE);
        binding.imgPhoto.setVisibility(View.VISIBLE);
        //Glide.with(this).load(photoUri).into(binding.imgPhoto);
        Glide.with(this).load(photoUri).
                apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy))
                .into(binding.imgPhoto);
    }
}
