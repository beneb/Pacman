package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class Ghost extends Character {

    private MoveStrategy _moveStrategy;

    protected Ghost(Resources resources, Labyrinth labyrinth, int color, String name, String nickName, MoveStrategy moveStrategy) {
        super(resources, labyrinth, name, nickName, color);
        _foreground.setColor(color);
        _moveStrategy = moveStrategy;
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
        _wishDirection = _moveStrategy.GetNextDirection(this);
        return super.move();
    }
}