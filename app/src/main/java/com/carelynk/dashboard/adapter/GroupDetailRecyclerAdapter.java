package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.carelynk.R;
import com.carelynk.dashboard.GroupDetailActivity;
import com.carelynk.dashboard.model.GroupDiscussModel;
import com.carelynk.dashboard.model.HealthFeedModel;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class GroupDetailRecyclerAdapter extends RecyclerView.Adapter<GroupDetailRecyclerAdapter.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private View header;

    private List<GroupDiscussModel.Result> mListGroup;
    private Context mContext;
    private GroupDetailActivity feedDetailActivity;
    private static final String TAG = "HealthFeedRecyclerAdapter";
    private int mFromGroup = -1;
    private boolean isEditable;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView txtName, txtDesc, txt_comment, txt_support;
        public TextView txtPostTime;
        public ImageView imgCover;
        public EditText edtComment;
        public AppCompatButton btnSend;
        public CheckBox chkSupport;



        public ViewHolder(View rowView) {
            super(rowView);
            cardView = (CardView) rowView.findViewById(R.id.cardView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            txtPostTime = (TextView) rowView.findViewById(R.id.txtPostTime);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            imgCover = (ImageView) rowView.findViewById(R.id.imgCover);
            txt_comment = (TextView) rowView.findViewById(R.id.txt_comment);
            txt_support = (TextView) rowView.findViewById(R.id.txt_support);
            btnSend = (AppCompatButton) rowView.findViewById(R.id.btnSend);
            edtComment = (EditText) rowView.findViewById(R.id.edtComment);
            chkSupport = (CheckBox) rowView.findViewById(R.id.chkSupport);
        }
    }

    public GroupDetailRecyclerAdapter(View header, Context context, List<GroupDiscussModel.Result> mListPatient,
                                      GroupDetailActivity feedDetailActivity, int mFromWhich, boolean equals) {
        if (header == null) {
            throw new IllegalArgumentException("header may not be null");
        }
        this.header = header;
        this.mListGroup = mListPatient;
        mContext = context;
        this.feedDetailActivity = feedDetailActivity;
        this.isEditable = equals;
        this.mFromGroup = mFromWhich;
    }

    public boolean isHeader(int position) {
        return position == 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return new ViewHolder(header);
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_feed_detail, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (isHeader(position)) {
            return;
        }
        final GroupDiscussModel.Result feedModel = mListGroup.get(position-1);
        holder.txtName.setText(feedModel.getUserName());
        if(!isEditable && mFromGroup == 2){
            holder.edtComment.setEnabled(false);
            holder.edtComment.setFocusable(false);
        }
        holder.edtComment.setText(feedModel.comment);
        holder.edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                feedModel.comment = s.toString();
                //Log.e(TAG, "onTextChanged: "+s.toString()+"    "+feedModel.comment);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.txtPostTime.setText("Created by "+feedModel.getCreatedDate());
        holder.txtDesc.setText(feedModel.getUpdatemsg());
        holder.txt_comment.setText(""+feedModel.getComments()+" Comments");
        holder.txt_support.setText(""+feedModel.getSupports()+" Support");

        if(feedModel.getPhotoURL() != null && !feedModel.getPhotoURL().equals("") && !feedModel.getPhotoURL().equals("null")) {
            holder.imgCover.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(R.drawable.ic_placeholder)
                    .into(holder.imgCover);
        }else
            holder.imgCover.setVisibility(View.GONE);

        holder.txt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(feedModel.getComments()) > 0)
                    feedDetailActivity.showCommnetDialog(feedModel.getGroupUpdateId());
            }
        });

        holder.chkSupport.setChecked(feedModel.getSelfSupport().equalsIgnoreCase("1") ? true : false);
        holder.chkSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                if(checkBox.isChecked()){
                    feedModel.isSupport = false;
                    feedDetailActivity.supportGroupDiscuss(feedModel.getGroupUpdateId(), feedModel.getGroupUserId());
                }else{
                    feedModel.isSupport = true;
                    feedDetailActivity.unSupportGroupDiscuss(feedModel.getGroupUpdateId(), feedModel.getGroupUserId());
                }
                //notifyDataSetChanged();
            }
        });

        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!feedModel.comment.equals(""))
                        feedDetailActivity.addCommentToGroupDiscuss(feedModel.comment, feedModel.getGroupUpdateId());
                    else
                        Toast.makeText(mContext, "Please Enter", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mContext, "Please Enter", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListGroup.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

}
