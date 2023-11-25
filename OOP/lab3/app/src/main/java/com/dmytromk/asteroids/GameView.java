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
import com.dmytromk.asteroids.gameobjects.GameObject2D;
import com.dmytromk.asteroids.gameobjects.Missile;
import com.dmytromk.asteroids.gameobjects.Spaceship;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final Joystick joystick;
    private final Spaceship spaceship;
    private final List<Asteroid> asteroidList = new ArrayList<>();
    private final List<Missile> missileList = new ArrayList<>();

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
        List<Map.Entry<GameObject2D, GameObject2D>> collidingAsteroidsPairsList = new ArrayList<>();

        //spawning
        if (Asteroid.readyToSpawn() && asteroidList.size() < 10) {
            Asteroid toAdd = new Asteroid(getContext());

            for (int i = 0; i < asteroidList.size(); i++) {
                if (GameObject2D.checkCollisionCircles(toAdd, asteroidList.get(i))
                || GameObject2D.checkCollisionCircles(toAdd, spaceship)) {
                    toAdd.resetCoordinates();
                    i = 0;
                }
            }

            asteroidList.add(toAdd);
        }

        for (int i = 0; i < asteroidList.size(); i++) {
            // static collision between asteroids
            for (int j = i + 1; j < asteroidList.size(); j++) {
                if (GameObject2D.checkCollisionCircles(asteroidList.get(i), asteroidList.get(j))) {
                    GameObject2D.resolveStaticCollisionCircles(asteroidList.get(i),
                            asteroidList.get(j));

                    collidingAsteroidsPairsList.add(new AbstractMap.SimpleImmutableEntry<> (asteroidList.get(i),
                            asteroidList.get(j)));
                }
            }

            // collision with spaceship
            if (GameObject2D.checkCollisionCircles(asteroidList.get(i), spaceship)) {
                asteroidList.remove(i);
                i--;
            }
        }

        // dynamic collisions
        for (Map.Entry<GameObject2D, GameObject2D> pair : collidingAsteroidsPairsList) {
            GameObject2D.resolveDynamicCollisionCircles(pair.getKey(), pair.getValue());
        }

        for (Asteroid asteroid : asteroidList) {
            asteroid.update();
        }

        joystick.update();
        spaceship.update();
    }
}
