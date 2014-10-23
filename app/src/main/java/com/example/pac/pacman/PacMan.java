package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class PacMan {
    private final int _radius = 10;
    private Paint _paint;
    private Rect _bounds;

    private int _x, _y;
    private int _direction = 1;
    private Rect _invalidateRect;

    public PacMan(Resources resources) {
        _paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _paint.setStyle(Paint.Style.FILL);
        _paint.setColor(resources.getColor(R.color.pacman));
    }

    public Rect getInvalidateRect() {
        return _invalidateRect;
    }

    public void setBounds(Rect bounds) {
        _bounds = new Rect(bounds.left+_radius, bounds.top+_radius, bounds.right-_radius, bounds.bottom-_radius);
        _x = _bounds.centerX();
        _y = _bounds.centerY();
    }

    public void draw(Canvas canvas) {
        RectF r = new RectF(_x-_radius, _y-_radius, _x+2*_radius, _y+2*_radius);
        canvas.drawArc(r, 30, 300, true, _paint);
    }

    public void move() {
        int newX = _x + 2 * _direction;
        int newY = _y + 2 * _direction;
        if (_bounds.contains(newX, newY)) {
            newInvalidateRect(newX, newY);
            _x = newX;
            _y = newY;
        } else {
            _direction = - _direction;
        }
    }

    private void newInvalidateRect(float newX, float newY) {
        int l = (int)Math.min(_x, newX)-2*_radius;
        int t = (int)Math.min(_y, newY)-2*_radius;
        int r = (int)Math.max(_x, newX)+2*_radius+1;
        int b = (int)Math.max(_y, newY)+2*_radius+1;
        _invalidateRect = new Rect(l, t, r, b);
    }
}