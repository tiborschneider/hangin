package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Tibor Schneider on 18.06.2016.
 */


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static int screenWidth = 1080;  //default values
    public static int screenHeight = 1920;
    public static int numScenes = 20;
    private static int numNpc = 30;
    private MainThread thread;
    private GameScene[] scenes = new GameScene[numScenes];
    private NonPlayerCharacter[] npc = new NonPlayerCharacter[numNpc];
    private int currentScene = 0;
    private Player player;
    private InteractionHandler interactionHandler;
    private DatabaseHelper databaseHelper;
    private StateHandler stateHandler;
    Context context;

    public GamePanel(Context aContext, int sizeX, int sizeY)
    {
        super(aContext);
        context = aContext;
        //add the callback to surface holder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);

        screenWidth = sizeX;
        screenHeight = sizeY;
        InterfaceElement.initSizes();


        //create Database
        databaseHelper = new DatabaseHelper(context, this);
        stateHandler = new StateHandler(this, databaseHelper);

        //load Scenes and NPC's from Database;
        scenes = databaseHelper.getGameScenes();
        databaseHelper.getAllNpc();

        //create Player
        player = new Player(this, context, 5, 5);

        //get Saved States of Player
        databaseHelper.initSavedPlayer();

        //create Controller for Player
        interactionHandler = new InteractionHandler(context, player, thread, this);
    }

    @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        while(retry)
        {
            try {
                thread.setRunning(false);
                thread.join();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override public void surfaceCreated(SurfaceHolder holder)
    {
        //we can safely start the game loop
        if (thread.getState()==Thread.State.TERMINATED) {
            thread = new MainThread(getHolder(),this);
        }
        if (thread.getState() == Thread.State.NEW) {
            thread.setRunning(true);
            thread.start();
        } else {
            System.out.println("try to restart thread");
            thread.setRunning(true);
            thread.start();
        }
    }

    public void update()
    {
        player.update();

        //update npc on the same GameScene
        for (int i = 0; i < numNpc; i++)
            if (npc[i] != null && npc[i].isOnScene(scenes[currentScene]))
                npc[i].update();

        //check game Jumps
        if (GameJumpHandler.jumpsAllowed && player.getTmpX() == 0 && player.getTmpY() == 0 && scenes[currentScene].isJumpTile(player.getX(), player.getY()))
        {
            int nextScene = scenes[currentScene].getTargetScene(player.getX(), player.getY());
            player.teleport(scenes[currentScene].getXTarget(player.getX(), player.getY()), scenes[currentScene].getYTarget(player.getX(), player.getY()));
            currentScene = nextScene;
            GameJumpHandler.jumpsAllowed = false;
        }
    }

    @Override public void draw(Canvas canvas)
    {
        super.draw(canvas);
        if (canvas != null)
        {
            scenes[currentScene].draw(canvas);
            player.draw(canvas);

            //npc npc on the same GameScene
            for (int i = 0; i < numNpc; i++) {
                if (npc[i] != null && npc[i].isOnScene(scenes[currentScene])) {
                    npc[i].draw(canvas);
                }
            }

            interactionHandler.drawController(canvas);
            interactionHandler.drawInterface(canvas);


        }
    }

    public boolean onTouchEvent(MotionEvent e)
    {
        return interactionHandler.onTouch(e);
    }

    public boolean isWalkable(int aX, int aY) { return scenes[currentScene].isWalkable(aX, aY); }

    public boolean canWalkUp(int aX, int aY) {return scenes[currentScene].canWalkUp(aX, aY);}

    public boolean canWalkDown(int aX, int aY) {return scenes[currentScene].canWalkDown(aX, aY);}

    public boolean canWalkLeft(int aX, int aY) {return scenes[currentScene].canWalkLeft(aX, aY);}

    public boolean canWalkRight(int aX, int aY) {return scenes[currentScene].canWalkRight(aX, aY);}

    public GameScene getScene() { return scenes[currentScene]; }

    public MainThread getThread()
    {
        return thread;
    }

    public InteractionHandler getInteractionHandler()
    {
        return interactionHandler;
    }

    public DatabaseHelper getDatabaseHelper()
    {
        return databaseHelper;
    }

    public Player getPlayer() {
        return player;
    }

    public StateHandler getStateHandler()
    {
        return stateHandler;
    }

    public void newNpc(String aName, String aImageBaseName, Direction aDirection, int aX, int aY, GameScene aGameScene, int aSpeed)
    {
        for (int i = 0; i < numNpc; i++)
            if (npc[i] == null)
                npc[i] = new NonPlayerCharacter(this, context, aName, aImageBaseName, aDirection, aX, aY, aGameScene, aSpeed);
    }

    public void newNpc(String aName, String aImageBaseName, Direction aDirection, int aX, int aY, int gameSceneIndex, int aSpeed)
    {
        for (int i = 0; i < numNpc; i++) {
            if (npc[i] == null) {
                npc[i] = new NonPlayerCharacter(this, context, aName, aImageBaseName, aDirection, aX, aY, scenes[gameSceneIndex], aSpeed);
                break;
            }
        }
    }

    public NonPlayerCharacter getNpc(GameScene aScene, int aX, int aY)
    {
        for (int i = 0; i < numNpc; i++)
            if (npc[i] != null && npc[i].isOnScene(aScene) && npc[i].getX() == aX && npc[i].getY() == aY)
                return npc[i];
        return null;
    }

    public void setCurrentScene(int index)
    {
        //this method should not be called while the game is running!!
        currentScene = index;
    }

    public int getCurrentScene()
    {
        return currentScene;
    }
}
