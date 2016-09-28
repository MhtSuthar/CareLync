package com.carelynk.recent.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.carelynk.R;
import com.carelynk.databinding.ListItemMyForumBinding;
import com.carelynk.databinding.ListItemTimelineBinding;
import com.carelynk.recent.model.TimelineModel;
import com.carelynk.utilz.CircleTransform;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class MyForumRecyclerAdapter extends RecyclerView.Adapter<MyForumRecyclerAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<TimelineModel> mListPatient;
    private Context mContext;
    private int lastPos = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemMyForumBinding binding;

        public ViewHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }
        public ListItemMyForumBinding getBinding() {
            return binding;
        }
    }

    public MyForumRecyclerAdapter(Context context, List<TimelineModel> mListPatient) {
        this.mListPatient = mListPatient;
        mContext = context;
    }

    public void setPatientList(List<TimelineModel> patientList){
        mListPatient.clear();
        mListPatient.addAll(patientList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_my_forum, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TimelineModel feedModel= mListPatient.get(position);
        holder.getBinding().setFeed(feedModel);
        holder.getBinding().executePendingBindings();
        //animateStackByStack(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mListPatient.size();
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
