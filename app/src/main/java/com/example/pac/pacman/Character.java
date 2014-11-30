package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public abstract class Character {

    public abstract String getName();
    public abstract String getNickName();
    public abstract char getId();

    private final Paint _debugPaint;

    protected float _maxMoveDelta;
    protected Paint _foreground;

    protected IMoveStrategy _moveStrategy;

    protected final PointF _position = new PointF();
    protected float _size;

    public PointF getPosition() {
        return _position;
    }

    public int getCell() {
        return _labyrinth.cellAt(_position.x, _position.y);
    }

    private Rect _invalidateRect;

    public Rect getInvalidateRect() {
        return _invalidateRect;
    }

    protected final Labyrinth _labyrinth;

    private Direction _newDirection;
    private Direction _currentDirection;

    public Character(IMoveStrategy moveStrategy, Labyrinth labyrinth) {
        _moveStrategy = moveStrategy;
        _labyrinth = labyrinth;

        _foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        _foreground.setStyle(Paint.Style.FILL);

        _debugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _debugPaint.setStyle(Paint.Style.STROKE);
        _debugPaint.setColor(Color.RED);
    }

    public void init() {
        _size = _labyrinth.getCellSize() - 2;
        _maxMoveDelta = _size / 6;
        Log.i("Character", "MOVE DELTA = " + _maxMoveDelta);
        newInvalidateRect(_position);

        int cellNum = _labyrinth.getCharacterPosition(this);
        RectF cellBounds = _labyrinth.getCellBounds(cellNum);
        _position.set(cellBounds.centerX(), cellBounds.centerY());

        _newDirection = Direction.Stopped;
        _currentDirection = Direction.Stopped;
    }

    protected void newInvalidateRect(PointF newPosition) {
        float l = Math.min(_position.x, newPosition.x) - _size;
        float t = Math.min(_position.y, newPosition.y) - _size;
        float r = Math.max(_position.x, newPosition.x) + _size + 1;
        float b = Math.max(_position.y, newPosition.y) + _size + 1;
        _invalidateRect = new Rect((int) l, (int) t, (int) r + 1, (int) b + 1);
    }

    public void draw(Canvas canvas) {
        //int cell = _labyrinth.cellAt(_x, _y);
        //RectF bounds = _labyrinth.getCellBounds(cell);
        //canvas.drawRect(bounds, _debugPaint);
        //canvas.drawCircle(_x, _y, 1, _debugPaint);
    }

    public Direction move() {
        _newDirection = _moveStrategy.GetCurrentOrNextDirection(_position.x, _position.y);

        int cell = _labyrinth.cellAt(_position.x, _position.y);
        RectF bounds = _labyrinth.getCellBounds(cell);
        float centerX = bounds.centerX();
        float centerY = bounds.centerY();

        if (_newDirection.isPerpendicular(_currentDirection) || _newDirection == Direction.Stopped) {
            // don´t let the char walk behind the centerX oder centerY position
            _currentDirection = getDirectionInTheSameCell(centerX, centerY);
        } else {
            _currentDirection = _newDirection;
        }

        PointF newPosition = new PointF(_position.x, _position.y);

        float delta = getDelta(centerX, centerY);
        switch (_currentDirection) {
            case Left:
                newPosition.x = _position.x - delta;
                break;
            case Right:
                newPosition.x = _position.x + delta;
                break;
            case Up:
                newPosition.y = _position.y - delta;
                break;
            case Down:
                newPosition.y = _position.y + delta;
                break;
        }

        if (_currentDirection != Direction.Stopped) {
            // use teleportation to get on the other side ;)
            if (newPosition.x > _labyrinth.getBounds().right) {
                newPosition.x = _labyrinth.getBounds().left;
            }
            if (newPosition.x < _labyrinth.getBounds().left) {
                newPosition.x = _labyrinth.getBounds().right;
            }

            _position.set(newPosition);
            newInvalidateRect(_position);
        }

        _labyrinth.setCharacterPosition(this, cell);
        return _currentDirection;
    }

    private float getDelta(float centerX, float centerY) {
        float delta = _maxMoveDelta;
        if (_position.x != centerX) {
            delta = centerX - _position.x;
        } else if (_position.y != centerY) {
            delta = centerY - _position.y;
        }
        delta = Math.abs(delta);
        delta = delta > _maxMoveDelta ? _maxMoveDelta : delta;
        return delta;
    }

    private Direction getDirectionInTheSameCell(float centerX, float centerY) {
        if (_position.x == centerX && _position.y == centerY) {
            return _newDirection;
        } else if (_position.x != centerX) {
            return centerX - _position.x > 0 ? Direction.Right : Direction.Left;
        } else if (_position.y != centerY) {
            return centerY - _position.y > 0 ? Direction.Down : Direction.Up;
        }
        return _currentDirection;
    }
}
