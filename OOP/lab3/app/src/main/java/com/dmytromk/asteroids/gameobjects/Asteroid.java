package com.dmytromk.asteroids.gameobjects;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.dmytromk.asteroids.GameLoop;
import com.dmytromk.asteroids.R;
import com.dmytromk.asteroids.common.Vector2;
import com.dmytromk.asteroids.utils.utils;

import java.util.Random;

public class Asteroid extends GameObject2D {
    private final Spaceship spaceship;

    private static final double SPEED_PIXELS_PER_SECOND = Spaceship.SPEED_PIXELS_PER_SECOND * 0.6;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double MIN_SPEED = MAX_SPEED / 2;
    private static final double SPAWNS_PER_MINUTE = 50;
    private static final double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE/60.0;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SECOND;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;

    public Asteroid(Context context, Spaceship spaceship, Vector2 coordinates) {
        super(context, coordinates);
        this.spaceship = spaceship;
        this.currentSprite = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.asteroid_blue);
    }

    public Asteroid(Context context, Spaceship spaceship) {

        this(context, spaceship, new Vector2(0, 0));

        this.coordinates = new Vector2(
                (float) (Math.random() * (windowWidth - this.getWidth())),
                (float) (Math.random() * (windowHeight - this.getHeight()))
        );

        this.velocity = new Vector2(
                (float) (MIN_SPEED + Math.random() * MAX_SPEED),
                (float) (MIN_SPEED + Math.random() * MAX_SPEED)
        );

        if (Math.random() < 0.5) {
            velocity.x *= -1;
        }

        if (Math.random() < 0.5) {
            velocity.y *= -1;
        }
    }

    public static boolean readyToSpawn() {
        if (updatesUntilNextSpawn <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        } else {
            updatesUntilNextSpawn--;
            return false;
        }
    }

    public void resetCoordinates() {
        this.coordinates = new Vector2(
                (float) (Math.random() * (windowWidth - this.getWidth())),
                (float) (Math.random() * (windowHeight - this.getHeight()))
        );
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(currentSprite, coordinates.x, coordinates.y, null);
    }

    public boolean isInWindowBoundaries() {
        return coordinates.x < windowWidth & coordinates.y < windowHeight;
    }

    public void update() {
        this.coordinates = Vector2.add(this.coordinates, this.velocity);
    }

    public void reset() {
        //int newX = this.random.nextInt(GameView.width-this.getWidth());
        //int newY = this.random.nextInt(GameView.height-this.getHeight());
        //this.coordinates = new Vector2(newX, newY);

        //int newVelocityX = this.random.nextInt(MAX_SPEED) + MIN_VELOCITY;
        //int newVelocityY = this.random.nextInt(MAX_SPEED) + MIN_VELOCITY;
        //this.velocity = new Vector2(newVelocityX, newVelocityY);
    }
}
