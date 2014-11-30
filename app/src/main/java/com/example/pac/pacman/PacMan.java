package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.pac.pacman.event.EnergizerEatenEvent;
import com.example.pac.pacman.event.EnergizerEndsEvent;
import com.example.pac.pacman.event.EventListener;

public class PacMan extends Character {

    public EventListener<EnergizerEatenEvent> EnergizerStartsListener =
            new EventListener<EnergizerEatenEvent>() {
                @Override
                public void onEvent(EnergizerEatenEvent event) {
                    _unbreakable = true;
                }
            };

    public EventListener<EnergizerEndsEvent> EnergizerEndsListener =
            new EventListener<EnergizerEndsEvent>() {
                @Override
                public void onEvent(EnergizerEndsEvent event) {
                    _unbreakable = false;
                }
            };

    private static final int MOUTH_RIGHT = 0;
    private static final int MOUTH_LEFT = 180;
    private static final int MOUTH_UP = 270;
    private static final int MOUTH_DOWN = 90;

    private int _pMouth = MOUTH_RIGHT;

    private static final int MOUTH_OPEN_GRAD = 30;
    private static final int MOUTH_CLOSED_GRAD = 0;

    private int _mouthOpenGrad = MOUTH_OPEN_GRAD;
    private boolean _mouthClosing;
    private boolean _unbreakable;

    @Override
    public String getName() {
        return "Pac-Man";
    }

    @Override
    public String getNickName() {
        return "Al Bundy";
    }

    @Override
    public char getId() {
        return 'P';
    }

    public PacMan(int color, IMoveStrategy pacManStrategy, Labyrinth labyrinth) {
        super(pacManStrategy, labyrinth);
        _foreground.setColor(color);
    }

    private final RectF _drawRect = new RectF();
    @Override
    public void draw(Canvas canvas) {
        float radius = _size / 2;
        _drawRect.set(_position.x - radius, _position.y - radius, _position.x + radius, _position.y + radius);
        canvas.drawArc(_drawRect, _pMouth + _mouthOpenGrad, 360 - 2 * _mouthOpenGrad, true, _foreground);
        super.draw(canvas);
    }

    @Override
    public Direction move() {
        Direction direction = super.move();
        if (!direction.equals(Direction.Stopped)) {
            setMouthOpen(direction);
        }
        return direction;
    }

    private void setMouthOpen(Direction direction) {
        if (_mouthOpenGrad == MOUTH_OPEN_GRAD) {
            _mouthClosing = true;
        } else if (_mouthOpenGrad == MOUTH_CLOSED_GRAD) {
            _mouthClosing = false;
        }
        _mouthOpenGrad += _mouthClosing ? -5 : 5;
        switch (direction) {
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

    public boolean IsUnbreakable() {
        return _unbreakable;
    }
}
