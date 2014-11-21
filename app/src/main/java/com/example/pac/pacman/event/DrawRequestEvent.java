package com.example.pac.pacman.event;

import android.graphics.Canvas;

public class DrawRequestEvent {
    private Canvas _canvas;

    public Canvas getCanvas() {
        return _canvas;
    }

    public void setCanvas(Canvas _canvas) {
        this._canvas = _canvas;
    }
}

