package com.carelynk.friends.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.recent.model.TimelineModel;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<TimelineModel> mListFriendReq;
    private Context mContext;
    private int lastPos = 0;
    private static final String TAG = "HealthFeedRecyclerAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public ImageView imgOk, imgCancel;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            imgOk = (ImageView) rowView.findViewById(R.id.imgOk);
            imgCancel = (ImageView) rowView.findViewById(R.id.imgCancel);
        }
    }

    public FriendRequestAdapter(Context context, List<TimelineModel> mListPatient) {
        this.mListFriendReq = mListPatient;
        mContext = context;
    }

    public void setPatientList(List<TimelineModel> patientList) {
        mListFriendReq.clear();
        mListFriendReq.addAll(patientList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_friend_request, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TimelineModel feedModel = mListFriendReq.get(position);
        holder.txtName.setText(feedModel.getName());

        holder.imgOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListFriendReq.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListFriendReq.remove(position);
                notifyDataSetChanged();
            }
        });

        animateStackByStack(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mListFriendReq.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    private void animateStackByStack(View view, final int pos) {
        if (pos > lastPos) {
            view.animate().cancel();
            view.setTranslationY(50);
            view.setAlpha(0);
            view.animate().alpha(1.0f).translationY(0).setDuration(ANIM_DURATION).setStartDelay(100);
            lastPos = pos;
        }
    }

}
