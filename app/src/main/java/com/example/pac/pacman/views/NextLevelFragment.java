package com.example.pac.pacman.views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pac.pacman.R;
import com.example.pac.pacman.util.Fonts;


public class NextLevelFragment extends Fragment {

    private static final String LEVEL_PARAM = "LEVEL_PARAM";

    private int _levelNum;

    public static NextLevelFragment newInstance(int levelNum) {
        NextLevelFragment fragment = new NextLevelFragment();
        Bundle args = new Bundle();
        args.putInt(LEVEL_PARAM, levelNum);
        fragment.setArguments(args);
        return fragment;
    }
    public NextLevelFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _levelNum = getArguments().getInt(LEVEL_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_next_level, container, false);
        TextView levelNumberView = (TextView)fragmentView.findViewById(R.id.level_number_view);
        levelNumberView.setText(String.format("%d", _levelNum));
        return fragmentView;
    }

    private static final int ANIMATION_DURATION = 500;
    private static final int DIP_LABEL_SHIFT = 80;
    private static final int DIP_NUMBER_SHIFT = 25;

    @Override
    public void onStart() {
        super.onStart();
        Fonts.setRegularFont(this, R.id.level_label_view);
        Fonts.setRegularFont(this, R.id.level_number_view);
        animate(getView());
    }

    private void animate(View fragmentView) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fragmentView.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_VERTICAL);

        View container = (View) fragmentView.getParent();
        int left = container.getLeft();
        int right = container.getRight();
        int center = (right - left) / 2;

        float labelShift = DipToPixels(DIP_LABEL_SHIFT);
        float numberShift = DipToPixels(DIP_NUMBER_SHIFT);

        ObjectAnimator animatorLeft = createAnimator(container.findViewById(R.id.level_label_view), left, center - labelShift);
        ObjectAnimator animatorRight = createAnimator(container.findViewById(R.id.level_number_view), right, center + numberShift);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorLeft).with(animatorRight);
        animatorSet.start();
    }

    private float DipToPixels(int dipLabelShift) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipLabelShift, getResources().getDisplayMetrics());
    }

    private ObjectAnimator createAnimator(View view, float from, float to) {
        ObjectAnimator animatorLeft = ObjectAnimator.ofFloat(view, "x", from, to);
        animatorLeft.setDuration(ANIMATION_DURATION);
        animatorLeft.setInterpolator(new AccelerateInterpolator());
        return animatorLeft;
    }
}
