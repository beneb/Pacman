package com.example.pac.pacman;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Labyrinth {

    public enum Item {
        EMPTY(0),
        ORDINAL_WALL(1),
        HORIZONTAL_WALL(4),
        DOT(2),
        ENERGIZER(3);


        private final int _value;

        Item(int value) {
            _value = value;
        }

        public boolean isWall() {
            return this == ORDINAL_WALL || this == HORIZONTAL_WALL;
        }
        public boolean isDot() {
            return this == DOT || this == ENERGIZER;
        }

        public boolean isNotWall() {
            return this == EMPTY || this == DOT || this == ENERGIZER;
        }

        public static Item parse(int val) {
            for (Item i : values()) {
                if (i._value == val) {
                    return i;
                }
            }
            return EMPTY;
        }

        public int asInt() {
            return _value;
        }
    }

    private Item _layout[][];
    private RectF _bounds[][];
    private int _width;
    public int getWidth() {return _width;}
    private int _height;
    public int getHeight() {return _height;}
    private float _cellSize;
    private int _dotsCount;

    private Map<java.lang.Character, Integer> _initialCharacterPositions = new HashMap<>();
    private Map<java.lang.Character, Integer> _characterPositions = new HashMap<>();

    public float getCellSize() {
        return _cellSize;
    }

    private RectF _labyrinthBounds;

    public RectF getBounds() {
        return _labyrinthBounds;
    }


    public Labyrinth(String state) {
        load(state);
    }

    public void load(String state) {
        String[] rows = state.trim().split(" ");
        _width = rows[0].length();
        _height = rows.length;
        _layout = new Item[_height][_width];
        _dotsCount = 0;
        for (int row = 0; row < _height; row++) {
            for (int col = 0; col < _width; col++) {
                String cellValue = rows[row].substring(col, col + 1);
                char id = cellValue.charAt(0);
                if (java.lang.Character.isDigit(id)) {
                    Item item = Item.parse(Integer.parseInt(cellValue));
                    _layout[row][col] = item;
                    if (item.isDot()) {
                        _dotsCount++;
                    }
                } else {
                    _layout[row][col] = Item.EMPTY;
                    int cell = getCell(row, col);
                    _initialCharacterPositions.put(id, cell);
                    setCharacterPosition(id, cell);
                }
            }
        }
    }

    public String getState() {
        StringBuilder sb = new StringBuilder(_width * _height + 1);
        for (int row = 0; row < _height; row++) {
            for (int col = 0; col < _width; col++) {
                int cell = getCell(row, col);
                java.lang.Character id = getCharacterCodeForPosition(cell);
                if (id != null) {
                    sb.append(id);
                } else {
                    sb.append(getCellValue(row, col).asInt());
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
        _labyrinthBounds = new RectF(bounds.left,
                bounds.top,
                bounds.left + _width * _cellSize,
                bounds.top + _height * _cellSize);

        _bounds = new RectF[_height][_width];
        for (int row = 0; row < _height; row++) {
            for (int col = 0; col < _width; col++) {
                float startX = _cellSize * col + _labyrinthBounds.left;
                float startY = _cellSize * row + _labyrinthBounds.top;
                _bounds[row][col] = new RectF(startX, startY, startX + _cellSize, startY + _cellSize);
            }
        }
    }

    public void setCharacterPosition(Character ch, int cell) {
        if (getCharacterPosition(ch) != cell) {
            Log.d("Labyrinth", String.format("%s Cell: %d", ch.getName(), cell));
            setCharacterPosition(ch.getId(), cell);
        }
    }

    public void resetAllCharacters() {
        for (Map.Entry<java.lang.Character, Integer> entry : _initialCharacterPositions.entrySet()) {
            setCharacterPosition(entry.getKey(), entry.getValue());
        }
    }
    private void setCharacterPosition(char id, int cell) {
        // Set here the labyrinth layout to empty, pacman is eating dots here.
        _characterPositions.put(id, cell);
    }

    public int getCharacterPosition(Character c) {
        return _characterPositions.containsKey(c.getId())
                ? _characterPositions.get(c.getId())
                : 0; // just somewhere
    }

    public java.lang.Character getCharacterCodeForPosition(int cell) {
        for (Map.Entry<java.lang.Character, Integer> entry : _characterPositions.entrySet()) {
            if (entry.getValue() == cell) {
                return entry.getKey();
            }
        }
        return null;
    }

    private int getRow(float y) {
        return (int) ((y - _labyrinthBounds.top) / _cellSize);
    }

    private int getCol(float x) {
        return (int) ((x - _labyrinthBounds.left) / _cellSize);
    }

    private int getCell(int row, int col) {
        return row * _width + col;
    }

    public int getCellRow(int cellNum) {
        return cellNum / _width;
    }

    public int getCellCol(int cellNum) {
        return cellNum % _width;
    }

    public int cellAt(PointF position) {
        int col = getCol(position.x);
        int row = getRow(position.y);

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

    private Item getCellValue(int cell) {
        if (_width * _height > cell + 1) {
            int row = cell / _width;
            int col = cell % _width;
            return _layout[row][col];
        }

        return Item.EMPTY;
    }

    public Item getCellValue(int row, int col) {
        return row < 0 || row >= _height || col < 0 || col >= _width
                ? Item.EMPTY
                : _layout[row][col];
    }

    private void setCellValue(int cellNum, Item value) {
        int col = getCellCol(cellNum);
        int row = getCellRow(cellNum);

        if (row >= 0 && row < _height && col >= 0 && col < _width) {
            _layout[row][col] = value;
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
        return getCellValue(row, col).isNotWall();
    }

    public boolean tryEatDot(PacMan pacMan) {
        return tryEat(pacMan, Item.DOT);
    }

    public boolean tryEatEnergizer(PacMan pacMan) {
        return tryEat(pacMan, Item.ENERGIZER);
    }

    private boolean tryEat(PacMan pacMan, Item eatable) {
        int pacMansCell = getCharacterPosition(pacMan);
        if (getCellValue(pacMansCell) == eatable) {
            setCellValue(pacMansCell, Item.EMPTY);
            if (eatable.isDot()) {
                _dotsCount--;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean hasDots() {
        return _dotsCount > 0;
    }

    public RectF getCellBounds(int row, int col) {
        return _bounds[row][col];
    }

    public RectF getCellBounds(int cellNum) {
        int row = getCellRow(cellNum);
        int col = getCellCol(cellNum);
        return getCellBounds(row, col);
    }
}
