package com.example.pac.pacman.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.example.pac.pacman.*;
import com.example.pac.pacman.Character;
import com.example.pac.pacman.views.GameplayView;

import java.util.ArrayList;
import java.util.List;

public class PacmanActivity extends ActionBarActivity {

    public static final String RESUME_ACTION = "RESUME";
    public static final String SETTINGS = "SETTINGS";
    public static final String LABYRINTH_STATE = "LABYRINTH_STATE";

    private Handler _handler = new Handler();
    private GameplayView _view;
    private List<com.example.pac.pacman.Character> _characters;
    private Labyrinth _labyrinth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String state = loadLabyrinthState(action);

        _labyrinth = new Labyrinth(state, getResources().getColor(R.color.walls));

        GameEnv.getInstance().InitOnce(getResources());

        _characters = new ArrayList<Character>();
        PacMan pacMan = new PacMan(_labyrinth, getResources());
        _characters.add(pacMan);
        _characters.addAll(GhostRepository.CreateGhosts(_labyrinth));

        _view = new GameplayView(this, _labyrinth, pacMan, _characters);
        setContentView(_view);

        _handler.postDelayed(_updateView, 1000);
    }

    private String loadLabyrinthState(String action) {
        String state = getResources().getString(R.string.level_classic);
        if (RESUME_ACTION.equals(action)) {
            SharedPreferences settings =  getSharedPreferences(SETTINGS, 0);
            state = settings.getString(LABYRINTH_STATE, state);
        }
        return state;
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
        saveLabyrinthState();

        _handler.removeCallbacks(_updateView);
    }

    private void saveLabyrinthState() {
        SharedPreferences settings =  getSharedPreferences(SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(LABYRINTH_STATE, _labyrinth.getState());
        editor.apply();
    }
}




