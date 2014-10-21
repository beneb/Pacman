package com.example.pac.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.view.View;

/*
*   TODO: Maybe it should be a SurfaceView
*   see  http://android-journey.blogspot.co.il/2010/02/android-2d-simple-example.html
*   http://pierrchen.blogspot.de/2014/03/anroid-graphics-surfaceview-all-you.html
*   http://source.android.com/devices/graphics/architecture.html
* */
public class GameplayView extends View {

    private final Labyrinth _labyrinth;
    private final Paint _tmpPaint2;
    private RectF _bounds;

    private int _radius = 0;
    private int _delta = 1;

    public GameplayView(Context context, Labyrinth labyrinth) {
        super(context);
        _labyrinth = labyrinth;

        _tmpPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        _tmpPaint2.setStyle(Paint.Style.STROKE);
        _tmpPaint2.setStrokeWidth(2);
        _tmpPaint2.setColor(getResources().getColor(R.color.walls));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        _labyrinth.draw(canvas);

        float x = _bounds.centerX();
        float y = _bounds.centerY();
        canvas.drawCircle(x, y, _radius, _tmpPaint2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        final int margin = 10;
        _bounds = new RectF(margin, margin, w-margin, h-margin);
        _labyrinth.setBounds(_bounds);
        _radius = w/2;
    }

    public void drawCircle() {
        float l = _bounds.centerX() - _radius - 2;
        float t = _bounds.centerY() - (int) _radius - 2;
        float r = _bounds.centerX() + (int) _radius + 2;
        float b = _bounds.centerY() + (int) _radius + 2;
        _radius += _delta;
        if (_radius <= 0) {
            _radius  = 1;
            _delta = -_delta;
        }
        if (_radius >= _bounds.width()/2) {
            _radius = (int)_bounds.width()/2-1;
            _delta = -_delta;
        }
        invalidate((int)l, (int)t, (int)r, (int)b);
    }
}

