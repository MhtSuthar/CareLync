package com.carelynk.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.chat.ChatListActivity;
import com.carelynk.chat.model.ChatListModel;
import com.carelynk.event.EventListActivity;
import com.carelynk.event.model.EventList;
import com.carelynk.utilz.AppUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private List<ChatListModel.Result> mListGroup;
    private Context mContext;
    private ChatListActivity chatListActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtCount;
        public ImageView mImageUser, imgActive;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            mImageUser = (ImageView) rowView.findViewById(R.id.imgUser);
            txtCount = (TextView) rowView.findViewById(R.id.txtCount);
            imgActive = (ImageView) rowView.findViewById(R.id.imgActive);
        }
    }

    public ChatListAdapter(Context context, List<ChatListModel.Result> mListPatient, ChatListActivity eventListActivity) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.chatListActivity = eventListActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_chat, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtName.setText(mListGroup.get(position).getName());
        Glide.with(mContext).load(AppUtils.getImagePath(mListGroup.get(position).getImage())).
                apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy)).into(holder.mImageUser);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatListActivity.onClickDetail(mListGroup.get(position));
            }
        });

        if(!TextUtils.isEmpty(mListGroup.get(position).getCounting()) && Integer.parseInt(mListGroup.get(position).getCounting()) > 0){
            holder.txtCount.setVisibility(View.VISIBLE);
            holder.txtCount.setText(mListGroup.get(position).getCounting());
        }else
            holder.txtCount.setVisibility(View.GONE);

        if(mListGroup.get(position).getAccepted().equalsIgnoreCase("true")){
            holder.imgActive.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(AppUtils.getImagePath(mListGroup.get(position).getLivesta())).
                    apply(RequestOptions.circleCropTransform()).into(holder.imgActive);
        }else
            holder.imgActive.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }


}
