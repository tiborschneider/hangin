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
    private GameScene[] scenes = new GameScene[numScenes];
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

        long timeStart = System.currentTimeMillis();

        //create Database
        databaseHelper = new DatabaseHelper(context, this);
        long timeDB = System.currentTimeMillis();
        System.out.println("loading database took " + (timeDB - timeStart));

        //create State Handler
        stateHandler = new StateHandler(this, databaseHelper);;
        long timeState = System.currentTimeMillis();
        System.out.println("loading states from db took " + (timeState - timeDB));

        //load Images
        imageHandler = new ImageHandler();
        long timeImages = System.currentTimeMillis();
        System.out.println("loading Images took " + (timeImages - timeState));

        //load Scenes
        scenes = databaseHelper.getGameScenes();
        long timeScenes = System.currentTimeMillis();
        System.out.println("loading scenes from db took " + (timeScenes - timeImages));

        //load NPCs
        databaseHelper.getAllNpc();
        long timeNpc = System.currentTimeMillis();
        System.out.println("loading npcs from db took " + (timeNpc - timeScenes));

        //load Quests
        databaseHelper.getAllQuestsFromDB();
        long timeQuests = System.currentTimeMillis();
        System.out.println("loading npcs from db took " + (timeQuests - timeNpc));

        //create Player
        player = new Player(this, context, 5, 5);

        //create Controller for Player
        interactionHandler = new InteractionHandler(context, player, thread, this, questHandler);

        //get Saved States of Player
        if (!databaseHelper.initSavedPlayer()) {
            startGame = true;
        }
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
            scenes[currentScene].update();

            //update npc on the same GameScene¨
            for (int i = 0; i < numNpc; i++)
                if (npc[i] != null && npc[i].isOnScene(scenes[currentScene]))
                    npc[i].update();

            //check game Jumps
            if (GameJumpHandler.jumpsAllowed && player.getTmpX() == 0 && player.getTmpY() == 0 && scenes[currentScene].isJumpTile(player.getX(), player.getY())) {
                stateHandler.updateTimeToPass();
                int nextScene = scenes[currentScene].getTargetScene(player.getX(), player.getY());
                player.teleport(scenes[currentScene].getXTarget(player.getX(), player.getY()), scenes[currentScene].getYTarget(player.getX(), player.getY()));
                currentScene = nextScene;
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
                scenes[currentScene].createSceneImage(stonedPaint);
            }

            scenes[currentScene].draw(canvas, stonedPaint);
            player.draw(canvas, stonedPaint);

            //npc npc on the same GameScene
            for (int i = 0; i < numNpc; i++) {
                if (npc[i] != null && npc[i].isOnScene(scenes[currentScene])) {
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

    public boolean isWalkable(int aX, int aY) { return scenes[currentScene].isWalkable(aX, aY); }

    public boolean canWalkUp(int aX, int aY) {return scenes[currentScene].canWalkUp(aX, aY);}

    public boolean canWalkDown(int aX, int aY) {return scenes[currentScene].canWalkDown(aX, aY);}

    public boolean canWalkLeft(int aX, int aY) {return scenes[currentScene].canWalkLeft(aX, aY);}

    public boolean canWalkRight(int aX, int aY) {return scenes[currentScene].canWalkRight(aX, aY);}

    public GameScene getScene() { return scenes[currentScene]; }

    public GameScene getScene(int index) {
        if (index >= 0 && index < numScenes) {
            return scenes[index];
        }
        return null;
    }

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

    public int getSceneId(GameScene scene) {
        for (int i = 0; i < numScenes; i++) {
            if (scenes[i] == scene) {
                return i;
            }
        }
        return -1;
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
