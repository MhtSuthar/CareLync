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
public class MyGroupRecyclerAdapter extends RecyclerView.Adapter<MyGroupRecyclerAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<GroupModelGson.OwnGroupDet> mListGroup;
    private Context mContext;
    private MyGroupFragment myGroupFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtGroupName, txt_edit, txt_delete, txtRequestCount;
        public TextView txtCreatedName;
        public ImageView imgGroup;
        public TextView txtDesc;


        public ViewHolder(View rowView) {
            super(rowView);
            txtGroupName = (TextView) rowView.findViewById(R.id.txtGroupName);
            txt_edit = (TextView) rowView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) rowView.findViewById(R.id.txt_delete);
            txtCreatedName = (TextView) rowView.findViewById(R.id.txtCreatedName);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            imgGroup = (ImageView) rowView.findViewById(R.id.imgGroup);
            txtRequestCount= (TextView) rowView.findViewById(R.id.txtRequestCount);
        }
    }

    public MyGroupRecyclerAdapter(Context context, List<GroupModelGson.OwnGroupDet> mListPatient, MyGroupFragment myGroupFragment) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.myGroupFragment = myGroupFragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_my_group, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GroupModelGson.OwnGroupDet feedModel = mListGroup.get(position);

        holder.txtGroupName.setText(feedModel.getGroupName());
        holder.txtCreatedName.setText("Created on " + AppUtils.formattedDate("MM/dd/yyyy", "dd-MMM-yyyy", feedModel.getCreatedDate()));
        holder.txtDesc.setText("" + feedModel.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGroupFragment.onItemClick(position, feedModel);
            }
        });

        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGroupFragment.onEditItemClick(feedModel);
            }
        });

        holder.txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGroupFragment.onDeleteItemClick(feedModel.getGroupId());
            }
        });

        if(Integer.parseInt(feedModel.getRequestCount()) > 0){
            holder.txtRequestCount.setVisibility(View.VISIBLE);
            holder.txtRequestCount.setText(feedModel.getRequestCount());
        }

        if(!TextUtils.isEmpty(mListGroup.get(position).getPhotoURL())) {
            Glide.with(mContext).load(AppUtils.getImagePath(mListGroup.get(position).getPhotoURL())).apply(RequestOptions.circleCropTransform()).into(holder.imgGroup);
        }else
            Glide.with(mContext).load(R.drawable.ic_launcher_black).apply(RequestOptions.circleCropTransform()).into(holder.imgGroup);
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }


}
