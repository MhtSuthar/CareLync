package com.carelynk.prelogin.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.dashboard.MenuActivity;
import com.carelynk.databinding.FragmentLoginBinding;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.PrefUtils;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 12-Sep-16.
 */
public class LoginFragment extends BaseFragment {

    FragmentLoginBinding binding;
    private static final String TAG = "LoginFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()) {
                    SharedPreferenceUtil.putValue(PrefUtils.PREF_IS_LOGIN, true);
                    SharedPreferenceUtil.save();
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    moveActivity(intent, getActivity(), true);
                }else {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    moveActivity(intent, getActivity(), true);
                }

            }
        });

        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addFragment(new RegistrationFragment());
            }
        });
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

    private void attempLogin() {
        if(isOnline(getContext())){
            ApiInterface apiInterface = ApiFactory.provideInterface();
            HashMap<String, String> mMap = new HashMap<>();
            mMap.put(Constants.Email, binding.edtEmail.getText().toString());
            mMap.put(Constants.PasswordHash, binding.edtPass.getText().toString());
            Call<JsonObject> call = apiInterface.login(mMap);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try{
                            JSONArray jsonArray = new JSONArray(response.body().toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //[{"mMainGroupID":1,"MainGroupName":"Heath"},
                                JSONObject object = jsonArray.getJSONObject(i);

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
