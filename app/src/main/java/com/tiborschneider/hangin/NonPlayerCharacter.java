package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.util.Random;

/**
 * Created by Tibor Schneider on 26.06.2016.
 */
public class NonPlayerCharacter extends GameObject{

    public static int numImages = 8;
    public static int originalImageSize= 64;
    private String name;
    private String imageBaseName;
    boolean randomMovement = false;
    private int motionCounter = 0;
    private int prevMotionCounter = 0;
    private int animationSpeed = 10;
    private static int changeOfNotMovingRandom = 8;
    private GameScene currentScene;
    private Bitmap[] imageArray = new Bitmap[numImages];

    public NonPlayerCharacter(GamePanel aGamePanel, Context aContext, String aName, String aImageBaseName, Direction aDirection, int aX, int aY, GameScene aGameScene, int aSpeed)
    {
        super(aGamePanel, aContext, aDirection, aX, aY, aSpeed);
        name = aName;
        imageBaseName = aImageBaseName;
        currentScene = aGameScene;

        imageArray[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.npc_green_up1);
        imageArray[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.npc_green_up2);
        imageArray[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.npc_green_down1);
        imageArray[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.npc_green_down2);
        imageArray[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.npc_green_left1);
        imageArray[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.npc_green_left2);
        imageArray[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.npc_green_right1);
        imageArray[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.npc_green_right2);
        //scale all Images
        for (int i = 0; i < numImages; i++)
        {
            float scale = ((float) InterfaceElement.tileSize) / imageArray[i].getWidth();
            Matrix matrix = new Matrix();
            matrix.postScale(scale,scale);
            imageArray[i] = Bitmap.createBitmap(imageArray[i], 0, 0, imageArray[i].getWidth(), imageArray[i].getHeight(), matrix, false);
        }

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


    @Override
    public void updateImage()
    {
        int imageNr;
        if (motionCounter < animationSpeed / 2 )
            imageNr = 0;
        else
            imageNr = 1;
        switch (direction)
        {
            case UP:
                image = imageArray[imageNr];
                break;
            case DOWN:
                image = imageArray[2+imageNr];
                break;
            case LEFT:
                image = imageArray[4+imageNr];
                break;
            case RIGHT:
                image = imageArray[6+imageNr];
                break;
            default:
                image = imageArray[0];
        }
    }
}
