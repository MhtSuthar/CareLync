package com.carelynk.utilz.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimeNewRomanTextView extends TextView {

    public TimeNewRomanTextView(Context context) {
        super(context);
        setFont();
    }

    public TimeNewRomanTextView(Context context, AttributeSet set) {
        super(context, set);
        setFont();
    }

    public TimeNewRomanTextView(Context context, AttributeSet set, int defaultStyle) {
        super(context, set, defaultStyle);
        setFont();
    }

    private void setFont() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/TimesNewRoman.ttf");
        setTypeface(typeface);
    }
}