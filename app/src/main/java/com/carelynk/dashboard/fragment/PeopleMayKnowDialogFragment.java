package com.carelynk.dashboard.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.carelynk.R;
import com.carelynk.dashboard.adapter.PeopleMayKnowListAdapter;
import com.carelynk.databinding.DialogCommentListBinding;
import com.carelynk.event.adapter.ViewMemberEventListAdapter;
import com.carelynk.profile.DashboardActivity;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.rest.Urls;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.PrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 19-Oct-16.
 */

public class PeopleMayKnowDialogFragment extends DialogFragment {

    private DialogCommentListBinding binding;
    private static final String TAG = "EventDetailDialogFragme";
    private PeopleMayKnowListAdapter peopleMayKnowListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_comment_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog();

        initToolbar();

        getViewMember();
    }

    void getViewMember(){
        if(AppUtils.isOnline(getActivity())){
            binding.progressBar.setVisibility(View.VISIBLE);
            AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(getActivity(), new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
                @Override
                public void onTaskComplete(String result) {
                    Log.e(TAG, "onTaskComplete: "+result);
                    binding.progressBar.setVisibility(View.GONE);
                    try {
                        if(result != null) {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("PeoplemayData");
                            List<HashMap<String, String>> mList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("name", obj.getString("UserName"));
                                hashMap.put("profile", obj.getString("ProfilePicUrl"));
                                hashMap.put("user_id", obj.getString("FromUserID"));
                                mList.add(hashMap);
                            }
                            setRecyclerAdapter(mList);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            asyncTaskGetCommon.execute(ApiFactory.API_BASE_URL+""+ Urls.PEOPLE_MAY_KNOW+""+ SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""));
        }else{
            Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void setRecyclerAdapter(List<HashMap<String, String>> mList) {
        peopleMayKnowListAdapter = new PeopleMayKnowListAdapter(getActivity(), mList, PeopleMayKnowDialogFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(peopleMayKnowListAdapter);
    }

    private void initToolbar() {
        binding.includeToolbar.toolbarTitle.setText("People You May Know");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.relBottom.setVisibility(View.GONE);
    }

    private void initDialog() {
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ThemeDialogFit;
        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_HORIZONTAL;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void onClickDetail(String user_id) {
        Intent intentDash = new Intent(getActivity(), DashboardActivity.class);
        intentDash.putExtra(Constants.EXTRA_USERID, user_id);
        startActivity(intentDash);
    }
}
