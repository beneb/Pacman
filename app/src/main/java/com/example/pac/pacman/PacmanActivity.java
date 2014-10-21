package com.example.pac.pacman;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;


public class PacmanActivity extends ActionBarActivity {

    private Labyrinth _labyrinth;
    private Handler _handler = new Handler();
    private GameplayView _view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _labyrinth = new Labyrinth(getResources()); // TODO deserialize lab from res
        _view = new GameplayView(this, _labyrinth);
        setContentView(_view);
        _handler.postDelayed(_updateView, 1000);
    }

    private Runnable _updateView = new Runnable() {
        public void run() {
            Log.d("MainLoop", "handler:" + new Date());
            _view.drawCircle();
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




