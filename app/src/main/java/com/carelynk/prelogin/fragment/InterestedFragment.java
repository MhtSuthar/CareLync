package com.carelynk.prelogin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.databinding.FragmentInterestedBinding;
import com.carelynk.prelogin.PreLoginActivity;
import com.carelynk.prelogin.adapter.InterestRecyclerAdapter;
import com.carelynk.prelogin.model.RegisterStepTwo;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 27-Feb-17.
 */

public class InterestedFragment extends BaseFragment {

    FragmentInterestedBinding binding;
    private String IdInterest = "id";
    private String InterestArea = "interest_area";
    private InterestRecyclerAdapter interestRecyclerAdapter;
    private static final String TAG = "InterestedFragment";
    private List<HashMap<String, String>> mListInterest = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_interested, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(isOnline(getContext())){
            getIntrest();
        }
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    attemptRegistration();
                }
            }
        });
    }

    private void getIntrest() {
        showProgressDialog();
        ApiInterface apiInterface = ApiFactory.provideInterface();
        Call<JsonObject> call = apiInterface.getAreaOfIntrest();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject>call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try{
                        stopProgressDialog();
                        Log.e(TAG, "onResponse: "+response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONObject object = jsonObject.getJSONObject("result");
                        JSONArray jsonArray = object.getJSONArray("Table");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objects = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(IdInterest, ""+objects.getInt("id"));
                            map.put(InterestArea, ""+objects.getString("InterestOfArea"));
                            mListInterest.add(map);
                        }
                        setAdapter();
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

    void setAdapter() {
        //List<String> list = Arrays.asList(getResources().getStringArray(R.array.interest_area));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        interestRecyclerAdapter = new InterestRecyclerAdapter(getContext(), mListInterest);
        binding.recyclerView.setAdapter(interestRecyclerAdapter);
    }

    boolean isValid() {
        if (!isOnline(getContext())) {
            showSnackbar(binding.getRoot(), getString(R.string.no_internet));
            return false;
        }/* else if (TextUtils.isEmpty(binding.edtAbout.getText().toString())) {
            showSnackbar(binding.getRoot(), getString(R.string.enter_about_me));
            return false;
        }*/ else if (interestRecyclerAdapter != null && !interestRecyclerAdapter.isSelectedInterest()) {
            showSnackbar(binding.getRoot(), getString(R.string.select_interest));
            return false;
        }
        return true;
    }

    private void attemptRegistration() {
        DialogUtils.showProgressDialog(getActivity());
        ApiInterface apiInterface = ApiFactory.provideInterface();
        RegisterStepTwo  registerStepTwo = new RegisterStepTwo(binding.edtAbout.getText().toString(),
                getArguments().getString(Constants.BUNDLE_USER_ID, ""),
                interestRecyclerAdapter.getInterestArea());
        Log.e(TAG, "attemptRegistration: "+interestRecyclerAdapter.getInterestArea()+"   \n us  "+getArguments().getString(Constants.BUNDLE_USER_ID, ""));
//        HashMap<String, String> map = new HashMap<>();
//        map.put("AboutMeText", ""+binding.edtAbout.getText().toString());
//        map.put("InterestArea", interestRecyclerAdapter.getInterestArea());
//        map.put("UserId", getArguments().getString(Constants.BUNDLE_USER_ID, ""));
       // Log.e(TAG, "attemptRegistration: "+map.toString());
        Call<JsonObject> call = apiInterface.registrationTwo(registerStepTwo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                DialogUtils.stopProgressDialog();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getBoolean("IsSuccess")) {
                            showSnackbar(binding.getRoot(), ""+getString(R.string.registation_success));
                            ((PreLoginActivity)getActivity()).replaceFragmentWithoutAnim(new LoginFragment());
                        }else{
                            showSnackbar(binding.getRoot(), jsonObject.getString("ErrorMessage"));
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
}
