package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.dashboard.fragment.MyGroupFragment;
import com.carelynk.dashboard.model.GroupModel;
import com.carelynk.dashboard.model.GroupModelGson;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.CircleTransform;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class AllGroupRecyclerAdapter extends RecyclerView.Adapter<AllGroupRecyclerAdapter.ViewHolder> {

    private List<GroupModelGson.AllGroupDet> mListGroup;
    private Context mContext;
    private MyGroupFragment myGroupFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtGroupName;
        public TextView txtCreatedName, txtDesc;
        public ImageView imgGroup;


        public ViewHolder(View rowView) {
            super(rowView);
            txtGroupName = (TextView) rowView.findViewById(R.id.txtGroupName);
            txtCreatedName = (TextView) rowView.findViewById(R.id.txtCreatedName);
            imgGroup = (ImageView) rowView.findViewById(R.id.imgGroup);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
        }
    }

    public AllGroupRecyclerAdapter(Context context, List<GroupModelGson.AllGroupDet> mListPatient, MyGroupFragment myGroupFragment) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.myGroupFragment = myGroupFragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_all_group, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //final GroupModel feedModel= mListGroup.get(position);

        holder.txtGroupName.setText(mListGroup.get(position).getGroupName());
        holder.txtDesc.setText(mListGroup.get(position).getDescription());
        holder.txtCreatedName.setText("Created on "+ AppUtils.formattedDate("MM/dd/yyyy", "dd-MMM-yyyy", mListGroup.get(position).getCreatedDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGroupFragment.onItemAllGroupClick(position, mListGroup.get(position));
            }
        });

        if(!TextUtils.isEmpty(mListGroup.get(position).getPhotoURL())) {
            holder.imgGroup.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(AppUtils.getImagePath(mListGroup.get(position).getPhotoURL())).apply(RequestOptions.circleCropTransform()).into(holder.imgGroup);
        }else
            Glide.with(mContext).load(R.drawable.ic_launcher_black).apply(RequestOptions.circleCropTransform()).into(holder.imgGroup);

    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }



}
