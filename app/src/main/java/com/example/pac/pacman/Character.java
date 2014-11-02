package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Character {

    private static final int MOVE_DELTA = 2;

    public enum Direction {
        Stopped,
        Left,
        Right,
        Up,
        Down,
    }

    protected int _x, _y;
    protected int _size;
    protected Direction _wishDirection = Direction.Left;
    protected Direction _direction = Direction.Stopped;

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

        LogMove(newX, newY);

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

    private void LogMove(int newX, int newY) {
        Log.i("move", "_direction: " + _direction +
                    ", _wishDirection: " + _wishDirection +
                    ", canMove(): " + canMove(newX, newY));
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
