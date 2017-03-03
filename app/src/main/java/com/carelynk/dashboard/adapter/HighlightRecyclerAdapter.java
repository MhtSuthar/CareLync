package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carelynk.R;
import com.carelynk.recent.model.TimelineModel;
import com.carelynk.utilz.CircleTransform;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class HighlightRecyclerAdapter extends RecyclerView.Adapter<HighlightRecyclerAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<TimelineModel> mListPatient;
    private Context mContext;
    private int lastPos = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtPostTime, txtDesc, txtFollow;
        public ImageView imgCover, imgUser;
        public CheckBox chkFav;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName= (TextView) rowView.findViewById(R.id.txtName);
            txtPostTime = (TextView) rowView.findViewById(R.id.txtPostTime);
            txtDesc= (TextView) rowView.findViewById(R.id.txtDesc);
            txtFollow= (TextView) rowView.findViewById(R.id.txtFollow);
            imgCover= (ImageView) rowView.findViewById(R.id.imgCover);
            chkFav = (CheckBox) rowView.findViewById(R.id.chkFav);
            imgUser = (ImageView) rowView.findViewById(R.id.imgUser);
        }

    }

    public HighlightRecyclerAdapter(Context context, List<TimelineModel> mListPatient) {
        this.mListPatient = mListPatient;
        mContext = context;
    }

    public void setPatientList(List<TimelineModel> patientList){
        mListPatient.clear();
        mListPatient.addAll(patientList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_timeline, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TimelineModel feedModel= mListPatient.get(position);
        Glide.with(mContext).load(R.drawable.dummy_img).
                transform(new CircleTransform(mContext)).into(holder.imgUser);
        holder.txtName.setText(feedModel.getName());

        if(feedModel.isFollowing){
            holder.txtFollow.setTextColor(Color.WHITE);
            holder.txtFollow.setText("Following");
            holder.txtFollow.setBackgroundResource(R.drawable.round_corner_white);
        }else{
            holder.txtFollow.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            holder.txtFollow.setText("Follow");
            holder.txtFollow.setBackgroundResource(R.drawable.round_corner_blue_fill);
        }
        holder.txtFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feedModel.isFollowing)
                    feedModel.isFollowing = false;
                else
                    feedModel.isFollowing = true;
                notifyDataSetChanged();
            }
        });
        animateStackByStack(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mListPatient.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    private void animateStackByStack(View view, final int pos) {
        if(pos > lastPos) {
            view.animate().cancel();
            view.setTranslationY(50);
            view.setAlpha(0);
            view.animate().alpha(1.0f).translationY(0).setDuration(ANIM_DURATION).setStartDelay(100);
            lastPos = pos;
        }
    }

}
