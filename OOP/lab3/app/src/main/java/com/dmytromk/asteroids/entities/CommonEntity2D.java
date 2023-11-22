package com.dmytromk.asteroids.entities;

import static java.lang.Math.max;

import android.graphics.Bitmap;

import com.dmytromk.asteroids.common.Vector2;

public abstract class CommonEntity2D {
    protected Bitmap currentSprite;
    protected Vector2 coordinates;
    protected Vector2 velocity;

    public CommonEntity2D(Vector2 coordinates, Vector2 velocity, Bitmap currentSprite) {
        this.coordinates = coordinates;
        this.velocity = velocity;
        this.radius = max(currentSprite.getWidth(), currentSprite.getHeight());
    }

    public int getHeight() {
        return currentSprite.getHeight();
    }

    public int getWidth() {
        return currentSprite.getWidth();
    }

    public int getLeftX() {
        return coordinates.getX() - getWidth()/2;
    }

    public int getRightX() {
        return coordinates.getX() + getWidth()/2;
    }

    public int getTopY() {
        return coordinates.getY() + getHeight()/2;
    }

    public int getBottomY() {
        return coordinates.getY() - getHeight()/2;
    }
}
