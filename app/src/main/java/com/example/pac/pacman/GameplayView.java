package com.example.pac.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/*
*   TODO: Maybe it should be a SurfaceView
*   see  http://android-journey.blogspot.co.il/2010/02/android-2d-simple-example.html
*   http://pierrchen.blogspot.de/2014/03/anroid-graphics-surfaceview-all-you.html
*   http://source.android.com/devices/graphics/architecture.html
* */
public class GameplayView extends View {

    private final Labyrinth _labyrinth;
    private final PacMan _pacman;
    private final Paint _tmpPaint2;
    private Rect _bounds;

    public GameplayView(Context context, Labyrinth labyrinth, PacMan pacman) {
        super(context);
        _labyrinth = labyrinth;
        _pacman = pacman;

        _tmpPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        _tmpPaint2.setStyle(Paint.Style.STROKE);
        _tmpPaint2.setStrokeWidth(2);
        _tmpPaint2.setColor(getResources().getColor(R.color.walls));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        _labyrinth.draw(canvas);
        _pacman.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Context context = this.getContext();
        CharSequence text = describeEvent(event);

        _pacman.go(event.getX(), event.getY());


        Toast.makeText(context, text, Toast.LENGTH_LONG).show();

        return false;
    }

    private CharSequence describeEvent(MotionEvent event) {
        StringBuilder sb = new StringBuilder(300);
        sb.append("Action: ").append(event.getAction()).append("\n");
        sb.append("Location: ").append(event.getX()).append(" x ").append(event.getY()).append("\n");


        return sb.toString();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        final int margin = 10;
        _bounds = new Rect(margin, margin, w-margin, h-margin);
        _labyrinth.setBounds(_bounds);
        _pacman.setBounds(_bounds);
    }
}

