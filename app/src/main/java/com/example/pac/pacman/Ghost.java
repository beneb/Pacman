package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Ghost extends Character {

    private IMoveStrategy _moveStrategy;

    protected Ghost(int foreground, String name, String nickName, IMoveStrategy moveStrategy, Labyrinth labyrinth) {
        super(name, nickName, labyrinth);
        _foreground.setColor(foreground);
        _moveStrategy = moveStrategy;
    }

    @Override
    public void init() {
        super.init();
        Labyrinth lab = GameEnv.getInstance().getLabyrinth();
        Rect bounds = lab.getBounds();
        _x = bounds.left + lab.getCellSize() + lab.getCellSize() / 2;
        _y = bounds.top + lab.getCellSize() + lab.getCellSize() / 2;
        _foreground.setTextSize(_size);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText("G", _x, _y, _foreground);
        super.draw(canvas);
    }

    @Override
    public void move() {
        Direction direction = _moveStrategy.getNextDirection(_x, _y);
        super.move(direction);
    }
}