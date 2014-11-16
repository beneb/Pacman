package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.RectF;

import junit.framework.TestCase;

public class LabyrinthTests extends TestCase {
    private Labyrinth _labyrinth;

    @Override
    public void setUp() throws Exception {
        _labyrinth = new Labyrinth("101 P00 101 101", null);
        _labyrinth.init(new RectF(0, 0, 30, 40));
    }

    public void testCellSize() {
        assertEquals(10f, _labyrinth.getCellSize());
    }
    public void testCellFromCoordinates() {
        assertEquals(0, _labyrinth.cellAt(1, 1));
        assertEquals(4, _labyrinth.cellAt(15, 15));
    }

    public void testGetLabyrinthState() {
        assertEquals("101 P00 101 101", _labyrinth.getState());
    }
    public void testCanMove() {
        assertTrue("101 P00 101 101", _labyrinth.canMove(7, Direction.Down));
    }

    public void testCellBounds() {
        assertEquals(new RectF(0, 0, 10, 10), _labyrinth.getCellBounds(0));
        assertEquals(new RectF(10, 0, 20, 10), _labyrinth.getCellBounds(1));
        assertEquals(new RectF(0, 10, 10, 20), _labyrinth.getCellBounds(3));
    }
}
