package com.dmytromk.asteroids.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.dmytromk.asteroids.GameLoop;
import com.dmytromk.asteroids.R;

public class Performance {
    private GameLoop gameLoop;
    private Context context;
    private int textSize = 50;

    public Performance(Context context, GameLoop gameLoop) {
        this.context = context;
        this.gameLoop = gameLoop;
    }

    public void draw(Canvas canvas) {
        drawFPS(canvas);
        //drawUPS(canvas);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString((int) this.gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.yellow);
        paint.setColor(color);
        paint.setTextSize(textSize);
        canvas.drawText("FPS: " + averageFPS, 100, 100, paint);
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString((int) this.gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.yellow);
        paint.setColor(color);
        paint.setTextSize(textSize);
        canvas.drawText("UPS: " + averageUPS, 100, 200, paint);
    }
}
