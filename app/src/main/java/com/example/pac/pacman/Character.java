package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;

public abstract class Character {

    private String _name;
    private String _nickName;
    private int _typedColor;

    private final float MOVE_DELTA;
    protected Paint _foreground;

    protected float _x, _y;
    protected int _size;

    private Rect _invalidateRect;
    public Rect getInvalidateRect() {
        return _invalidateRect;
    }

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

    public void init() {
        _size = GameEnv.getInstance().getLabyrinth().getCellSize()-2;
        newInvalidateRect(_x, _y);
    }

    protected void newInvalidateRect(float newX, float newY) {
        int l = (int)Math.min(_x, newX) - _size;
        int t = (int)Math.min(_y, newY) - _size;
        int r = (int)Math.max(_x, newX) + _size + 1;
        int b = (int)Math.max(_y, newY) + _size + 1;
        _invalidateRect = new Rect(l, t, r, b);
    }

    public abstract void draw(Canvas canvas) ;
    public abstract void move();

    protected boolean move(Direction direction) {
        float newX = _x;
        float newY = _y;

        switch (direction) {
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

        if (direction != Direction.Stopped && canMove(newX, newY)) {
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
