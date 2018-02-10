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
import com.carelynk.dashboard.model.CommentModel;
import com.carelynk.utilz.AppUtils;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {

    private List<CommentModel.Result> mList;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtDesc;
        public ImageView imgComment;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName= (TextView) rowView.findViewById(R.id.txtName);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            imgComment = (ImageView) rowView.findViewById(R.id.img_comment);
        }

    }

    public CommentRecyclerAdapter(Context context, List<CommentModel.Result> mListPatient) {
        this.mList = mListPatient;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_comment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtName.setText(mList.get(position).getUserName());
        holder.txtDesc.setText(mList.get(position).getCommentText());
        if(!TextUtils.isEmpty(mList.get(position).getPhotoURL())) {
            holder.imgComment.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(AppUtils.getImagePath(mList.get(position).getPhotoURL()))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop()).into(holder.imgComment);
        }else
            holder.imgComment.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
