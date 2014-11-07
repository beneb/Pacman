package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class Ghost extends Character {

    private IMoveStrategy _moveStrategy;

    protected Ghost(int color, String name, String nickName, IMoveStrategy moveStrategy) {
        super(name, nickName, color);
        _foreground.setColor(color);
        _moveStrategy = moveStrategy;
    }

    @Override
    public void init() {
        super.init();
        Labyrinth lab = GameEnv.getInstance().getLabyrinth();
        Rect bounds = lab.getBounds();
        _x = bounds.left + lab.getCellSize() + lab.getCellSize()/2;
        _y = bounds.top  + lab.getCellSize() + lab.getCellSize()/2;
        _foreground.setTextSize(_size);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText("G", _x, _y, _foreground);
    }

    @Override
    public boolean move() {
        Direction direction = _moveStrategy.getNextDirection(_x, _y);
        return super.move(direction);
    }
}