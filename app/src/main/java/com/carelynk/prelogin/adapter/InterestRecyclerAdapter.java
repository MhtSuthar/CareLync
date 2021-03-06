package com.carelynk.prelogin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carelynk.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class InterestRecyclerAdapter extends RecyclerView.Adapter<InterestRecyclerAdapter.ViewHolder> {

    private List<HashMap<String, String>> mListPatient;
    private Context mContext;
    private boolean[] mSelectInterest;

    public boolean isSelectedInterest() {
        int mCount = 0;
        for (int i = 0; i < mSelectInterest.length; i++) {
            if(mSelectInterest[i]){
                mCount = mCount + 1;
            }
        }
        if(mCount >= 3)
            return true;
        else
            return false;
    }

    public String getInterestArea() {
        String mInterest = "";
        for (int i = 0; i < mSelectInterest.length; i++) {
            if(mSelectInterest[i]){
                if(mInterest.equals("")){
                    mInterest = mListPatient.get(i).get("interest_area");
                }else{
                    mInterest = mInterest+","+mListPatient.get(i).get("interest_area");
                }
            }
        }
        return mInterest;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtInterest;
        ImageView imgSelect;
        public ViewHolder(View rowView) {
            super(rowView);
            txtInterest = (TextView) rowView.findViewById(R.id.txtInterest);
            imgSelect = (ImageView) rowView.findViewById(R.id.imgSelect);
        }
    }

    public InterestRecyclerAdapter(Context context, List<HashMap<String, String>> mListPatient) {
        this.mListPatient = mListPatient;
        mContext = context;
        mSelectInterest = new boolean[mListPatient.size()];
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_interest, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtInterest.setText(mListPatient.get(position).get("interest_area"));
        holder.imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectInterest[position]){
                    mSelectInterest[position] = false;
                    notifyDataSetChanged();
                }else{
                    mSelectInterest[position] = true;
                    notifyDataSetChanged();
                }
            }
        });

        if(mSelectInterest[position]){
            holder.txtInterest.setTextColor(Color.WHITE);
            holder.imgSelect.setBackgroundResource(R.drawable.circle_red_bg);
        }else{
            holder.txtInterest.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            holder.imgSelect.setBackgroundResource(R.drawable.circle_blue_trasparent_bg);
        }
    }

    @Override
    public int getItemCount() {
        return mListPatient.size();
    }


}
