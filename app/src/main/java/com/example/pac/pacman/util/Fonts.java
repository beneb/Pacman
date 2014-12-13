package com.example.pac.pacman.util;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Fonts {

    private static final String EMULOGIC_TTF = "emulogic.ttf";
    private static final String PACMAN_TTF = "pacfont.ttf";

    public static void setRegularFont(Activity activity, int textViewId) {
        setTypeface(activity, (TextView) activity.findViewById(textViewId), EMULOGIC_TTF);
    }

    public static void setRegularFont(Fragment fragment, int textViewId) {
        setFont(fragment, textViewId, EMULOGIC_TTF);
    }

    public static void setPacManFont(Activity activity, int textViewId) {
        setTypeface(activity, (TextView) activity.findViewById(textViewId), PACMAN_TTF);
    }

    private static void setFont(Fragment fragment, int textViewId, String fontName) {
        View fragmentView = fragment.getView();
        if (null == fragmentView) {
            Log.e(Fonts.class.getSimpleName(), "fragmentView is null. Font will not be set");
            return;
        }
        setTypeface(fragment.getActivity(),
                (TextView) fragmentView.findViewById(textViewId),
                fontName);
    }

    private static void setTypeface(Activity activity, TextView tv, String fontName) {
        tv.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/" + fontName));
    }
}
