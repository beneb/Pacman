package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;

public class Character extends GameObject {

    private String _name;
    private String _nickName;
    private int _typedColor;

    private final float MOVE_DELTA;
    protected Paint _foreground;

    public enum Direction {
        Stopped,
        Left,
        Right,
        Up,
        Down,
    }

    protected Direction _wishDirection = Direction.Left;
    protected Direction _direction = Direction.Stopped;

    public Character(Resources r, Labyrinth labyrinth, String name, String nickName, int typedColor) {
        super(labyrinth);
        _name = name;
        _nickName = nickName;
        _typedColor = typedColor;

        _foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        _foreground.setStyle(Paint.Style.FILL);
        _foreground.setColor(r.getColor(typedColor));

        final float MOVE_DELTA_DIP = 2.5f;
        MOVE_DELTA = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MOVE_DELTA_DIP, r.getDisplayMetrics());
        Log.i("Character", "MOVE_DELTA = " + MOVE_DELTA);
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

    public void SetColor(int color) {
        _foreground.setColor(color);
    }

    public void ResetColor() {
        _foreground.setColor(_typedColor);
    }
}
