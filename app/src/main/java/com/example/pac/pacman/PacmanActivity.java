package com.example.pac.pacman;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PacmanActivity extends ActionBarActivity {

    private Labyrinth _labyrinth;
    private PacMan _pacman;
    private Handler _handler = new Handler();
    private GameplayView _view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _labyrinth = new Labyrinth(getResources());
        _pacman = new PacMan(getResources(), _labyrinth);
        _view = new GameplayView(this, _labyrinth, _pacman);
        setContentView(_view);
        // _handler.postDelayed(_updateView, 1000);
        _handler.post(_updateView);
    }

    private Runnable _updateView = new Runnable() {
        public void run() {
            _pacman.move();
            _view.invalidate(_pacman.getInvalidateRect());
            _handler.removeCallbacks(_updateView);
            // _handler.postDelayed(this, 30);
            _handler.post(this);
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




