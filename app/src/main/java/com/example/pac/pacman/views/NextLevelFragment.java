package com.example.pac.pacman.views;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pac.pacman.R;


public class NextLevelFragment extends Fragment {

    public NextLevelFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_next_level, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("FRAGMENT", "--Start--");
        View container = getView();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)container.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        ObjectAnimator animator = ObjectAnimator.ofFloat(container, "x", 300);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }
}
