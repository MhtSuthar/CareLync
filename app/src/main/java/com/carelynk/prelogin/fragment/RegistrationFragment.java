package com.carelynk.prelogin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.base.BaseFragment;
import com.carelynk.databinding.FragmentLoginBinding;
import com.carelynk.databinding.FragmentRegistrationBinding;

/**
 * Created by Admin on 12-Sep-16.
 */
public class RegistrationFragment extends BaseFragment {

    FragmentRegistrationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*Glide.with(getActivity()).load(R.drawable.dummy_img).
                transform(new CircleTransform(getActivity())).into(binding.imgPhoto);*/
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
