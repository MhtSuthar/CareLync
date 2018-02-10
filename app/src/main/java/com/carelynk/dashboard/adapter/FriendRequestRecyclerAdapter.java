package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.dashboard.fragment.FollowFragment;
import com.carelynk.dashboard.fragment.MyGroupFragment;
import com.carelynk.dashboard.model.FollowRequestModel;
import com.carelynk.dashboard.model.ReuestGroupModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class FriendRequestRecyclerAdapter extends RecyclerView.Adapter<FriendRequestRecyclerAdapter.ViewHolder> {

    private List<HashMap<String, String>> mListGroup;
    private Context mContext;
    private FollowFragment myGroupFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtCancel, txtAccept;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            txtCancel = (TextView) rowView.findViewById(R.id.txtCancel);
            txtAccept = (TextView) rowView.findViewById(R.id.txtAccept);
        }
    }

    public FriendRequestRecyclerAdapter(Context context, List<HashMap<String, String>> mListPatient, FollowFragment myGroupFragment) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.myGroupFragment = myGroupFragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_request, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtName.setText(mListGroup.get(position).get("message"));

        holder.txtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGroupFragment.onAcceptRequestClick(position, mListGroup.get(position).get("from_user_id"), mListGroup.get(position).get("isEvent"));
            }
        });

        holder.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGroupFragment.onRejectRequestClick(position, mListGroup.get(position).get("from_user_id"), mListGroup.get(position).get("isEvent"));
            }
        });

    }

    public String getGroupId(int pos){
        return  mListGroup.get(pos).get("GroupId");
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }



}
