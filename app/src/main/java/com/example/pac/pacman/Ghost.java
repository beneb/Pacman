package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Arrays;
import java.util.Collection;

public class Ghost extends Character {

    public static Collection<Ghost> createGhosts(Resources resources, Labyrinth labyrinth) {
        return Arrays.asList(
                new Ghost(resources, labyrinth, Color.RED),
                new Ghost(resources, labyrinth, Color.CYAN),
                new Ghost(resources, labyrinth, Color.GRAY),
                new Ghost(resources, labyrinth, Color.MAGENTA));
    }

    private final Paint _foreground;

    private Ghost(Resources resources, Labyrinth labyrinth, int color) {
        super(labyrinth);
        _foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        _foreground.setStyle(Paint.Style.FILL);
        _foreground.setColor(color);
    }

    @Override
    public void init() {
        super.init();
        RectF bounds = _labyrinth.getBounds();
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
            _move = Direction.HorizontalLeft;
        } else if (rnd < .5) {
            _move = Direction.HorizontalRight;
        } else if (rnd < .75) {
            _move = Direction.VerticalUp;
        } else {
            _move = Direction.VerticalDown;
        }

        return super.move();
    }
}