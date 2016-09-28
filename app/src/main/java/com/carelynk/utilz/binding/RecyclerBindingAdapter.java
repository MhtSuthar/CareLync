package com.carelynk.utilz.binding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.AbstractList;
import java.util.ArrayList;

public class RecyclerBindingAdapter<T>
        extends RecyclerView.Adapter<RecyclerBindingAdapter.BindingHolder> {
    private int holderLayout, variableId;
    private AbstractList<T> items = new ArrayList<>();
    private OnItemClickListener<T> onItemClickListener;
    private int lastPos = 0;

    public RecyclerBindingAdapter(int holderLayout, int variableId, AbstractList<T> items) {
        this.holderLayout = holderLayout;
        this.variableId = variableId;
        this.items = items;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(holderLayout, parent, false);
        return new BindingHolder(v);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        final T item = items.get(position);
        holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(position, item);
            }
        });
        holder.getBinding().setVariable(variableId, item);
        animateStackByStack(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T item);
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public BindingHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    private void animateStackByStack(View view, final int pos) {
        if(pos > lastPos) {
            view.animate().cancel();
            view.setTranslationY(100);
            view.setAlpha(0);
            view.animate().alpha(1.0f).translationY(0).setDuration(300).setStartDelay(100);
            lastPos = pos;
        }
    }
}