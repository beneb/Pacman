package com.example.pac.pacman.views;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.example.pac.pacman.Ghost;
import com.example.pac.pacman.R;

public class GhostView implements IChildView {
    private final Ghost _ghost;
    private final Resources _resources;
    protected int _defaultColor;
    protected Paint _foreground;

    public GhostView(Ghost ghost, Resources resources) {
        _ghost = ghost;
        _resources = resources;
        _foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        _foreground.setStyle(Paint.Style.FILL);
    }


    private final RectF _drawRect = new RectF();

    @Override
    public void draw(Canvas canvas) {
        float radius = _ghost.getSize() / 2;
        PointF pos = _ghost.getPosition();
        _drawRect.set(
                pos.x - radius,
                pos.y - radius,
                pos.x + radius,
                pos.y + radius);

        switch (_ghost.getMode()) {
            case Default:
                _foreground.setColor(_defaultColor);
                break;
            case Scared:
                _foreground.setColor(_resources.getColor(R.color.ScaredGhost));
                break;
        }
        canvas.drawRect(_drawRect, _foreground);
    }
}