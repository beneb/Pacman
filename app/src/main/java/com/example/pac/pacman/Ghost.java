package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.Rect;

public class Ghost extends Character {

    private IMoveStrategy _moveStrategy;

    protected Ghost(int color, String name, String nickName, Labyrinth labyrinth, IMoveStrategy moveStrategy) {
        super(name, nickName, labyrinth);
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
    public void move() {
        Direction direction = _moveStrategy.getNextDirection(_x, _y);
        super.move(direction);
    }
}