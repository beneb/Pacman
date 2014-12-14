package com.example.pac.pacman.views;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.example.pac.pacman.PacMan;
import com.example.pac.pacman.R;

public class PacManView implements IChildView {
    private final PacMan _pacMan;
    protected Paint _foreground;

    public PacManView(PacMan pacMan, Resources resources) {
        _pacMan = pacMan;
        _foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        _foreground.setStyle(Paint.Style.FILL);
        _foreground.setColor(resources.getColor(R.color.pacman));
    }

    RectF _pacManRect = new RectF();

    @Override
    public void onSizeChanged() { }

    @Override
    public void draw(Canvas canvas) {
        float radius = _pacMan.getSize() / 2;
        PointF pos = _pacMan.getPosition();
        _pacManRect.set(
                pos.x - radius,
                pos.y - radius,
                pos.x + radius,
                pos.y + radius);

        int initAngle = getMouthInitAngle();
        int openAngle = _pacMan.getMouthOpenAngle();
        canvas.drawArc(_pacManRect,
                initAngle + openAngle,
                360 - 2 * openAngle,
                true, _foreground);
    }

    private int getMouthInitAngle() {
        switch (_pacMan.getDirection()) {
            case Right:
                return 0;
            case Left:
                return 180;
            case Up:
                return 270;
            case Down:
                return 90;
            default:
                return 0;
        }
    }
}