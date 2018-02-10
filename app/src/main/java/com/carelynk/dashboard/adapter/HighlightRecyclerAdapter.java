package com.carelynk.dashboard.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Build;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.dashboard.fragment.HighlightFragment;
import com.carelynk.dashboard.model.HighlightModel;
import com.carelynk.dashboard.model.HighliteCommonModel;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.CircleTransform;
import com.carelynk.utilz.ImageGetterAsyncTask;

import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class HighlightRecyclerAdapter extends RecyclerView.Adapter<HighlightRecyclerAdapter.ViewHolder> {

    private static final int ANIM_DURATION = 300;
    private List<HighliteCommonModel> mListPatient;
    private Context mContext;
    private int lastPos = 0;
    private HighlightFragment highlightFragment;
    private RecyclerView recyclerView;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName, txtPostTime, txtDesc, txtFollow, txtTitle, txtSeeMore, txtDescTemp;
        public ImageView imgCover, imgUser;
        public CheckBox chkFav;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName= (TextView) rowView.findViewById(R.id.txtName);
            txtTitle = (TextView) rowView.findViewById(R.id.txtTitle);
            txtPostTime = (TextView) rowView.findViewById(R.id.txtPostTime);
            txtDesc= (TextView) rowView.findViewById(R.id.txtDesc);
            txtFollow= (TextView) rowView.findViewById(R.id.txtFollow);
            imgCover= (ImageView) rowView.findViewById(R.id.imgCover);
            chkFav = (CheckBox) rowView.findViewById(R.id.chkFav);
            imgUser = (ImageView) rowView.findViewById(R.id.imgUser);
            txtSeeMore = (TextView) rowView.findViewById(R.id.txtSeeMore);
            txtDescTemp = (TextView) rowView.findViewById(R.id.txtDescTemp);
        }

    }

    public HighlightRecyclerAdapter(Context context, List<HighliteCommonModel> mListPatient, HighlightFragment highlightFragment, RecyclerView recyclerView) {
        this.mListPatient = mListPatient;
        mContext = context;
        this.highlightFragment = highlightFragment;
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_heighlight, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final HighliteCommonModel feedModel= mListPatient.get(position);
        holder.txtName.setText(""+feedModel.created_date);
        holder.txtPostTime.setText(getTypes(feedModel.type)+" "+feedModel.userName);
        holder.txtTitle.setText(Html.fromHtml(feedModel.title));
        if(!TextUtils.isEmpty(feedModel.description)) {
            holder.txtDesc.setVisibility(View.VISIBLE);
            holder.txtDescTemp.setVisibility(View.VISIBLE);
            holder.txtSeeMore.setVisibility(View.VISIBLE);
            Spanned spanned = Html.fromHtml(feedModel.description.replace("@", ""),
                    new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String source) {
                            LevelListDrawable d = new LevelListDrawable();
                            Drawable empty = mContext.getResources().getDrawable(R.drawable.ic_placeholder);
                            d.addLevel(0, 0, empty);
                            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                            new ImageGetterAsyncTask(mContext, source, d).execute(holder.txtDesc);
                            return d;
                        }
                    }, null);
            holder.txtDesc.setText(spanned);
            holder.txtDescTemp.setText(spanned);
        } else {
            holder.txtDesc.setVisibility(View.GONE);
            holder.txtDescTemp.setVisibility(View.GONE);
            holder.txtSeeMore.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(feedModel.image) && feedModel.image.contains("Content")) {
            holder.imgCover.setVisibility(View.VISIBLE);
            holder.txtName.setTag(AppUtils.getImagePath(feedModel.image));
            Glide.with(mContext).load(AppUtils.getImagePath(feedModel.image)).
                    apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop()).into(holder.imgCover);

        }else
            holder.imgCover.setVisibility(View.GONE);
        holder.imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightFragment.onItemClickImage(holder.txtName.getTag().toString(), holder.imgCover);
            }
        });

        Glide.with(mContext).load(AppUtils.getImagePath(feedModel.profilePic)).
                apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy)).into(holder.imgUser);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    highlightFragment.onItemClick(feedModel);
            }
        });

        holder.txtPostTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightFragment.onClickUserDetail(feedModel.userId);
            }
        });

        holder.txtDesc.setVisibility(View.GONE);

        if(holder.txtDesc.isShown()){
            holder.txtSeeMore.setText("Less");
        }else{
            holder.txtSeeMore.setText("See More");
        }

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

                    TransitionManager.beginDelayedTransition(recyclerView, transition);
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

    private String getTypes(int type) {
        switch (type){
            case 0:
                return "Health Feed Created By";
            case 1:
                return "Health Feed Posted By";
            case 2:
                return "Group Created By";
            case 3:
                return "Group Posted By";
            case 4:
                return "Event Created By";
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return mListPatient.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        //holder.itemView.clearAnimation();
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
