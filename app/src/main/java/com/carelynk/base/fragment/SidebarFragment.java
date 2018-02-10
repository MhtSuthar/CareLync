package com.carelynk.base.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.base.BaseActivityWithSidebar;
import com.carelynk.base.BaseFragment;
import com.carelynk.base.adapter.SlideMenuRecyclerAdapter;
import com.carelynk.dashboard.MenuActivity;
import com.carelynk.databinding.FragmentSidebarBinding;
import com.carelynk.prelogin.PreLoginActivity;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.profile.MyProfileActivity;
import com.carelynk.search.DiscoverActivity;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.CircleTransform;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.PrefUtils;

/**
 * Created by Admin on 12-Sep-16.
 */
public class SidebarFragment extends BaseFragment {

    FragmentSidebarBinding binding;
    SlideMenuRecyclerAdapter slideMenuRecyclerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sidebar, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(getActivity()).load(R.drawable.ic_default_avatar)
               .apply(RequestOptions.circleCropTransform()).into(binding.imgUser);
        setRecyclerAdapter();
        binding.imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
    }

    private void showLogoutDialog() {
        showAlertDialog(new BaseActivity.OnDialogClick() {
            @Override
            public void onPositiveBtnClick() {
                SharedPreferenceUtil.putValue(PrefUtils.PREF_IS_LOGIN, false);
                SharedPreferenceUtil.save();
                moveActivity(new Intent(getActivity(), PreLoginActivity.class), getActivity(), true);
                //getActivity().finish();
            }

            @Override
            public void onNegativeBtnClick() {

            }
        }, getString(R.string.logout), getString(R.string.sure_logout), true);
    }


    private void setRecyclerAdapter() {
        slideMenuRecyclerAdapter = new SlideMenuRecyclerAdapter(getActivity(), SidebarFragment.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(slideMenuRecyclerAdapter);
    }


    public void onItemClick(int position) {
        switch (position){
            case 0:
                ((BaseActivityWithSidebar)getActivity()).closeDrawer();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moveActivity(new Intent(getActivity(), MenuActivity.class), getActivity(), true);
                    }
                }, 400);

                break;
            case 1:
                ((BaseActivityWithSidebar)getActivity()).closeDrawer();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.putExtra(Constants.BUNDLE_WHICH_LINK, 0);
                        moveActivity(intent, getActivity(), false);
                    }
                }, 400);

                break;
            case 2:
                ((BaseActivityWithSidebar)getActivity()).closeDrawer();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moveActivity(new Intent(getActivity(), DiscoverActivity.class), getActivity(), false);
                    }
                }, 400);

                break;
            case 3:
                ((BaseActivityWithSidebar)getActivity()).closeDrawer();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moveActivity(new Intent(getActivity(), MyProfileActivity.class), getActivity(), false);
                    }
                }, 400);

                break;
            case 4:
                ((BaseActivityWithSidebar)getActivity()).closeDrawer();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       // moveActivity(new Intent(getActivity(), DiscoverActivity.class), getActivity(), false);
                    }
                }, 400);

                break;
        }
    }
}
