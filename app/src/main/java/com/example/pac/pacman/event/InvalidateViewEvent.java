package com.example.pac.pacman.event;

import android.graphics.Rect;

public class InvalidateViewEvent {
    private Rect _rect;

    public InvalidateViewEvent(Rect rect) {
        _rect = rect;
    }

    public Rect GetRect() {
        return _rect;
    }
}
