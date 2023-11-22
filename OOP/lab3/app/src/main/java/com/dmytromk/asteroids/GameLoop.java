package com.dmytromk.asteroids;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
    private GameView gameView;
    private SurfaceHolder surfaceHolder;
    private boolean isRunning = false;


    public GameLoop(GameView gameView, SurfaceHolder surfaceHolder) {
        this.gameView = gameView;
        this.surfaceHolder = surfaceHolder;
    }

    public double getAverageUPS() {
        return 0;
    }

    public double getAverageFPS() {
        return 0;
    }

    public void startLoop() {
        this.isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();

        // Actual loop
        Canvas canvas;
        while (isRunning) {
            try {
                canvas = surfaceHolder.lockCanvas();
                gameView.update();
                gameView.draw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
