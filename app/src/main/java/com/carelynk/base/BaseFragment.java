package com.carelynk.base;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import com.carelynk.R;

/**
 * Created by Admin on 12-Sep-16.
 */
public abstract class BaseFragment extends Fragment {

    protected void moveActivity(Intent intent, Activity context, boolean isFinish){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
        } else {
            startActivity(intent);
        }

        if(isFinish)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getActivity().finish();
                }
            }, 400);
    }

    public void addFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                add(R.id.container, fragment, fragment.getTag()).addToBackStack(fragment.getTag()).commit();
    }

    public void replaceFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                replace(R.id.container, fragment, fragment.getTag()).commit();
    }

    protected void showAlertDialog(final BaseActivity.OnDialogClick onDialogClick, String title, String msg, boolean isNagative){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDialogClick.onPositiveBtnClick();
            }
        });
        if(isNagative)
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onDialogClick.onNegativeBtnClick();
                }
            });
        builder.show();
    }
}
