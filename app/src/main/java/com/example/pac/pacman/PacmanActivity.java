package com.example.pac.pacman;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

public class PacmanActivity extends ActionBarActivity {

    private Labyrinth _labyrinth;
    private Handler _handler = new Handler();
    private GameplayView _view;
    private List<Character> _characters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _labyrinth = new Labyrinth(getResources());
        _characters = new ArrayList<Character>();
        PacMan pacMan = new PacMan(getResources(), _labyrinth, Color.YELLOW, "Pac-Man", "Al Bundy");
        _characters.add(pacMan);
        // _characters.addAll(GhostRepository.CreateGhosts(getResources(), _labyrinth));

        _view = new GameplayView(this, _labyrinth, pacMan, _characters);
        setContentView(_view);

        _handler.postDelayed(_updateView, 1000);
    }

    private Runnable _updateView = new Runnable() {
        public void run() {
            for (Character character : _characters) {
                character.move();
                _view.invalidate(character.getInvalidateRect());
            }
            _handler.removeCallbacks(_updateView);
            _handler.postDelayed(this, 30);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        _handler.removeCallbacks(_updateView);
    }
}




