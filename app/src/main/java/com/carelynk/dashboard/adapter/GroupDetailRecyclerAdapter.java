package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.os.Build;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
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
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.dashboard.GroupDetailActivity;
import com.carelynk.dashboard.model.GroupDiscussModel;
import com.carelynk.dashboard.model.HealthFeedModel;
import com.carelynk.utilz.AppUtils;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class GroupDetailRecyclerAdapter extends RecyclerView.Adapter<GroupDetailRecyclerAdapter.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private View header;

    private List<GroupDiscussModel.GroupDatum> mListGroup;
    private Context mContext;
    private GroupDetailActivity feedDetailActivity;
    private static final String TAG = "HealthFeedRecyclerAdapter";
    private int mFromGroup = -1;
    private boolean isEditable;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtDesc, txt_comment, txt_support;
        public TextView txtPostTime, txtSeeMore, txtDescTemp;
        public ImageView imgCover, imgCamera, imgUser;
        public EditText edtComment;
        public ImageView btnSend;
        public CheckBox chkSupport;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            txtPostTime = (TextView) rowView.findViewById(R.id.txtPostTime);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            imgCover = (ImageView) rowView.findViewById(R.id.imgCover);
            txt_comment = (TextView) rowView.findViewById(R.id.txt_comment);
            txt_support = (TextView) rowView.findViewById(R.id.txt_support);
            btnSend = (ImageView) rowView.findViewById(R.id.btnSend);
            edtComment = (EditText) rowView.findViewById(R.id.edtComment);
            chkSupport = (CheckBox) rowView.findViewById(R.id.chkSupport);
            imgCamera = (ImageView) rowView.findViewById(R.id.imgCamera);
            imgUser = (ImageView) rowView.findViewById(R.id.imgUser);
            txtSeeMore = (TextView) rowView.findViewById(R.id.txtSeeMore);
            txtDescTemp = (TextView) rowView.findViewById(R.id.txtDescTemp);
        }
    }

    public GroupDetailRecyclerAdapter(View header, Context context, List<GroupDiscussModel.GroupDatum> mListPatient,
                                      GroupDetailActivity feedDetailActivity, int mFromWhich) {
        if (header == null) {
            throw new IllegalArgumentException("header may not be null");
        }
        this.header = header;
        this.mListGroup = mListPatient;
        mContext = context;
        this.feedDetailActivity = feedDetailActivity;
        //this.isEditable = equals;
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
        final GroupDiscussModel.GroupDatum feedModel = mListGroup.get(position-1);
        holder.txtName.setText(feedModel.getUserName());

        holder.edtComment.setText(feedModel.comment);
        holder.edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                feedModel.comment = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        holder.txtPostTime.setText(""+feedModel.getUpdateDate());
        holder.txtDesc.setText(feedModel.getUpdatemsg());
        holder.txtDescTemp.setText(feedModel.getUpdatemsg());
        holder.txt_comment.setText(""+feedModel.getComments()+" Discussions");
        holder.txt_support.setText(""+feedModel.getSupports()+" Support");

        holder.imgUser.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(AppUtils.getImagePath(feedModel.getGroupUserProfilePic())).
                apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_default_avatar)).into(holder.imgUser);
        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedDetailActivity.openDashboard(feedModel.getGroupUsersId());
            }
        });

        if(!TextUtils.isEmpty(feedModel.getUpdatePhotoURL()) && feedModel.getUpdatePhotoURL().contains("Content")) {
            holder.imgCover.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(AppUtils.getImagePath(feedModel.getUpdatePhotoURL())).
                    apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop()).into(holder.imgCover);

        }else
            holder.imgCover.setVisibility(View.GONE);

        holder.imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedDetailActivity.openImage(holder.imgCover, AppUtils.getImagePath(feedModel.getUpdatePhotoURL()));
            }
        });

        holder.txt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(Integer.parseInt(feedModel.getComments()) > 0)
                    feedDetailActivity.showCommnetDialog(feedModel.getGroupUpdateId());
            }
        });

        holder.chkSupport.setChecked(feedModel.getSelfSupport().equalsIgnoreCase("1") ? true : false);
        holder.chkSupport.setText(" "+feedModel.getSupports()+" Support");
        holder.chkSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                if(checkBox.isChecked()){
                    feedDetailActivity.supportGroupDiscuss(feedModel.getGroupUpdateId(), feedModel.getGroupUserId());
                }else{
                    feedDetailActivity.supportGroupDiscuss(feedModel.getGroupUpdateId(), feedModel.getGroupUserId());
                }
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

        holder.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedDetailActivity.openGalleryDialog();

            }
        });

        holder.txtSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean shouldExpand = holder.txtDesc.getVisibility() == View.GONE;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ChangeBounds transition = new ChangeBounds();
                    transition.setDuration(125);

                    if (shouldExpand) {
                        holder.txtDescTemp.setVisibility(View.GONE);
                        holder.txtDesc.setVisibility(View.VISIBLE);
                        holder.txtSeeMore.setText("Less");
                    } else {
                        holder.txtDescTemp.setVisibility(View.VISIBLE);
                        holder.txtDesc.setVisibility(View.GONE);
                        holder.txtSeeMore.setText("See More");
                    }

                    TransitionManager.beginDelayedTransition(feedDetailActivity.binding.recyclerView, transition);
                }else{
                    if (shouldExpand) {
                        holder.txtDescTemp.setVisibility(View.GONE);
                        holder.txtDesc.setVisibility(View.VISIBLE);
                        holder.txtSeeMore.setText("Less");
                    } else {
                        holder.txtDescTemp.setVisibility(View.VISIBLE);
                        holder.txtDesc.setVisibility(View.GONE);
                        holder.txtSeeMore.setText("See More");
                    }
                }
                holder.txtSeeMore.setActivated(shouldExpand);
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
