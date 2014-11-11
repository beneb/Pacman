package com.example.pac.pacman.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.example.pac.pacman.*;
import com.example.pac.pacman.Character;
import com.example.pac.pacman.views.GameplayView;

import java.util.ArrayList;
import java.util.List;

public class PacmanActivity extends ActionBarActivity {

    private Handler _handler = new Handler();
    private GameplayView _view;
    private List<com.example.pac.pacman.Character> _characters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameEnv.getInstance().InitOnce(getResources());

        _characters = new ArrayList<Character>();
        PacMan pacMan = new PacMan(
                getResources().getColor(R.color.pacman),
                GameEnv.getInstance().getLabyrinth());
        _characters.add(pacMan);
        _characters.addAll(GhostRepository.CreateGhosts());

        _view = new GameplayView(this, pacMan, _characters);
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
            _handler.postDelayed(this, 50);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        _handler.removeCallbacks(_updateView);
    }
}




