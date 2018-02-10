package com.carelynk.prelogin.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.databinding.FragmentLoginBinding;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12-Sep-16.
 */
public class LoginFragment extends BaseFragment {

    FragmentLoginBinding binding;
    private static final String TAG = "LoginFragment";
    private String deviceUniqueIdentifier = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermission();
        binding.chkRemember.setChecked(SharedPreferenceUtil.getBoolean(PrefUtils.PREF_REMEMBER_ME, false));
        if(SharedPreferenceUtil.getBoolean(PrefUtils.PREF_REMEMBER_ME, false)){
            binding.edtEmail.setText(SharedPreferenceUtil.getString(PrefUtils.PREF_EMAIL, ""));
            binding.edtPass.setText(SharedPreferenceUtil.getString(PrefUtils.PREF_PASSWORD, ""));
        }

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()) {
                    attemptLogin();
                }else {
                    /*Intent intent = new Intent(getActivity(), HomeActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    moveActivity(intent, getActivity(), true);*/
                }
            }
        });

        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addFragment(new RegistrationFragment());
            }
        });

        binding.txtForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addFragment(new ForgetPassFragment());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://carelynk.com/Account/ForgotPassword"));
                startActivity(i);
            }
        });
    }

    void sendIp(){
        if(isOnline(getActivity())){
            AsyncTaskGetCommon asyncTaskCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    if (result.length() > 0) {
                        try{
                            JSONObject jsonObject = new JSONObject(result);
                            if(jsonObject.getBoolean("IsSuccess")){
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                moveActivity(intent, getActivity(), true);
                            }else{
                                showSnackbar(binding.getRoot(), jsonObject.getString("Message"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } else
                        showSnackbar(binding.getRoot(), getString(R.string.error_server));
                }
            });
            asyncTaskCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.SEND_IP+""+SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "")+
                    "&IpAdress="+getLocalIpAddress()+"_"+deviceUniqueIdentifier);
        }else
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
    }

    public void checkPermission() {
        if (checkPermission(Manifest.permission.READ_PHONE_STATE, getActivity())) {
            getDeviceIMEI(getContext());
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, Constants.REQUEST_PERMISSION_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_PERMISSION_WRITE_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceIMEI(getContext());
            }
        }
    }

    private void getDeviceIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            deviceUniqueIdentifier = tm.getDeviceId();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.e(TAG, "***** IP="+ ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return "192.168.1.1";
    }

    private boolean isValid() {
        if(!isOnline(getContext())) {
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
            return false;
        } else if(TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
            showSnackbar(binding.getRoot(), "Please enter email");
            return false;
        }else  if(TextUtils.isEmpty(binding.edtPass.getText().toString())) {
            showSnackbar(binding.getRoot(), "Please enter pass");
            return false;
        }
        return true;
    }

    private void attemptLogin() {
        if(isOnline(getContext())){
            DialogUtils.showProgressDialog(getContext());
            ApiInterface apiInterface = ApiFactory.provideInterface();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty(Constants.Email, binding.edtEmail.getText().toString());
            payerReg.addProperty(Constants.PasswordHash, binding.edtPass.getText().toString());
            Log.e(TAG, "attemptLogin: "+payerReg.toString());
            Call<JsonObject> call = apiInterface.login(payerReg);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    DialogUtils.stopProgressDialog();
                    if (response.isSuccessful()) {
                        try{
                            // {"PasswordHash":null,"ProfilePicUrl":null,"IsSuccess":false,"ErrorMessage":"Login Id is invalid!"
                            // ,"Message":"Login Id is invalid!","UserProfileId":0,"DateEdited":"0001-01-01T00:00:00",
                            // "Email":null,"FirstName":null,"LastName":null,
                            // "DateOfBirth":null,"Gender":null,"Address":null,"City":null,"State":null,"Country":null,"ZipCode":null,"ContactNo":null,"UserId":null,"DisplayName":null,"AboutMe":null,"AboutMeText":null,"Alternate_EmailID":null,"Marital_Status":null,"Occupation":null,"Food_Habits":null,"Someone_Message_Email":null,"Friend_Request_Email":null,"News_Updates_Protal_Email":null,"Recd_notif_Email":null,"IsPrivate_Profile":null,"Profile_Persent":null,"InterestArea":null}
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            if (jsonObject.getBoolean("IsSuccess")) {
                                SharedPreferenceUtil.putValue(PrefUtils.PREF_IS_LOGIN, true);
                                SharedPreferenceUtil.putValue(PrefUtils.PREF_PASSWORD, binding.edtPass.getText().toString());
                                SharedPreferenceUtil.putValue(PrefUtils.PREF_PROFILE_PIC,  jsonObject.getString("ProfilePicUrl"));
                                SharedPreferenceUtil.putValue(PrefUtils.PREF_USER_PROFILE_ID, jsonObject.getInt("UserProfileId"));
                                SharedPreferenceUtil.putValue(PrefUtils.PREF_EMAIL, jsonObject.getString("Email"));
                                SharedPreferenceUtil.putValue(PrefUtils.PREF_FIRST_NAME, jsonObject.getString("FirstName"));
                                SharedPreferenceUtil.putValue(PrefUtils.PREF_LAST_NAME,  jsonObject.getString("LastName"));
                                SharedPreferenceUtil.putValue(PrefUtils.PREF_DATE_OF_BIRTH,  jsonObject.getString("DateOfBirth"));
                                SharedPreferenceUtil.putValue(PrefUtils.PREF_GENDER,  jsonObject.getString("Gender"));
                                SharedPreferenceUtil.putValue(PrefUtils.PREF_CONTACT,  jsonObject.getString("ContactNo"));
                                SharedPreferenceUtil.putValue(PrefUtils.PREF_INTEREST_AREA,  jsonObject.getString("InterestArea"));
                                SharedPreferenceUtil.putValue(PrefUtils.PREF_USER_ID, jsonObject.getString("UserId"));
                                if(binding.chkRemember.isChecked()) {
                                    SharedPreferenceUtil.putValue(PrefUtils.PREF_REMEMBER_ME, true);
                                }
                                SharedPreferenceUtil.save();
                                sendIp();

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
            showSnackbar(binding.getRoot(), "Error");
        }
    }


}
