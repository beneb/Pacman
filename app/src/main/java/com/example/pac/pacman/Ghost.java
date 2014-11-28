package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.pac.pacman.event.BigDotEatenEvent;
import com.example.pac.pacman.event.EnergizerEndsEvent;
import com.example.pac.pacman.event.EnergizerWillBeRunningOutEvent;
import com.example.pac.pacman.event.EventListener;

public abstract class Ghost extends Character {

    public enum GhostMode {
        Default,
        Scared,
        ScaredAndFlashing,
        Dying,
        WalkingBack
    }

    public EventListener<BigDotEatenEvent> EnergizerStartsListener =
            new EventListener<BigDotEatenEvent>() {
                @Override
                public void onEvent(BigDotEatenEvent event) {
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
    private Resources _resources;
    private int _defaultColor;


    protected Ghost(int color, IMoveStrategy moveStrategy, Labyrinth labyrinth, Resources resources) {
        super(moveStrategy, labyrinth);
        _resources = resources;
        _defaultColor = color;
        _foreground.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        float radius = _size/2;
        RectF r = new RectF(_x - radius, _y - radius, _x + radius, _y + radius);
        canvas.drawRect(r, _foreground);
        super.draw(canvas);
    }

    private void SetGhostMode(GhostMode mode) {
        switch (mode) {

            case Default:
                ResetMode();
                break;
            case Scared:
                _foreground.setColor(_resources.getColor(R.color.ScaredGhost));
                // TODO: change speed
                break;
            case ScaredAndFlashing:
                // TODO: flashing color
                break;
            case Dying:
                // TODO: dieing ghost animation
                break;
            case WalkingBack:
                // TODO: show only ghost´s eyes
                break;
        }
    }

    private void ResetMode() {
        _foreground.setColor(_defaultColor);
        // TODO: default speed
    }
}
