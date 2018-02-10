package com.carelynk.prelogin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.PrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by admin on 09/03/17.
 */

public class ForgetPassFragment extends BaseFragment {

    private AppCompatButton mButtonSubmit;
    private EditText mEditEmail;
    private LinearLayout mRoot;
    private static final String TAG = "ForgetPassFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forget, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditEmail = (EditText) view.findViewById(R.id.edtEmail);
        mRoot = (LinearLayout) view.findViewById(R.id.linRoot);
        mButtonSubmit = (AppCompatButton) view.findViewById(R.id.btnSubmit);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()){
                    attemptToForgetPass();
                }
            }
        });
    }

    private void attemptToForgetPass() {
        if (isOnline(getActivity())) {
            showProgressDialog();
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    stopProgressDialog();
                    try{
                        JSONObject jsonObject = new JSONObject(result);
                        if(jsonObject.getBoolean("IsSuccess")){
                            showSnackbar(mRoot, "We have send link to your register email id");
                            getActivity().getSupportFragmentManager().popBackStack();
                        }else{
                            showSnackbar(mRoot, "No email id exist in our system, try another");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.GET_FOTGET_PASS+"?Email="+mEditEmail.getText().toString());
        } else {
            showSnackbar(mRoot, getString(R.string.no_internet));
        }
    }

    private boolean isValid() {
        if(!isOnline(getContext())) {
            showSnackbar(mRoot, getString(R.string.no_internet));
            return false;
        } else if(TextUtils.isEmpty(mEditEmail.getText().toString())) {
            showSnackbar(mRoot, "Please enter email");
            return false;
        }else if(!AppUtils.isValidEmail(mEditEmail.getText().toString())) {
            showSnackbar(mRoot, "Please enter valid email");
            return false;
        }
        return true;
    }
}
