package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Labyrinth {

    enum Item {
        EMPTY(0),
        ORDINAL_WALL(1),
        HORIZONTAL_WALL(4),
        DOT(2),
        BIG_DOT(3);


        private final int _value;
        Item(int value) {
            _value = value;
        }

        public boolean isWall(){
            return this == ORDINAL_WALL || this == HORIZONTAL_WALL;
        }

        public boolean isNotWall(){
            return this == EMPTY || this == DOT || this == BIG_DOT;
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
    private int _width;
    private int _height;
    private float _cellSize;

    private Map<java.lang.Character, Integer> _characterPositions = new HashMap<java.lang.Character, Integer>();

    public float getCellSize() {
        return _cellSize;
    }

    private RectF _bounds;

    public RectF getBounds() {
        return _bounds;
    }


    private Paint _dot;
    private Paint _big_dot;
    private Paint _wallPaint;
    private Paint _debugPaint;

    public Labyrinth(String state, Resources resource) {
        load(state);
        if (resource != null) {
            _dot = PaintObjectsFactory.createDot(resource.getColor(R.color.dot));
            _big_dot = PaintObjectsFactory.createBigDot(resource.getColor(R.color.dot));
            _wallPaint = PaintObjectsFactory.createWall(resource.getColor(R.color.walls));
            _debugPaint = PaintObjectsFactory.createDebugPaint(Color.RED);
        }
    }

    private void load(String state) {
        String[] rows = state.trim().split(" ");
        _width = rows[0].length();
        _height = rows.length;
        _layout = new Item[_width][_height];
        for (int w = 0; w < _width; w++) {
            for (int h = 0; h < _height; h++) {
                String cellValue = rows[h].substring(w, w + 1);
                if (java.lang.Character.isDigit(cellValue.charAt(0))) {
                    _layout[w][h] = Item.parse(Integer.parseInt(cellValue));
                } else {
                    _layout[w][h] = Item.EMPTY;
                    setCharacterPosition(cellValue.charAt(0), getCell(h, w));
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
        _bounds = new RectF(bounds.left,
                bounds.top,
                bounds.left + _width * _cellSize,
                bounds.top + _height * _cellSize);
    }

    public void setCharacterPosition(Character ch, int cell) {
        if (getCharacterPosition(ch) != cell) {
            Log.d("Labyrinth", String.format("%s Cell: %d", ch.getName(), cell));
            setCharacterPosition(ch.getId(), cell);
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
        return (int) ((y - _bounds.top) / _cellSize);
    }

    private int getCol(float x) {
        return (int) ((x - _bounds.left) / _cellSize);
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

    private Item getCellValue(int cell) {
        if (_width * _height > cell + 1) {
            int row = cell / _width;
            int col = cell % _width;
            return _layout[col][row];
        }

        return Item.EMPTY;
    }

    public Item getCellValue(int row, int col) {
        return row < 0 || row >= _height || col < 0 || col >= _width
                ? Item.EMPTY
                : _layout[col][row];
    }

    private void setCellValue(int cellNum, Item value) {
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
        return getCellValue(row, col).isNotWall();
    }

    public boolean eatDot(PacMan pacMan) { return eat(pacMan, Item.DOT); }

    public boolean eatBigDot(PacMan pacMan) {
        return eat(pacMan, Item.BIG_DOT);
    }

    private boolean eat(PacMan pacMan, Item eatable) {
        int pacMansCell = getCharacterPosition(pacMan);
        if (getCellValue(pacMansCell) == eatable) {
            setCellValue(pacMansCell, Item.EMPTY);
            return true;
        } else {
            return false;
        }
    }

    public void draw(Canvas canvas) {
        for (int col = 0; col < _width; col++) {
            for (int row = 0; row < _height; row++) {
                RectF cellBounds = getCellBounds(row, col);
                if (getCellValue(row, col).isWall()) {
                    drawWall(row, col, canvas);
                }
                if (getCellValue(row, col) == Item.DOT) {
                    float startX = cellBounds.centerX();
                    float startY = cellBounds.centerY();
                    canvas.drawCircle(startX, startY, getDotSize(), _dot);
                }
                if (getCellValue(row, col) == Item.BIG_DOT) {
                    float startX = cellBounds.centerX();
                    float startY = cellBounds.centerY();
                    canvas.drawCircle(startX, startY, getBigDotSize(), _big_dot);
                }

                //--Grid for Debugging
                //float l = col * _cellSize + _bounds.left;
                //float t = row * _cellSize + _bounds.top;
                //canvas.drawRect(l, t, l + _cellSize, t + _cellSize, _debugPaint);
            }
        }
    }

    private void drawWall(int row, int col, Canvas canvas) {
        RectF cellBounds = getCellBounds(row, col);
        Item cell = getCellValue(row, col);
        boolean wallLeft = getCellValue(row, col - 1).isWall();
        boolean wallLeftTop = getCellValue(row - 1, col - 1).isWall();
        boolean wallTop = getCellValue(row - 1, col).isWall();
        boolean wallRightTop = getCellValue(row - 1, col + 1).isWall();
        boolean wallRight = getCellValue(row, col + 1).isWall();
        boolean wallRightBottom = getCellValue(row + 1, col + 1).isWall();
        boolean wallBottom = getCellValue(row + 1, col).isWall();
        boolean wallLeftBottom = getCellValue(row + 1, col - 1).isWall();

        float smallShift = cellBounds.width() / 4;
        float bigShift = 3 * smallShift;

        if (cell == Item.HORIZONTAL_WALL || wallLeft && wallRight) {
            float firstLine = cellBounds.top + smallShift;
            float secondLine = cellBounds.top + bigShift;
            if (!wallTop) {
                canvas.drawLine(cellBounds.left, firstLine, cellBounds.right, firstLine, _wallPaint);
            } else if (!wallLeftTop) {
                drawRoundCorner(canvas, cellBounds.left, cellBounds.top, smallShift, 0);
                drawRoundCorner(canvas, cellBounds.right - smallShift, cellBounds.top, smallShift, 90);
            }
            if (!wallBottom) {
                canvas.drawLine(cellBounds.left, secondLine, cellBounds.right, secondLine, _wallPaint);
            } else if (!wallLeftBottom) {
                drawRoundCorner(canvas, cellBounds.right - smallShift, cellBounds.bottom - smallShift, smallShift, 180);
                drawRoundCorner(canvas, cellBounds.left, cellBounds.bottom - smallShift, smallShift, 270);
            }
        } else if (wallTop && wallBottom) {
            float firstLineX = cellBounds.left + smallShift;
            float secondLineX = cellBounds.left + bigShift;
            if (!wallLeft) {
                canvas.drawLine(firstLineX, cellBounds.top, firstLineX, cellBounds.bottom, _wallPaint);
            } else {
                drawRoundCorner(canvas, cellBounds.left, cellBounds.top, smallShift, 0);
                drawRoundCorner(canvas, cellBounds.left, cellBounds.bottom - smallShift, smallShift, 270);
            }
            if (!wallRight) {
                canvas.drawLine(secondLineX, cellBounds.top, secondLineX, cellBounds.bottom, _wallPaint);
            } else {
                drawRoundCorner(canvas, cellBounds.right - smallShift, cellBounds.top, smallShift, 90);
                drawRoundCorner(canvas, cellBounds.right - smallShift, cellBounds.bottom - smallShift, smallShift, 180);
            }
        } else {
            if (wallLeft && wallTop) {
                drawRoundCorner(canvas, cellBounds.left, cellBounds.top, bigShift, 0);
                if (!wallLeftTop) {
                    drawRoundCorner(canvas, cellBounds.left, cellBounds.top, smallShift, 0);
                }
            } else if (wallTop && wallRight) {
                drawRoundCorner(canvas, cellBounds.right - bigShift, cellBounds.top, bigShift, 90);
                if (!wallRightTop) {
                    drawRoundCorner(canvas, cellBounds.right - smallShift, cellBounds.top, smallShift, 90);
                }
            } else if (wallRight && wallBottom) {
                drawRoundCorner(canvas, cellBounds.right - bigShift, cellBounds.bottom - bigShift, bigShift, 180);
                if (!wallRightBottom) {
                    drawRoundCorner(canvas, cellBounds.right - smallShift, cellBounds.bottom - smallShift, smallShift, 180);
                }
            } else if (wallBottom && wallLeft) {
                drawRoundCorner(canvas, cellBounds.left, cellBounds.bottom - bigShift, bigShift, 270);
                if (!wallLeftBottom) {
                    drawRoundCorner(canvas, cellBounds.left, cellBounds.bottom - smallShift, smallShift, 270);
                }
            } else if (wallTop) {
                float firstLineX = cellBounds.left + smallShift;
                float secondLineX = cellBounds.left + bigShift;
                canvas.drawLine(firstLineX, cellBounds.top, firstLineX, cellBounds.top + cellBounds.height() / 2, _wallPaint);
                canvas.drawLine(secondLineX, cellBounds.top, secondLineX, cellBounds.top + cellBounds.height() / 2, _wallPaint);

                RectF bounds = new RectF(cellBounds.left + smallShift, cellBounds.top + cellBounds.height() / 2 - smallShift, cellBounds.right - smallShift, cellBounds.top + cellBounds.height() / 2 + smallShift);
                canvas.drawArc(bounds, 0, 180, false, _wallPaint);
            } else if (wallRight) {
                float firstLine = cellBounds.top + smallShift;
                float secondLine = cellBounds.top + bigShift;

                canvas.drawLine(cellBounds.left + cellBounds.height() / 2, firstLine, cellBounds.right, firstLine, _wallPaint);
                canvas.drawLine(cellBounds.left + cellBounds.height() / 2, secondLine, cellBounds.right, secondLine, _wallPaint);

                RectF bounds = new RectF(cellBounds.left + cellBounds.height() / 2 - smallShift, cellBounds.top + smallShift, cellBounds.left + cellBounds.height() / 2 + smallShift, cellBounds.bottom - smallShift);
                canvas.drawArc(bounds, 90, 180, false, _wallPaint);
            } else if (wallBottom) {
                float firstLineX = cellBounds.left + smallShift;
                float secondLineX = cellBounds.left + bigShift;
                canvas.drawLine(firstLineX, cellBounds.top + cellBounds.height() / 2, firstLineX, cellBounds.bottom, _wallPaint);
                canvas.drawLine(secondLineX, cellBounds.top + cellBounds.height() / 2, secondLineX, cellBounds.bottom, _wallPaint);

                RectF bounds = new RectF(cellBounds.left + smallShift, cellBounds.top + cellBounds.height() / 2 - smallShift, cellBounds.right - smallShift, cellBounds.top + cellBounds.height() / 2 + smallShift);
                canvas.drawArc(bounds, 180, 180, false, _wallPaint);
            } else if (wallLeft) {
                float firstLine = cellBounds.top + smallShift;
                float secondLine = cellBounds.top + bigShift;

                canvas.drawLine(cellBounds.left, firstLine, cellBounds.left + cellBounds.height() / 2, firstLine, _wallPaint);
                canvas.drawLine(cellBounds.left, secondLine, cellBounds.left + cellBounds.height() / 2, secondLine, _wallPaint);

                RectF bounds = new RectF(cellBounds.left + cellBounds.height() / 2 - smallShift, cellBounds.top + smallShift, cellBounds.left + cellBounds.height() / 2 + smallShift, cellBounds.bottom - smallShift);
                canvas.drawArc(bounds, 270, 180, false, _wallPaint);
            }
        }
    }

    private void drawRoundCorner(Canvas canvas, float left, float top, float width, float angle) {
        RectF bounds = new RectF(left, top, left + width, top + width);
        canvas.drawArc(bounds, angle, 90, false, _wallPaint);
        float left1 = bounds.left;
        float left2 = bounds.left;
        float top1 = bounds.top;
        float top2 = bounds.top;


        if (angle == 0) {
            top1 = bounds.bottom;
            left2 = bounds.right;
        } else if (angle == 90) {
            top1 = bounds.bottom;
            left1 = bounds.left + bounds.width() / 2;
        } else if (angle == 180) {
            left1 = bounds.left + bounds.width() / 2;
            top2 = bounds.top + bounds.height() / 2;
        } else {
            left2 = bounds.right;
            top2 = bounds.top + bounds.height() / 2;
        }
        float right1 = left1 + bounds.width() / 2;
        float right2 = left2;
        float bottom1 = top1;
        float bottom2 = top2 + bounds.height() / 2;

        canvas.drawLine(left1, top1, right1, bottom1, _wallPaint);
        canvas.drawLine(left2, top2, right2, bottom2, _wallPaint);
    }

    private float getDotSize() {
        return _cellSize / 14;
    }

    private float getBigDotSize() {
        return _cellSize / 4;
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
