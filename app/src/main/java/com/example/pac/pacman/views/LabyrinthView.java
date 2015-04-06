package com.example.pac.pacman.views;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.pac.pacman.Labyrinth;
import com.example.pac.pacman.R;

public class LabyrinthView implements IChildView {
    private final Labyrinth _labyrinth;
    private Paint _dot;
    private Paint _energizer;
    private Paint _wallPaint;
    // private Paint _debugPaint;

    public LabyrinthView(Labyrinth labyrinth, Resources resource) {
        _labyrinth = labyrinth;
        _dot = PaintObjectsFactory.createDot(resource.getColor(R.color.dot));
        _energizer = PaintObjectsFactory.createEnergizer(resource.getColor(R.color.dot));
        _wallPaint = PaintObjectsFactory.createWall(resource.getColor(R.color.walls));
        // _debugPaint = PaintObjectsFactory.createDebugPaint(Color.RED);
    }

    @Override
    public void onSizeChanged() { }

    public void draw(Canvas canvas) {

        int width = _labyrinth.getWidth();
        int height = _labyrinth.getHeight();

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                RectF cellBounds = _labyrinth.getCellBounds(row, col);
                Labyrinth.Item cellValue = _labyrinth.getCellValue(row, col);
                if (cellValue.isWall()) {
                    drawWall(row, col, canvas);
                } else if (cellValue == Labyrinth.Item.DOT) {
                    float startX = cellBounds.centerX();
                    float startY = cellBounds.centerY();
                    canvas.drawCircle(startX, startY, getDotSize(), _dot);
                } else if (cellValue == Labyrinth.Item.ENERGIZER) {
                    float startX = cellBounds.centerX();
                    float startY = cellBounds.centerY();
                    canvas.drawCircle(startX, startY, getEnergizerSize(), _energizer);
                }

                //--Grid for Debugging
                // float l = col * _labyrinth.getCellSize() + _labyrinth.getBounds().left;
                // float t = row * _labyrinth.getCellSize() + _labyrinth.getBounds().top;
                // canvas.drawRect(l, t, l + _labyrinth.getCellSize(), t + _labyrinth.getCellSize(), _debugPaint);
            }
        }
    }

    private final RectF _drawRect = new RectF();

    private void drawWall(int row, int col, Canvas canvas) {
        // seems to be very complex and not comprehensible
        // TODO: will try to introduce custom wall types in layout
        // this would allow to reduce ifs
        // but the drawing logic still stays complex
        RectF cellBounds = _labyrinth.getCellBounds(row, col);
        Labyrinth.Item cell = _labyrinth.getCellValue(row, col);
        boolean wallLeft = _labyrinth.getCellValue(row, col - 1).isWall();
        boolean wallLeftTop = _labyrinth.getCellValue(row - 1, col - 1).isWall();
        boolean wallTop = _labyrinth.getCellValue(row - 1, col).isWall();
        boolean wallRightTop = _labyrinth.getCellValue(row - 1, col + 1).isWall();
        boolean wallRight = _labyrinth.getCellValue(row, col + 1).isWall();
        boolean wallRightBottom = _labyrinth.getCellValue(row + 1, col + 1).isWall();
        boolean wallBottom = _labyrinth.getCellValue(row + 1, col).isWall();
        boolean wallLeftBottom = _labyrinth.getCellValue(row + 1, col - 1).isWall();

        float smallShift = cellBounds.width() / 4;
        float bigShift = 3 * smallShift;

        if (cell == Labyrinth.Item.HORIZONTAL_WALL || wallLeft && wallRight) {
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
        } else if (wallLeft && wallTop) {
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

            _drawRect.set(cellBounds.left + smallShift, cellBounds.top + cellBounds.height() / 2 - smallShift, cellBounds.right - smallShift, cellBounds.top + cellBounds.height() / 2 + smallShift);
            canvas.drawArc(_drawRect, 0, 180, false, _wallPaint);
        } else if (wallRight) {
            float firstLine = cellBounds.top + smallShift;
            float secondLine = cellBounds.top + bigShift;

            canvas.drawLine(cellBounds.left + cellBounds.height() / 2, firstLine, cellBounds.right, firstLine, _wallPaint);
            canvas.drawLine(cellBounds.left + cellBounds.height() / 2, secondLine, cellBounds.right, secondLine, _wallPaint);

            _drawRect.set(cellBounds.left + cellBounds.height() / 2 - smallShift, cellBounds.top + smallShift, cellBounds.left + cellBounds.height() / 2 + smallShift, cellBounds.bottom - smallShift);
            canvas.drawArc(_drawRect, 90, 180, false, _wallPaint);
        } else if (wallBottom) {
            float firstLineX = cellBounds.left + smallShift;
            float secondLineX = cellBounds.left + bigShift;
            canvas.drawLine(firstLineX, cellBounds.top + cellBounds.height() / 2, firstLineX, cellBounds.bottom, _wallPaint);
            canvas.drawLine(secondLineX, cellBounds.top + cellBounds.height() / 2, secondLineX, cellBounds.bottom, _wallPaint);

            _drawRect.set(cellBounds.left + smallShift, cellBounds.top + cellBounds.height() / 2 - smallShift, cellBounds.right - smallShift, cellBounds.top + cellBounds.height() / 2 + smallShift);
            canvas.drawArc(_drawRect, 180, 180, false, _wallPaint);
        } else if (wallLeft) {
            float firstLine = cellBounds.top + smallShift;
            float secondLine = cellBounds.top + bigShift;

            canvas.drawLine(cellBounds.left, firstLine, cellBounds.left + cellBounds.height() / 2, firstLine, _wallPaint);
            canvas.drawLine(cellBounds.left, secondLine, cellBounds.left + cellBounds.height() / 2, secondLine, _wallPaint);

            _drawRect.set(cellBounds.left + cellBounds.height() / 2 - smallShift, cellBounds.top + smallShift, cellBounds.left + cellBounds.height() / 2 + smallShift, cellBounds.bottom - smallShift);
            canvas.drawArc(_drawRect, 270, 180, false, _wallPaint);
        }
    }

    private void drawRoundCorner(Canvas canvas, float left, float top, float width, float angle) {
        _drawRect.set(left, top, left + width, top + width);
        canvas.drawArc(_drawRect, angle, 90, false, _wallPaint);
        float left1 = _drawRect.left;
        float left2 = _drawRect.left;
        float top1 = _drawRect.top;
        float top2 = _drawRect.top;


        if (angle == 0) {
            top1 = _drawRect.bottom;
            left2 = _drawRect.right;
        } else if (angle == 90) {
            top1 = _drawRect.bottom;
            left1 = _drawRect.left + _drawRect.width() / 2;
        } else if (angle == 180) {
            left1 = _drawRect.left + _drawRect.width() / 2;
            top2 = _drawRect.top + _drawRect.height() / 2;
        } else {
            left2 = _drawRect.right;
            top2 = _drawRect.top + _drawRect.height() / 2;
        }
        float right1 = left1 + _drawRect.width() / 2;
        float right2 = left2;
        float bottom1 = top1;
        float bottom2 = top2 + _drawRect.height() / 2;

        canvas.drawLine(left1, top1, right1, bottom1, _wallPaint);
        canvas.drawLine(left2, top2, right2, bottom2, _wallPaint);
    }

    private float getDotSize() {
        return _labyrinth.getCellSize() / 14;
    }

    private float getEnergizerSize() {
        return _labyrinth.getCellSize() / 4;
    }

    public void writeGameOver(Canvas canvas, int width) {
        Paint p = PaintObjectsFactory.createPaintForGameOver(Color.RED);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(60);

        RectF xy = _labyrinth.getCellBounds(12, 8);
        int x = width /2;
        int y = (int)xy.bottom;

        canvas.drawText("Game Over", x, y, p);
    }
}
