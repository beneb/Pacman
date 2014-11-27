package com.example.pac.pacman.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.pac.pacman.event.DrawRequestEvent;
import com.example.pac.pacman.event.DummyEventManager;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.IEventManager;
import com.example.pac.pacman.event.InitEvent;
import com.example.pac.pacman.event.InvalidateViewEvent;
import com.example.pac.pacman.event.PacManDirectionRequestEvent;

/*
*   TODO: Maybe it should be a SurfaceView
*   see  http://android-journey.blogspot.co.il/2010/02/android-2d-simple-example.html
*   http://pierrchen.blogspot.de/2014/03/anroid-graphics-surfaceview-all-you.html
*   http://source.android.com/devices/graphics/architecture.html
* */
public class GameplayView extends View {

    private static final int MARGIN = 10;

    private RectF getInnerBounds(int w, int h) {
        return new RectF(MARGIN, MARGIN, w - MARGIN, h - MARGIN);
    }

    public GameplayView(Context context) {
        super(context);
    }

    public GameplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private IEventManager _eventManager = new DummyEventManager();

    public void init(IEventManager eventManager) {
        _eventManager = eventManager;
        _eventManager.registerObserver(InvalidateViewEvent.class,
                new EventListener<InvalidateViewEvent>() {
                    @Override
                    public void onEvent(InvalidateViewEvent event) {
                        invalidate(event.GetRect());
                    }
                });
    }

    private final DrawRequestEvent _drawEvent = new DrawRequestEvent();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        _drawEvent.setCanvas(canvas);
        _eventManager.fire(_drawEvent);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Act only on ACTION_UP - should be good enough for touch and wipe
        if (event.getAction() == MotionEvent.ACTION_UP) {
            _eventManager.fire(new PacManDirectionRequestEvent(event.getX(), event.getY()));
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int _, int __) {
        RectF bounds = getInnerBounds(w, h);
        _eventManager.fire(new InitEvent(bounds));
    }
}

