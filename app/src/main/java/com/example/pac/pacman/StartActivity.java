package com.example.pac.pacman;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class StartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        setTypeface(R.id.caption, "pacfont.ttf");
        setTypeface(R.id.new_game, "emulogic.ttf");
    }

    private void setTypeface(int textViewId, final String fontName) {
        TextView tv = (TextView) findViewById(textViewId);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/" + fontName));
    }

    public void buttonPacmanClick(View v) {
        startActivity(new Intent(this, PacmanActivity.class));
    }
}
