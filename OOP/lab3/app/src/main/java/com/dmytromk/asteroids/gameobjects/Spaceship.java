package com.dmytromk.asteroids.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.dmytromk.asteroids.GameLoop;
import com.dmytromk.asteroids.R;
import com.dmytromk.asteroids.common.Vector2;
import com.dmytromk.asteroids.controls.Joystick;

public class Spaceship {
    private final Context context;
    private Bitmap currentSprite;
    // top-left position
    private Vector2 coordinates;
    private Vector2 velocity = new Vector2(0, 0);

    private static final double SPEED_PIXELS_PER_SECOND = 1000;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;


    public Spaceship(Context context, Vector2 coordinates) {
        this.context = context;
        this.currentSprite = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ship);
        this.coordinates = coordinates;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.currentSprite, coordinates.x, coordinates.y, null);
    }

    public void update(Joystick joystick) {
        this.coordinates = Vector2.add(this.coordinates, this.velocity);

        if (joystick.getIsPressed()) {
            Vector2 acceleration = Vector2.multiply(joystick.getActuator(), (float) (MAX_SPEED * 0.05));
            this.velocity = Vector2.add(acceleration, velocity);
        }

        this.velocity = Vector2.multiply(this.velocity, 0.98F);
    }
}
