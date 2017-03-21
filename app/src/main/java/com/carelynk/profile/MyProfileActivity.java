package com.carelynk.profile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.databinding.ActivityMyProfileBinding;
import com.carelynk.profile.model.ProfileModel;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 15-Sep-16.
 */
public class MyProfileActivity extends BaseActivity {

    private ActivityMyProfileBinding binding;
    private static final String TAG = "lMyProfileActivity";
    private ProfileModel profileModel = new ProfileModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupSlideWindowAnimationSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);
        init();
        getProfile();
    }

    void getProfile() {
        if (isOnline(this)) {
            Log.e(TAG, "getProfile: "+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    //{"UserProfileId":1078,"DateEdited":"0001-01-01T00:00:00","Email":"mht.suthar@gmail.com",
                    // "FirstName":"kittu","LastName":"Male","DateOfBirth":"2017-10-03T00:00:00",
                    // "Gender":true,"Address":null,"City":null,"State":null,"Country":null,"ZipCode":null,
                    // "ContactNo":646644416.0,"UserId":"08facb07-2161-4c9f-9905-d6cdafcb2c10","DisplayName":null,
                    // "AboutMe":"Individual","AboutMeText":"what","Alternate_EmailID":null,"Marital_Status":null,"Occupation":null,
                    // "Food_Habits":null,"Someone_Message_Email":null,"Friend_Request_Email":null,"News_Updates_Protal_Email":null,
                    // "Recd_notif_Email":null,"IsPrivate_Profile":null,"Profile_Persent":null,
                    // "InterestArea":"Kids Health,Teenage Health,Men and Women Health","ProfilePicUrl":null,"RoleId":null}
                    Log.e(TAG, "onTaskComplete: "+result);
                    binding.progressBar.setVisibility(View.GONE);
                    try{
                        JSONObject jsonObject = new JSONObject(result);
                        binding.txtUserName.setText(jsonObject.getString("FirstName")+""+jsonObject.getString("LastName"));

                        binding.txtFirstName.setText(jsonObject.getString("FirstName"));
                        profileModel.FirstName = jsonObject.getString("FirstName");
                        binding.txtLastName.setText(jsonObject.getString("LastName"));
                        binding.txtAboutMe.setText(jsonObject.getString("AboutMe"));
                        profileModel.AboutMe = jsonObject.getString("AboutMe");
                        binding.txtDateOfBirth.setText(jsonObject.getString("DateOfBirth"));
                        profileModel.DateOfBirth = jsonObject.getString("DateOfBirth");
                        binding.txtEmail.setText(jsonObject.getString("Email"));
                        profileModel.Email = jsonObject.getString("Email");
                        binding.txtGender.setText(jsonObject.getBoolean("Gender") ? "Male" : "Female");
                        profileModel.Gender = jsonObject.getBoolean("Gender");
                        binding.txtMarital.setText(jsonObject.getString("Marital_Status"));
                        profileModel.Marital_Status = jsonObject.getString("Marital_Status");
                        binding.txtWhoAmI.setText(jsonObject.getString("AboutMeText"));
                        profileModel.AboutMeText = jsonObject.getString("AboutMeText");
                        // TODO: 21-Mar-17 who am i and gender not coming

                        binding.txtAddress.setText(jsonObject.getString("Address"));
                        profileModel.Address = jsonObject.getString("Address");
                        binding.txtCity.setText(jsonObject.getString("City"));
                        profileModel.City = jsonObject.getString("City");
                        binding.txtCountry.setText(jsonObject.getString("Country"));
                        profileModel.Country = jsonObject.getString("Country");
                        binding.txtState.setText(jsonObject.getString("State"));
                        profileModel.State = jsonObject.getString("State");
                        binding.txtZip.setText(""+jsonObject.getString("ZipCode"));
                        profileModel.ZipCode = jsonObject.getString("ZipCode");
                        binding.txtMobile.setText(jsonObject.getString("ContactNo"));
                        profileModel.ContactNo = jsonObject.getString("ContactNo");
                        profileModel.UserProfileId = jsonObject.getInt("UserProfileId");

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_PROFILE);
        } else {
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
        }
    }

    void init(){
        /**
         * For image draw over status bar
         */
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            binding.imgBack.setPadding(getStatusBarHeight()-10, getStatusBarHeight()+20, getStatusBarHeight(), getStatusBarHeight());
            binding.progressBar.setPadding(getStatusBarHeight()-10, getStatusBarHeight()+20, getStatusBarHeight(), getStatusBarHeight());
        }

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
                intent.putExtra(Constants.EXTRA_PROFILE, profileModel);
                moveActivityResult(intent, MyProfileActivity.this, Constants.REQUEST_PROFILE_UPDATE);
            }
        });
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.REQUEST_PROFILE_UPDATE){
                getProfile();
            }
        }
    }
}
