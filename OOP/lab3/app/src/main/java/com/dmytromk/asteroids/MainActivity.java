package com.dmytromk.asteroids;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void startGame(View view) {
        GameView gameView = new GameView(this);
        gameView.setZOrderOnTop(true);
        gameView.getHolder().setFormat(PixelFormat.TRANSPARENT);

        ImageView bgImagePanel = new ImageView(this);
        bgImagePanel.setBackgroundResource(R.drawable.background_2);

        RelativeLayout.LayoutParams fillParentLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        RelativeLayout rootPanel = new RelativeLayout(this);
        rootPanel.setLayoutParams(fillParentLayout);
        rootPanel.addView(gameView, fillParentLayout);
        rootPanel.addView(bgImagePanel, fillParentLayout);

        setContentView(rootPanel);
    }
}