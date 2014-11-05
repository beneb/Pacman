package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

public class Labyrinth {

    public class Cell {
        private int row;
        private int col;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        @Override
        public String toString() {
            return String.format("{row = %d, col = %d}", row, col);
        }

        public boolean equals(Cell c) {
            return c != null && col == c.col && row == c.row;
        }
    }

    private int _width;
    private int _height;
    private Paint _debugPaint;

    public Rect getBounds() {
        return _bounds;
    }

    final int WALL = 1;
    final int PAC_MAN = 0x8000;

    private Cell _pacManCell;

    private int layout[][];

    private Rect _bounds;
    private int _cellSize;

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
                String cellValue = rows[h].substring(w, w + 1);
                if (cellValue.equals("P")) {
                    layout[w][h] = 0;
                    _pacManCell = new Cell(h, w);
                } else {
                    layout[w][h] = Integer.parseInt(cellValue);
                }
            }
        }
    }

    public Cell getPacManCell() {
        return _pacManCell;
    }

    public void setPacManPosition(float x, float y) {
        Cell c = cellAt(x, y);
        if (!_pacManCell.equals(c)) {
            Log.d("Labyrinth", "Pac-Man Cell: " + c);
            _pacManCell = c;
        }
    }

    public int getCellSize() {
        return _cellSize;
    }

    public void init(Rect bounds) {
        int width = bounds.width() / _width;
        int height = bounds.height() / _height;
        _cellSize = Math.min(width, height);
        _bounds = new Rect(bounds.left,
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

    public boolean canMove(float targetX, float targetY) {
        int cell = cellValueAt(targetX, targetY);
        return cell == 0;
    }

    private int getCellValue(Cell cell) {
        return layout[cell.col][cell.row];
    }


    private int cellValueAt(float x, float y) {
        Cell cell = cellAt(x, y);
        return getCellValue(cell);
    }

    public Cell cellAt(float x, float y) {
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
        return new Cell(row, col);
    }

    public boolean canMove(Cell cell, Direction direction) {
        switch (direction) {
            case Stopped:
                return false;
            case Left:
                return cell.col > 0 && getCellValue(new Cell(cell.row, cell.col - 1)) == 0;
            case Right:
                return cell.col < _width - 1 && getCellValue(new Cell(cell.row, cell.col + 1)) == 0;
            case Up:
                return cell.row > 0 && getCellValue(new Cell(cell.row - 1, cell.col)) == 0;
            case Down:
                return cell.row < _height - 1 && getCellValue(new Cell(cell.row + 1, cell.col)) == 0;
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
    public RectF getCellBounds(int col, int row) {
        float startX = _cellSize * col + _bounds.left;
        float startY = _cellSize * row + _bounds.top;
        return new RectF(startX, startY, startX + _cellSize, startY + _cellSize);
    }
}
