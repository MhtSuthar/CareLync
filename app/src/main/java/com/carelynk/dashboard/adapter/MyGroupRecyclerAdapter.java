package com.carelynk.dashboard.adapter;

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
import com.carelynk.recent.model.GroupModel;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class MyGroupRecyclerAdapter extends RecyclerView.Adapter<MyGroupRecyclerAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<GroupModel> mListGroup;
    private Context mContext;
    private int lastPos = 0;
    private MyGroupFragment myGroupFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView txtGroupName;
        public TextView txtCreatedName;
        public ImageView imgGroup;
        public TextView txtDesc;


        public ViewHolder(View rowView) {
            super(rowView);
            cardView = (CardView) rowView.findViewById(R.id.cardView);
            txtGroupName = (TextView) rowView.findViewById(R.id.txtGroupName);
            txtCreatedName = (TextView) rowView.findViewById(R.id.txtCreatedName);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            imgGroup = (ImageView) rowView.findViewById(R.id.imgGroup);
        }
    }

    public MyGroupRecyclerAdapter(Context context, List<GroupModel> mListPatient, MyGroupFragment myGroupFragment) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.myGroupFragment = myGroupFragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_my_group, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GroupModel feedModel= mListGroup.get(position);

        holder.txtGroupName.setText(feedModel.GroupName);
        holder.txtCreatedName.setText("Created by ");
        holder.txtDesc.setText(""+feedModel.Description);

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
            holder.imgGroup.setVisibility(View.GONE);

        animateStackByStack(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    private void animateStackByStack(View view, final int pos) {
        if(pos > lastPos) {
            view.animate().cancel();
            view.setTranslationY(50);
            view.setAlpha(0);
            view.animate().alpha(1.0f).translationY(0).setDuration(ANIM_DURATION).setStartDelay(100);
            lastPos = pos;
        }
    }

}
