package com.example.pac.pacman;

import com.example.pac.pacman.event.InvalidateRectInViewEvent;

import java.util.List;

public class CollisionDetection {

    private Labyrinth _labyrinth;

    public CollisionDetection(Labyrinth labyrinth) {
        _labyrinth = labyrinth;
    }

    public boolean PacManCanEatADot(PacMan pacMan) {
        int pacMansCell = _labyrinth.getCharacterPosition(pacMan);
        return _labyrinth.getCellValue(pacMansCell) == _labyrinth.DOT;
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
