package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.Rect;

public class Character {

    private static final int MOVE_DELTA = 2;

    public enum Direction {
        Stopped,
        HorizontalLeft,
        HorizontalRight,
        VerticalUp,
        VerticalDown,
    }

    protected int _x, _y;
    protected int _size;
    protected Direction _move = Direction.Stopped;

    protected final Labyrinth _labyrinth;

    public Character(Labyrinth labyrinth) {
        _labyrinth = labyrinth;
    }

    public void init() {
        _size = _labyrinth.getCellSize()-4;
        newInvalidateRect(_x, _y);
    }

    private Rect _invalidateRect;

    public Rect getInvalidateRect() {
        return _invalidateRect;
    }

    public boolean move() {
        int newX = _x;
        int newY = _y;

        switch (_move) {
            case Stopped:
                break;
            case HorizontalLeft:
                newX = _x - MOVE_DELTA;
                break;
            case HorizontalRight:
                newX = _x + MOVE_DELTA;
                break;
            case VerticalUp:
                newY = _y - MOVE_DELTA;
                break;
            case VerticalDown:
                newY = _y + MOVE_DELTA;
                break;
        }

        if (_move != Direction.Stopped && canMove(newX, newY)) {
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
        return _labyrinth.canMove(newX, newY - radius) &&
                _labyrinth.canMove(newX, newY + radius) &&
                _labyrinth.canMove(newX - radius, newY) &&
                _labyrinth.canMove(newX + radius, newY);
    }

    protected void newInvalidateRect(int newX, int newY) {
        int l = Math.min(_x, newX) - _size;
        int t = Math.min(_y, newY) - _size;
        int r = Math.max(_x, newX) + _size + 1;
        int b = Math.max(_y, newY) + _size + 1;
        _invalidateRect = new Rect(l, t, r, b);
    }

    public void draw(Canvas canvas) {}
}
