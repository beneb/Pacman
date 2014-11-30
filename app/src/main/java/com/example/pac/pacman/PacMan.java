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

    private static final int MOUTH_OPEN_GRAD = 30;
    private static final int MOUTH_CLOSED_GRAD = 0;

    private int _mouthOpenGrad = MOUTH_OPEN_GRAD;
    private boolean _mouthClosing;
    public int getMouthOpenAngle() { return _mouthOpenGrad; }

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

    public PacMan(IMoveStrategy pacManStrategy, Labyrinth labyrinth) {
        super(pacManStrategy, labyrinth);
    }

    @Override
    public Direction move() {
        Direction direction = super.move();
        if (!direction.equals(Direction.Stopped)) {
            setMouthOpen();
        }
        return direction;
    }

    private void setMouthOpen() {
        if (_mouthOpenGrad == MOUTH_OPEN_GRAD) {
            _mouthClosing = true;
        } else if (_mouthOpenGrad == MOUTH_CLOSED_GRAD) {
            _mouthClosing = false;
        }
        _mouthOpenGrad += _mouthClosing ? -5 : 5;

    }

    public boolean IsUnbreakable() {
        return _unbreakable;
    }
}
