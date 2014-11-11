package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;

public abstract class Character {

    private String _name;
    private String _nickName;

    private final Paint _debugPaint;

    protected final float MOVE_DELTA;
    protected Paint _foreground;

    protected float _x, _y;
    protected int _size;

    private Rect _invalidateRect;

    public Rect getInvalidateRect() {
        return _invalidateRect;
    }

    protected final Labyrinth _labyrinth;

    private Direction _newDirection = Direction.Stopped;
    protected Direction _direction = Direction.Stopped;

    public Character(String name, String nickName, Labyrinth labyrinth) {
        _labyrinth = labyrinth;

        _name = name;
        _nickName = nickName;

        _foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        _foreground.setStyle(Paint.Style.FILL);

        _debugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _debugPaint.setStyle(Paint.Style.STROKE);
        _debugPaint.setColor(Color.RED);

        final float MOVE_DELTA_DIP = 2.5f;
        MOVE_DELTA = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MOVE_DELTA_DIP, GameEnv.getInstance().getResources().getDisplayMetrics());
        Log.i("Character", "MOVE_DELTA = " + MOVE_DELTA);
    }

    public void init() {
        _size = _labyrinth.getCellSize() - 2;
        newInvalidateRect(_x, _y);
    }

    protected void newInvalidateRect(float newX, float newY) {
        int l = (int) Math.min(_x, newX) - _size;
        int t = (int) Math.min(_y, newY) - _size;
        int r = (int) Math.max(_x, newX) + _size + 1;
        int b = (int) Math.max(_y, newY) + _size + 1;
        _invalidateRect = new Rect(l, t, r, b);
    }

    public void draw(Canvas canvas) {
        int cell = _labyrinth.cellAt(_x, _y);
        RectF bounds = _labyrinth.getCellBounds(cell);
        canvas.drawRect(bounds, _debugPaint);
        canvas.drawCircle(_x, _y, 1, _debugPaint);
    }

    public abstract void move();

    protected boolean move(Direction direction) {
        int cell = _labyrinth.cellAt(_x, _y);
        RectF bounds = _labyrinth.getCellBounds(cell);
        float centerX = bounds.centerX();
        float centerY = bounds.centerY();
        float delta = MOVE_DELTA;
        boolean canMove = true;
        if (_newDirection.isPerpendicular(_direction)) {
            if (_x == centerX && _y == centerY) {
                _direction = _newDirection;
            } else if (_x != centerX) {
                delta = centerX - _x;
                _direction = delta > 0 ? Direction.Right : Direction.Left;
            } else if (_y != centerY) {
                delta = centerY - _y;
                _direction = delta > 0 ? Direction.Down: Direction.Up;
            }
            delta = Math.abs(delta);
            delta = delta > MOVE_DELTA ? MOVE_DELTA : delta;
        } else {
            _newDirection = direction;
            if (!_newDirection.isPerpendicular(_direction)) {
                _direction = _newDirection;
            }
            canMove = _labyrinth.canMove(cell, _direction);
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

        if (direction != Direction.Stopped && canMove) {
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
}
