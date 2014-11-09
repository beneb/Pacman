package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class Labyrinth {

    final int WALL = 1;

    private int layout[][];
    private int _width;
    private int _height;
    private int _cellSize;
    private int _pacManCell;

    public int getCellSize() {
        return _cellSize;
    }
    public int getPacManCell() {
        return _pacManCell;
    }

    private Rect _bounds;

    public Rect getBounds() {
        return _bounds;
    }

    private Paint _wallPaint;
    private Paint _debugPaint;

    public Labyrinth(String state, int wallColor) {
        load(state);

        _wallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _wallPaint.setColor(wallColor);
        _wallPaint.setStrokeWidth(5);

        _debugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _debugPaint.setStyle(Paint.Style.STROKE);
        _debugPaint.setColor(Color.RED);
    }

    private void load(String state) {
        String[] rows = state.trim().split(" ");
        _width = rows[0].length();
        _height = rows.length;
        layout = new int[_width][_height];
        for (int w = 0; w < _width; w++) {
            for (int h = 0; h < _height; h++) {
                String cellValue = rows[h].substring(w, w + 1);
                if (cellValue.equals("P")) {
                    layout[w][h] = 0;
                    _pacManCell = getCell(w, h);
                } else {
                    layout[w][h] = Integer.parseInt(cellValue);
                }
            }
        }
    }

    public void init(Rect bounds) {
        int width = bounds.width() / _width;
        int height = bounds.height() / _height;
        _cellSize = Math.min(width, height);
        Log.i("Labyrinth", "Cell size: " + _cellSize);
        _bounds = new Rect(bounds.left,
                bounds.top,
                bounds.left + _width * _cellSize,
                bounds.top + _height * _cellSize);
    }

    public void setPacManPosition(float x, float y) {
        int c = cellAt(x, y);
        if (_pacManCell != c) {
            Log.d("Labyrinth", "Pac-Man Cell: " + c);
            _pacManCell = c;
        }
    }

    private int getCell(int col, int row) {
        return row * _width + col;
    }

    private int getCellRow(int cellNum) {
        return cellNum / _width;
    }

    private int getCellCol(int cellNum) {
        return cellNum % _width;
    }

    public int cellAt(float x, float y) {
        int col = (int) ((x - _bounds.left) / _cellSize);
        int row = (int) ((y - _bounds.top) / _cellSize);

        if (col >= _width) {
            col = 0;
        } else if (col < 0) {
            col = _width - 1;
        }
        if (row >= _height) {
            row = 0;
        } else if (row < 0) {
            row = _height - 1;
        }
        return getCell(col, row);
    }

    private int getCellValue(int cellNum) {
        int row = getCellRow(cellNum);
        int col = getCellCol(cellNum);
        return row < 0 || row >= _width || col < 0 || col >= _height
                ? WALL
                : layout[col][row];
    }

    public boolean canMove(float targetX, float targetY) {
        int cellValue = getCellValue(cellAt(targetX, targetY));
        return cellValue == 0;
    }

    public boolean canMove(int cell, Direction direction) {
        switch (direction) {
            case Stopped:
                return false;
            case Left:
                return getCellValue(cell - 1) == 0;
            case Right:
                return getCellValue(cell + 1) == 0;
            case Up:
                return getCellValue(cell - _width) == 0;
            case Down:
                return getCellValue(cell + _width) == 0;
        }
        return false;
    }

    public void draw(Canvas canvas) {
        for (int col = 0; col < _width; col++) {
            for (int row = 0; row < _height; row++) {
                if (layout[col][row] == WALL) {
                    RectF cellBounds = getCellBounds(col, row);
                    canvas.drawRect(cellBounds, _wallPaint);
                }
                //--Grid for Debugging
                //float l = col * _cellSize + _bounds.left;
                //float t = row * _cellSize + _bounds.top;
                //canvas.drawRect(l, t, l + _cellSize, t + _cellSize, _debugPaint);
            }
        }
    }

    private RectF getCellBounds(int col, int row) {
        float startX = _cellSize * col + _bounds.left;
        float startY = _cellSize * row + _bounds.top;
        return new RectF(startX, startY, startX + _cellSize, startY + _cellSize);
    }

    public RectF getCellBounds(int cellNum) {
        int row = getCellRow(cellNum);
        int col = getCellCol(cellNum);
        return getCellBounds(col, row);
    }
}
