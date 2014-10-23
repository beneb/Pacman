package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Labyrinth {

    private final int _width = 20;
    private final int _height = 20;
    private final Resources _resources;
    private Paint _debugPaint;

    public RectF getBounds() {
        return _bounds;
    }

    private enum LayoutType {
        Space,
        WallHorizontal,
        WallVertical,
    }

    private LayoutType layout[][] = new LayoutType[_width][_height];

    private RectF _bounds;
    private float _cellSize;

    private Paint _backgroundPaint;
    private Paint _wallPaint;

    public Labyrinth(Resources resources) {
        _resources = resources;
        initTestLabyrinth();

        _backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _backgroundPaint.setStyle(Paint.Style.FILL);
        _backgroundPaint.setColor(_resources.getColor(R.color.background));

        _wallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _wallPaint.setColor(_resources.getColor(R.color.walls));
        _wallPaint.setStrokeWidth(5);

        _debugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _debugPaint.setStyle(Paint.Style.STROKE);
        _debugPaint.setColor(Color.RED);
    }

    private void initTestLabyrinth() {
        for (int i = 0; i < _width; i++) {
            layout[i][0] = LayoutType.WallHorizontal;
            layout[i][_height - 1] = LayoutType.WallHorizontal;
        }
        for (int i = 0; i < _height; i++) {
            layout[0][i] = LayoutType.WallVertical;
            layout[_width - 1][i] = LayoutType.WallVertical;
        }
    }

    public void init(RectF bounds) {
        float width = bounds.width() / _width;
        float height = bounds.height() / _height;
        _cellSize = Math.min(width, height);
        _bounds = new RectF(bounds.left,
                bounds.top,
                bounds.left + _width * _cellSize,
                bounds.top + _height * _cellSize);
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < _width; i++) {
            for (int j = 0; j < _height; j++) {
                if (layout[i][j] == LayoutType.WallVertical) {
                    float startX = _cellSize * i + _cellSize / 2 + _bounds.left;
                    float startY = _cellSize * j + _bounds.top;
                    float stopX = startX;
                    float stopY = _cellSize * (j + 1) + _bounds.top;
                    canvas.drawLine(startX, startY, stopX, stopY, _wallPaint);
                } else if (layout[i][j] == LayoutType.WallHorizontal) {
                    float startX = _cellSize * i + _bounds.left;
                    float startY = _cellSize * j + _cellSize / 2 + _bounds.top;
                    float stopX = _cellSize * (i + 1) + _bounds.left;
                    float stopY = startY;
                    canvas.drawLine(startX, startY, stopX, stopY, _wallPaint);
                }

                float l = i * _cellSize + _bounds.left;
                float t = j * _cellSize + _bounds.top;
                canvas.drawRect(l, t, l + _cellSize, t + _cellSize, _debugPaint);
            }
        }
    }
}
