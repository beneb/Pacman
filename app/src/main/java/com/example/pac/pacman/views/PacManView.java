package com.example.pac.pacman.views;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

import com.example.pac.pacman.PacMan;
import com.example.pac.pacman.R;

public class PacManView implements IChildView {
    private final PacMan _pacMan;
    protected Paint _foreground;
    protected int _color;
    protected int _colorTransparent;

    public PacManView(PacMan pacMan, Resources resources) {
        _pacMan = pacMan;
        _foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        _foreground.setStyle(Paint.Style.FILL);
        _color = resources.getColor(R.color.pacman);
        _colorTransparent = resources.getColor(R.color.pacman_transparent);
        _foreground.setColor(_color);
    }

    RectF _pacManRect = new RectF();

    @Override
    public void onSizeChanged() {
    }

    @Override
    public void draw(Canvas canvas) {
        if (_pacMan.isHidden()) {
            return;
        }

        float radius = _pacMan.getSize() / 2;
        PointF pos = _pacMan.getPosition();

        int openAngle = _pacMan.getMouthOpenAngle();

        if (openAngle < 180) {
            _deadlyCircleRepetition = 0;
            _pacManRect.set(
                    pos.x - radius,
                    pos.y - radius,
                    pos.x + radius,
                    pos.y + radius);

            int initAngle = getMouthInitAngle();
            canvas.drawArc(_pacManRect,
                    initAngle + openAngle,
                    360 - 2 * openAngle,
                    true, _foreground);
        } else {
            paintDeadlyCircle(canvas, pos, radius);
        }
    }

    private void paintDeadlyCircle(Canvas canvas, PointF pos, float radius) {
        if (_deadlyGradientRadius < radius && _deadlyCircleRepetition < 2) {
            _deadlyGradientRadius += radius / 8;
        } else {
            _deadlyGradientRadius = 0;
            _deadlyCircleRepetition++;
        }
        RadialGradient gradient = new RadialGradient(
                pos.x, pos.y, radius,
                _color, _colorTransparent,
                Shader.TileMode.CLAMP);
        Paint p = new Paint();
        p.setShader(gradient);
        p.setStrokeWidth(radius / 2);
        p.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(pos.x, pos.y, _deadlyGradientRadius, p);
    }

    private float _deadlyGradientRadius;
    private int _deadlyCircleRepetition;

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