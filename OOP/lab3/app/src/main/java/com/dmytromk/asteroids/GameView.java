package com.dmytromk.asteroids;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.dmytromk.asteroids.common.Vector2;
import com.dmytromk.asteroids.controls.Joystick;
import com.dmytromk.asteroids.gameobjects.Asteroid;
import com.dmytromk.asteroids.gameobjects.Spaceship;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final Joystick joystick;
    private final Spaceship spaceship;
    private final List<Asteroid> asteroidList = new ArrayList<>();

    private GameLoop gameLoop;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (joystick.isPressed(new Vector2(event.getX(), event.getY()))) {
                    joystick.setIsPressed(true);
                }

                return true;

            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()) {
                    joystick.setActuator(new Vector2(event.getX(), event.getY()));
                }

                return true;

            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();

                return true;
        }

        return super.onTouchEvent(event);
    }

    public GameView(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.gameLoop = new GameLoop(this, surfaceHolder);

        this.joystick = new Joystick(context, new Vector2(275, 700), 50, 70);
        this.spaceship = new Spaceship(getContext(), joystick, new Vector2(1000, 500));

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        joystick.draw(canvas);
        spaceship.draw(canvas);
        for (Asteroid asteroid : asteroidList) {
            asteroid.draw(canvas);
        }

        drawUPS(canvas);
        drawFPS(canvas);
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(this.gameLoop.getAverageUPS());
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(this.gameLoop.getAverageFPS());
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }

    public void update() {
        joystick.update();
        spaceship.update();

        if (Asteroid.readyToSpawn()) {
            asteroidList.add(new Asteroid(getContext(), spaceship));
        }

        for (Asteroid asteroid : asteroidList) {
            asteroid.update();
        }
    }
}
