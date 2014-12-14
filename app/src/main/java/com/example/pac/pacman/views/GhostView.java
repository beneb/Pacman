package com.example.pac.pacman.views;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.example.pac.pacman.Ghost;
import com.example.pac.pacman.R;

public class GhostView implements IChildView {
    private final Ghost _ghost;
    private final Resources _resources;
    private final Paint _eyePaint;
    private final Paint _middleEyePaint;
    protected int _defaultColor;
    protected final Paint _foreground;
    private final Path _samplePath1;
    private final Path _samplePath2;
    private final Path _ghostPath;

    private boolean _waveShift;

    public GhostView(Ghost ghost, Resources resources) {
        _ghost = ghost;
        _resources = resources;
        _foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        _foreground.setStyle(Paint.Style.FILL);

        _eyePaint = PaintObjectsFactory.createGhostEye(resources);
        _middleEyePaint = PaintObjectsFactory.createGhostMiddleEye(resources);

        _samplePath1 = new Path();
        _samplePath2 = new Path();
        _ghostPath = new Path();
    }

    @Override
    public void onSizeChanged() {
        createGhostPath(_samplePath1, true);
        createGhostPath(_samplePath2, false);
    }

    private void createGhostPath(Path path, boolean withShift) {
        float radius = _ghost.getSize() / 2;
        path.addArc(new RectF(-radius, -radius, radius, radius), 180, 180);

        float quadStep = _ghost.getSize() / 12;
        path.lineTo(radius, radius - (withShift ? quadStep : 0));
        for (int quadNum = 0; quadNum < 3; quadNum++) {
            path.quadTo(
                    radius - quadNum * 4 * quadStep - quadStep,
                    radius - (withShift ? 2 : 1) * quadStep,
                    radius - quadNum * 4 * quadStep - 2 * quadStep,
                    radius - (withShift ? 1 : 2) * quadStep);
            path.quadTo(
                    radius - quadNum * 4 * quadStep - 3 * quadStep,
                    radius - (withShift ? 0 : 1) * quadStep,
                    radius - quadNum * 4 * quadStep - 4 * quadStep,
                    radius - (withShift ? 1 : 0) * quadStep);
        }
    }

    @Override
    public void draw(Canvas canvas) {

        switch (_ghost.getMode()) {
            case Default:
                _foreground.setColor(_defaultColor);
                break;
            case Scared:
                _foreground.setColor(_resources.getColor(R.color.ScaredGhost));
                break;
        }

        PointF position = _ghost.getPosition();

        if (_waveShift = !_waveShift) {
            _samplePath1.offset(position.x, position.y, _ghostPath);
        } else {
            _samplePath2.offset(position.x, position.y, _ghostPath);
        }

        canvas.drawPath(_ghostPath, _foreground);

        float eyeDistance = _ghost.getSize() / 5;
        float eyeSize = _ghost.getSize() / 7;
        float directionShiftX = 0, directionShiftY = 0;
        switch (_ghost.getDirection()) {
            case Left:
                directionShiftX = -eyeDistance / 2;
                break;
            case Right:
                directionShiftX = +eyeDistance / 2;
                break;
            case Up:
                directionShiftY = -eyeDistance / 2;
                break;
            case Down:
                directionShiftY = +eyeDistance / 2;
                break;
        }
        canvas.drawCircle(
                position.x - eyeDistance + directionShiftX,
                position.y - eyeSize + directionShiftY,
                eyeSize,
                _eyePaint);
        canvas.drawCircle(position.x + eyeDistance + directionShiftX,
                position.y - eyeSize + directionShiftY,
                eyeSize,
                _eyePaint);
        canvas.drawCircle(
                position.x - eyeDistance + 2 * directionShiftX,
                position.y - eyeSize + 2 * directionShiftY,
                eyeSize / 2,
                _middleEyePaint);
        canvas.drawCircle(
                position.x + eyeDistance + 2 * directionShiftX,
                position.y - eyeSize + 2 * directionShiftY,
                eyeSize / 2,
                _middleEyePaint);
    }
}