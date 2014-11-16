package com.example.pac.pacman;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class Labyrinth {

    final int DOT = 0;
    final int WALL = 1;

    private int _layout[][];
    private int _width;
    private int _height;
    private float _cellSize;
    private int _pacManCell;

    public float getCellSize() {
        return _cellSize;
    }

    public int getPacManCell() {
        return _pacManCell;
    }

    private RectF _bounds;

    public RectF getBounds() {
        return _bounds;
    }


    private Paint _dot;
    private Paint _wallPaint;
    private Paint _debugPaint;

    public Labyrinth(String state, int wallColor) {
        load(state);

        _wallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        _wallPaint.setStrokeWidth(1.5f);
        _wallPaint.setColor(wallColor);

        _dot = new Paint(Paint.ANTI_ALIAS_FLAG);
        _dot.setColor(Color.YELLOW);
        _dot.setStrokeWidth(5);


        _debugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _debugPaint.setStyle(Paint.Style.STROKE);
        _debugPaint.setColor(Color.RED);
    }

    private void load(String state) {
        String[] rows = state.trim().split(" ");
        _width = rows[0].length();
        _height = rows.length;
        _layout = new int[_width][_height];
        for (int w = 0; w < _width; w++) {
            for (int h = 0; h < _height; h++) {
                String cellValue = rows[h].substring(w, w + 1);
                if (cellValue.equals("P")) {
                    _layout[w][h] = 0;
                    _pacManCell = getCell(w, h);
                } else {
                    _layout[w][h] = Integer.parseInt(cellValue);
                }
            }
        }
    }

    public String getState() {
        StringBuilder sb = new StringBuilder(_width * _height + 1);
        for (int row = 0; row < _height; row++) {
            for (int col = 0; col < _width; col++) {
                int cell = getCell(col, row);
                if (cell == _pacManCell) {
                    sb.append("P");
                } else {
                    sb.append(getCellValue(row, col));
                }
            }
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    public void init(RectF bounds) {
        float width = bounds.width() / _width;
        float height = bounds.height() / _height;
        _cellSize = Math.min(width, height);
        Log.i("Labyrinth", "Cell size: " + _cellSize);
        _bounds = new RectF(bounds.left,
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

    private int getCellValue(int row, int col) {
        return row < 0 || row >= _height || col < 0 || col >= _width
                ? 0
                : _layout[col][row];
    }

    public boolean canMove(int currentCell, Direction direction) {
        int row = getCellRow(currentCell);
        int col = getCellCol(currentCell);
        switch (direction) {
            case Stopped:
                return false;
            case Left:
                return getCellValue(row, col - 1) == 0;
            case Right:
                return getCellValue(row, col + 1) == 0;
            case Up:
                return getCellValue(row - 1, col) == 0;
            case Down:
                return getCellValue(row + 1, col) == 0;
        }
        return false;
    }

    public void draw(Canvas canvas) {
        for (int col = 0; col < _width; col++) {
            for (int row = 0; row < _height; row++) {
                if (_layout[col][row] == WALL) {
                    RectF cellBounds = getCellBounds(col, row);
                    canvas.drawRect(cellBounds, _wallPaint);
                }
                if (_layout[col][row] == DOT) {
                    float startX = _cellSize * col + _cellSize/2 + _bounds.left;
                    float startY = _cellSize * row + _cellSize/2 + _bounds.top;
                    canvas.drawCircle(startX, startY, 5, _dot);
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
