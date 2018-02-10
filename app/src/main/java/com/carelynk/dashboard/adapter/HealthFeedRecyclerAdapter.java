package com.carelynk.dashboard.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.dashboard.fragment.HealthFeedsFragment;
import com.carelynk.dashboard.model.HealthFeedModel;
import com.carelynk.dashboard.model.HighlightModel;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.CircleTransform;
import com.carelynk.utilz.ImageGetterAsyncTask;
import com.carelynk.utilz.MyTagHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ubuntu on 19/4/16.
 */
public class HealthFeedRecyclerAdapter extends RecyclerView.Adapter<HealthFeedRecyclerAdapter.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private View header;

    private static final int ANIM_DURATION = 300;
    private List<HighlightModel> mListPatient;
    private Context mContext;
    private int lastPos = 0;
    private HealthFeedsFragment myForumFragment;
    private static final String TAG = "HealthFeedRecyclerAdapter";
    private RecyclerView recyclerView;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtGoalName, txtDesc, txtSeeMore, txtDescTemp, txtExpert,
                txtDescTempHidden, txtPlayVideo, txtNameOne, txtNameTwo, txtNameThree, txtSeeAll,
                txtSpam, txtShare;
        public TextView txtCreatedName, txtAnswer, txtSupport;
        public ImageView imgGroup, imgUser, imgUserOne, imgUserTwo, imgUserThree;
        public LinearLayout mLinWeb, linPeople, linUserOne, linUserTwo, linUserThree;
        public WebView mWebView;
        public RecyclerView mRecyclerPeople;

        public ViewHolder(View rowView) {
            super(rowView);
            txtGoalName = (TextView) rowView.findViewById(R.id.txtGoalName);
            txtAnswer = (TextView) rowView.findViewById(R.id.txtAnswer);
            txtSupport = (TextView) rowView.findViewById(R.id.txtSupport);
            txtCreatedName = (TextView) rowView.findViewById(R.id.txtCreatedName);
            txtDesc = (TextView) rowView.findViewById(R.id.txtDesc);
            txtSpam = (TextView) rowView.findViewById(R.id.txtSpam);
            txtShare = (TextView) rowView.findViewById(R.id.txtShare);
            imgGroup = (ImageView) rowView.findViewById(R.id.imgGroup);
            imgUser = (ImageView) rowView.findViewById(R.id.imgUser);
            txtSeeMore = (TextView) rowView.findViewById(R.id.txtSeeMore);
            txtExpert = (TextView) rowView.findViewById(R.id.txtExpert);
            txtDescTemp = (TextView) rowView.findViewById(R.id.txtDescTemp);
            mLinWeb = (LinearLayout) rowView.findViewById(R.id.linWeb);
            mWebView = (WebView) rowView.findViewById(R.id.webView);
            txtDescTempHidden = (TextView) rowView.findViewById(R.id.txtDescTempHidden);
            txtPlayVideo = (TextView) rowView.findViewById(R.id.txtPlayVideo);

            linPeople = (LinearLayout) rowView.findViewById(R.id.linPeople);
            imgUserOne = (ImageView) rowView.findViewById(R.id.imgUserOne);
            imgUserTwo = (ImageView) rowView.findViewById(R.id.imgUserTwo);
            imgUserThree = (ImageView) rowView.findViewById(R.id.imgUserThree);
            txtNameOne = (TextView) rowView.findViewById(R.id.txtNameOne);
            txtNameTwo = (TextView) rowView.findViewById(R.id.txtNameTwo);
            txtNameThree = (TextView) rowView.findViewById(R.id.txtNameThree);
            txtSeeAll= (TextView) rowView.findViewById(R.id.txtSeeAll);
            linUserOne = (LinearLayout) rowView.findViewById(R.id.linUserOne);
            linUserTwo = (LinearLayout) rowView.findViewById(R.id.linUserTwo);
            linUserThree = (LinearLayout) rowView.findViewById(R.id.linUserThree);
            mRecyclerPeople = (RecyclerView) rowView.findViewById(R.id.recycler_view_people);
        }
    }

    public HealthFeedRecyclerAdapter(View header, Context context, List<HighlightModel> mListPatient, HealthFeedsFragment myForumFragment, RecyclerView recyclerView) {
        if (header == null) {
            throw new IllegalArgumentException("header may not be null");
        }
        this.header = header;
        this.mListPatient = mListPatient;
        mContext = context;
        this.myForumFragment = myForumFragment;
        this.recyclerView = recyclerView;
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
                .inflate(R.layout.list_item_my_health_feed, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (isHeader(position)) {
            return;
        }
        final HighlightModel feedModel = mListPatient.get(position - 1);
        holder.txtGoalName.setText(Html.fromHtml(feedModel.GoalName));
        holder.txtCreatedName.setText(feedModel.UserName);
        holder.txtExpert.setText(feedModel.Expertise);
        holder.txtSupport.setText("" + feedModel.SupportCount + " Supports");

        holder.txtDescTempHidden.setText(feedModel.Desc);
        if (!TextUtils.isEmpty(feedModel.Desc)) {
            holder.txtAnswer.setText("" + feedModel.AnswerCount + " Comments");
            holder.txtDesc.setVisibility(View.VISIBLE);
            holder.txtDescTemp.setVisibility(View.VISIBLE);
            holder.txtSeeMore.setVisibility(View.VISIBLE);

            Spanned spanned = Html.fromHtml(feedModel.Desc,
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
                    }, new MyTagHandler());
            holder.txtDesc.setText(spanned);
            holder.txtDesc.setMovementMethod(LinkMovementMethod.getInstance());
            holder.txtDescTemp.setText(spanned);
        } else {
            holder.txtSeeMore.setVisibility(View.GONE);
            holder.txtDesc.setVisibility(View.GONE);
            holder.txtDescTemp.setVisibility(View.GONE);
            holder.txtAnswer.setText("" + feedModel.AnswerCount + " Answers");
        }

        if (!TextUtils.isEmpty(feedModel.PhotoURL) && feedModel.PhotoURL.contains("Content")) {
            holder.imgGroup.setVisibility(View.VISIBLE);
            holder.txtGoalName.setTag(AppUtils.getImagePath(feedModel.PhotoURL));
            Glide.with(mContext).load(AppUtils.getImagePath(feedModel.PhotoURL)).
                    apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop()).into(holder.imgGroup);

        } else if(feedModel.Desc.contains("<img")){
            String imgRegex = "<[iI][mM][gG][^>]+[sS][rR][cC]\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
            Pattern p = Pattern.compile(imgRegex);
            Matcher m = p.matcher(feedModel.Desc);
            if (m.find()) {
                String imgSrc = m.group(1);
                holder.imgGroup.setVisibility(View.VISIBLE);
                holder.txtGoalName.setTag(imgSrc);
                Glide.with(mContext).load(imgSrc).
                        apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop()).into(holder.imgGroup);
            }else
                holder.imgGroup.setVisibility(View.GONE);
        }else
            holder.imgGroup.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(feedModel.ProfilePicUrl) && feedModel.ProfilePicUrl.contains("Content")) {
            Glide.with(mContext).load(AppUtils.getImagePath(feedModel.ProfilePicUrl)).
                    apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy)).into(holder.imgUser);
        }else{
            Glide.with(mContext).load(R.drawable.ic_user_dummy).
                    apply(RequestOptions.circleCropTransform()).into(holder.imgUser);
        }

        holder.txtGoalName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myForumFragment.onItemClick(position - 1);
            }
        });

        holder.imgGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myForumFragment.onItemClickImage(holder.txtGoalName.getTag().toString(), holder.imgGroup);
            }
        });

        holder.txtCreatedName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myForumFragment.onClickUserDetail(feedModel.UserId);
            }
        });

        holder.txtDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myForumFragment.onItemClick(position - 1);
            }
        });

        holder.txtDescTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myForumFragment.onItemClick(position - 1);
            }
        });

        holder.txtAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myForumFragment.onItemClick(position - 1);
            }
        });

        holder.txtSpam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myForumFragment.onSpamCall(feedModel);
            }
        });

        holder.txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myForumFragment.onShareClick(feedModel);
            }
        });

        holder.txtPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myForumFragment.onPlayVideo(feedModel.VideoUrl);
            }
        });

        holder.txtDesc.setVisibility(View.GONE);

        if (holder.txtDesc.isShown()) {
            holder.txtSeeMore.setText("Less");
        } else {
            holder.mLinWeb.setVisibility(View.GONE);
            holder.txtSeeMore.setText("See More");
        }

        if (TextUtils.isEmpty(feedModel.VideoUrl))
            holder.txtPlayVideo.setVisibility(View.GONE);
        else
            holder.txtPlayVideo.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(feedModel.Expertise))
            holder.txtExpert.setVisibility(View.GONE);
        else
            holder.txtExpert.setVisibility(View.VISIBLE);

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
                        if (holder.txtDescTempHidden.getText().toString().contains("<iframe")) {
                            holder.mLinWeb.setVisibility(View.VISIBLE);
                            holder.mWebView.getSettings().setJavaScriptEnabled(true);
                            holder.mWebView.setWebChromeClient(new WebChromeClient() {
                            });
                            String mLink = holder.txtDescTempHidden.getText().toString().split("<iframe")[1].split("</iframe>")[0];
                            String mFull = "<iframe " + mLink + "</iframe>";
                            Matcher matcher = Pattern.compile("src=\"([^\"]+)\"").matcher(mFull);
                            matcher.find();
                            String src = matcher.group(1);
                            holder.mWebView.loadData(AppUtils.getYoutubeUrl(src), "text/html", "utf-8");
                        } else {
                            holder.mLinWeb.setVisibility(View.GONE);
                        }
                        holder.txtSeeMore.setText("Less");

                        if (!TextUtils.isEmpty(feedModel.PhotoURL) && feedModel.PhotoURL.contains("Content")) {}
                        else if(feedModel.Desc.contains("<img")){
                            holder.imgGroup.setVisibility(View.GONE);
                        }
                    } else {
                        holder.mLinWeb.setVisibility(View.GONE);
                        holder.txtDescTemp.setVisibility(View.VISIBLE);
                        holder.txtDesc.setVisibility(View.GONE);
                        if (!TextUtils.isEmpty(feedModel.PhotoURL) && feedModel.PhotoURL.contains("Content")) {
                            holder.imgGroup.setVisibility(View.VISIBLE);
                        }
                        else if(feedModel.Desc.contains("<img")){
                            holder.imgGroup.setVisibility(View.VISIBLE);
                        }
                        holder.txtSeeMore.setText("See More");
                    }

                    TransitionManager.beginDelayedTransition(recyclerView, transition);
                } else {
                    if (shouldExpand) {
                        holder.mLinWeb.setVisibility(View.VISIBLE);
                        holder.txtDescTemp.setVisibility(View.GONE);
                        holder.txtDesc.setVisibility(View.VISIBLE);
                        holder.txtSeeMore.setText("Less");
                    } else {
                        holder.mLinWeb.setVisibility(View.GONE);
                        holder.txtDescTemp.setVisibility(View.VISIBLE);
                        holder.txtDesc.setVisibility(View.GONE);
                        holder.txtSeeMore.setText("See More");
                    }
                }
                holder.txtSeeMore.setActivated(shouldExpand);
            }
        });

        if (position % 10 == 0 && feedModel.PepPleMayKnow.length() > 0) {
            holder.linPeople.setVisibility(View.VISIBLE);
            final JSONArray jsonArray = feedModel.PepPleMayKnow;

            holder.mRecyclerPeople.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            holder.mRecyclerPeople.setAdapter(new PeopleMayKnowRecyclerAdapter(myForumFragment, jsonArray));
            holder.txtSeeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myForumFragment.onSeeAllClick(feedModel);
                }
            });
            /* try {
                if (jsonArray.length() > 0) {
                    holder.linUserOne.setVisibility(View.VISIBLE);
                    holder.txtNameOne.setText(jsonArray.getJSONObject(0).getString("UserName"));
                    Glide.with(mContext).load(AppUtils.getImagePath(jsonArray.getJSONObject(0).getString("ProfilePicUrl"))).
                            apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy)).into(holder.imgUserOne);
                } else {
                    holder.linUserOne.setVisibility(View.GONE);
                }

                if (jsonArray.length() > 1) {
                    holder.linUserTwo.setVisibility(View.VISIBLE);
                    holder.txtNameTwo.setText(jsonArray.getJSONObject(1).getString("UserName"));
                    Glide.with(mContext).load(AppUtils.getImagePath(jsonArray.getJSONObject(1).getString("ProfilePicUrl"))).
                            apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy)).into(holder.imgUserTwo);
                } else {
                    holder.linUserTwo.setVisibility(View.GONE);
                }

                if (jsonArray.length() > 2) {
                    holder.linUserThree.setVisibility(View.VISIBLE);
                    holder.txtNameThree.setText(jsonArray.getJSONObject(2).getString("UserName"));
                    Glide.with(mContext).load(AppUtils.getImagePath(jsonArray.getJSONObject(2).getString("ProfilePicUrl"))).
                            apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_user_dummy)).into(holder.imgUserThree);
                } else {
                    holder.linUserThree.setVisibility(View.GONE);
                }

                holder.linUserOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            myForumFragment.onClickUserDetail(jsonArray.getJSONObject(0).getString("FromUserID"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                holder.linUserTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            myForumFragment.onClickUserDetail(jsonArray.getJSONObject(1).getString("FromUserID"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                holder.linUserThree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            myForumFragment.onClickUserDetail(jsonArray.getJSONObject(2).getString("FromUserID"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                holder.txtSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myForumFragment.onSeeAllClick(feedModel);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        } else {
            holder.linPeople.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mListPatient.size() + 1;
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    private void animateStackByStack(View view, final int pos) {
        if (pos > lastPos) {
            view.animate().cancel();
            view.setTranslationY(50);
            view.setAlpha(0);
            view.animate().alpha(1.0f).translationY(0).setDuration(ANIM_DURATION).setStartDelay(100);
            lastPos = pos;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }


}
