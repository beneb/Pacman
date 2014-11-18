package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.RectF;

public class Ghost extends Character {

    protected Ghost(String name, String nickName, int color, IMoveStrategy moveStrategy, Labyrinth labyrinth) {
        super(name, nickName, moveStrategy, labyrinth);
        _foreground.setColor(color);
    }

    @Override
    public void init() {
        super.init();
        RectF bounds = _labyrinth.getBounds();
        _x = bounds.left + _labyrinth.getCellSize() + _labyrinth.getCellSize()/2;
        _y = bounds.top  + _labyrinth.getCellSize() + _labyrinth.getCellSize()/2;
    }

    @Override
    public void draw(Canvas canvas) {
        float radius = _size/2;
        RectF r = new RectF(_x - radius, _y - radius, _x + radius, _y + radius);
        canvas.drawRect(r, _foreground);
        super.draw(canvas);
    }
}
