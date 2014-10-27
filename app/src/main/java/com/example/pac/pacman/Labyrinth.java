package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Labyrinth {

    private int _width;
    private int _height;
    private Paint _debugPaint;

    public RectF getBounds() {
        return _bounds;
    }

    final int WALL = 1;

    private int layout[][];

    private RectF _bounds;
    private float _cellSize;

    private Paint _wallPaint;

    public Labyrinth(Resources resources) {
        String levelResource = resources.getString(R.string.level_classic);
        initFromResource(levelResource);

        _wallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _wallPaint.setColor(resources.getColor(R.color.walls));
        _wallPaint.setStrokeWidth(5);

        _debugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _debugPaint.setStyle(Paint.Style.STROKE);
        _debugPaint.setColor(Color.RED);
    }

    private void initFromResource(String levelResource) {
        String[] rows = levelResource.trim().split(" ");
        _width = rows[0].length();
        _height = rows.length;
        layout = new int[_width][_height];
        for (int w = 0; w < _width; w++) {
            for (int h = 0; h < _height; h++) {
                layout[w][h] = Integer.parseInt(rows[h].substring(w, w + 1));
            }
        }
    }

    public float getCellSize() {
        return _cellSize;
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

    public boolean canMove(RectF objectBounds) {
        return canMove(objectBounds.left, objectBounds.top) &&
                canMove(objectBounds.left, objectBounds.bottom) &&
                canMove(objectBounds.right, objectBounds.top) &&
                canMove(objectBounds.right, objectBounds.bottom);
    }

    public boolean canMove(float x, float y) {
        int cell = cellAt(x, y);
        return cell == 0;
    }

    private int cellAt(float x, float y) {
        int cellX = (int) ((x - _bounds.left) / _cellSize);
        int cellY = (int) ((y - _bounds.top) / _cellSize);

        if (cellX >= _width) {
            cellX = 0;
        } else if (cellX < 0) {
            cellX = _width - 1;
        }
        if (cellY >= _height) {
            cellY = 0;
        } else if (cellY < 0) {
            cellY = _height - 1;
        }
        return layout[cellX][cellY];
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < _width; i++) {
            for (int j = 0; j < _height; j++) {
                if (layout[i][j] == WALL) {
                    float startX = _cellSize * i + _bounds.left;
                    float startY = _cellSize * j + _bounds.top;
                    canvas.drawRect(startX, startY, startX + _cellSize, startY + _cellSize, _wallPaint);
                }
                //--Grid for Debugging
                //float l = i * _cellSize + _bounds.left;
                //float t = j * _cellSize + _bounds.top;
                //canvas.drawRect(l, t, l + _cellSize, t + _cellSize, _debugPaint);

            }
        }
    }

}
