package com.example.pac.pacman;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;


public class StartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
