package com.example.pac.pacman;


public abstract class Ghost extends Character {

    private GhostMode _mode = GhostMode.Default;
    private int _scoreTextToShow;

    public GhostMode getMode() {
        return _mode;
    }
    private void setMode(GhostMode mode) {
        _mode = mode;
        if (mode != GhostMode.FadeAwayAndShowingScore) {
            _scoreTextToShow = 0;
        }
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
            setMode(GhostMode.Scared);
        }
    }

    public void TryToFlash() {
        if (_mode == GhostMode.Scared) {
            setMode(GhostMode.ScaredAndFlashing);
        }
    }

    public void TryToCalmDown() {
        if (_mode == GhostMode.Scared ||
            _mode == GhostMode.ScaredAndFlashing) {
            setMode(GhostMode.Default);
            setMoveStrategy(new RandomMoveStrategy(_labyrinth));
        }
    }

    public void Hide() {
        setMode(GhostMode.Hidden);
        setMoveStrategy(new StopMoveStrategy());
    }

    public void SetScoreText(int score) {
        if (_mode == GhostMode.FadeAwayAndShowingScore) {
            _scoreTextToShow = score;
        }
    }

    public String GetScoreText() {
        return String.valueOf(_scoreTextToShow);
    }
}
