package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.dashboard.fragment.MyGroupFragment;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class GroupFollowedRecyclerAdapter extends RecyclerView.Adapter<GroupFollowedRecyclerAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<String> mListGroup;
    private Context mContext;
    private MyGroupFragment myGroupFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtGroupName;
        public TextView txtCreatedName;
        public ImageView imgGroup;


        public ViewHolder(View rowView) {
            super(rowView);
            txtGroupName = (TextView) rowView.findViewById(R.id.txtGroupName);
            txtCreatedName = (TextView) rowView.findViewById(R.id.txtCreatedName);
            imgGroup = (ImageView) rowView.findViewById(R.id.imgGroup);
        }
    }

    public GroupFollowedRecyclerAdapter(Context context, List<String> mListPatient, MyGroupFragment myGroupFragment) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.myGroupFragment = myGroupFragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_follow, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //final GroupModel feedModel= mListGroup.get(position);

       /* holder.txtGroupName.setText(feedModel.GroupName);
        holder.txtCreatedName.setText("Created by ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGroupFragment.onItemClick(position);
            }
        });

        if(feedModel.PhotoURL != null && !feedModel.PhotoURL.equals("") && !feedModel.PhotoURL.equals("null")) {
            holder.imgGroup.setVisibility(View.VISIBLE);
            Log.e("Image", "onBindVieold http://www.demo.carelynk.com/Content"+feedModel.PhotoURL.
                    split("Content")[1]);
            Glide.with(mContext).load("http://www.demo.carelynk.com/Content"+feedModel.PhotoURL.split("Content")[1]).into(holder.imgGroup);
        }else
            holder.imgGroup.setVisibility(View.GONE);*/
    }

    @Override
    public int getItemCount() {
        return 3;
    }



}
