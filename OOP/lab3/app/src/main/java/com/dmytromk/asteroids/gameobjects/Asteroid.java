package com.dmytromk.asteroids.gameobjects;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.dmytromk.asteroids.R;
import com.dmytromk.asteroids.common.Vector2;

import java.util.Random;

public class Asteroid extends GameObject2D {
    private final Random random;
    private static final int MIN_VELOCITY = 15;
    private static final int MAX_VELOCITY = 30;

    public Asteroid(Context context, Vector2 coordinates) {
        super(context, coordinates);
        this.currentSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_blue);
        this.random = new Random();
    }

    public void draw(Canvas canvas) {

    }

    public void update() {

    }

    public void reset() {
        //int newX = this.random.nextInt(GameView.width-this.getWidth());
        //int newY = this.random.nextInt(GameView.height-this.getHeight());
        //this.coordinates = new Vector2(newX, newY);

        int newVelocityX = this.random.nextInt(MAX_VELOCITY-MIN_VELOCITY) + MIN_VELOCITY;
        int newVelocityY = this.random.nextInt(MAX_VELOCITY-MIN_VELOCITY) + MIN_VELOCITY;
        this.velocity = new Vector2(newVelocityX, newVelocityY);
    }
}
