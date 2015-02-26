package com.example.pac.pacman.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;

import com.example.pac.pacman.event.EventListener;

public abstract class LevelChangeEventListener<T> implements EventListener<T> {
    private static final int NEXT_LEVEL_SHOW_DELAY = 3000;

    private final Activity _activity;

    protected LevelChangeEventListener(Activity activity) {
        _activity = activity;
    }

    public void changeLevelDelayed(final Runnable runnable) {
        Handler levelCompleteDelayHandler = new Handler();
        levelCompleteDelayHandler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void run() {
                if (!_activity.isDestroyed()) {
                    runnable.run();
                }
            }
        }, NEXT_LEVEL_SHOW_DELAY);
    }
}
