package com.example.pac.pacman.event;

import android.graphics.Rect;

public class InvalidateRectInViewEvent {
    private Rect _rect;

    public InvalidateRectInViewEvent(Rect rect) {
        _rect = rect;
    }

    public Rect GetRect() {
        return _rect;
    }
}
