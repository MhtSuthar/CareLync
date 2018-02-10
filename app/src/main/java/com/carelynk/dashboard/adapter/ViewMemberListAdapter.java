package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.dashboard.ViewMemberActivity;
import com.carelynk.dashboard.model.ViewMemberModel;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.trending.TrendingListActivity;
import com.carelynk.utilz.PrefUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class ViewMemberListAdapter extends RecyclerView.Adapter<ViewMemberListAdapter.ViewHolder> {

    private List<ViewMemberModel.Result> mListGroup;
    private Context mContext;
    private ViewMemberActivity eventListActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtUnfollow;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            txtUnfollow = (TextView) rowView.findViewById(R.id.txtUnfollow);
        }
    }

    public ViewMemberListAdapter(Context context, List<ViewMemberModel.Result> mListPatient, ViewMemberActivity eventListActivity) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.eventListActivity = eventListActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_group_view_member, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ViewMemberModel.Result data = mListGroup.get(position);
        holder.txtName.setText(data.getUserName());

        if(data.getAdmin().equalsIgnoreCase("false") &&
                data.getUserId().equals(SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, ""))){
            holder.txtUnfollow.setVisibility(View.VISIBLE);
        }else
            holder.txtUnfollow.setVisibility(View.GONE);

        holder.txtUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListActivity.onUnfollow(data);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListActivity.onClickDetail(mListGroup.get(position).getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }


}
