package com.example.pac.pacman.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.pac.pacman.event.DummyEventManager;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.IEventManager;
import com.example.pac.pacman.event.InitEvent;
import com.example.pac.pacman.event.InvalidateViewEvent;
import com.example.pac.pacman.event.PacManDirectionRequestEvent;

import java.util.ArrayList;
import java.util.Collection;

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
    private Collection<IChildView> _childViews = new ArrayList<IChildView>();

    public void init(IEventManager eventManager, Collection<IChildView> childViews) {
        _eventManager = eventManager;
        _eventManager.registerObserver(InvalidateViewEvent.class,
                new EventListener<InvalidateViewEvent>() {
                    @Override
                    public void onEvent(InvalidateViewEvent event) {
                        invalidate(event.GetRect());
                    }
                });
        _eventManager.registerObserver(InitEvent.class,
                new EventListener<InitEvent>() {
                    @Override
                    public void onEvent(InitEvent event) {
                        RectF bounds = event.getBounds();
                        Rect invalidateRect = new Rect(
                                (int) bounds.left,
                                (int) bounds.top,
                                (int) bounds.right,
                                (int) bounds.bottom);
                        invalidate(invalidateRect);
                    }
                });
        _childViews = childViews;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (IChildView view : _childViews) {
            view.draw(canvas);
        }
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
        for (IChildView childView : _childViews) {
            childView.onSizeChanged();
        }
    }
}

