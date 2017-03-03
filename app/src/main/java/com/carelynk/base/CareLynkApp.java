package com.carelynk.base;

import android.app.Application;
import android.os.SystemClock;

import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.binding.FontCache;

import java.util.concurrent.TimeUnit;

/**
 * Created by Admin on 15-Sep-16.
 */
public class CareLynkApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferenceUtil.init(this);

         /*
        *  Add Font using binding
        *
         */
        FontCache.addFont("bold","TimesNewRomanBold.ttf");
        FontCache.addFont("light","TimesNewRoman.ttf");
        FontCache.addFont("medium","TimesNewRoman.ttf");
    }
}
