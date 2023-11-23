package com.dmytromk.asteroids.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.dmytromk.asteroids.R;
import com.dmytromk.asteroids.common.Vector2;

public class Spaceship {
    protected Context context;
    protected Bitmap currentSprite;
    // top-left position
    protected Vector2 coordinates;

    public Spaceship(Context context, Vector2 coordinates) {
        this.context = context;
        this.currentSprite = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ship);
        this.coordinates = coordinates;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.currentSprite, coordinates.getX(), coordinates.getY(), null);
    }

    public void update() {
    }
}
