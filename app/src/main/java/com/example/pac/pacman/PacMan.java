package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class PacMan extends Character {
    private int _pMouth;

    private static final int MOUTH_OPEN_GRAD = 30;
    private static final int MOUTH_CLOSED_GRAD = 0;

    private int _mouthOpenGrad = MOUTH_OPEN_GRAD;
    private boolean _mouthClosing;

    private Paint _paint;

    public PacMan(Resources resources, Labyrinth labyrinth) {
        super(labyrinth);
        _paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(resources.getColor(R.color.pacman));
    }

    @Override
    public void init() {
        super.init();
        RectF bounds = _labyrinth.getBounds();
        _x = bounds.centerX();
        _y = bounds.centerY();
    }

    @Override
    public void draw(Canvas canvas) {
        float radius = _size/2;
        RectF r = new RectF(_x - radius, _y - radius, _x + radius, _y + radius);
        canvas.drawArc(r, _pMouth + _mouthOpenGrad, 360 - 2*_mouthOpenGrad, true, _paint);
    }

    @Override
    public boolean move() {
        boolean moved = super.move();
        setMouthOpen (moved);

        switch (_move) {
            case Stopped:
            case HorizontalRight:
                _pMouth = 0;
                break;
            case HorizontalLeft:
                _pMouth = 180;
                break;
            case VerticalUp:
                _pMouth = 270;
                break;
            case VerticalDown:
                _pMouth = 90;
                break;
        }
        return moved;
    }

    private void setMouthOpen(boolean canMove) {
        if (!canMove) {
            _mouthOpenGrad = MOUTH_OPEN_GRAD;
            _mouthClosing = true;
            return;
        }
        if (_mouthOpenGrad == MOUTH_OPEN_GRAD) {
            _mouthClosing = true;
        } else if (_mouthOpenGrad == MOUTH_CLOSED_GRAD) {
            _mouthClosing = false;
        }
        _mouthOpenGrad += _mouthClosing ? -5 : 5;
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
        _move = Direction.VerticalDown;
    }

    private void goUp() {
        _move = Direction.VerticalUp;
    }

    private void goRight() {
        _move = Direction.HorizontalRight;
    }

    private void goLeft() {
        _move = Direction.HorizontalLeft;
    }

    private boolean isHorizontal(float x_touched, float y_touched) {
        return Math.abs(x_touched - _x) > Math.abs(y_touched - _y);
    }
}