package com.dmytromk.asteroids.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dmytromk.asteroids.R;
import com.dmytromk.asteroids.common.Vector2;

import java.util.Random;

public class Asteroid extends CommonEntity2D {
    private final Random random;
    private static final int MIN_VELOCITY = 15;
    private static final int MAX_VELOCITY = 30;

    public Asteroid(Context context, Vector2 coordinates, Vector2 velocity) {
        super(context, coordinates, velocity,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_blue));
        this.random = new Random();
    }

    public Asteroid(Context context) {
        super(context, BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_blue));
        this.random = new Random();
        this.reset();
    }

    public void reset() {
        int newX = this.random.nextInt(GameView.width-this.getWidth());
        int newY = this.random.nextInt(GameView.height-this.getHeight());
        this.coordinates = new Vector2(newX, newY);

        int newVelocityX = this.random.nextInt(MAX_VELOCITY-MIN_VELOCITY) + MIN_VELOCITY;
        int newVelocityY = this.random.nextInt(MAX_VELOCITY-MIN_VELOCITY) + MIN_VELOCITY;
        this.velocity = new Vector2(newVelocityX, newVelocityY);
    }
}
