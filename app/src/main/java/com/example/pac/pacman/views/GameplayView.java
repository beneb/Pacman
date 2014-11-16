package com.example.pac.pacman.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import com.example.pac.pacman.*;
import com.example.pac.pacman.Character;

import java.util.Collection;

/*
*   TODO: Maybe it should be a SurfaceView
*   see  http://android-journey.blogspot.co.il/2010/02/android-2d-simple-example.html
*   http://pierrchen.blogspot.de/2014/03/anroid-graphics-surfaceview-all-you.html
*   http://source.android.com/devices/graphics/architecture.html
* */
public class GameplayView extends View {

    private final PacMan _pacMan;
    private final Collection<com.example.pac.pacman.Character> _characters;
    private final Paint _paintBackground;
    private Labyrinth _labyrinth;

    public GameplayView(Context context, Labyrinth labyrinth, PacMan pacMan, Collection<Character> characters) {
        super(context);
        _pacMan = pacMan;
        _characters = characters;
        _labyrinth = labyrinth;

        _paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        _paintBackground.setStyle(Paint.Style.FILL);
        _paintBackground.setColor(getResources().getColor(R.color.background));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(_paintBackground);
        _labyrinth.draw(canvas);
        for (Character ch : _characters) {
            ch.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // currently  act only on ACTION_DOWN
        if (event.getAction() == 1) {
            _pacMan.go(event.getX(), event.getY());
        }
        // Log.i("ME", describeEvent(event));
        return true;
    }

    @SuppressLint("NewApi")
    private String describeEvent(MotionEvent event) {
        StringBuilder sb = new StringBuilder(300);
        int action = event.getAction();
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            sb.append("Action: ").append(MotionEvent.actionToString(action));
        }else {
            sb.append("Action: ").append(action);
        }
        sb.append(" Location: ").append(event.getX()).append(" x ").append(event.getY());
        return sb.toString();
    }

     @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        final int margin = 10;
         RectF bounds = new RectF(margin, margin, w-margin, h-margin);
         _labyrinth.init(bounds);
         for (Character ch : _characters) {
             ch.init();
         }
    }
}

