package com.carelynk.event.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carelynk.R;
import com.carelynk.dashboard.fragment.MyGroupFragment;
import com.carelynk.dashboard.model.GroupModel;
import com.carelynk.event.EventListActivity;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<String> mListGroup;
    private Context mContext;
    private EventListActivity eventListActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView txt_event_name;
        public ImageView img_edit, img_delete;

        public ViewHolder(View rowView) {
            super(rowView);
            cardView = (CardView) rowView.findViewById(R.id.cardView);
            txt_event_name = (TextView) rowView.findViewById(R.id.txt_event_name);
            img_edit = (ImageView) rowView.findViewById(R.id.img_edit);
            img_delete = (ImageView) rowView.findViewById(R.id.img_delete);
        }
    }

    public EventListAdapter(Context context, List<String> mListPatient, EventListActivity eventListActivity) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.eventListActivity = eventListActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_event, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //holder.txt_event_name.setText(mListGroup.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListActivity.onClickDetail(position);
            }
        });

        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListActivity.onEditClick(position);
            }
        });

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListActivity.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }


}
