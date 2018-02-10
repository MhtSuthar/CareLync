package com.carelynk.profile.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.dashboard.FeedDetailActivity;
import com.carelynk.databinding.FragmentProfileBinding;
import com.carelynk.databinding.FragmentTimelineBinding;
import com.carelynk.profile.adapter.AnswerDashboardRecyclerAdapter;
import com.carelynk.profile.model.DashboardModel;
import com.carelynk.utilz.AppUtils;

import java.util.List;

/**
 * Created by admin on 09/02/17.
 */

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    private DashboardModel.profileDetail mList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mList.getAboutMe().equalsIgnoreCase("Entity")){
            binding.linEducation.setVisibility(View.GONE);
            binding.txtFirstNameHint.setText("Entity Name: ");
            binding.linLastName.setVisibility(View.GONE);
        }
        binding.txtAboutMe.setText(mList.getAboutMeText());
        binding.txtCertification.setText(mList.getCertification());
        binding.txtCity.setText(mList.getCity());
        binding.txtEducation.setText(mList.getEducation());
        binding.txtState.setText(mList.getState());
        binding.txtFirstName.setText(mList.getFirstName());
        binding.txtLastName.setText(mList.getLastName());
        binding.txtExpertise.setText(mList.getExpertise());
    }

    public void setmList(DashboardModel.profileDetail mList) {
        this.mList = mList;
    }


}
