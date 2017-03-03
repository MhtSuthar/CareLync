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
import com.carelynk.recent.model.GroupPostModel;
import com.carelynk.recent.model.TimelineModel;
import com.carelynk.utilz.CircleTransform;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class GroupDetailRecyclerAdapter extends RecyclerView.Adapter<GroupDetailRecyclerAdapter.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private View header;

    private static final int ANIM_DURATION = 300;
    private List<GroupPostModel> mListPatient;
    private Context mContext;
    private int lastPos = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtPostTime, txtDesc, txtComment;
        public ImageView imgCover;

        public ViewHolder(View rowView) {
            super(rowView);
            txtPostTime = (TextView) rowView.findViewById(R.id.txtPostTime);
            txtDesc= (TextView) rowView.findViewById(R.id.txtDesc);
            imgCover= (ImageView) rowView.findViewById(R.id.imgCover);
            txtComment = (TextView) rowView.findViewById(R.id.txtComment);
        }

    }

    public boolean isHeader(int position) {
        return position == 0;
    }

    public GroupDetailRecyclerAdapter(View header, Context context, List<GroupPostModel> mListPatient) {
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
            return new GroupDetailRecyclerAdapter.ViewHolder(header);
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_group_detail, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (isHeader(position)) {
            return;
        }

        final GroupPostModel feedModel= mListPatient.get(position-1);

        if(feedModel.PhotoURL != null && !feedModel.PhotoURL.equals("") && !feedModel.PhotoURL.equals("null")) {
            holder.imgCover.setVisibility(View.VISIBLE);
            Glide.with(mContext).load("http://www.demo.carelynk.com/Content"+feedModel.PhotoURL.split("Content")[1])
                    .into(holder.imgCover);
        }else
            holder.imgCover.setVisibility(View.GONE);

        holder.txtDesc.setText(feedModel.Updatemsg);
        holder.txtPostTime.setText(feedModel.UpdateDate);

        holder.txtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
