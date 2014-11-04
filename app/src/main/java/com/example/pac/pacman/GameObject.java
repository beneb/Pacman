package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.Rect;

public class GameObject {

    public GameObject(Labyrinth labyrinth) {
        _labyrinth = labyrinth;
    }

    protected float _x, _y;
    protected int _size;
    protected final Labyrinth _labyrinth;

    public void init() {
        _size = _labyrinth.getCellSize()-2;
        newInvalidateRect(_x, _y);
    }

    private Rect _invalidateRect;

    protected void newInvalidateRect(float newX, float newY) {
        int l = (int)Math.min(_x, newX) - _size;
        int t = (int)Math.min(_y, newY) - _size;
        int r = (int)Math.max(_x, newX) + _size + 1;
        int b = (int)Math.max(_y, newY) + _size + 1;
        _invalidateRect = new Rect(l, t, r, b);
    }

    public Rect getInvalidateRect() {
        return _invalidateRect;
    }

    public void draw(Canvas canvas) {}
}
