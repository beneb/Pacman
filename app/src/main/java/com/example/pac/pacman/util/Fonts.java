package com.example.pac.pacman.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

public class Fonts {

    private static final String EMULOGIC_TTF = "emulogic.ttf";
    private static final String PACMAN_TTF = "pacfont.ttf";

    public static void setRegularFont(Activity activity, int textViewId) {
        setFont(activity, textViewId, EMULOGIC_TTF);
    }

    public static void setPacManFont(Activity activity, int textViewId) {
        setFont(activity, textViewId, PACMAN_TTF);
    }

    private static void setFont(Activity activity, int textViewId, String fontName) {
        TextView tv = (TextView) activity.findViewById(textViewId);
        tv.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/" + fontName));
    }
}
