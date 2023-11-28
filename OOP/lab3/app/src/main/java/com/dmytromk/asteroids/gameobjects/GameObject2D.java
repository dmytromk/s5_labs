package com.dmytromk.asteroids.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.dmytromk.asteroids.common.Vector2;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public abstract class GameObject2D {
    protected Context context;
    protected Bitmap currentSprite;
    // top-left position
    protected Vector2 coordinates;
    protected Vector2 velocity = new Vector2(0, 0);
    protected Matrix position;
    protected int windowWidth;
    protected int windowHeight;

    public GameObject2D(Context context, Vector2 coordinates) {
        this.context = context;
        this.coordinates = coordinates;
        this.position = new Matrix();

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

    public Vector2 getCenter() {
        return new Vector2(coordinates.x + getWidth() / 2, coordinates.y + getHeight() / 2);
    }

    // radius for escribed circle
    public float getRadius() {
        return (float) (Math.sqrt(getHeight() * getHeight() + getWidth() * getWidth()) / 2);
    }

    public double distanceCenters(GameObject2D obj2) {
        return Vector2.length(Vector2.subtract(obj2.getCenter(), getCenter()));
    }

//    public float getLeftX() {
//        return coordinates.x;
//    }
//
//    public float getRightX() {
//        return coordinates.x + currentSprite.getWidth();
//    }
//
//    public float getTopY() {
//        return coordinates.y;
//    }
//
//    public float getBottomY() {
//        return coordinates.y + currentSprite.getHeight();
//    }
//
//    // https://kishimotostudios.com/articles/aabb_collision/
//    public static boolean checkCollisionAABB(GameObject2D obj1, GameObject2D obj2) {
//        // check collision between object A and B
//        return !(obj1.getLeftX() > obj2.getRightX()      // A is to the right of B
//                || obj1.getRightX() < obj2.getLeftX()    // A is to the left of B
//                || obj1.getBottomY() < obj2.getTopY()    // A is above B
//                || obj1.getTopY() > obj2.getBottomY());  // A is below B
//    }
//
//    public static double getCollisionAngle(GameObject2D obj1, GameObject2D obj2) {
//        Vector2 difference = new Vector2(obj2.coordinates.x - obj1.coordinates.x,
//                obj2.coordinates.y - obj1.coordinates.y);
//
//        /// for simplicity convert radians to degree
//        double angle = Math.atan2(difference.y, difference.x) * 180 / Math.PI;
//        if (angle < 0)
//            angle += 360;
//
//        return angle;
//    }

    public static boolean checkCollisionCircles(GameObject2D obj1, GameObject2D obj2) {
        float sumRadius = obj1.getRadius() + obj2.getRadius();
        float distance = (float) obj1.distanceCenters(obj2);

        if (distance <= sumRadius)
        {
            return true;
        }

        return false;
    }

    public static void resolveStaticCollisionCircles(GameObject2D obj1, GameObject2D obj2) {
        Vector2 difference = Vector2.subtract(obj1.coordinates, obj2.coordinates);
        float distance = Vector2.distance(obj1.getCenter(), obj2.getCenter());
        float overlap = (Vector2.distance(obj1.getCenter(), obj2.getCenter())
                - obj1.getRadius() - obj2.getRadius()) / 2;

        obj1.coordinates = Vector2.subtract(obj1.coordinates,
                Vector2.multiply(difference, overlap/distance));
        obj2.coordinates = Vector2.add(obj2.coordinates,
                Vector2.multiply(difference, overlap/distance));
    }

    public static void resolveDynamicCollisionCircles(GameObject2D obj1, GameObject2D obj2) {
        double velocitySum = Vector2.length(Vector2.add(obj1.velocity, obj2.velocity));

        Vector2 normal = Vector2.subtract(obj2.getCenter(), obj1.getCenter());
        float distance = Vector2.distance(obj2.getCenter(), obj1.getCenter());
        normal = Vector2.divide(normal, distance);

        Vector2 relativeVelocity = Vector2.subtract(obj2.velocity, obj1.velocity);
        float dotProduct = Vector2.dotProduct(relativeVelocity, normal);

        float elasticity = 1;

        float impulse = (2.0f * dotProduct) / (2);

        Vector2 impulseVector = Vector2.multiply(normal, impulse);

        obj1.velocity = Vector2.add(obj1.velocity, Vector2.multiply(impulseVector, elasticity));
        obj2.velocity = Vector2.subtract(obj2.velocity, Vector2.multiply(impulseVector, elasticity));

        double newVelocitySum = Vector2.length(Vector2.add(obj1.velocity, obj2.velocity));
        Log.d("TAG", String.valueOf((newVelocitySum - velocitySum)));
    }

    protected void rotateBitmap(float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees, getWidth()/2, getHeight()/2);
        matrix.postTranslate(coordinates.x, coordinates.y);

        position.set(matrix);
    }
}
