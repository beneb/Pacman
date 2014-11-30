package com.example.pac.pacman;

import android.graphics.PointF;

public interface IMoveStrategy {
    Direction GetNextDirection(int currentCell);
}

