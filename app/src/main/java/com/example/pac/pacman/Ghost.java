package com.example.pac.pacman;

import android.content.res.Resources;

import com.example.pac.pacman.event.EnergizerEatenEvent;
import com.example.pac.pacman.event.EnergizerEndsEvent;
import com.example.pac.pacman.event.EnergizerWillBeRunningOutEvent;
import com.example.pac.pacman.event.EventListener;


public abstract class Ghost extends Character {

    private GhostMode _mode = GhostMode.Default;

    public GhostMode getMode() {
        return _mode;
    }

    public EventListener<EnergizerEatenEvent> EnergizerStartsListener =
            new EventListener<EnergizerEatenEvent>() {
                @Override
                public void onEvent(EnergizerEatenEvent event) {
                    SetGhostMode(GhostMode.Scared);
                }
            };

    public EventListener<EnergizerWillBeRunningOutEvent> EnergizerWillBeRunningOutListener =
            new EventListener<EnergizerWillBeRunningOutEvent>() {
                @Override
                public void onEvent(EnergizerWillBeRunningOutEvent event) {
                    SetGhostMode(GhostMode.ScaredAndFlashing);
                }
            };

    public EventListener<EnergizerEndsEvent> EnergizerEndsListener =
            new EventListener<EnergizerEndsEvent>() {
                @Override
                public void onEvent(EnergizerEndsEvent event) {
                    SetGhostMode(GhostMode.Default);
                }
            };


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
            case Dying:
                velocityRate = 0f;
                break;
            case WalkingBack:
                velocityRate = 3f;
                break;
        }

        return velocityRate * _maxMoveDelta;
    }

    private void SetGhostMode(GhostMode mode) {
        _mode = mode;
    }
}
