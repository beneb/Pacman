package com.example.pac.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;

public class GameplayView extends View {

    private final Labyrinth _labyrinth;
    private RectF _bounds;


    public GameplayView(Context context, Labyrinth labyrinth) {
        super(context);
        _labyrinth = labyrinth;
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

