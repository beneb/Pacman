package com.example.pac.pacman;

import android.graphics.Canvas;
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

    private PacManMoveStrategy _moveStrategy = new PacManMoveStrategy(_labyrinth);

    public PacMan(int color, Labyrinth labyrinth) {
        super("Pac-Man", "Al Bundy", labyrinth);
        _foreground.setColor(color);
    }

    @Override
    public void init() {
        super.init();
        int cellNum = _labyrinth.getPacManCell();
        RectF cellBounds = _labyrinth.getCellBounds(cellNum);
        _x = cellBounds.centerX();
        _y = cellBounds.centerY();
    }

    @Override
    public void draw(Canvas canvas) {
        float radius = _size/2;
        RectF r = new RectF(_x - radius, _y - radius, _x + radius, _y + radius);
        canvas.drawArc(r, _pMouth + _mouthOpenGrad, 360 - 2*_mouthOpenGrad, true, _foreground);
        super.draw(canvas);
    }

    @Override
    public void move() {
        Direction direction= super.move(_moveStrategy.GetCurrentOrNextDirection(_x, _y));

        if (!direction.equals(Direction.Stopped)) {
            _labyrinth.setPacManPosition(_x, _y);
        }
        setMouthOpen (!direction.equals(Direction.Stopped));

        switch (direction) {
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
                _moveStrategy.setWishDirection(Direction.Left);
            } else {
                _moveStrategy.setWishDirection(Direction.Right);
            }
        } else { // vertical move
            if (y_touched < _y) {
                _moveStrategy.setWishDirection(Direction.Up);
            } else {
                _moveStrategy.setWishDirection(Direction.Down);
            }
        }
    }

    private boolean isHorizontal(float x_touched, float y_touched) {
        return Math.abs(x_touched - _x) > Math.abs(y_touched - _y);
    }
}
