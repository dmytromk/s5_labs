package com.dmytromk.asteroids.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.dmytromk.asteroids.R;
import com.dmytromk.asteroids.common.Vector2;

public class Joystick {
    private Vector2 innerCircleCenterPosition;
    private Vector2 outerCircleCenterPosition;

    private final int innerCircleRadius;
    private final int outerCircleRadius;

    private Paint innerCirclePaint;
    private Paint outerCirclePaint;

    public Joystick(Context context, Vector2 center, int innerCircleRadius, int outerCircleRadius) {
        this.innerCircleCenterPosition = center;
        this.outerCircleCenterPosition = center;
        
        this.innerCircleRadius = innerCircleRadius;
        this.outerCircleRadius = outerCircleRadius;

        // paint of circles
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(ContextCompat.getColor(context, R.color.grey));
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(ContextCompat.getColor(context, R.color.blue));
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(
                outerCircleCenterPosition.x,
                outerCircleCenterPosition.y,
                outerCircleRadius,
                outerCirclePaint
        );

        // Draw inner circle
        canvas.drawCircle(
                innerCircleCenterPosition.x,
                innerCircleCenterPosition.y,
                innerCircleRadius,
                innerCirclePaint
        );
    }

    public void update() {
    }
}
