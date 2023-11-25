package com.dmytromk.asteroids.gameobjects;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.dmytromk.asteroids.GameLoop;
import com.dmytromk.asteroids.R;
import com.dmytromk.asteroids.common.Vector2;

public class Missile extends GameObject2D {
//    public static final double MAX_DISTANCE = 500;
//    public static final double CURRENT_DISTANCE = 0;
//    public static final double DISTANCE_PER_SECOND = MAX_DISTANCE / ;
    public static final double SPEED_PIXELS_PER_SECOND = 1000;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;

    public Missile(Context context, Spaceship spaceship) {
        super(
                context,
                spaceship.coordinates
        );

        Vector2 spaceshipGunCoordinates = spaceship.getCenter();

        spaceshipGunCoordinates.x += spaceship.getRadius() * Math.cos(Math.toRadians(spaceship.angle));
        spaceshipGunCoordinates.y += spaceship.getRadius() * Math.sin(Math.toRadians(spaceship.angle));

        this.coordinates = spaceshipGunCoordinates;

        this.velocity = new Vector2(
                (float) (Math.cos(Math.toRadians(spaceship.angle)) * MAX_SPEED),
                (float) (Math.sin(Math.toRadians(spaceship.angle)) * MAX_SPEED)
        );

        this.currentSprite = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.shot2);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(currentSprite, coordinates.x, coordinates.y, null);
    }

    public void update() {
        this.coordinates = Vector2.add(this.coordinates, this.velocity);
    }
}
