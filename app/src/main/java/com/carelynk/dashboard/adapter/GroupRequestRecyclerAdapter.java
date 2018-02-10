package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.dashboard.GroupRequestActivity;
import com.carelynk.dashboard.model.ReuestGroupModel;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class GroupRequestRecyclerAdapter extends RecyclerView.Adapter<GroupRequestRecyclerAdapter.ViewHolder> {

    private List<ReuestGroupModel.Result> mListGroup;
    private Context mContext;
    private GroupRequestActivity myGroupFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtCancel, txtAccept;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            txtCancel = (TextView) rowView.findViewById(R.id.txtCancel);
            txtAccept = (TextView) rowView.findViewById(R.id.txtAccept);
        }
    }

    public GroupRequestRecyclerAdapter(Context context, List<ReuestGroupModel.Result> mListPatient, GroupRequestActivity myGroupFragment) {
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
        holder.txtName.setText(mListGroup.get(position).getMessage());

        holder.txtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGroupFragment.onAcceptRequestClick(position, mListGroup.get(position));
            }
        });

        holder.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGroupFragment.onRejectRequestClick(position, mListGroup.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }



}
