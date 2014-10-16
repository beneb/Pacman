package com.example.pac.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class GameplayView extends View {

    private Paint _shadowPaint;
    private RectF _bounds;
    private Labyrinth _labyrinth;


    public GameplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        _labyrinth = new Labyrinth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        _labyrinth.draw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        final int margin = 10;
        _bounds = new RectF(margin, margin, w-margin, h-margin);
        _labyrinth.setBounds(_bounds);
    }
}

