package com.example.pac.pacman;

public class CollisionDetection {

    private Labyrinth _labyrinth;

    public CollisionDetection(Labyrinth labyrinth) {
        _labyrinth = labyrinth;
    }

    public boolean PacManCanEatADot(Character ch) {
        int cell = _labyrinth.getCharacterPosition(ch);
        return _labyrinth.getCellValue(cell) == _labyrinth.DOT;
    }
}
