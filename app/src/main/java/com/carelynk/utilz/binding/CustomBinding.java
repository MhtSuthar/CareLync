package com.carelynk.utilz.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carelynk.R;
import com.carelynk.utilz.CircleTransform;

/**
 * Created by ubuntu on 19/7/16.
 */
public class CustomBinding {

    private static final String TAG = "CustomBinding";

    @BindingAdapter({"imageUrl"})
    public static void setImage(ImageView imageView, Drawable url){
        Glide.with(imageView.getContext()).load(R.drawable.ic_default_avatar).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    @BindingAdapter({"imageResource"})
    public static void setImageResource(ImageView imageView, int drawable){
        imageView.setImageResource(drawable);
    }

    @BindingAdapter({"font"})
    public static void setFont(TextView textView, String fontName) {
        FontCache fontCache = new FontCache(textView.getContext());
        textView.setTypeface(fontCache.get(fontName));
    }

    @BindingAdapter({"font_checkbox"})
    public static void setFont(CheckBox checkBox, String fontName) {
        FontCache fontCache = new FontCache(checkBox.getContext());
        checkBox.setTypeface(fontCache.get(fontName));
    }

}
