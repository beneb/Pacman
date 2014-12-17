package com.example.pac.pacman;

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

    @Override
    protected float getDeltaInternal() {
        int level = 1; // TODO: implement getLevel() ... maybe within _labyrinth?
        float velocityRate;

        if (level == 1) {
            if (_unbreakable) {
                velocityRate = 0.90f;
            } else {
                velocityRate = 0.80f;
            }
        } else if (level <= 4 || level > 20){
            velocityRate = 0.90f;
        } else{
            // level 5 bis 20
            velocityRate = 1f;
        }

        return velocityRate * _maxMoveDelta;
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
