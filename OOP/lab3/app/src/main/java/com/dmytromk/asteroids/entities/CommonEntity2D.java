package com.dmytromk.asteroids.entities;

import static java.lang.Math.max;

import android.content.Context;
import android.graphics.Bitmap;

import com.dmytromk.asteroids.common.Vector2;

public abstract class CommonEntity2D {
    protected Context context;
    protected Bitmap currentSprite;
    // top-left position
    protected Vector2 coordinates;
    protected Vector2 velocity;

    public CommonEntity2D(Context context, Vector2 coordinates, Vector2 velocity, Bitmap currentSprite) {
        this.context = context;
        this.coordinates = coordinates;
        this.velocity = velocity;
        this.currentSprite = currentSprite;
    }

    public CommonEntity2D(Context context, Bitmap currentSprite) {
        this(context, new Vector2(0, 0), new Vector2(0, 0), currentSprite);
    }

    public int getHeight() {
        return currentSprite.getHeight();
    }

    public int getWidth() {
        return currentSprite.getWidth();
    }

    public int getLeftX() {
        return coordinates.getX();
    }

    public int getRightX() {
        return coordinates.getX() + getWidth();
    }

    public int getTopY() {
        return coordinates.getY();
    }

    public int getBottomY() {
        return coordinates.getY() + getHeight();
    }

    // https://kishimotostudios.com/articles/aabb_collision/
    static boolean checkCollision(CommonEntity2D A, CommonEntity2D B) {
        return !(A.getLeftX() > B.getRightX()      // A is to the right of B
                || A.getRightX() < B.getLeftX()    // A is to the left of B
                || A.getBottomY() < B.getTopY()    // A is above B
                || A.getTopY() > B.getBottomY());  // A is below B
    }
}
