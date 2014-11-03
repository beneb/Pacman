package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;

public class Character {

    private final float MOVE_DELTA;

    public enum Direction {
        Stopped,
        Left,
        Right,
        Up,
        Down,
    }

    protected float _x, _y;
    protected int _size;
    protected Direction _wishDirection = Direction.Left;
    protected Direction _direction = Direction.Stopped;

    protected final Labyrinth _labyrinth;

    public Character(Resources r, Labyrinth labyrinth) {
        _labyrinth = labyrinth;

        final float MOVE_DELTA_DIP = 2.5f;
        MOVE_DELTA = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MOVE_DELTA_DIP, r.getDisplayMetrics());
        Log.i("Character", "MOVE_DELTA = " + MOVE_DELTA);
    }

    public void init() {
        _size = _labyrinth.getCellSize()-2;
        newInvalidateRect(_x, _y);
    }

    private Rect _invalidateRect;

    public Rect getInvalidateRect() {
        return _invalidateRect;
    }

    public boolean move() {
        float newX = _x;
        float newY = _y;

        switch (_wishDirection) {
            case Stopped:
                break;
            case Left:
                newX = _x - MOVE_DELTA;
                break;
            case Right:
                newX = _x + MOVE_DELTA;
                break;
            case Up:
                newY = _y - MOVE_DELTA;
                break;
            case Down:
                newY = _y + MOVE_DELTA;
                break;
        }


        if (!canMove(newX, newY)) {
            // reset new values
            newX = _x;
            newY = _y;

            switch (_direction) {
                case Stopped:
                    break;
                case Left:
                    newX = _x - MOVE_DELTA;
                    break;
                case Right:
                    newX = _x + MOVE_DELTA;
                    break;
                case Up:
                    newY = _y - MOVE_DELTA;
                    break;
                case Down:
                    newY = _y + MOVE_DELTA;
                    break;
            }
        } else {
            _direction = _wishDirection;
        }

        if (_wishDirection != Direction.Stopped && canMove(newX, newY)) {
            if (newX > _labyrinth.getBounds().right) {
                newX = _labyrinth.getBounds().left;
            }
            if (newX < _labyrinth.getBounds().left) {
                newX = _labyrinth.getBounds().right;
            }

            newInvalidateRect(newX, newY);
            _x = newX;
            _y = newY;
            return true;
        } else {
            return false;
        }
    }

    protected boolean canMove(float newX, float newY) {
        float radius = _size/2;
        return _labyrinth.canMove(newX - radius, newY - radius) &&
                _labyrinth.canMove(newX + radius, newY - radius) &&
                _labyrinth.canMove(newX + radius, newY + radius) &&
                _labyrinth.canMove(newX - radius, newY + radius);
    }

    protected void newInvalidateRect(float newX, float newY) {
        int l = (int)Math.min(_x, newX) - _size;
        int t = (int)Math.min(_y, newY) - _size;
        int r = (int)Math.max(_x, newX) + _size + 1;
        int b = (int)Math.max(_y, newY) + _size + 1;
        _invalidateRect = new Rect(l, t, r, b);
    }

    public void draw(Canvas canvas) {}
}
