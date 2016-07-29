package com.tiborschneider.hangin.mainGame;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.Display;

public class game extends Activity {

    private GamePanel gamePanel;
    public boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Game.onCreate()");
        super.onCreate(savedInstanceState);

        running = true;

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
        System.out.println("Game.onPause()");
        running = false;

        //close all Windows
        gamePanel.getInteractionHandler().saveDialogue();
        gamePanel.getInteractionHandler().closeAllWindows();
        gamePanel.getThread().pause();
        super.onPause();
    }

    @Override protected void onResume() {
        System.out.println("Game.onResume()");
        running = true;

        //restore saved Dialogue
        gamePanel.getInteractionHandler().restoreSavedDialogue();

        super.onResume();
    }
}
