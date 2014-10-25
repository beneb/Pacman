package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class PacMan {
    private static final int STOPPED = 0;
    private static final int HORIZONTAL_LEFT = 1;
    private static final int HORIZONTAL_RIGHT = 2;
    private static final int VERTICAL_UP = 3;
    private static final int VERTICAL_DOWN = 4;


    private int _move = STOPPED;

    private final Labyrinth _labyrinth;
    private float _radius;
    private Paint _paint;

    private float _x, _y;
    private float _direction = 1;
    private Rect _invalidateRect;

    public PacMan(Resources resources, Labyrinth labyrinth) {
        _labyrinth = labyrinth;
        _paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(resources.getColor(R.color.pacman));
    }

    public Rect getInvalidateRect() {
        return _invalidateRect;
    }

    public void init() {
        RectF bounds = _labyrinth.getBounds();
        _radius = _labyrinth.getCellSize() / 2;
        _x = bounds.centerX();
        _y = bounds.centerY();
    }

    public void draw(Canvas canvas) {
        RectF r = new RectF(_x - _radius, _y - _radius, _x + _radius, _y + _radius);
        canvas.drawArc(r, 30, 300, true, _paint);
    }

    /*public void move() {
        int newX = _x + 2 * _direction;
        int newY = _y + 2 * _direction;
        if (_bounds.contains(newX, newY)) {
            newInvalidateRect(newX, newY);
            _x = newX;
            _y = newY;
        } else {
            _direction = - _direction;
        }
    }*/

    public void move() {
        float newX = _x + 2 * _direction;
        float newY = _y + 2 * _direction;
        if (_move == STOPPED) {
            newInvalidateRect(newX, newY);
            return;
        }
        if (_move == HORIZONTAL_LEFT) {
            newX = _x - 2;
        }
        if (_move == HORIZONTAL_RIGHT) {
            newX = _x + 2;
        }
        if (_move == VERTICAL_UP) {
            newY = _y - 2;
        }
        if (_move == VERTICAL_DOWN) {
            newY = _y + 2;
        }

        if (canMove(newX, newY)) {
            newInvalidateRect(newX, newY);
            _x = newX;
            _y = newY;
        } else {
            _direction = -_direction;
        }
    }

    private boolean canMove(float newX, float newY) {
        return _labyrinth.canMove(newX, newY - _radius) &&
                _labyrinth.canMove(newX, newY + _radius) &&
                _labyrinth.canMove(newX - _radius, newY) &&
                _labyrinth.canMove(newX + _radius, newY);
    }

    private void newInvalidateRect(float newX, float newY) {
        int l = (int) (Math.min(_x, newX) - 2 * _radius);
        int t = (int) (Math.min(_y, newY) - 2 * _radius);
        int r = (int) (Math.max(_x, newX) + 2 * _radius + 1);
        int b = (int) (Math.max(_y, newY) + 2 * _radius + 1);
        _invalidateRect = new Rect(l, t, r, b);
    }

    public void go(float x_touched, float y_touched) {
        if (isHorizontal(x_touched, y_touched)) { // horizontal move
            if (x_touched < _x) {
                goLeft();
            } else {
                goRight();
            }
        } else { // vertical move
            if (y_touched < _y) {
                goUp();
            } else {
                goDown();
            }
        }
    }

    private void goDown() {
        _move = VERTICAL_DOWN;
    }

    private void goUp() {
        _move = VERTICAL_UP;
    }

    private void goRight() {
        _move = HORIZONTAL_RIGHT;
    }

    private void goLeft() {
        _move = HORIZONTAL_LEFT;
    }

    private boolean isHorizontal(float x_touched, float y_touched) {
        return Math.abs(x_touched - _x) < Math.abs(y_touched - _y);
    }
}