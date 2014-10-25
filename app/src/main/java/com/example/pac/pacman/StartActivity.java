package com.example.pac.pacman;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class StartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setTypeface(R.id.caption, "PAC-FONT.TTF");
        setTypeface(R.id.new_game, "emulogic.ttf");
    }

    private void setTypeface(int textViewId, final String fontName) {
        TextView tv = (TextView) findViewById(textViewId);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/" + fontName));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    public void buttonPacmanClick(View v) {
        Intent intent = new Intent(this, PacmanActivity.class);
        startActivity(intent);
    }

    public void buttonExitClick(View v) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
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
