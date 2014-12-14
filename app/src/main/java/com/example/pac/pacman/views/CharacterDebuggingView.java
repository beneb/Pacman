package com.example.pac.pacman.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class CharacterDebuggingView implements IChildView {

    private final Paint _debugPaint;

    public CharacterDebuggingView() {
        _debugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _debugPaint.setStyle(Paint.Style.STROKE);
        _debugPaint.setColor(Color.RED);
    }

    @Override
    public void onSizeChanged() { }

    @Override
    public void draw(Canvas canvas) {
//        int cell = _labyrinth.cellAt(_x, _y);
//        RectF bounds = _labyrinth.getCellBounds(cell);
//        canvas.drawRect(bounds, _debugPaint);
//        canvas.drawCircle(_x, _y, 1, _debugPaint);
    }
}

