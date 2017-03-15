package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carelynk.R;
import com.carelynk.dashboard.FeedDetailActivity;
import com.carelynk.dashboard.model.HealthFeedModel;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class FeedDetailRecyclerAdapter extends RecyclerView.Adapter<FeedDetailRecyclerAdapter.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private View header;

    private static final int ANIM_DURATION = 300;
    private List<HealthFeedModel> mListPatient;
    private Context mContext;
    private int lastPos = 0;
    private FeedDetailActivity feedDetailActivity;
    private static final String TAG = "HealthFeedRecyclerAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView txtName, txtDesc, txt_comment;
        public TextView txtPostTime;
        public ImageView imgCover;

        public ViewHolder(View rowView) {
            super(rowView);
            cardView = (CardView) rowView.findViewById(R.id.cardView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            txtPostTime = (TextView) rowView.findViewById(R.id.txtPostTime);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            imgCover = (ImageView) rowView.findViewById(R.id.imgCover);
            txt_comment = (TextView) rowView.findViewById(R.id.txt_comment);
        }
    }

    public FeedDetailRecyclerAdapter(View header, Context context, List<HealthFeedModel> mListPatient, FeedDetailActivity feedDetailActivity) {
        if (header == null) {
            throw new IllegalArgumentException("header may not be null");
        }
        this.header = header;
        this.mListPatient = mListPatient;
        mContext = context;
        this.feedDetailActivity = feedDetailActivity;
    }

    public boolean isHeader(int position) {
        return position == 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return new ViewHolder(header);
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_feed_detail, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (isHeader(position)) {
            return;
        }
        final HealthFeedModel feedModel = mListPatient.get(position-1);
        holder.txtName.setText(feedModel.GoalName);

        holder.txtPostTime.setText("Created by "+feedModel.UserName);
        holder.txtDesc.setText(feedModel.Desc);

        if(feedModel.PhotoURL != null && !feedModel.PhotoURL.equals("") && !feedModel.PhotoURL.equals("null")) {
            holder.imgCover.setVisibility(View.VISIBLE);
            Glide.with(mContext).load("http://www.demo.carelynk.com/Content"+feedModel.PhotoURL.split("Content")[1])
                    .into(holder.imgCover);
        }else
            holder.imgCover.setVisibility(View.GONE);

        holder.txt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedDetailActivity.showCommnetDialog();
            }
        });

        //animateStackByStack(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mListPatient.size() + 1;
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

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

}
