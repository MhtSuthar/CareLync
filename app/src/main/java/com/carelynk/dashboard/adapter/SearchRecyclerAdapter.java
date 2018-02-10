package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.dashboard.SearchActivity;
import com.carelynk.dashboard.model.SearchData;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.CircleTransform;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    private List<SearchData.Group> mSearchGroup;
    private List<SearchData.Goal> mSearchFeed;
    private List<SearchData.UserProfile> mSearchUser;
    private Context mContext;
    private SearchActivity searchActivity;

    public SearchRecyclerAdapter(Context context, List<SearchData.Goal> goals, SearchActivity searchActivity, int u) {
        this.mSearchFeed = goals;
        mContext = context;
        this.searchActivity = searchActivity;
    }

    public SearchRecyclerAdapter(Context context, List<SearchData.UserProfile> userProfiles, SearchActivity searchActivity, String f) {
        this.mSearchUser = userProfiles;
        mContext = context;
        this.searchActivity = searchActivity;
    }

    public SearchRecyclerAdapter(Context context, List<SearchData.Group> mListPatient, SearchActivity searchActivity) {
        this.mSearchGroup = mListPatient;
        mContext = context;
        this.searchActivity = searchActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public ImageView imgSearch;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txt_name);
            imgSearch = (ImageView) rowView.findViewById(R.id.imgSearch);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(mSearchGroup != null){
            holder.txtName.setText(Html.fromHtml(mSearchGroup.get(position).getName()));
            holder.imgSearch.setVisibility(View.GONE);
        }
        else if(mSearchFeed != null){
            holder.txtName.setText(Html.fromHtml(mSearchFeed.get(position).getName()));
            holder.imgSearch.setVisibility(View.GONE);
        }
        else if(mSearchUser != null){
            holder.txtName.setText(mSearchUser.get(position).getName());
            Glide.with(mContext).load(AppUtils.getImagePath(mSearchUser.get(position).getProfilePicUrl()))
                    .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy)).into(holder.imgSearch);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSearchGroup != null){
                    searchActivity.onClick(mSearchGroup.get(position).getID(), 0);
                }
                else if(mSearchFeed != null){
                    searchActivity.onClick(mSearchFeed.get(position).getID(), 1);
                }
                else if(mSearchUser != null){
                    searchActivity.onClick(mSearchUser.get(position).getID(), 2);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mSearchGroup != null)
            return mSearchGroup.size();
        else if(mSearchFeed != null)
            return mSearchFeed.size();
        else if(mSearchUser != null)
            return mSearchUser.size();
        return 0;
    }

}
