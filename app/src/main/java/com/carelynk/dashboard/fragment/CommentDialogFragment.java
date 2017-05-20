package com.carelynk.dashboard.fragment;

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
import com.carelynk.dashboard.adapter.CommentRecyclerAdapter;
import com.carelynk.dashboard.model.CommentModel;
import com.carelynk.dashboard.model.GroupModel;
import com.carelynk.databinding.DialogCommentListBinding;
import com.carelynk.rest.ApiFactory;
import com.carelynk.rest.ApiInterface;
import com.carelynk.rest.Urls;
import com.carelynk.utilz.AppUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 19-Oct-16.
 */

public class CommentDialogFragment extends DialogFragment {

    private DialogCommentListBinding binding;
    private List<String> mGroupList;
    private CommentRecyclerAdapter commentRecyclerAdapter;
    private static final String TAG = "CommentDialogFragment";
    private String groupUpdateId;

    public CommentDialogFragment(String groupUpdateId) {
        this.groupUpdateId = groupUpdateId;
    }

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

        getGroupDiscussDetails();
    }

    private void initToolbar() {
        binding.includeToolbar.toolbarTitle.setText(getString(R.string.comment));
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initDialog() {
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ThemeDialogFit;
        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
        wmlp.gravity = Gravity.FILL_HORIZONTAL;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void setRecyclerAdapter(List<CommentModel.Result> mList) {
        commentRecyclerAdapter = new CommentRecyclerAdapter(getContext(), mList);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(commentRecyclerAdapter);
    }

    private void getGroupDiscussDetails() {
        if(AppUtils.isOnline(getContext())){
            binding.progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInterface = ApiFactory.provideInterface();
            Call<CommentModel> call = apiInterface.getCommentList(ApiFactory.API_BASE_URL+""+ Urls.GET_COMMENT_LIST+"?groupupdate_id="+groupUpdateId);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel>call, Response<CommentModel> response) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try{
                            Log.e(TAG, "create group: "+new Gson().toJson(response.body()));
                            if(response.body().getResult() != null && response.body().getResult().size() > 0){
                                setRecyclerAdapter(response.body().getResult());
                            }else{

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel>call, Throwable t) {
                    Log.e(TAG, t.toString());
                    binding.progressBar.setVisibility(View.GONE);
                }
            });
        }else{
            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

}
