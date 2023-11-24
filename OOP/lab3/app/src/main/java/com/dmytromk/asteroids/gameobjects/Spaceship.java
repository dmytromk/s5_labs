package com.dmytromk.asteroids.gameobjects;

import static java.lang.Math.abs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import com.dmytromk.asteroids.GameLoop;
import com.dmytromk.asteroids.R;
import com.dmytromk.asteroids.common.Vector2;
import com.dmytromk.asteroids.controls.Joystick;
import com.dmytromk.asteroids.utils.utils;

public class Spaceship {
    private final Context context;
    private final Joystick joystick;
    private Bitmap currentSprite;
    // top-left position
    private Vector2 coordinates;
    private Vector2 velocity = new Vector2(0, 0);
    private int windowHeight, windowWidth;

    private static final double SPEED_PIXELS_PER_SECOND = 1000;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;


    public Spaceship(Context context, Joystick joystick, Vector2 coordinates) {
        this.context = context;
        this.joystick = joystick;
        this.currentSprite = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ship);
        this.coordinates = coordinates;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((AppCompatActivity) this.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        this.windowHeight = displayMetrics.heightPixels;
        this.windowWidth = displayMetrics.widthPixels;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(currentSprite, coordinates.x, coordinates.y, null);
    }

    public void update() {
        coordinates = Vector2.add(this.coordinates, this.velocity);

        coordinates.x = utils.positiveMod(coordinates.x, windowWidth
                - currentSprite.getWidth()/2);
        coordinates.y = utils.positiveMod(coordinates.y, windowHeight
                - currentSprite.getHeight()/2);

        if (joystick.getIsPressed()) {
            Vector2 acceleration = Vector2.multiply(joystick.getActuator(), (float) (MAX_SPEED * 0.05));
            velocity = Vector2.add(acceleration, velocity);
        }

        velocity = Vector2.multiply(velocity, 0.98F);
    }
}
