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

    private Vector2 actuator = new Vector2(0, 0);
    private float centerToTouchDistance;

    private boolean isPressed = false;

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
        updateInnerCirclePosition();
    }

    private void updateInnerCirclePosition() {
        this.innerCircleCenterPosition = Vector2.add(outerCircleCenterPosition,
                Vector2.multiply(actuator, innerCircleRadius));
    }

    public boolean getIsPressed() {
        return this.isPressed;
    }

    public void setIsPressed(boolean value) {
        this.isPressed = value;
    }

    public boolean isPressed(Vector2 touchPosition) {
        this.centerToTouchDistance = Vector2.distance(this.outerCircleCenterPosition, touchPosition);
        return this.centerToTouchDistance < outerCircleRadius;
    }

    public void setActuator(Vector2 touchPosition) {
        Vector2 delta = Vector2.subtract(touchPosition, outerCircleCenterPosition);
        float deltaDistance = Vector2.distance(touchPosition, outerCircleCenterPosition);

        // normalization to ensure that results falls in [-1, 1]
        if(deltaDistance < outerCircleRadius) {
            this.actuator = Vector2.divide(delta, this.outerCircleRadius);
        } else {
            this.actuator = Vector2.divide(delta, deltaDistance);
        }
    }

    public void resetActuator() {
        this.actuator = new Vector2(0, 0);
    }
}
