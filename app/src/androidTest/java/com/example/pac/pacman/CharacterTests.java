package com.example.pac.pacman;

import android.graphics.PointF;
import android.graphics.Rect;

import junit.framework.TestCase;

public class CharacterTests extends TestCase {

    private PacMan _pacMan;

    @Override
    protected void setUp() throws Exception {
        _pacMan = initFromLabyrinth_3x3("101 0P0 101");
    }

    private PacMan initFromLabyrinth_3x3(String state) {
        Labyrinth labyrinth = new Labyrinth(state, 0);
        labyrinth.init(new Rect(0, 0, 30, 30));
        PacMan pacMan = new PacMan(0, labyrinth);
        pacMan.init();
        return pacMan;
    }

    private void movePacManToTheWall() {
        for (int i = 0; i < 10; i++) {
            _pacMan.move();
        }
    }

    public void testMoveUpToTheWall() {
        _pacMan.go(15, 0);
        movePacManToTheWall();
        assertEquals(new PointF(15, 5), _pacMan.getPosition());
    }

    public void testMoveLeftToTheWall() {
        _pacMan.go(0, 15);
        movePacManToTheWall();
        assertEquals(new PointF(5, 15), _pacMan.getPosition());
    }
}
