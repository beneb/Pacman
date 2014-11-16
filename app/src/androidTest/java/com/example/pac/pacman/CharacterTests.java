package com.example.pac.pacman;

import android.graphics.PointF;
import android.graphics.Rect;

import junit.framework.TestCase;

public class CharacterTests extends TestCase {

    private PacMan _pacMan;

    @Override
    protected void setUp() throws Exception {
        _pacMan = initFromLabyrinth_4x4("111 110 10P 110");
    }

    private PacMan initFromLabyrinth_4x4(String state) {
        Labyrinth labyrinth = new Labyrinth(state, 0);
        labyrinth.init(new Rect(0, 0, 40, 40));
        PacMan pacMan = new PacMan(0, labyrinth);
        pacMan.init();
        return pacMan;
    }

    private static void movePacManToTheWall(PacMan pacMan) {
        for (int i = 0; i < 10; i++) {
            pacMan.move();
        }
    }

    private static void moveToTheNextCell(PacMan pacMan, Labyrinth labyrinth) {
        int cell = labyrinth.getPacManCell();
        for (int i = 0; i < 10; i++) {
            pacMan.move();
            if (labyrinth.getPacManCell() != cell) {
                break;
            }
        }
    }

    public void testMoveUpToTheWall() {
        _pacMan.go(15, 0);
        movePacManToTheWall(_pacMan);
        assertEquals(new PointF(25, 15), _pacMan.getPosition());
    }

    public void testMoveLeftToTheWall() {
        _pacMan.go(0, 15);
        movePacManToTheWall(_pacMan);
        assertEquals(new PointF(15, 25), _pacMan.getPosition());
    }

    public void testTeleportationLeft() {
        Labyrinth labyrinth = new Labyrinth("P00", 0);
        labyrinth.init(new Rect(0, 0, 30, 10));
        PacMan pacMan = new PacMan(0, labyrinth);
        pacMan.init();
        pacMan.go(0, 5);
        moveToTheNextCell(pacMan, labyrinth);
        assertEquals(2, labyrinth.getPacManCell());
    }

    public void testTeleportationRight() {
        Labyrinth labyrinth = new Labyrinth("00P", 0);
        labyrinth.init(new Rect(0, 0, 30, 10));
        PacMan pacMan = new PacMan(0, labyrinth);
        pacMan.init();
        pacMan.go(30, 5);
        moveToTheNextCell(pacMan, labyrinth);
        assertEquals(0, labyrinth.getPacManCell());
    }
}
