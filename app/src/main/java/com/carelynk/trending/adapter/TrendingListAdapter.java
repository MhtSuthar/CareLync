package com.carelynk.trending.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.event.EventListActivity;
import com.carelynk.trending.TrendingListActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class TrendingListAdapter extends RecyclerView.Adapter<TrendingListAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<HashMap<String, String>> mListGroup;
    private Context mContext;
    private TrendingListActivity eventListActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_trending_name;

        public ViewHolder(View rowView) {
            super(rowView);
            txt_trending_name = (TextView) rowView.findViewById(R.id.txt_trending_name);
        }
    }

    public TrendingListAdapter(Context context, List<HashMap<String, String>> mListPatient, TrendingListActivity eventListActivity) {
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
        holder.txt_trending_name.setText("* "+mListGroup.get(position).get("GoalName"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListActivity.onClickDetail(mListGroup.get(position).get("GoalId"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }


}
