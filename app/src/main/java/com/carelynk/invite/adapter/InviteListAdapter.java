package com.carelynk.invite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.invite.InviteActivity;
import com.carelynk.trending.TrendingListActivity;
import com.carelynk.utilz.AppUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class InviteListAdapter extends RecyclerView.Adapter<InviteListAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<HashMap<String, String>> mListGroup;
    private Context mContext;
    private InviteActivity eventListActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public CheckBox chkInvite;
        public ImageView imgUser;
        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            chkInvite = (CheckBox) rowView.findViewById(R.id.chkInvite);
            imgUser  = (ImageView) rowView.findViewById(R.id.imgUser);
        }
    }

    public InviteListAdapter(Context context, List<HashMap<String, String>> mListPatient, InviteActivity eventListActivity) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.eventListActivity = eventListActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_friend_invitation, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtName.setText(mListGroup.get(position).get("UserName"));
        if(mListGroup.get(position).get("IsSelect").equals("0"))
            holder.chkInvite.setChecked(false);
        else
            holder.chkInvite.setChecked(true);

        Glide.with(mContext).load(AppUtils.getImagePath(mListGroup.get(position).get("ProfilePicUrl"))).
                apply(RequestOptions.circleCropTransform()).into(holder.imgUser);

        holder.chkInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                if(checkBox.isChecked()){
                    mListGroup.get(position).put("IsSelect", "1");
                    notifyDataSetChanged();
                }else{
                    mListGroup.get(position).put("IsSelect", "0");
                    notifyDataSetChanged();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListActivity.onClickDetail(mListGroup.get(position).get("UserId"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }

    public boolean getSelectedCount(){
        for (int i = 0; i < mListGroup.size(); i++) {
            if(mListGroup.get(i).get("IsSelect").equals("1"))
                return true;
        }
        return false;
    }

    public List<HashMap<String, String>> getAllData(){
        return mListGroup;
    }


}
