package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class Ghost extends Character {

    private IMoveStrategy _moveStrategy;

    protected Ghost(Resources resources, Labyrinth labyrinth, int color, String name, String nickName, IMoveStrategy moveStrategy) {
        super(resources, labyrinth, name, nickName, color);
        _foreground.setColor(color);
        _moveStrategy = moveStrategy;
    }

    @Override
    public void init() {
        super.init();
        Rect bounds = _labyrinth.getBounds();
        _x = bounds.left + _labyrinth.getCellSize() + _labyrinth.getCellSize()/2;
        _y = bounds.top  + _labyrinth.getCellSize() + _labyrinth.getCellSize()/2;
        _foreground.setTextSize(_size);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText("G", _x, _y, _foreground);
    }

    @Override
    public boolean move() {
        _wishDirection = _moveStrategy.getNextDirection(_x, _y);
        return super.move();
    }
}