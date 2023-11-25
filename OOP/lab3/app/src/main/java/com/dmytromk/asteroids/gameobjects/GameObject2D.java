package com.dmytromk.asteroids.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import com.dmytromk.asteroids.common.Vector2;

public abstract class GameObject2D {
    protected Context context;
    protected Bitmap currentSprite;
    // top-left position
    protected Vector2 coordinates;
    protected Vector2 velocity = new Vector2(0, 0);
    protected int windowWidth;
    protected int windowHeight;

    public GameObject2D(Context context, Vector2 coordinates) {
        this.context = context;
        this.coordinates = coordinates;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((AppCompatActivity) this.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        this.windowWidth = displayMetrics.widthPixels;
        this.windowHeight = displayMetrics.heightPixels;
    }

    public abstract void draw(Canvas canvas);
    public abstract void update();

    public int getHeight() {
        return currentSprite.getHeight();
    }

    public int getWidth() {
        return currentSprite.getWidth();
    }

    public float getLeftX() {
        return coordinates.x;
    }

    public float getRightX() {
        return coordinates.x + currentSprite.getWidth();
    }

    public float getTopY() {
        return coordinates.y;
    }

    public float getBottomY() {
        return coordinates.y + currentSprite.getHeight();
    }

    // https://kishimotostudios.com/articles/aabb_collision/
    public static boolean checkCollision(GameObject2D obj1, GameObject2D obj2) {
        // check collision between object A and B
        return !(obj1.getLeftX() > obj2.getRightX()      // A is to the right of B
                || obj1.getRightX() < obj2.getLeftX()    // A is to the left of B
                || obj1.getBottomY() < obj2.getTopY()    // A is above B
                || obj1.getTopY() > obj2.getBottomY());  // A is below B
    }
}
