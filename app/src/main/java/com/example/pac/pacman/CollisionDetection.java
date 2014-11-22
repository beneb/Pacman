package com.example.pac.pacman;

import java.util.List;

public class CollisionDetection {

    private Labyrinth _labyrinth;

    public CollisionDetection(Labyrinth labyrinth) {
        _labyrinth = labyrinth;
    }

    public boolean PacManInteractWithAGhost(PacMan pacMan, List<Character> ghosts) {
        int pacMansCell = _labyrinth.getCharacterPosition(pacMan);

        for (Character ghost : ghosts) {
            int ghostsCell = _labyrinth.getCharacterPosition(ghost);
            if (ghostsCell == pacMansCell) {
                return true;
            }
        }

        return false;
    }
}
