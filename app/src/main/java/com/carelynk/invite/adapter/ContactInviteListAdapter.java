package com.carelynk.invite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.carelynk.R;
import com.carelynk.invite.ContactInviteActivity;
import com.carelynk.invite.InviteActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class ContactInviteListAdapter extends RecyclerView.Adapter<ContactInviteListAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<HashMap<String, String>> mListGroup;
    private Context mContext;
    private ContactInviteActivity eventListActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtInvite, txtNomber;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            txtInvite = (TextView) rowView.findViewById(R.id.txtInvite);
            txtNomber = (TextView) rowView.findViewById(R.id.txtNomber);
        }
    }

    public ContactInviteListAdapter(Context context, List<HashMap<String, String>> mListPatient, ContactInviteActivity eventListActivity) {
        this.mListGroup = mListPatient;
        mContext = context;
        this.eventListActivity = eventListActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_contact, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtName.setText(mListGroup.get(position).get("UserName"));
        holder.txtNomber.setText(mListGroup.get(position).get("Mobile"));

        holder.txtInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListActivity.onClickDetail(mListGroup.get(position).get("Mobile"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListGroup.size();
    }

    public boolean getSelectedCount(){
        for (int i = 0; i < mListGroup.size(); i++) {
            if(mListGroup.get(i).get("IsSelect").equals("1"))
                return true;
        }
        return false;
    }

    public List<HashMap<String, String>> getAllData(){
        return mListGroup;
    }


}
