package com.example.pac.pacman.event;

import android.graphics.RectF;

public class InitEvent {
    private final RectF _bounds;

    public InitEvent(RectF bounds) {
        _bounds = bounds;
    }

    public RectF getBounds() {
        return _bounds;
    }
}

