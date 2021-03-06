package com.carelynk.dashboard.adapter;

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
import com.carelynk.dashboard.fragment.FollowFragment;
import com.carelynk.dashboard.model.FollowersModel;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.CircleTransform;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class FollowRecyclerAdapter extends RecyclerView.Adapter<FollowRecyclerAdapter.ViewHolder> {

    private List<FollowersModel.Result> mListGroup;
    private Context mContext;
    private FollowFragment followFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public ImageView mImage;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName= (TextView) rowView.findViewById(R.id.txtName);
            mImage= (ImageView) rowView.findViewById(R.id.img);
        }
    }

    public FollowRecyclerAdapter(Context context, List<FollowersModel.Result> mListPatient, FollowFragment myGroupFragment) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.followFragment = myGroupFragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_follow, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FollowersModel.Result feedModel= mListGroup.get(position);

        holder.txtName.setText(feedModel.getUserName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followFragment.onFollowItemClick(feedModel.getFromUserId());
            }
        });

        Glide.with(mContext).load(AppUtils.getImagePath(feedModel.getProfilePicUrl())).
               apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy)).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }



}
