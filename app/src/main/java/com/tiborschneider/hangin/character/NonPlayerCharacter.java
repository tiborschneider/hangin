package com.tiborschneider.hangin.character;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.tiborschneider.hangin.dialogue.Dialogue;
import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.scene.GameScene;
import com.tiborschneider.hangin.userInteraction.InteractionHandler;
import com.tiborschneider.hangin.userInteraction.InterfaceElement;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Tibor Schneider on 26.06.2016.
 */
public class NonPlayerCharacter extends GameObject {

    private String name;
    private String imageBaseName;
    boolean randomMovement = false;
    private static int changeOfNotMovingRandom = 8;
    //private GameScene currentScene;
    private int currentScene;
    private Queue<MoveCommand> queue;

    public NonPlayerCharacter(GamePanel aGamePanel, Context aContext, String aName, String aImageBaseName, Direction aDirection, int aX, int aY, int aSceneIndex, int aSpeed)
    {
        super(aGamePanel, aContext, aDirection, aX, aY, aSpeed);
        name = aName;
        imageBaseName = aImageBaseName;
        currentScene = aSceneIndex;
        queue = new LinkedList<>();
        loadImages();
    }

    public void queueMovement(MoveCommand command) {
        queue.offer(command);
    }

    @Override public void update()
    {
        Direction prevDirection = Direction.NDEF;
        if (randomMovement && (dx == 0 && dy == 0)) walk(randomDirection());
        if (dx == 0 && dy == 0 && !InteractionHandler.interfaceActive) {
            MoveCommand command = queue.peek();
            if (command != null) {
                if (command.isJump()) {
                    queue.poll();
                    teleport(command.getJumpToScene(), command.getJumpToX(), command.getJumpToY(), prevDirection);
                    Direction newDirection = queue.poll().getDirection();
                    if (newDirection != prevDirection)
                        walk(newDirection);
                } else {
                    boolean doMovement = true;
                    switch (command.getDirection()) {
                        case UP:
                            doMovement = !gamePanel.isPlayerOn(x, y-1);
                            break;
                        case DOWN:
                            doMovement = !gamePanel.isPlayerOn(x, y+1);
                            break;
                        case LEFT:
                            doMovement = !gamePanel.isPlayerOn(x-1, y);
                            break;
                        case RIGHT:
                            doMovement = !gamePanel.isPlayerOn(x+1, y);
                            break;
                        default:
                            doMovement = false;
                    }
                    if (doMovement) {
                        walk(command.getDirection());
                        prevDirection = command.getDirection();
                        queue.poll();
                    }
                }

                //save new Position
                gamePanel.getDatabaseHelper().updateNpcPosition(this, currentScene, x, y, direction);
            }
        }
        updateMovement();

        updateStepCounter();
    }

    @Override
    public void draw(Canvas canvas, Paint stonedPaint)
    {
        canvas.drawBitmap(image, x* InterfaceElement.tileSize + tmpX + InterfaceElement.gameBorderSize, y* InterfaceElement.tileSize + tmpY + 2*InterfaceElement.statusBarOuterMargin + InterfaceElement.statusBarHeight + playerDisplayOffset, stonedPaint);
    }

    public boolean isOnScene(int sceneIndex)
    {
        return (currentScene == sceneIndex);
    }

    public void teleport(int sceneIndex, int aX, int aY, Direction aDirection)
    {
        currentScene = sceneIndex;
        x = aX;
        y = aY;
        direction = aDirection;
    }

    public void setRandomMovement(boolean b)
    {
        randomMovement = b;
    }

    private Direction randomDirection()
    {
        int min = 0;
        int max = 3 + changeOfNotMovingRandom;
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        switch (randomNum)
        {
            case 0:
                return Direction.UP;
            case 1:
                return Direction.DOWN;
            case 2:
                return Direction.LEFT;
            case 3:
                return Direction.RIGHT;
            default:
                return Direction.NDEF;
        }
    }

    public Dialogue getCurrentDialogue()
    {
        return gamePanel.getStateHandler().getNpcDialogue(name);
    }

    public void turn(Direction aDirection)
    {
        direction = aDirection;
        updateImage();
    }

    public String getName() {
        return name;
    }

    public void loadImages() {

        imageArray = GameObject.cutCharacterAnimation(imageBaseName, 9);

        //scale all Images
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                float scale = ((float) InterfaceElement.tileSize) / imageArray[i][j].getWidth();
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                imageArray[i][j] = Bitmap.createBitmap(imageArray[i][j], 0, 0, imageArray[i][j].getWidth(), imageArray[i][j].getHeight(), matrix, false);
            }
        }

        updateImage();
    }

    public int getCurrentScene() {
        return currentScene;
    }

    //public int getSceneIndex() {
    //    return gamePanel.getSceneId(currentScene);
    //}

    @Override
    protected boolean continueWalking() {
        if (queue.peek() != null)
            return true;
        return false;
    }
}
