package com.example.pac.pacman;


public abstract class Ghost extends Character {

    private GhostMode _mode = GhostMode.Default;

    public GhostMode getMode() {
        return _mode;
    }
    private void setMode(GhostMode mode) {
        _mode = mode;
    }

    protected Ghost(IMoveStrategy moveStrategy, Labyrinth labyrinth) {
        super(moveStrategy, labyrinth);
    }

    @Override
    public void init() {
        super.init();
        setMode(GhostMode.Default);
    }

    @Override
    protected float getDeltaInternal() {
        int level = 1;
        float velocityRate;

        switch (_mode) {
            default:
            case Default:
                if (level == 1) {
                    velocityRate = 0.75f;
                } else if (level <= 4) {
                    velocityRate = 0.85f;
                } else {
                    velocityRate = 0.95f;
                }
                break;
            case Scared:
            case ScaredAndFlashing:
                velocityRate = 0.50f;
                break;
            case FadeAwayAndShowingScore:
                velocityRate = 0f;
                break;
            case WalkingBack:
                velocityRate = 3f;
                break;
        }

        return velocityRate * _maxMoveDelta;
    }

    public boolean TryToEatThisGhost() {
        if (_mode == GhostMode.Scared ||
            _mode == GhostMode.ScaredAndFlashing) {
            setMode(GhostMode.FadeAwayAndShowingScore);
            return true;

        } else {
            return false;
        }
    }

    public void TryToScare() {
        if (_mode == GhostMode.Default) {
            _mode = GhostMode.Scared;
        }
    }

    public void TryToFlash() {
        if (_mode == GhostMode.Scared) {
            _mode = GhostMode.ScaredAndFlashing;
        }
    }

    public void TryToCalmDown() {
        if (_mode == GhostMode.Scared ||
            _mode == GhostMode.ScaredAndFlashing) {
            _mode = GhostMode.Default;
            setMoveStrategy(new RandomMoveStrategy(_labyrinth));
        }
    }

    public void Hide() {
        _mode = GhostMode.Hidden;
        setMoveStrategy(new StopMoveStrategy());
    }
}
