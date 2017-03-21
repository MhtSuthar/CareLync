package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carelynk.R;
import com.carelynk.dashboard.fragment.HighlightFragment;
import com.carelynk.dashboard.model.HighlightModel;
import com.carelynk.utilz.CircleTransform;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class HighlightRecyclerAdapter extends RecyclerView.Adapter<HighlightRecyclerAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<HighlightModel> mListPatient;
    private Context mContext;
    private int lastPos = 0;
    private HighlightFragment highlightFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtPostTime, txtDesc, txtFollow, txtTitle;
        public ImageView imgCover, imgUser;
        public CheckBox chkFav;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName= (TextView) rowView.findViewById(R.id.txtName);
            txtTitle = (TextView) rowView.findViewById(R.id.txtTitle);
            txtPostTime = (TextView) rowView.findViewById(R.id.txtPostTime);
            txtDesc= (TextView) rowView.findViewById(R.id.txtDesc);
            txtFollow= (TextView) rowView.findViewById(R.id.txtFollow);
            imgCover= (ImageView) rowView.findViewById(R.id.imgCover);
            chkFav = (CheckBox) rowView.findViewById(R.id.chkFav);
            imgUser = (ImageView) rowView.findViewById(R.id.imgUser);
        }

    }

    public HighlightRecyclerAdapter(Context context, List<HighlightModel> mListPatient, HighlightFragment highlightFragment) {
        this.mListPatient = mListPatient;
        mContext = context;
        this.highlightFragment = highlightFragment;
    }

    public void setPatientList(List<HighlightModel> patientList){
        mListPatient.clear();
        mListPatient.addAll(patientList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_heighlight, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final HighlightModel feedModel= mListPatient.get(position);
        Glide.with(mContext).load(R.drawable.dummy_img).
                transform(new CircleTransform(mContext)).into(holder.imgUser);
        holder.txtName.setText(feedModel.UserName);
        holder.txtTitle.setText(feedModel.GoalName);
        if(!TextUtils.isEmpty(feedModel.Desc))
            holder.txtDesc.setText(feedModel.Desc);
        else
            holder.txtDesc.setVisibility(View.GONE);

        if(position / 2 == 0)
            holder.imgCover.setVisibility(View.GONE);
        else
            holder.imgCover.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    highlightFragment.onItemClick(position);
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
