package com.dmytromk.asteroids.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dmytromk.asteroids.R;
import com.dmytromk.asteroids.common.Vector2;

import java.util.Random;

public class Asteroid extends CommonEntity2D {
    Random random;
    Context context;

    public Asteroid(Context context) {
        super(new Vector2(0, 0), new Vector2(0, 0),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_blue));
        this.context = context;
        this.random = new Random();
        this.reset();
    }

    public Asteroid(Vector2 coordinates, Vector2 velocity, Context context) {
        super(coordinates, velocity,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_blue));
        this.context = context;
        this.random = new Random();
    }

    public void reset() {
        int newX = this.random.nextInt(GameView.width-this.getWidth());
        int newY = this.random.nextInt(GameView.height-this.getHeight());
        this.coordinates = new Vector2(newX, newY);

        int newVelocityX = this.random.nextInt(15) + 15;
        int newVelocityY = this.random.nextInt(15) + 15;
        this.velocity = new Vector2(newVelocityX, newVelocityY);
    }
}
