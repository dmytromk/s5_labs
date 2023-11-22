package com.dmytromk.asteroids.entities;

import static java.lang.Math.max;

import android.graphics.Bitmap;

import com.dmytromk.asteroids.common.Vector2;

public abstract class CommonEntity2D {
    protected Bitmap currentSprite;
    protected int radius;
    protected Vector2 coordinates;
    protected Vector2 velocity;

    public CommonEntity2D(Vector2 coordinates, Vector2 velocity, Bitmap currentSprite) {
        this.coordinates = coordinates;
        this.velocity = velocity;
        this.radius = max(currentSprite.getWidth(), currentSprite.getHeight());
    }
}
