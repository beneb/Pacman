package com.example.pac.pacman;
import android.graphics.Paint;

public class PaintObjectsFactory {

    static Paint createWall(int color) {
        return createPaint(color);
    }
    static Paint createDot(int color) {
        return createPaint(color);
    }
    static Paint createEmptyDot(int color) {
        return createPaint(color);
    }

    public static Paint createDebugPaint(int color) {
        Paint p = createPaint(color);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(1);
        return p;
    }

    private static Paint createPaint(int color){
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(color);
        p.setStrokeWidth(1.5f);
        return p;
    }
}
