package com.carelynk.recent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carelynk.R;
import com.carelynk.recent.model.HealthFeedDetailModel;
import com.carelynk.recent.model.HealthFeedModel;
import com.carelynk.recent.model.TimelineModel;
import com.carelynk.utilz.CircleTransform;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class HealthFeedDetailRecyclerAdapter extends RecyclerView.Adapter<HealthFeedDetailRecyclerAdapter.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private View header;

    private static final int ANIM_DURATION = 300;
    private List<HealthFeedDetailModel> mListPatient;
    private Context mContext;
    private int lastPos = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTime, txtDesc;
        public ImageView imgCover;

        public ViewHolder(View rowView) {
            super(rowView);
            txtTime = (TextView) rowView.findViewById(R.id.txtTime);
            txtDesc= (TextView) rowView.findViewById(R.id.txtDesc);
            imgCover= (ImageView) rowView.findViewById(R.id.imgCover);
        }

    }

    public boolean isHeader(int position) {
        return position == 0;
    }

    public HealthFeedDetailRecyclerAdapter(View header, Context context, List<HealthFeedDetailModel> mListPatient) {
        if (header == null) {
            throw new IllegalArgumentException("header may not be null");
        }
        this.header = header;
        this.mListPatient = mListPatient;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return new HealthFeedDetailRecyclerAdapter.ViewHolder(header);
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_health_feed_detail, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (isHeader(position)) {
            return;
        }

        final HealthFeedDetailModel feedModel = mListPatient.get(position-1);
        holder.txtDesc.setText(feedModel.Updatemsg);

        holder.txtTime.setText("Created by "+feedModel.UpdateDate);

        if(feedModel.PhotoURL != null && !feedModel.PhotoURL.equals("") && !feedModel.PhotoURL.equals("null")) {
            holder.imgCover.setVisibility(View.VISIBLE);
            Glide.with(mContext).load("http://www.demo.carelynk.com/Content"+feedModel.PhotoURL.split("Content")[1])
                    .into(holder.imgCover);
        }else
            holder.imgCover.setVisibility(View.GONE);

        //animateStackByStack(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mListPatient.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
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
