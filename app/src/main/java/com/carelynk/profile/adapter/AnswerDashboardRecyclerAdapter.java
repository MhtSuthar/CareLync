package com.carelynk.profile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.profile.fragment.AnswersFragment;
import com.carelynk.profile.model.DashboardModel;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class AnswerDashboardRecyclerAdapter extends RecyclerView.Adapter<AnswerDashboardRecyclerAdapter.ViewHolder> {

    private List<DashboardModel.AnswerArray> mList;
    private AnswersFragment mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtDesc;
        public ImageView mImage;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName= (TextView) rowView.findViewById(R.id.txtName);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            mImage= (ImageView) rowView.findViewById(R.id.img);
        }

    }

    public AnswerDashboardRecyclerAdapter(AnswersFragment context, List<DashboardModel.AnswerArray> mListPatient) {
        this.mList = mListPatient;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_article_dashboard, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtName.setText(Html.fromHtml(mList.get(position).getGoalName()));
        holder.txtDesc.setVisibility(View.VISIBLE);
        holder.txtDesc.setText(Html.fromHtml(mList.get(position).getDesc()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.onClick(mList.get(position).getGoalId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
