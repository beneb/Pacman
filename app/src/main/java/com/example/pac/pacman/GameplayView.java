package com.example.pac.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
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
    private final Paint _paintBackground;
    private RectF _bounds;

    public GameplayView(Context context, Labyrinth labyrinth, PacMan pacman) {
        super(context);
        _labyrinth = labyrinth;
        _pacman = pacman;

        _paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        _paintBackground.setStyle(Paint.Style.FILL);
        _paintBackground.setColor(getResources().getColor(R.color.background));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(_paintBackground);
        _labyrinth.draw(canvas);
        _pacman.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Context context = this.getContext();
        CharSequence text = describeEvent(event);

        _pacman.go(event.getX(), event.getY());


        // Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        // TODO bene: replace this part with log or with a status line in the bottom of the game board.

        return true;
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
        _bounds = new RectF(margin, margin, w-margin, h-margin);
        _labyrinth.init(_bounds);
        _pacman.init();
    }
}

