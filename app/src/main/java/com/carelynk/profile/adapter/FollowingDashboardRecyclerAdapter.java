package com.carelynk.profile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.profile.fragment.FollowingFragment;
import com.carelynk.profile.model.DashboardModel;
import com.carelynk.utilz.AppUtils;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class FollowingDashboardRecyclerAdapter extends RecyclerView.Adapter<FollowingDashboardRecyclerAdapter.ViewHolder> {

    private List<DashboardModel.FollowingArray> mList;
    private FollowingFragment mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtDesc;
        public ImageView mImage;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName= (TextView) rowView.findViewById(R.id.txtName);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            mImage= (ImageView) rowView.findViewById(R.id.img);
        }

    }

    public FollowingDashboardRecyclerAdapter(FollowingFragment context, List<DashboardModel.FollowingArray> mListPatient) {
        this.mList = mListPatient;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_article_dashboard, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtName.setText(mList.get(position).getName());
        holder.mImage.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(AppUtils.getImagePath(mList.get(position).getProfilePicUrl()))
                .apply(RequestOptions.circleCropTransform()).into(holder.mImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.onClick(mList.get(position).getUserid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
