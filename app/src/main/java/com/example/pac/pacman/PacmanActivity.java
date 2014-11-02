package com.example.pac.pacman;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

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
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        _labyrinth = new Labyrinth(getResources());
        _characters = new ArrayList<Character>();
        PacMan pacMan = new PacMan(getResources(), _labyrinth);
        _characters.add(pacMan);
        // _characters.addAll(Ghost.createGhosts(getResources(), _labyrinth));

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




