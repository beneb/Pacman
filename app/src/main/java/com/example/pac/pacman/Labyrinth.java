package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.example.pac.pacman.event.DotEvent;
import com.example.pac.pacman.event.EventListener;
import com.example.pac.pacman.event.EventManager;

public class Labyrinth {

    private EventManager _eventManager;

    final int DOT = 0;
    final int WALL = 1;
    final int EMPTY = 2;

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
    private Paint _empty;
    private Paint _wallPaint;
    private Paint _debugPaint;

    public Labyrinth(String state, Resources resource) {
        load(state);
        if (resource != null) {
            _dot = PaintObjectsFactory.createDot(resource.getColor(R.color.dot));
            _empty = PaintObjectsFactory.createEmptyDot(resource.getColor(R.color.empty_dot));
            _wallPaint = PaintObjectsFactory.createWall(resource.getColor(R.color.walls));
            // _debugPaint = PaintObjectsFactory.createDebugPaint(Color.RED);
        }

        _eventManager = new EventManager();
    }

    public void registerScoreListener(EventListener<DotEvent> dotEventEventListener){
        _eventManager.registerObserver(DotEvent.class, dotEventEventListener);
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
                    _pacManCell = getCell(h, w);
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
                int cell = getCell(row, col);
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

        // Set here the labyrinth layout to empty, pacman is eating dots here.
        fireDotEvent(c);
        setCellValue(c, EMPTY);

        if (_pacManCell != c) {
            Log.d("Labyrinth", "Pac-Man Cell: " + c);
            _pacManCell = c;
        }
    }

    private void fireDotEvent(int cellNum) {
        int col = getCellCol(cellNum);
        int row = getCellRow(cellNum);

        if (_layout[col][row] == DOT) {
            _eventManager.fire(new DotEvent());
        }
    }

    private int getRow(float y) {
        return (int) ((y - _bounds.top) / _cellSize);
    }

    private int getCol(float x) {
        return (int) ((x - _bounds.left) / _cellSize);
    }

    private int getCell(int row, int col) {
        return row * _width + col;
    }

    private int getCellRow(int cellNum) {
        return cellNum / _width;
    }

    private int getCellCol(int cellNum) {
        return cellNum % _width;
    }

    public int cellAt(float x, float y) {
        int col = getCol(x);
        int row = getRow(y);

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
        return getCell(row, col);
    }

    private int getCellValue(int row, int col) {
        return row < 0 || row >= _height || col < 0 || col >= _width
                ? 0
                : _layout[col][row];
    }

    private void setCellValue(int cellNum, int value) {
        int col = getCellCol(cellNum);
        int row = getCellRow(cellNum);

        if (row >= 0 && row < _height && col >= 0 && col < _width) {
            _layout[col][row] = value;
        }
    }

    public boolean canMove(int currentCell, Direction direction) {
        int row = getCellRow(currentCell);
        int col = getCellCol(currentCell);
        switch (direction) {
            case Stopped:
                return false;
            case Left:
                return canMoveForCell(row, col - 1);
            case Right:
                return canMoveForCell(row, col + 1);
            case Up:
                return canMoveForCell(row - 1, col);
            case Down:
                return canMoveForCell(row + 1, col);
        }
        return false;
    }

    private boolean canMoveForCell(int row, int col) {
        int cellValue = getCellValue(row, col);
        return cellValue == EMPTY || cellValue == DOT;
    }

    public void draw(Canvas canvas) {
        for (int col = 0; col < _width; col++) {
            for (int row = 0; row < _height; row++) {
                if (getCellValue(row, col) == WALL) {
                    RectF cellBounds = getCellBounds(row, col);
                    canvas.drawRect(cellBounds, _wallPaint);
                }
                if (getCellValue(row, col) == DOT) {
                    float startX = _cellSize * col + _cellSize / 2 + _bounds.left;
                    float startY = _cellSize * row + _cellSize / 2 + _bounds.top;
                    canvas.drawCircle(startX, startY, 5, _dot);
                }
                if (getCellValue(row, col) == EMPTY) {
                    float startX = _cellSize * col + _cellSize / 2 + _bounds.left;
                    float startY = _cellSize * row + _cellSize / 2 + _bounds.top;
                    canvas.drawCircle(startX, startY, 5, _empty);
                }

                //--Grid for Debugging
                // float l = col * _cellSize + _bounds.left;
                // float t = row * _cellSize + _bounds.top;
                // canvas.drawRect(l, t, l + _cellSize, t + _cellSize, _debugPaint);
            }
        }
    }

    private RectF getCellBounds(int row, int col) {
        float startX = _cellSize * col + _bounds.left;
        float startY = _cellSize * row + _bounds.top;
        return new RectF(startX, startY, startX + _cellSize, startY + _cellSize);
    }

    public RectF getCellBounds(int cellNum) {
        int row = getCellRow(cellNum);
        int col = getCellCol(cellNum);
        return getCellBounds(row, col);
    }
}
