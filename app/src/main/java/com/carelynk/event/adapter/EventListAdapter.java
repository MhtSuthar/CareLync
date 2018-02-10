package com.carelynk.event.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.dashboard.fragment.MyGroupFragment;
import com.carelynk.dashboard.model.GroupModel;
import com.carelynk.event.EventListActivity;
import com.carelynk.event.model.EventList;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.CircleTransform;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<EventList.Result> mListGroup;
    private Context mContext;
    private EventListActivity eventListActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtGroupName, txt_edit, txt_delete;
        public TextView txtCreatedName;
        public ImageView imgGroup;
        public TextView txtDesc, txtTime;
        public RelativeLayout rel_footer;

        public ViewHolder(View rowView) {
            super(rowView);
            txtGroupName = (TextView) rowView.findViewById(R.id.txtGroupName);
            rel_footer = (RelativeLayout) rowView.findViewById(R.id.rel_footer);
            txt_edit = (TextView) rowView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) rowView.findViewById(R.id.txt_delete);
            txtCreatedName = (TextView) rowView.findViewById(R.id.txtCreatedName);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            imgGroup = (ImageView) rowView.findViewById(R.id.imgGroup);
            txtTime = (TextView) rowView.findViewById(R.id.txtTime);
        }
    }

    public EventListAdapter(Context context, List<EventList.Result> mListPatient, EventListActivity eventListActivity) {
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
        holder.txtGroupName.setText(mListGroup.get(position).getEventName());
        if(!TextUtils.isEmpty(mListGroup.get(position).getEventDesc()))
            holder.txtDesc.setText(mListGroup.get(position).getEventDesc());
        else
            holder.txtDesc.setVisibility(View.GONE);
        holder.txtCreatedName.setText(mListGroup.get(position).getAddress());
        holder.txtTime.setText(mListGroup.get(position).getEventDateFrom()+" "+mListGroup.get(position).getEventTimeFrom()+" To "+
                mListGroup.get(position).getEventDateTo()+" "+mListGroup.get(position).getEventTimeTo());

        if(!TextUtils.isEmpty(mListGroup.get(position).getPhotoURL())) {
            holder.imgGroup.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(AppUtils.getImagePath(mListGroup.get(position).getPhotoURL())).
                    apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_launcher_black)).into(holder.imgGroup);
        }else
            holder.imgGroup.setVisibility(View.GONE);

        if(mListGroup.get(position).getIsPrivate().equalsIgnoreCase("False")){
            holder.rel_footer.setVisibility(View.GONE);
        }else{
            holder.rel_footer.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListActivity.onClickDetail(position);
            }
        });

        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListActivity.onEditClick(position);
            }
        });

        holder.txt_delete.setOnClickListener(new View.OnClickListener() {
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
