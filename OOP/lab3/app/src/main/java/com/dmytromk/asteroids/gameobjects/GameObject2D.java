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
    
    public Vector2 getCenter() {
        return new Vector2(coordinates.x + getWidth() / 2, coordinates.y + getHeight() / 2);
    }

    public float getRadius() {
        return (float) (Math.sqrt(getHeight() * getHeight() + getWidth() * getWidth()) / 2);
    }

    public double distanceCenters(GameObject2D obj2) {
        return Vector2.length(Vector2.subtract(obj2.getCenter(), getCenter()));
    }

    // https://kishimotostudios.com/articles/aabb_collision/
    public static boolean checkCollisionAABB(GameObject2D obj1, GameObject2D obj2) {
        // check collision between object A and B
        return !(obj1.getLeftX() > obj2.getRightX()      // A is to the right of B
                || obj1.getRightX() < obj2.getLeftX()    // A is to the left of B
                || obj1.getBottomY() < obj2.getTopY()    // A is above B
                || obj1.getTopY() > obj2.getBottomY());  // A is below B
    }

    public static double getCollisionAngle(GameObject2D obj1, GameObject2D obj2) {
        Vector2 difference = new Vector2(obj2.coordinates.x - obj1.coordinates.x,
                obj2.coordinates.y - obj1.coordinates.y);

        /// for simplicity convert radians to degree
        double angle = Math.atan2(difference.y, difference.x) * 180 / Math.PI;
        if (angle < 0)
            angle += 360;

        return angle;
    }

    public static boolean checkCollisionCircles(GameObject2D obj1, GameObject2D obj2) {
        float sumRadius = obj1.getRadius() + obj2.getRadius();
        float distance = Vector2.distance(obj1.getCenter(), obj2.getCenter());

        if (distance <= sumRadius)
        {
            return true;
        }

        return false;
    }

    public static void resolveCollisionCircles(GameObject2D obj1, GameObject2D obj2) {
        Vector2 temp = obj1.velocity;

        obj1.velocity = obj2.velocity;
        obj2.velocity = temp;
    }
}
