package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carelynk.R;
import com.carelynk.dashboard.fragment.HealthFeedsFragment;
import com.carelynk.dashboard.model.HealthFeedModel;
import com.carelynk.dashboard.model.HighlightModel;
import com.carelynk.utilz.AppUtils;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class HealthFeedRecyclerAdapter extends RecyclerView.Adapter<HealthFeedRecyclerAdapter.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private View header;

    private static final int ANIM_DURATION = 300;
    private List<HighlightModel> mListPatient;
    private Context mContext;
    private int lastPos = 0;
    private HealthFeedsFragment myForumFragment;
    private static final String TAG = "HealthFeedRecyclerAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView txtGoalName, txtDesc;
        public TextView txtCreatedName, txtAnswer, txtSupport;
        public ImageView imgGroup;

        public ViewHolder(View rowView) {
            super(rowView);
            cardView = (CardView) rowView.findViewById(R.id.cardView);
            txtGoalName = (TextView) rowView.findViewById(R.id.txtGoalName);
            txtAnswer = (TextView) rowView.findViewById(R.id.txtAnswer);
            txtSupport = (TextView) rowView.findViewById(R.id.txtSupport);
            txtCreatedName = (TextView) rowView.findViewById(R.id.txtCreatedName);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            imgGroup = (ImageView) rowView.findViewById(R.id.imgGroup);
        }
    }

    public HealthFeedRecyclerAdapter(View header, Context context, List<HighlightModel> mListPatient, HealthFeedsFragment myForumFragment) {
        if (header == null) {
            throw new IllegalArgumentException("header may not be null");
        }
        this.header = header;
        this.mListPatient = mListPatient;
        mContext = context;
        this.myForumFragment = myForumFragment;
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
                .inflate(R.layout.list_item_my_health_feed, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (isHeader(position)) {
            return;
        }
        final HighlightModel feedModel = mListPatient.get(position-1);
        holder.txtGoalName.setText(feedModel.GoalName);

        holder.txtCreatedName.setText("Created by "+feedModel.UserName+" at "+ AppUtils.formattedDate("yyyy-MM-dd", "dd-MMM-yyyy", feedModel.CreatedDate.split("T")[0]));
        holder.txtSupport.setText(""+feedModel.SupportCount+" Supports");
        holder.txtSupport.setText(""+feedModel.AnswerCount+" Answers");
        if(!TextUtils.isEmpty(feedModel.Desc))
            holder.txtDesc.setText(feedModel.Desc);

        if(feedModel.PhotoURL != null && !feedModel.PhotoURL.equals("") && !feedModel.PhotoURL.equals("null")) {
            holder.imgGroup.setVisibility(View.VISIBLE);
            Glide.with(mContext).load("http://www.demo.carelynk.com/Content"+feedModel.PhotoURL.split("Content")[1])
                    .into(holder.imgGroup);
        }else
            holder.imgGroup.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myForumFragment.onItemClick(position-1);
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
