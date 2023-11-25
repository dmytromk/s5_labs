package com.dmytromk.asteroids.gameobjects;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.dmytromk.asteroids.GameLoop;
import com.dmytromk.asteroids.R;
import com.dmytromk.asteroids.common.Vector2;
import com.dmytromk.asteroids.controls.Joystick;
import com.dmytromk.asteroids.utils.utils;

public class Spaceship extends GameObject2D {
    private final Joystick joystick;
    public float angle = 0; // degree

    public static final double SPEED_PIXELS_PER_SECOND = 1000;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double ROTATION_SPEED = MAX_SPEED / 5;


    public Spaceship(Context context, Joystick joystick, Vector2 coordinates) {
        super(context, coordinates);
        this.joystick = joystick;
        this.currentSprite = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ship);
    }

    @Override
    public float getRadius() {
        return (float) getWidth() / 2;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(currentSprite, position, null);
    }

    public void update() {
        this.coordinates = Vector2.add(this.coordinates, this.velocity);

        coordinates.x = utils.positiveMod(coordinates.x + getWidth()/2, windowWidth) - getWidth()/2;
        coordinates.y = utils.positiveMod(coordinates.y + getHeight()/2, windowHeight) - getHeight()/2;
        
        if (joystick.getIsPressed()) {
            Vector2 acceleration = Vector2.multiply(joystick.getActuator(), (float) (MAX_SPEED * 0.05));
            velocity = Vector2.add(acceleration, velocity);


            // rotate spaceship
            double joystickAngle = Math.atan2(joystick.getActuator().y, joystick.getActuator().x)
                    * 180 / Math.PI;
            if (joystickAngle < 0)
                joystickAngle += 360;

            if (utils.positiveMod((float) (angle - joystickAngle), 360) < 0.1) {
                float result = (float) ((angle - joystickAngle + 360) % 360);
                Log.d("STATE", String.valueOf(angle));
            } else if ((angle - joystickAngle + 360) % 360 < 180) {
                angle = utils.positiveMod((float) (angle - ROTATION_SPEED), 360);
            } else {
                angle = utils.positiveMod((float) (angle + ROTATION_SPEED), 360);
            }
        }

        rotateBitmap(angle);

        velocity = Vector2.multiply(velocity, 0.98F);
    }
}
