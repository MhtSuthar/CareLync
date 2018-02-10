package com.carelynk.utilz.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimeNewRomanBoldTextView extends TextView {

    public TimeNewRomanBoldTextView(Context context) {
        super(context);
        setFont();
    }

    public TimeNewRomanBoldTextView(Context context, AttributeSet set) {
        super(context, set);
        setFont();
    }

    public TimeNewRomanBoldTextView(Context context, AttributeSet set, int defaultStyle) {
        super(context, set, defaultStyle);
        setFont();
    }

    private void setFont() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/TimesNewRomanBold.ttf");
        setTypeface(typeface);
    }
}