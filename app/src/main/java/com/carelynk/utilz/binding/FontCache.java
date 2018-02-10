package com.carelynk.utilz.binding;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FontCache {

    private String TAG = "FontCache";
    private final String FONT_DIR = "fonts";
    private Map<String, Typeface> cache = new HashMap<>();
    private static Map<String, String> fontMapping = new HashMap<>();
    private Context mContext;

    public FontCache(Context context){
        if(mContext == null) {
            mContext = context;
            fontCache();
        }
    }

    public static void addFont(String name, String fontFilename) {
        fontMapping.put(name, fontFilename);
    }

    private void fontCache() {
        AssetManager am = mContext.getResources().getAssets();
        String fileList[];
        try {
            fileList = am.list(FONT_DIR);
        } catch (IOException e) {
            Log.e(TAG, "Error loading fonts from assets/fonts.");
            return;
        }

        for (String filename : fileList) {
            if(!filename.equals("ReadMe")) {
                String alias = filename.substring(0, filename.lastIndexOf('.'));
                fontMapping.put(alias, filename);
                fontMapping.put(alias.toLowerCase(), filename);
            }
        }
    }

    public Typeface get(String fontName) {
        String fontFilename = fontMapping.get(fontName);
        if (fontFilename == null) {
            Log.e(TAG, "Couldn't find font " + fontName + ". Maybe you need to call addFont() first?");
            return null;
        }
        if (cache.containsKey(fontName)) {
            return cache.get(fontName);
        } else {
            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), FONT_DIR + "/" + fontFilename);
            cache.put(fontFilename, typeface);
            return typeface;
        }
    }
}
