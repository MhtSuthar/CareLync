package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.dashboard.fragment.PeopleMayKnowDialogFragment;
import com.carelynk.event.fragment.EventViewMemberDialogFragment;
import com.carelynk.utilz.AppUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class PeopleMayKnowListAdapter extends RecyclerView.Adapter<PeopleMayKnowListAdapter.ViewHolder> {

    private List<HashMap<String, String>> mListGroup;
    private Context mContext;
    private PeopleMayKnowDialogFragment peopleMayKnowDialogFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public ImageView img;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            img = (ImageView) rowView.findViewById(R.id.img);
        }
    }

    public PeopleMayKnowListAdapter(Context context, List<HashMap<String, String>> mListPatient, PeopleMayKnowDialogFragment eventListActivity) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.peopleMayKnowDialogFragment = eventListActivity;
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
        holder.txtName.setText(mListGroup.get(position).get("name"));
        Glide.with(mContext).load(AppUtils.getImagePath(mListGroup.get(position).get("profile"))).
                apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy)).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peopleMayKnowDialogFragment.onClickDetail(mListGroup.get(position).get("user_id"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }


}
