package com.carelynk.event;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.databinding.ActivityEventDetailBinding;
import com.carelynk.event.fragment.EventViewMemberDialogFragment;
import com.carelynk.event.model.EventList;
import com.carelynk.invite.InviteActivity;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.CircleTransform;
import com.carelynk.utilz.Constants;

/**
 * Created by Mohit-Anjali on 27-Sep-17.
 */

public class EventDetailActivity extends BaseActivity {

    ActivityEventDetailBinding binding;
    EventList.Result mEventData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_detail);
        mEventData = (EventList.Result) getIntent().getSerializableExtra(Constants.EXTRA_EVENT);
        setData();

        binding.txtInviteUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailActivity.this, InviteActivity.class);
                intent.putExtra(Constants.EXTRA_EVENT_ID, "" + mEventData.getEventID());
                startActivityForResult(intent, Constants.REQUEST_CODE_INVITE);
            }
        });

        binding.txtViewMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new EventViewMemberDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EXTRA_EVENT_ID, mEventData.getEventID());
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
            }
        });
    }

    private void setData() {
        binding.txtTitle.setText(mEventData.getEventName());
        binding.txtLocation.setText(mEventData.getAddress());
        binding.txtTimeTo.setText(mEventData.getEventTimeFrom()+" To "+mEventData.getEventTimeTo());
        binding.txtDateTo.setText(mEventData.getEventDateFrom()+" To "+mEventData.getEventDateTo());
        binding.txtDescription.setText(mEventData.getEventDesc());
        binding.txtUsername.setText(mEventData.getUserName());
        if(mEventData.getIsPrivate().equalsIgnoreCase("true"))
            binding.txtInviteUsers.setVisibility(View.VISIBLE);
        Glide.with(this).load(AppUtils.getImagePath(mEventData.getPhotoURL())).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder))
                .into(binding.imgEvent);
    }

    public void onClickBack(View view){
        finish();
    }
}
