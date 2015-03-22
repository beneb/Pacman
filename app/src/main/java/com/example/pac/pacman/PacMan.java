package com.example.pac.pacman;

public class PacMan extends Character {
    private static final int MOUTH_OPEN_GRAD = 30;
    private static final int MOUTH_CLOSED_GRAD = 0;

    private int _mouthOpenGrad = MOUTH_OPEN_GRAD;
    private boolean _mouthClosing;
    public int getMouthOpenAngle() { return _mouthOpenGrad; }

    private boolean _unbreakable;
    private int _eatenGhostsInARow;

    private boolean _dead;

    public void setDead() {
        _dead = true;
    }

    public boolean isDead() {
        return _dead;
    }

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
    public void init() {
        _dead = false;
        _mouthOpenGrad = MOUTH_OPEN_GRAD;
        super.init();
    }

    @Override
    public Direction move() {
        Direction direction = super.move();
        setMouthOpen(direction.equals(Direction.Stopped));
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

    private void setMouthOpen(boolean stopped) {
        if (!stopped) {
            if (_mouthOpenGrad == MOUTH_OPEN_GRAD) {
                _mouthClosing = true;
            } else if (_mouthOpenGrad == MOUTH_CLOSED_GRAD) {
                _mouthClosing = false;
            }
            _mouthOpenGrad += _mouthClosing ? -5 : 5;
        }
        if (_dead && _mouthOpenGrad < 180){
            _mouthOpenGrad += 7;
        }
    }

    public boolean isUnbreakable() { return _unbreakable; }
    public void setUnbreakable(boolean unbreakable) {
        _unbreakable = unbreakable;
        if (!unbreakable) {
            _eatenGhostsInARow = 0;
        }
    }

    public int EatGhost(Ghost g) {
        int score = (int) Math.pow(2, (double) ++_eatenGhostsInARow) * 100;
        score = score > 1600 ? 1600 : score;
        g.SetScoreText(score);
        return score;
    }

    public IMoveStrategy getMoveStrategy() {
        return _moveStrategy;
    }
}
