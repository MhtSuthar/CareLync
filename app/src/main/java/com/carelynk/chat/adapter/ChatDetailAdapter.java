package com.carelynk.chat.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.chat.ChatDetailActivity;
import com.carelynk.chat.model.ChatDetailModel;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.AppUtils;
import com.carelynk.utilz.PrefUtils;

import java.text.ParseException;
import java.util.List;

/**
 * Created by ubuntu on 19/4/16.
 */
public class ChatDetailAdapter extends RecyclerView.Adapter<ChatDetailAdapter.ViewHolder> {

    private List<ChatDetailModel.Result> mListChat;
    private static final String TAG = "ChatDetailAdapter";
    private ChatDetailActivity chatDetailActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtLeft, txtRight, txtLeftTime, txtRightTime;
        public LinearLayout linLeft, linRight;
        public CardView cardViewLeft, cardViewRight;
        public ImageView imgRight, imgLeft;

        public ViewHolder(View rowView) {
            super(rowView);
            txtLeft = (TextView) rowView.findViewById(R.id.txtLeft);
            txtRight = (TextView) rowView.findViewById(R.id.txtRight);
            txtLeftTime = (TextView) rowView.findViewById(R.id.txtLeftTime);
            txtRightTime = (TextView) rowView.findViewById(R.id.txtRightTime);
            linLeft = (LinearLayout) rowView.findViewById(R.id.linLeft);
            linRight = (LinearLayout) rowView.findViewById(R.id.linRight);
            cardViewLeft = (CardView) rowView.findViewById(R.id.cardViewLeft);
            cardViewRight = (CardView) rowView.findViewById(R.id.cardViewRight);
            imgRight = (ImageView) rowView.findViewById(R.id.imgRight);
            imgLeft = (ImageView) rowView.findViewById(R.id.imgLeft);
        }
    }

    public ChatDetailAdapter(List<ChatDetailModel.Result> mListPatient, ChatDetailActivity eventListActivity) {
        this.mListChat = mListPatient;
        this.chatDetailActivity = eventListActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_chat_detail, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ChatDetailModel.Result chat = mListChat.get(position);
        if(SharedPreferenceUtil.getString(PrefUtils.PREF_USER_ID, "").equals(chat.getFromUserId())){
            //ME
            holder.linRight.setVisibility(View.VISIBLE);
            holder.linLeft.setVisibility(View.GONE);
            holder.txtRight.setText(chat.getMessage());
            holder.txtRightTime.setText(chat.getDate()+" "+chat.getTime());
            if(!TextUtils.isEmpty(chat.getFname())){
                holder.cardViewRight.setVisibility(View.VISIBLE);
                Glide.with(holder.cardViewRight.getContext()).load(AppUtils.getImagePathChat(chat.getFname())).
                        apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop()).into(holder.imgRight);

            }else{
                holder.cardViewRight.setVisibility(View.GONE);
            }

            holder.imgRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatDetailActivity.openImage(holder.imgRight, AppUtils.getImagePathChat(chat.getFname()));
                }
            });
        }else{
            //Samne wala
            holder.linRight.setVisibility(View.GONE);
            holder.linLeft.setVisibility(View.VISIBLE);
            holder.txtLeft.setText(chat.getMessage());
            holder.txtLeftTime.setText(chat.getDate()+" "+chat.getTime());
            if(!TextUtils.isEmpty(chat.getFname())){
                holder.cardViewLeft.setVisibility(View.VISIBLE);
                Glide.with(holder.cardViewLeft.getContext()).load(AppUtils.getImagePathChat(chat.getFname())).
                        apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop()).into(holder.imgLeft);

            }else{
                holder.cardViewLeft.setVisibility(View.GONE);
            }

            holder.imgLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatDetailActivity.openImage(holder.imgLeft, AppUtils.getImagePathChat(chat.getFname()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mListChat.size();
    }


}
