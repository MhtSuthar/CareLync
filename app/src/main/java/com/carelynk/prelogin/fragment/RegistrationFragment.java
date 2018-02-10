package com.carelynk.prelogin.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.databinding.FragmentRegistrationBinding;
import com.carelynk.prelogin.model.RegisterStepOne;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.CircleTransform;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12-Sep-16.
 */
public class RegistrationFragment extends BaseFragment {

    FragmentRegistrationBinding binding;
    private CameraIntentHelper mCameraIntentHelper;
    private String imagePath = "";
    private static final String TAG = "RegistrationFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkPermission();

        setupCameraIntentHelper();

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        /*binding.edtInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInterestDialog();
            }
        });*/


        binding.spnrAboutMe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                if(binding.spnrAboutMe.getSelectedItem().toString().equalsIgnoreCase("Individual")){
                    binding.edtLastName.setVisibility(View.VISIBLE);
                    binding.textInput.setHint("First Name");
                    binding.edtDateOfBirth.setVisibility(View.VISIBLE);
                    binding.spnrGender.setVisibility(View.VISIBLE);
                }else if(binding.spnrAboutMe.getSelectedItem().toString().equalsIgnoreCase("Entity")){
                    binding.edtLastName.setVisibility(View.GONE);
                    binding.textInput.setHint("Entity Name");
                    binding.edtDateOfBirth.setVisibility(View.GONE);
                    binding.spnrGender.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spnrGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid())
                    attemptRegistration();
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
                datePickerDialogFragment.show(getChildFragmentManager(), "datePicker");
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
    }

    private boolean isValid() {
        if (!isOnline(getContext())) {
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
            return false;
        }else if (binding.spnrAboutMe.getSelectedItem().toString().equalsIgnoreCase("Register As")) {
            showSnackbar(binding.getRoot(), "Select Register as first");
            return false;
        }  else if (TextUtils.isEmpty(binding.edtName.getText().toString())) {
            showSnackbar(binding.getRoot(), getString(R.string.enter_name));
            return false;
        } /*else if (TextUtils.isEmpty(binding.edtDateOfBirth.getText().toString()) &&
                !binding.spnrAboutMe.getSelectedItem().toString().equalsIgnoreCase("Entity")) {
            showSnackbar(binding.getRoot(), getString(R.string.enter_date_of_birth));
            return false;
        }*/ else if (binding.spnrGender.getSelectedItem().toString().equalsIgnoreCase("Gender") &&
                !binding.spnrAboutMe.getSelectedItem().toString().equalsIgnoreCase("Entity")) {
            showSnackbar(binding.getRoot(), getString(R.string.select_gender));
            return false;
        } else if (TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
            showSnackbar(binding.getRoot(), getString(R.string.enter_email));
            return false;
        } else if (!AppUtils.isValidEmail(binding.edtEmail.getText().toString())) {
            showSnackbar(binding.getRoot(), getString(R.string.enter_correcr_email));
            return false;
        } else if (TextUtils.isEmpty(binding.edtPassword.getText().toString())) {
            showSnackbar(binding.getRoot(), getString(R.string.enter_password));
            return false;
        } else if (TextUtils.isEmpty(binding.edtConfPassword.getText().toString())) {
            showSnackbar(binding.getRoot(), getString(R.string.enter_confirm_pass));
            return false;
        } else if (!binding.edtPassword.getText().toString().equals(binding.edtConfPassword.getText().toString())) {
            showSnackbar(binding.getRoot(), getString(R.string.pass_not_match));
            return false;
        } else if (TextUtils.isEmpty(binding.edtContactNo.getText().toString())) {
            showSnackbar(binding.getRoot(), getString(R.string.enter_contact_no));
            return false;
        } else if (binding.edtContactNo.getText().toString().length() < 9) {
            showSnackbar(binding.getRoot(), getString(R.string.enter_correct_contact_no));
            return false;
        }
        return true;
    }

    void openGalleryDialog() {
        final Dialog dialog = new DialogUtils(getContext()).setupCustomeDialogFromBottom(R.layout.dialog_gallery);
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

    void openInterestDialog() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.about_me));
        final CharSequence[] dialogList = list.toArray(new CharSequence[list.size()]);
        final android.app.AlertDialog.Builder builderDialog = new android.app.AlertDialog.Builder(getActivity());
        builderDialog.setTitle("Select Your Interest");
        int count = dialogList.length;
        final boolean[] is_checked = new boolean[count];

        // Creating multiple selection by using setMutliChoiceItem method
        builderDialog.setMultiChoiceItems(dialogList, is_checked,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton, boolean isChecked) {
                        Toast.makeText(getContext(), "" + whichButton + ", " + is_checked, Toast.LENGTH_SHORT).show();
                    }
                });

        builderDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ListView list = ((android.app.AlertDialog) dialog).getListView();
                        //ListView has boolean array like {1=true, 3=true}, that shows checked items
                    }
                });

        builderDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        android.app.AlertDialog alert = builderDialog.create();
        alert.show();
    }

    public void checkPermission() {
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getActivity()) &&
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getActivity()) && checkPermission(Manifest.permission.CAMERA, getActivity())) {
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
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getActivity()) &&
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getActivity()) && checkPermission(Manifest.permission.CAMERA, getActivity())) {

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

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Carelync_" + timeStamp + "_";
        File sdCard = new File(Environment.getExternalStorageDirectory() + "/Carelync/Images");
        if (!sdCard.exists())
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
     * @param savedInstanceState Start Camera Intent handler
     */

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mCameraIntentHelper.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCameraIntentHelper.onRestoreInstanceState(savedInstanceState);
    }

    private void setupCameraIntentHelper() {
        mCameraIntentHelper = new CameraIntentHelper(getActivity(), new CameraIntentHelperCallback() {
            @Override
            public void onPhotoUriFound(Date dateCameraIntentStarted, Uri photoUri, int rotateXDegrees) {
                if (photoUri != null) {
                    displayImage(photoUri);
                    imagePath = ImageFilePath.getPath(getActivity(), photoUri);
                    Log.e(TAG, "Image Camera" + imagePath);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, getActivity());
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
        if (resultCode == getActivity().RESULT_OK) {
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
            Cursor cursor = getActivity().getContentResolver().query(imageUri, projection, null, null,
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
        binding.imgPhotoSelection.setVisibility(View.GONE);
        binding.imgPhoto.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(photoUri).apply(RequestOptions.circleCropTransform()).into(binding.imgPhoto);
    }

    private void attemptRegistration() {
        DialogUtils.showProgressDialog(getActivity());
        ApiInterface apiInterface = ApiFactory.provideInterface();

        MultipartBody.Part bodyImage = null;
        if (!TextUtils.isEmpty(imagePath)) {
            File imageProfileFile = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageProfileFile);
            bodyImage = MultipartBody.Part.createFormData("fileUpload", imageProfileFile.getName(), requestFile);
        }

        RequestBody Email = RequestBody.create(MediaType.parse("text/plain"), binding.edtEmail.getText().toString());
        RequestBody PasswordHash = RequestBody.create(MediaType.parse("text/plain"), binding.edtPassword.getText().toString());
        RequestBody UserProfileId = RequestBody.create(MediaType.parse("text/plain"), "0");
        RequestBody AboutMe = RequestBody.create(MediaType.parse("text/plain"), binding.spnrAboutMe.getSelectedItem().toString());
        RequestBody FirstName = RequestBody.create(MediaType.parse("text/plain"),  binding.edtName.getText().toString());
        RequestBody LastName = RequestBody.create(MediaType.parse("text/plain"), binding.edtLastName.getText().toString());
        RequestBody DateOfBirth = RequestBody.create(MediaType.parse("text/plain"), binding.edtDateOfBirth.getText().toString().equals("") ? "01/01/2001" : AppUtils.formattedDate("dd MMMM yyyy", "MM/dd/yyyy", binding.edtDateOfBirth.getText().toString()));
        RequestBody Gender = RequestBody.create(MediaType.parse("text/plain"), ""+getGender());
        RequestBody ContactNo = RequestBody.create(MediaType.parse("text/plain"), binding.edtContactNo.getText().toString());


        Call<JsonObject> call = apiInterface.registrationOne(Email, PasswordHash, UserProfileId, AboutMe, FirstName, LastName, DateOfBirth, Gender, ContactNo, bodyImage);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                DialogUtils.stopProgressDialog();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getBoolean("IsSuccess")) {
                            openInterestFragment(jsonObject.getString("KeyID"));
                        }else{
                            showSnackbar(binding.getRoot(), jsonObject.getString("Message"));
                        }
                        //{"KeyID":8,"IsSuccess":true,"ErrorMessage":"","Message":""}
                        Log.e(TAG, "onResponse: " + response.body().toString());
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

    private String getGender() {
        if (binding.spnrGender.getSelectedItem().toString().equalsIgnoreCase("Male"))
            return "False";
        else if (binding.spnrGender.getSelectedItem().toString().equalsIgnoreCase("Female"))
            return "True";
        return "";
    }


    private void openInterestFragment(String keyID) {
        InterestedFragment interestedFragment = new InterestedFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_USER_ID, keyID);
        interestedFragment.setArguments(bundle);
        addFragment(interestedFragment);
    }


}
