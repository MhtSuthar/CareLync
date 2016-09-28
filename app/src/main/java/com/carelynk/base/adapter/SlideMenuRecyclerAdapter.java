package com.carelynk.base.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carelynk.R;
import com.carelynk.base.fragment.SidebarFragment;
import com.carelynk.databinding.ListItemSlideMenuBinding;

/**
 * Created by ubuntu on 19/4/16.
 */
public class SlideMenuRecyclerAdapter extends RecyclerView.Adapter<SlideMenuRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private SidebarFragment sidebarFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemSlideMenuBinding binding;

        public ViewHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }
        public ListItemSlideMenuBinding getBinding() {
            return binding;
        }
    }

    public SlideMenuRecyclerAdapter(Context context, SidebarFragment sidebarFragment) {
        mContext = context;
        this.sidebarFragment = sidebarFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_slide_menu, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        switch (position){
            case 0:
                holder.getBinding().txtMenu.setText("Home");
                holder.getBinding().imgMenu.setImageResource(R.drawable.ic_home);
                break;
            case 1:
                holder.getBinding().txtMenu.setText("My Timeline");
                holder.getBinding().imgMenu.setImageResource(R.drawable.ic_timeline);
                break;
            case 2:
                holder.getBinding().txtMenu.setText("My Profile");
                holder.getBinding().imgMenu.setImageResource(R.drawable.ic_profile);
                break;
            case 3:
                holder.getBinding().txtMenu.setText("Search");
                holder.getBinding().imgMenu.setImageResource(R.drawable.ic_search);
                break;
            case 4:
                holder.getBinding().txtMenu.setText("About Us");
                holder.getBinding().imgMenu.setImageResource(R.drawable.ic_about);
                break;
        }
        holder.getBinding().linMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sidebarFragment.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }



}
