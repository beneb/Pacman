package com.example.pac.pacman;

import android.graphics.PointF;

import junit.framework.TestCase;

public class CharacterTests extends TestCase {
    public void testMoveUpToTheWall() {
        PacManEnvironment p = new PacManEnvironment("111 110 10P 110", 40, 40);
        p.getMoveStrategy().setWishDirection(Direction.Up);
        p.movePacManToTheWall();
        assertEquals(new PointF(25, 15), p.getPacManCoordinates());
    }

    public void testMoveLeftToTheWall() {
        PacManEnvironment p = new PacManEnvironment("111 110 10P 110", 40, 40);
        p.getMoveStrategy().setWishDirection(Direction.Left);
        p.movePacManToTheWall();
        assertEquals(new PointF(15, 25), p.getPacManCoordinates());
    }

    public void testTeleportationLeft() {
        PacManEnvironment p = new PacManEnvironment("P00", 30, 10);
        p.getMoveStrategy().setWishDirection(Direction.Left);
        p.moveToTheNextCell();
        assertEquals(2, p.getPacManPosition());
    }

    public void testTeleportationRight() {
        PacManEnvironment p = new PacManEnvironment("00P", 30, 10);
        p.getMoveStrategy().setWishDirection(Direction.Right);
        p.moveToTheNextCell();
        assertEquals(0, p.getPacManPosition());
    }
}
