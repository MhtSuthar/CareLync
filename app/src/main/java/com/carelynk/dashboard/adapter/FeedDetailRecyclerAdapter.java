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
import com.carelynk.dashboard.FeedDetailActivity;
import com.carelynk.dashboard.model.GoalListDetail;
import com.carelynk.dashboard.model.HealthFeedModel;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.CircleTransform;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class FeedDetailRecyclerAdapter extends RecyclerView.Adapter<FeedDetailRecyclerAdapter.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private View header;

    private List<GoalListDetail.GoalArray> mListPatient;
    private Context mContext;
    private FeedDetailActivity feedDetailActivity;
    private static final String TAG = "HealthFeedRecyclerAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtDesc, txt_comment;
        public TextView txtPostTime, txt_support, txtSeeMore, txtDescTemp;
        public ImageView imgCover, imgCamera, imgUser;
        public EditText edtComment;
        public CheckBox chkSupport;
        public ImageView btnSend;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            txtPostTime = (TextView) rowView.findViewById(R.id.txtPostTime);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            imgCover = (ImageView) rowView.findViewById(R.id.imgCover);
            imgCamera = (ImageView) rowView.findViewById(R.id.imgCamera);
            txt_comment = (TextView) rowView.findViewById(R.id.txt_comment);
            edtComment = (EditText) rowView.findViewById(R.id.edtComment);
            chkSupport = (CheckBox) rowView.findViewById(R.id.chkSupport);
            btnSend = (ImageView) rowView.findViewById(R.id.btnSend);
            txt_support = (TextView) rowView.findViewById(R.id.txt_support);
            imgUser = (ImageView) rowView.findViewById(R.id.imgUser);
            txtSeeMore = (TextView) rowView.findViewById(R.id.txtSeeMore);
            txtDescTemp = (TextView) rowView.findViewById(R.id.txtDescTemp);
        }
    }

    public FeedDetailRecyclerAdapter(View header, Context context, List<GoalListDetail.GoalArray> mListPatient, FeedDetailActivity feedDetailActivity, RecyclerView recyclerView) {
        if (header == null) {
            throw new IllegalArgumentException("header may not be null");
        }
        this.header = header;
        this.mListPatient = mListPatient;
        mContext = context;
        this.feedDetailActivity = feedDetailActivity;
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
        final GoalListDetail.GoalArray feedModel = mListPatient.get(position-1);
        holder.txtName.setText(feedModel.getUserName());
        holder.txt_comment.setText(feedModel.getCommentsCount()+" Comments");
        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedDetailActivity.openDashboard(feedModel.getUserId());
            }
        });
        holder.imgUser.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(AppUtils.getImagePath(feedModel.getProfilePicUrl())).
                apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_default_avatar)).into(holder.imgUser);

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

        holder.txtPostTime.setText("Created by "+feedModel.getUpdateDate());
        holder.txtDesc.setText(feedModel.getUpdatemsg());
        holder.txtDescTemp.setText(feedModel.getUpdatemsg());

        if(!TextUtils.isEmpty(feedModel.getPhotoURL()) && feedModel.getPhotoURL().contains("Content")) {
            holder.imgCover.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(AppUtils.getImagePath(feedModel.getPhotoURL())).
                    apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop()).into(holder.imgCover);
        }else
            holder.imgCover.setVisibility(View.GONE);

        holder.imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedDetailActivity.openImage(holder.imgCover, AppUtils.getImagePath(feedModel.getPhotoURL()));
            }
        });

        holder.txt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedDetailActivity.showCommnetDialog(feedModel.getUpdateId());
            }
        });

        holder.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedDetailActivity.openGalleryDialog();
            }
        });
        holder.txt_support.setText(feedModel.getSupportsCount()+" Supports");
        holder.chkSupport.setText(" "+feedModel.getSupportsCount()+" Supports");
        holder.chkSupport.setChecked(feedModel.getSelfSupportCount().equalsIgnoreCase("1") ? true : false);
        holder.chkSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                if(checkBox.isChecked()){
                    feedDetailActivity.supportGroupDiscuss(feedModel.getUpdateId());
                }else{
                    feedDetailActivity.supportGroupDiscuss(feedModel.getUpdateId());
                }
            }
        });

        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //TODO change comment and updated id
                    if (!feedModel.comment.equals(""))
                        feedDetailActivity.addCommentToGoalDiscuss(feedModel.comment, feedModel.getUpdateId());
                    else
                        Toast.makeText(mContext, "Please Enter", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mContext, "Please Enter", Toast.LENGTH_LONG).show();
                }
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
        return mListPatient.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

}
