package com.tiborschneider.hangin.mainGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.tiborschneider.hangin.character.Direction;
import com.tiborschneider.hangin.character.NonPlayerCharacter;
import com.tiborschneider.hangin.character.Player;
import com.tiborschneider.hangin.scene.GameJumpHandler;
import com.tiborschneider.hangin.scene.GameScene;
import com.tiborschneider.hangin.state.QuestHandler;
import com.tiborschneider.hangin.state.StateHandler;
import com.tiborschneider.hangin.tile.ImageHandler;
import com.tiborschneider.hangin.userInteraction.InteractionHandler;
import com.tiborschneider.hangin.userInteraction.InterfaceElement;

/**
 * Created by Tibor Schneider on 18.06.2016.
 * Main Game calss which manages all objects
 */


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static int screenWidth = 1080;  //default values
    public static int screenHeight = 1920;
    public static int numScenes = 20;
    private static int numNpc = 30;
    private static GamePanel theGamePanel;
    private MainThread thread;
    private GameScene scene;
    private NonPlayerCharacter[] npc = new NonPlayerCharacter[numNpc];
    private int currentScene = 0;
    private int lastScene = -1;
    private Player player;
    private InteractionHandler interactionHandler;
    private DatabaseHelper databaseHelper;
    private StateHandler stateHandler;
    private ImageHandler imageHandler;
    boolean startGame = false;
    float lastPaintValue = 0.0f;
    private QuestHandler questHandler;
    Context context;

    public GamePanel(Context aContext, int sizeX, int sizeY)
    {
        super(aContext);
        theGamePanel = this;
        context = aContext;
        //add the callback to surface holder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);

        screenWidth = sizeX;
        screenHeight = sizeY;
        InterfaceElement.initSizes();

        //create Quest Handler
        questHandler = new QuestHandler();


        //create Database
        databaseHelper = new DatabaseHelper(context, this);

        //create State Handler
        stateHandler = new StateHandler(this, databaseHelper);;


        //create Player
        player = new Player(this, context, 5, 5);

        //get Saved States of Player
        if (!databaseHelper.initSavedPlayer()) {
            startGame = true;
        }


        long timeStart = System.currentTimeMillis();

        //load Images
        imageHandler = new ImageHandler();

        //load Scenes
        //scenes = databaseHelper.getGameScenes();
        scene = databaseHelper.getGameScene(currentScene);
        long timeEnd = System.currentTimeMillis();
        System.out.println("loading scenes from db took " + (timeEnd - timeStart));

        //load NPCs
        databaseHelper.getAllNpc();

        //load Quests
        databaseHelper.getAllQuestsFromDB();

        //create Controller for Player
        interactionHandler = new InteractionHandler(context, player, thread, this, questHandler);

    }


    private void startNewGame() {
        interactionHandler.createDialogue(interactionHandler.getDialogueFromDatabase("initialDialogue"));
    }

    @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override public void surfaceDestroyed(SurfaceHolder holder)
    {
        System.out.println("GamePanel.surfaceDestroyed()");
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
        System.out.println("GamePanel.surfaceCreated()");
        //we can safely start the game loop
        if (thread.getState()==Thread.State.TERMINATED) {
            thread = new MainThread(getHolder(),this);
        }
        if (thread.getState() == Thread.State.NEW) {
            thread.setRunning(true);
            thread.start();
        } else {
            System.out.println("GamePanel.surfaceCreated(): try to restart thread");
            thread.setRunning(true);
            thread.start();
        }
    }

    public void update()
    {
        if (startGame) {
            startGame = false;
            startNewGame();
        }

        //update Game Graphics only if no interface is active
        if (!interactionHandler.isInterfaceActive() || interactionHandler.isDialogueActive()) {
            player.update();

            //update animation tiles
            scene.update();

            //update npc on the same GameScene¨
            for (int i = 0; i < numNpc; i++)
                if (npc[i] != null && npc[i].isOnScene(currentScene))
                    npc[i].update();

            //check game Jumps
            if (GameJumpHandler.jumpsAllowed && player.getTmpX() == 0 && player.getTmpY() == 0 && scene.isJumpTile(player.getX(), player.getY())) {
                stateHandler.updateTimeToPass();
                int nextScene = scene.getTargetScene(player.getX(), player.getY());
                player.teleport(scene.getXTarget(player.getX(), player.getY()), scene.getYTarget(player.getX(), player.getY()));
                currentScene = nextScene;
                scene = databaseHelper.getGameScene(currentScene);
                GameJumpHandler.jumpsAllowed = false;
            }
        } else {
            //update Interface
            interactionHandler.update();
        }
    }

    @Override public void draw(Canvas canvas)
    {
        super.draw(canvas);
        if (canvas != null)
        {
            //get player's stoned level
            float stonedLevel = (((float)((int)(player.getStonedMeter()/4))/25)*4/5)+0.2f;
            Paint stonedPaint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(stonedLevel);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
            stonedPaint.setColorFilter(filter);

            //draw scene if scene has changed
            if (sceneHasChanged() || lastPaintValue != stonedLevel) {
                lastPaintValue = stonedLevel;
                lastScene = currentScene;
                //create Scene image
                scene.createSceneImage(stonedPaint);
            }

            scene.draw(canvas, stonedPaint);
            player.draw(canvas, stonedPaint);

            //npc npc on the same GameScene
            for (int i = 0; i < numNpc; i++) {
                if (npc[i] != null && npc[i].isOnScene(currentScene)) {
                    npc[i].draw(canvas, stonedPaint);
                }
            }

            interactionHandler.drawController(canvas);
            interactionHandler.drawInterface(canvas);


        }
    }

    private boolean sceneHasChanged()
    {
        //return true;
        return currentScene != lastScene;
    }

    public void redrawScene()
    {
        lastScene = -1;
    }

    public boolean onTouchEvent(MotionEvent e)
    {
        return interactionHandler.onTouch(e);
    }

    public boolean isWalkable(int aX, int aY) { return scene.isWalkable(aX, aY); }

    public boolean canWalkUp(int aX, int aY) {return scene.canWalkUp(aX, aY);}

    public boolean canWalkDown(int aX, int aY) {return scene.canWalkDown(aX, aY);}

    public boolean canWalkLeft(int aX, int aY) {return scene.canWalkLeft(aX, aY);}

    public boolean canWalkRight(int aX, int aY) {return scene.canWalkRight(aX, aY);}

    public GameScene getScene() { return scene; }


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

    public void newNpc(String aName, String aImageBaseName, Direction aDirection, int aX, int aY, int gameSceneIndex, int aSpeed)
    {
        for (int i = 0; i < numNpc; i++) {
            if (npc[i] == null) {
                npc[i] = new NonPlayerCharacter(this, context, aName, aImageBaseName, aDirection, aX, aY, gameSceneIndex, aSpeed);
                break;
            }
        }
    }

    public NonPlayerCharacter getNpc(int currentScene, int aX, int aY)
    {
        for (int i = 0; i < numNpc; i++)
            if (npc[i] != null && npc[i].isOnScene(currentScene) && npc[i].getX() == aX && npc[i].getY() == aY)
                return npc[i];
        return null;
    }

    public NonPlayerCharacter getNpc(String name) {
        for (int i = 0; i < GamePanel.numNpc; i++) {
            if (npc[i] != null && npc[i].getName().equals(name)) {
                return npc[i];
            }
        }
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

    public static GamePanel getGamePanel()
    {
        return theGamePanel;
    }


    public boolean isPlayerOn(int x, int y) {
        if (player.getX() == x && player.getY() == y) {
            return true;
        }
        return false;
    }

    public QuestHandler getQuestHandler() {
        return questHandler;
    }

    public ImageHandler getImageHandler() {
        return imageHandler;
    }

}
