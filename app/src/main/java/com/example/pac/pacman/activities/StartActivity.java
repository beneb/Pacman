package com.example.pac.pacman.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.example.pac.pacman.R;
import com.example.pac.pacman.util.Fonts;


public class StartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Fonts.setPacManFont(this, R.id.caption);
        Fonts.setRegularFont(this, R.id.new_game);
        Fonts.setRegularFont(this, R.id.resume);
    }


    public void buttonNewGameClick(View v) {
        startActivity(new Intent(this, PacmanActivity.class));
    }

    public void buttonResumeClick(View v) {
        Intent resumeIntent = new Intent(this, PacmanActivity.class);
        resumeIntent.setAction(PacmanActivity.State.RESUME_ACTION);
        startActivity(resumeIntent);
    }
}
