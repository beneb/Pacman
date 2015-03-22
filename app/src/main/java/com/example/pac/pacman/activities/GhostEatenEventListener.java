package com.example.pac.pacman.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;

import com.example.pac.pacman.event.EventListener;

public abstract class GhostEatenEventListener<T> implements EventListener<T> {
    private static final int GHOST_SCORE_SHOW_DELAY = 1500;

    private final Activity _activity;

    protected GhostEatenEventListener(Activity activity) {
        _activity = activity;
    }

    public void walkBackDelayed(final Runnable runnable) {
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void run() {
                if (!_activity.isDestroyed()) {
                    runnable.run();
                }
            }
        }, GHOST_SCORE_SHOW_DELAY);
    }
}
