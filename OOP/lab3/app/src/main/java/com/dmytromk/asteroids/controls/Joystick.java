package com.dmytromk.asteroids.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.dmytromk.asteroids.R;
import com.dmytromk.asteroids.common.Vector2;

public class Joystick {
    private Vector2 innerCenter;
    private Vector2 outerCenter;

    private final int innerRadius;
    private final int outerRadius;

    private Paint innerCirclePaint;
    private Paint outerCirclePaint;

    public Joystick(Context context, Vector2 center, int innerRadius, int outerRadius) {
        this.innerCenter = center;
        this.outerCenter = center;
        
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;

        // paint of circles
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(ContextCompat.getColor(context, R.color.grey));
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(ContextCompat.getColor(context, R.color.blue));
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
    }

    public void update() {
    }
}
