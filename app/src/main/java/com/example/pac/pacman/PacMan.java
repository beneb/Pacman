package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class PacMan extends Character {
    private static final int MOUTH_RIGHT = 0;
    private static final int MOUTH_LEFT = 180;
    private static final int MOUTH_UP = 270;
    private static final int MOUTH_DOWN = 90;

    private int _pMouth = MOUTH_RIGHT;

    private static final int MOUTH_OPEN_GRAD = 30;
    private static final int MOUTH_CLOSED_GRAD = 0;

    private int _mouthOpenGrad = MOUTH_OPEN_GRAD;
    private boolean _mouthClosing;

    protected Direction _wishDirection = Direction.Left;
    protected Direction _direction = Direction.Stopped;

    public PacMan(Resources resources) {
        super("Pac-Man", "Al Bundy", R.color.pacman);
        _foreground.setColor(resources.getColor(R.color.pacman));
    }

    @Override
    public void init() {
        super.init();
        Labyrinth labyrinth = GameEnv.getInstance().getLabyrinth();
        Labyrinth.Cell c = labyrinth.getPacManCell();
        RectF cellBounds = labyrinth.getCellBounds(c.getCol(), c.getRow());
        _x = cellBounds.centerX();
        _y = cellBounds.centerY();
    }

    @Override
    public void draw(Canvas canvas) {
        float radius = _size/2;
        RectF r = new RectF(_x - radius, _y - radius, _x + radius, _y + radius);
        canvas.drawArc(r, _pMouth + _mouthOpenGrad, 360 - 2*_mouthOpenGrad, true, _foreground);
    }

    @Override
    public void move() {
        boolean moved = super.move(_wishDirection);
        if (!moved) {
            moved = super.move(_direction);
        } else {
            _direction = _wishDirection;
        }
        if (moved) {
            GameEnv.getInstance().getLabyrinth().setPacManPosition(_x, _y);
        }
        setMouthOpen (moved);

        switch (_direction) {
            case Stopped:
            case Right:
                _pMouth = MOUTH_RIGHT;
                break;
            case Left:
                _pMouth = MOUTH_LEFT;
                break;
            case Up:
                _pMouth = MOUTH_UP;
                break;
            case Down:
                _pMouth = MOUTH_DOWN;
                break;
        }
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
        _wishDirection = Direction.Down;
    }

    private void goUp() {
        _wishDirection = Direction.Up;
    }

    private void goRight() {
        _wishDirection = Direction.Right;
    }

    private void goLeft() {
        _wishDirection = Direction.Left;
    }

    private boolean isHorizontal(float x_touched, float y_touched) {
        return Math.abs(x_touched - _x) > Math.abs(y_touched - _y);
    }
}