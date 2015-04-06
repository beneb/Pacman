package com.example.pac.pacman.views;

import android.content.res.Resources;
import android.graphics.Paint;

import com.example.pac.pacman.R;

public class PaintObjectsFactory {
    public static Paint createWall(int color) {
        // no anti aliasing for this paint
        // wall drawing is currently not optimized for anti aliased lines
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(color);
        p.setStrokeWidth(4f);
        return p;
    }

    public static Paint createDot(int color) {
        return createPaint(color);
    }

    public static Paint createEnergizer(int color) {
        return createPaint(color);
    }

    public static Paint createDebugPaint(int color) {
        Paint p = createPaint(color);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(.5f);
        return p;
    }

    private static Paint createPaint(int color) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(color);
        p.setStrokeWidth(1.5f);
        return p;
    }

    public static Paint createGhostEye(Resources resources) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(resources.getColor(R.color.ghost_eye));
        return p;
    }

    public static Paint createGhostMiddleEye(Resources resources) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(resources.getColor(R.color.ghost_middle_eye));
        return p;
    }

    public static Paint createPaintForGameOver(int color) {
        return createPaint(color);
    }
}
