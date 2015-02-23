package com.example.pac.pacman;


public abstract class Ghost extends Character {

    private GhostMode _mode = GhostMode.Default;

    public GhostMode getMode() {
        return _mode;
    }
    public void setMode(GhostMode mode) {
        _mode = mode;
    }

    protected Ghost(IMoveStrategy moveStrategy, Labyrinth labyrinth) {
        super(moveStrategy, labyrinth);
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

    public void wasEaten() {
        setMode(GhostMode.FadeAwayAndShowingScore);
    }
}
