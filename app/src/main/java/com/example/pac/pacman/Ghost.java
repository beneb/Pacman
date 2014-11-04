package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class Ghost extends Character {

    protected Ghost(Resources resources, Labyrinth labyrinth, int color, String name, String nickName) {
        super(resources, labyrinth, name, nickName, color);
    }

    @Override
    public void init() {
        super.init();
        Rect bounds = _labyrinth.getBounds();
        _x = bounds.centerX() - 2 * _size;
        _y = bounds.centerY() - 3 * _size;
        _foreground.setTextSize(_size);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText("G", _x, _y, _foreground);
    }

    @Override
    public boolean move() {
        double rnd = Math.random();
        if (rnd < .25) {
            _wishDirection = Direction.Left;
        } else if (rnd < .5) {
            _wishDirection = Direction.Right;
        } else if (rnd < .75) {
            _wishDirection = Direction.Up;
        } else {
            _wishDirection = Direction.Down;
        }

        return super.move();
    }
}