package com.example.pac.pacman;

import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.util.TypedValue;

public class Character extends GameObject {

    private String _name;
    private String _nickName;
    private int _typedColor;

    private final float MOVE_DELTA;
    protected Paint _foreground;

    protected Direction _wishDirection = Direction.Left;
    protected Direction _direction = Direction.Stopped;

    public Character(String name, String nickName, int typedColor) {
        super();
        _name = name;
        _nickName = nickName;
        _typedColor = typedColor;

        _foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        _foreground.setStyle(Paint.Style.FILL);

        final float MOVE_DELTA_DIP = 2.5f;
        MOVE_DELTA = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MOVE_DELTA_DIP, GameEnv.getInstance().getResources().getDisplayMetrics());
        Log.i("Character", "MOVE_DELTA = " + MOVE_DELTA);
    }

    public PointF GetNewWayPoint(Direction direction, float moveDelta) {
        float newX = _x;
        float newY = _y;

        switch (direction) {
            case Stopped:
                break;
            case Left:
                newX = _x - moveDelta;
                break;
            case Right:
                newX = _x + moveDelta;
                break;
            case Up:
                newY = _y - moveDelta;
                break;
            case Down:
                newY = _y + moveDelta;
                break;
        }

        return new PointF(newX, newY);
    }

    public boolean move() {
        float newX;
        float newY;

        PointF newWayPoint = GetNewWayPoint(_wishDirection, MOVE_DELTA);
        newX = newWayPoint.x;
        newY = newWayPoint.y;

        if (!canMove(newX, newY)) {
            newWayPoint = GetNewWayPoint(_direction, MOVE_DELTA);
            newX = newWayPoint.x;
            newY = newWayPoint.y;
        } else {
            _direction = _wishDirection;
        }

        if (_wishDirection != Direction.Stopped && canMove(newX, newY)) {
            Labyrinth lab = GameEnv.getInstance().getLabyrinth();
            if (newX > lab.getBounds().right) {
                newX = lab.getBounds().left;
            }
            if (newX < lab.getBounds().left) {
                newX = lab.getBounds().right;
            }

            newInvalidateRect(newX, newY);
            _x = newX;
            _y = newY;
            return true;
        } else {
            return false;
        }
    }

    private boolean canMove(float newX, float newY) {
        float radius = _size/2;
        Labyrinth lab = GameEnv.getInstance().getLabyrinth();
        return lab.canMove(newX - radius, newY - radius) &&
                lab.canMove(newX + radius, newY - radius) &&
                lab.canMove(newX + radius, newY + radius) &&
                lab.canMove(newX - radius, newY + radius);
    }
}
