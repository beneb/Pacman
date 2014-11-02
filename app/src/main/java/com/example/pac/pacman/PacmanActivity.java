package com.example.pac.pacman;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pacman, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}




