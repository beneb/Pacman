package com.example.pac.pacman;

import android.graphics.Rect;

import junit.framework.TestCase;

public class LabyrinthTests extends TestCase {
    private Labyrinth _labyrinth;

    @Override
    public void setUp() throws Exception {
        _labyrinth = new Labyrinth("101 P00 101", 0);
        _labyrinth.init(new Rect(0, 0, 30, 30));
    }

    public void testCellSize() {
        assertEquals(10, _labyrinth.getCellSize());
    }
    public void testCellFromCoordinates() {
        assertEquals(0, _labyrinth.cellAt(1, 1));
        assertEquals(4, _labyrinth.cellAt(15, 15));
    }

    public void testGetLabyrinthState() {
        assertEquals("101 P00 101", _labyrinth.getState());
    }
}
