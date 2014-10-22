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

    private enum LayoutType {
        Space,
        WallHorizontal,
        WallVertical,
    }
    private LayoutType layout[][] = new LayoutType[_width][_height];

    private RectF _bounds;
    private float _cellWidth;
    private float _cellHeight;

    private Paint _backgroundPaint;
    private Paint _wallPaint;

    public Labyrinth(Resources resources) {
        _resources = resources;
        init();
    }

    private void init() {
        initTestLabyrinth();

        _backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _backgroundPaint.setStyle(Paint.Style.FILL);
        _backgroundPaint.setColor(_resources.getColor(R.color.background));

        _wallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _wallPaint.setColor(_resources.getColor(R.color.walls));
        _wallPaint.setStrokeWidth(5);
    }

    private void initTestLabyrinth() {
        for (int i = 0; i < _width; i++) {
            layout[i][0] = LayoutType.WallHorizontal;
            layout[i][_height-1] = LayoutType.WallHorizontal;
        }
        for (int i = 0; i < _height; i++) {
            layout[0][i] = LayoutType.WallVertical;
            layout[_width-1][i] = LayoutType.WallVertical;
        }
    }

    public void setBounds(RectF bounds) {
        _bounds = bounds;
        _cellWidth = bounds.width() / _width;
        _cellHeight = bounds.height() / _height;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(_bounds, _backgroundPaint);

        for (int i = 0; i < _width; i++) {
            for (int j = 0; j < _height; j++) {
                if (layout[i][j] == LayoutType.WallVertical) {
                    float startX = _cellWidth*i+_cellWidth/2;
                    float startY = _cellHeight*j;
                    float stopX = startX;
                    float stopY = startY*(j+1);
                    canvas.drawLine(startX, startY, stopX, stopY, _wallPaint);
                } else if (layout[i][j] == LayoutType.WallHorizontal) {
                    float startX = _cellWidth*i;
                    float startY = _cellHeight*j+_cellWidth/2;
                    float stopX = _cellWidth*(i+1);
                    float stopY = startY;
                    canvas.drawLine(startX, startY, stopX, stopY, _wallPaint);
                }
            }
        }
    }
}
