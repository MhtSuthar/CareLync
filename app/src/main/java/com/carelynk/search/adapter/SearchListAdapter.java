package com.carelynk.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.search.MySearchActivity;
import com.carelynk.search.model.SearchModel;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private List<SearchModel.Result> mListGroup;
    private Context mContext;
    private MySearchActivity searchActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSearch;
        public ViewHolder(View rowView) {
            super(rowView);
            txtSearch = (TextView) rowView.findViewById(R.id.txtSearch);
        }
    }

    public SearchListAdapter(Context context, List<SearchModel.Result> mListPatient, MySearchActivity myGroupFragment) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.searchActivity = myGroupFragment;
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
        //final GroupModel feedModel= mListGroup.get(position);

        holder.txtSearch.setText(mListGroup.get(position).getName());
        //holder.txtCreatedName.setText("Created by ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchActivity.onSearchItemClick(mListGroup.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }



}
