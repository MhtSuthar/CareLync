package com.carelynk.event.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.event.fragment.EventViewMemberDialogFragment;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class ViewMemberEventListAdapter extends RecyclerView.Adapter<ViewMemberEventListAdapter.ViewHolder> {

    private List<HashMap<String, String>> mListGroup;
    private Context mContext;
    private EventViewMemberDialogFragment eventListActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_trending_name;

        public ViewHolder(View rowView) {
            super(rowView);
            txt_trending_name = (TextView) rowView.findViewById(R.id.txt_trending_name);
        }
    }

    public ViewMemberEventListAdapter(Context context, List<HashMap<String, String>> mListPatient, EventViewMemberDialogFragment eventListActivity) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.eventListActivity = eventListActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_trending, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txt_trending_name.setText(mListGroup.get(position).get("name"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListActivity.onClickDetail(mListGroup.get(position).get("user_id"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }


}
