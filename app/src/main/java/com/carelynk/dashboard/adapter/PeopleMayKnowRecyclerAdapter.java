package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.dashboard.fragment.HealthFeedsFragment;
import com.carelynk.dashboard.model.CommentModel;
import com.carelynk.utilz.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class PeopleMayKnowRecyclerAdapter extends RecyclerView.Adapter<PeopleMayKnowRecyclerAdapter.ViewHolder> {

    private JSONArray mList;
    private HealthFeedsFragment mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtExpertise;
        public ImageView imgUser;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            txtExpertise = (TextView) rowView.findViewById(R.id.txtNameExpertise);
            imgUser = (ImageView) rowView.findViewById(R.id.imgUser);
        }

    }

    public PeopleMayKnowRecyclerAdapter(HealthFeedsFragment context, JSONArray mListPatient) {
        this.mList = mListPatient;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_people_may_know, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            holder.txtName.setText(mList.getJSONObject(position).getString("UserName"));
            holder.txtExpertise.setText(mList.getJSONObject(position).getString("Expertise"));
            Glide.with(mContext).load(AppUtils.getImagePath(mList.getJSONObject(position).getString("ProfilePicUrl")))
                    .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy)).into(holder.imgUser);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mContext.onClickUserDetail(mList.getJSONObject(position).getString("FromUserID"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mList.length();
    }

}
