package com.tiborschneider.hangin;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.Display;

public class game extends Activity {

    private GamePanel gamePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set screen to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        //get Size of screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        gamePanel = new GamePanel(this, size.x, size.y);
        setContentView(gamePanel);
    }

    @Override protected void onPause()
    {
        //close all Windows
        gamePanel.getInteractionHandler().closeAllWindows();
        gamePanel.getThread().pause();
        super.onPause();
    }
}
