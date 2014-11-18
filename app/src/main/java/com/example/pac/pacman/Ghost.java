package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.RectF;

public abstract class Ghost extends Character {

    protected Ghost(int color, IMoveStrategy moveStrategy, Labyrinth labyrinth) {
        super(moveStrategy, labyrinth);
        _foreground.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        float radius = _size/2;
        RectF r = new RectF(_x - radius, _y - radius, _x + radius, _y + radius);
        canvas.drawRect(r, _foreground);
        super.draw(canvas);
    }
}
