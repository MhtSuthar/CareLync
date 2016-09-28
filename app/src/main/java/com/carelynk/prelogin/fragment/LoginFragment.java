package com.carelynk.prelogin.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.dashboard.HomeActivity;
import com.carelynk.databinding.FragmentLoginBinding;

/**
 * Created by Admin on 12-Sep-16.
 */
public class LoginFragment extends BaseFragment {

    FragmentLoginBinding binding;

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
                moveActivity(new Intent(getActivity(), HomeActivity.class), getActivity(), false);
            }
        });

        binding.txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addFragment(new RegistrationFragment());
            }
        });
    }

}
