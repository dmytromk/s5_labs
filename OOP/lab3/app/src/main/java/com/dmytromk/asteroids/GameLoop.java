package com.dmytromk.asteroids;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
    private final static double MAX_UPS = 120.0;
    // times for one update in milliseconds
    private final static double UPS_PERIOD = 1000 / MAX_UPS;

    private double averageUPS;
    private double averageFPS;

    private GameView gameView;
    private SurfaceHolder surfaceHolder;
    private boolean isRunning = false;


    public GameLoop(GameView gameView, SurfaceHolder surfaceHolder) {
        this.gameView = gameView;
        this.surfaceHolder = surfaceHolder;
    }

    public double getAverageUPS() {
        return averageUPS;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public void startLoop() {
        this.isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();

        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        // Actual loop
        Canvas canvas = null;
        startTime = System.currentTimeMillis();
        while (isRunning) {
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.update();
                    updateCount++;

                    this.gameView.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            updateCount++;
            frameCount++;

            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while(sleepTime < 0 && updateCount < MAX_UPS - 1) {
                this.gameView.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount*UPS_PERIOD - elapsedTime);
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= 1000) {
                this.averageUPS = updateCount / (elapsedTime / 1000.0);
                this.averageFPS = frameCount / (elapsedTime / 1000.0);

                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }
}
