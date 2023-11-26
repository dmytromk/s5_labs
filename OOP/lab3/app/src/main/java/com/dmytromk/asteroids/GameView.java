package com.dmytromk.asteroids;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.util.Log;
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
import com.dmytromk.asteroids.ui.Performance;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final MediaPlayer backgroundMediaPlayer;
    private final Joystick joystick;
    private final Spaceship spaceship;
    private final List<Asteroid> asteroidList = Collections.synchronizedList(new ArrayList<>());
    private final List<Missile> missileList = Collections.synchronizedList(new ArrayList<>());
    private Boolean missileIsFired = false;

    private int score = 0;
    private int lives = 3;

    private GameLoop gameLoop;
    private Performance performance;

    public GameView(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.gameLoop = new GameLoop(this, surfaceHolder);

        this.performance = new Performance(context, gameLoop);

        this.backgroundMediaPlayer = MediaPlayer.create(context, R.raw.glorious_morning);
        backgroundMediaPlayer.setVolume(30,30);
        backgroundMediaPlayer.setLooping(true);

        this.joystick = new Joystick(context, new Vector2(275, 700), 50, 70);
        this.spaceship = new Spaceship(getContext(), joystick, new Vector2(1000, 500));

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (joystick.isPressed(new Vector2(event.getX(), event.getY()))) {
                    joystick.setIsPressed(true);
                } else {
                    missileIsFired = true;
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

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        backgroundMediaPlayer.start();
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        Log.d("Game.java", "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.d("Game.java", "surfaceDestroyed()");
        backgroundMediaPlayer.stop();
        backgroundMediaPlayer.release();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        super.draw(canvas);

        joystick.draw(canvas);
        spaceship.draw(canvas);

        for (Asteroid asteroid : asteroidList) {
            asteroid.draw(canvas);
        }

        for (Missile missile : missileList) {
            missile.draw(canvas);
        }

        performance.draw(canvas);

        drawScore(canvas);
        //drawLives(canvas);
    }

    public void drawScore(Canvas canvas) {
        int color = ContextCompat.getColor(getContext(), R.color.white);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Score: " + score, this.getWidth() - 400, 100, paint);
    }

    public void drawLives(Canvas canvas) {
        String averageFPS = Double.toString((int) this.gameLoop.getAverageFPS());
        int color = ContextCompat.getColor(getContext(), R.color.white);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Current lives: " + lives, this.getWidth() - 400, 200, paint);
    }

    public void update() {
        // Update objects
        for (Asteroid asteroid : asteroidList) {
            asteroid.update();
        }
        for (Missile missile : missileList) {
            missile.update();
        }
        joystick.update();
        spaceship.update();


        List<Map.Entry<GameObject2D, GameObject2D>> collidingAsteroidsPairsList = new ArrayList<>();

        // Spawning asteroids
        if (Asteroid.readyToSpawn() && asteroidList.size() < 10) {
            Asteroid toAdd = new Asteroid(getContext());
            boolean collisionDetected = false;

            while (true) {
                for (Asteroid existingAsteroid : asteroidList) {
                    if (GameObject2D.checkCollisionCircles(toAdd, existingAsteroid) ||
                            GameObject2D.checkCollisionCircles(toAdd, spaceship)) {
                        collisionDetected = true;
                        break;
                    }
                }

                // Check for collisions with missiles
                for (Missile missile : missileList) {
                    if (GameObject2D.checkCollisionCircles(toAdd, missile)) {
                        collisionDetected = true;
                        break;
                    }
                }

                if (collisionDetected) {
                    toAdd.resetCoordinates();
                    collisionDetected = false;
                } else {
                    break;
                }
            }

            asteroidList.add(toAdd);
        }

        // Spawn missile if one is fired
        if (missileIsFired) {
            missileList.add(new Missile(getContext(), spaceship));
            missileIsFired = false;
        }


        // Collision missiles with asteroids
        Iterator<Missile> missileIterator = missileList.iterator();
        while (missileIterator.hasNext()) {
            Missile missile = missileIterator.next();

            if (missile.finished()) {
                missileIterator.remove();
                continue;
            }

            Iterator<Asteroid> asteroidIterator = asteroidList.iterator();
            while (asteroidIterator.hasNext()) {
                Asteroid asteroid = asteroidIterator.next();

                if (GameObject2D.checkCollisionCircles(asteroid, missile)) {
                    missileIterator.remove();
                    asteroidIterator.remove();
                    score += 100;
                    break;
                }
            }
        }


        Iterator<Asteroid> asteroidIterator = asteroidList.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid currentAsteroid = asteroidIterator.next();

            // Static collision between asteroids
            Iterator<Asteroid> otherAsteroidIterator = asteroidList.iterator();
            while (otherAsteroidIterator.hasNext()) {
                Asteroid otherAsteroid = otherAsteroidIterator.next();

                if (currentAsteroid != otherAsteroid && GameObject2D.checkCollisionCircles(currentAsteroid, otherAsteroid)) {
                    GameObject2D.resolveStaticCollisionCircles(currentAsteroid, otherAsteroid);

                    collidingAsteroidsPairsList.add(new AbstractMap.SimpleImmutableEntry<>(currentAsteroid, otherAsteroid));
                }
            }

            // Collision with spaceship
            if (GameObject2D.checkCollisionCircles(currentAsteroid, spaceship)) {
                asteroidIterator.remove();
                score -= 200;
                lives -= 1;
            }
        }

        // Dynamic collisions
        for (Map.Entry<GameObject2D, GameObject2D> pair : collidingAsteroidsPairsList) {
            GameObject2D.resolveDynamicCollisionCircles(pair.getKey(), pair.getValue());
        }
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
