package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.BitmapFactory;
import java.util.Random;

/**
 * Created by Tibor Schneider on 26.06.2016.
 */
public class NonPlayerCharacter extends GameObject{

    private String name;
    private String imageBaseName;
    boolean randomMovement = false;
    private int motionCounter = 0;
    private int prevMotionCounter = 0;
    private int animationSpeed = 10;
    private static int changeOfNotMovingRandom = 8;
    private GameScene currentScene;

    public NonPlayerCharacter(GamePanel aGamePanel, Context aContext, String aName, String aImageBaseName, Direction aDirection, int aX, int aY, GameScene aGameScene, int aSpeed)
    {
        super(aGamePanel, aContext, aDirection, aX, aY, aSpeed);
        name = aName;
        imageBaseName = aImageBaseName;
        currentScene = aGameScene;
        updateImage();
    }

    @Override public void update()
    {
        if (randomMovement && (dx != 0 || dy != 0)) walk(randomDirection());
        updateMovement();


        //update StepCounter
        prevMotionCounter = motionCounter;
        if (dx != 0 || dy != 0) {
            motionCounter++;
            if (motionCounter == animationSpeed) {
                motionCounter = 0;
                updateImage();
            } else if (motionCounter == animationSpeed / 2) {
                updateImage();
            }
        } else {
            motionCounter = 0;
            if (prevMotionCounter != 0)
                updateImage();
        }
    }

    @Override public int getImageId()
    {
        String imageName = imageBaseName;
        int imageNr;
        if (motionCounter < animationSpeed / 2 )
            imageNr = 1;
        else
            imageNr = 2;
        switch (direction)
        {
            case UP:
                imageName += "_up" + imageNr;
                break;
            case DOWN:
                imageName += "_down" + imageNr;
                break;
            case LEFT:
                imageName += "_left" + imageNr;
                break;
            case RIGHT:
                imageName += "_right" + imageNr;
                break;
            default:
                imageName += "_down" + imageNr;
        }
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    public boolean isOnScene(GameScene aGameScene)
    {
        return (currentScene == aGameScene);
    }

    public void teleport(GameScene aGameScene, int aX, int aY, Direction aDirection)
    {
        currentScene = aGameScene;
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
}
