package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public abstract class Character {

    private String _name;
    private String _nickName;

    private final Paint _debugPaint;

    protected float _moveDelta;
    protected Paint _foreground;

    protected float _x, _y;
    protected float _size;

    // NOTE: Test-induces design damage
    public PointF getPosition() {
        return new PointF(_x, _y);
    }

    private Rect _invalidateRect;

    public Rect getInvalidateRect() {
        return _invalidateRect;
    }

    protected final Labyrinth _labyrinth;

    private Direction _newDirection = Direction.Stopped;
    private Direction _direction = Direction.Stopped;

    public Character(String name, String nickName, Labyrinth labyrinth) {
        _labyrinth = labyrinth;

        _name = name;
        _nickName = nickName;

        _foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        _foreground.setStyle(Paint.Style.FILL);

        _debugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _debugPaint.setStyle(Paint.Style.STROKE);
        _debugPaint.setColor(Color.RED);
    }

    public void init() {
        _size = _labyrinth.getCellSize() - 2;
        _moveDelta = _size / 6;
        Log.i("Character", "MOVE DELTA = " + _moveDelta);
        newInvalidateRect(_x, _y);
    }

    protected void newInvalidateRect(float newX, float newY) {
        float l = Math.min(_x, newX) - _size;
        float t = Math.min(_y, newY) - _size;
        float r = Math.max(_x, newX) + _size + 1;
        float b = Math.max(_y, newY) + _size + 1;
        _invalidateRect = new Rect((int) l, (int) t, (int) r + 1, (int) b + 1);
    }

    public void draw(Canvas canvas) {
        int cell = _labyrinth.cellAt(_x, _y);
        RectF bounds = _labyrinth.getCellBounds(cell);
        canvas.drawRect(bounds, _debugPaint);
        canvas.drawCircle(_x, _y, 1, _debugPaint);
    }

    public abstract void move();

    protected Direction move(Direction direction) {
        int cell = _labyrinth.cellAt(_x, _y);
        RectF bounds = _labyrinth.getCellBounds(cell);
        float centerX = bounds.centerX();
        float centerY = bounds.centerY();
        float delta = _moveDelta;
        boolean canMove = true;
        // don´t let the char walk behind the centerX oder centerY position
        if (_newDirection.isPerpendicular(_direction)) {
            delta = getDelta(centerX, centerY, _moveDelta);
            _direction = getDirectionInTheSameCell(centerX, centerY);
        } else {
            _newDirection = direction;
            if (!_newDirection.isPerpendicular(_direction)) {
                _direction = _newDirection;
            }
            canMove = _labyrinth.canMove(cell, _direction);
            if (!canMove) {
                delta = getDelta(centerX, centerY, _moveDelta);
                _direction = getDirectionInTheSameCell(centerX, centerY);
                canMove = delta != 0;
            }
        }


        float newX = _x;
        float newY = _y;

        switch (_direction) {
            case Stopped:
                break;
            case Left:
                newX = _x - delta;
                break;
            case Right:
                newX = _x + delta;
                break;
            case Up:
                newY = _y - delta;
                break;
            case Down:
                newY = _y + delta;
                break;
        }

        if (_direction != Direction.Stopped && canMove) {
            // use teleportation to get on the other side ;)
            if (newX > _labyrinth.getBounds().right) {
                newX = _labyrinth.getBounds().left;
            }
            if (newX < _labyrinth.getBounds().left) {
                newX = _labyrinth.getBounds().right;
            }

            newInvalidateRect(newX, newY);
            _x = newX;
            _y = newY;
        }
        return _direction;
    }

    private float getDelta(float centerX, float centerY, float delta) {
        if (_x == centerX && _y == centerY) {
            delta = 0;
        } else if (_x != centerX) {
            delta = centerX - _x;
        } else if (_y != centerY) {
            delta = centerY - _y;
        }
        delta = Math.abs(delta);
        delta = delta > _moveDelta ? _moveDelta : delta;
        return delta;
    }

    private Direction getDirectionInTheSameCell(float centerX, float centerY) {
        if (_x == centerX && _y == centerY) {
            return _newDirection;
        } else if (_x != centerX) {
            return centerX - _x > 0 ? Direction.Right : Direction.Left;
        } else if (_y != centerY) {
            return centerY - _y > 0 ? Direction.Down : Direction.Up;
        }
        return _direction;
    }
}
