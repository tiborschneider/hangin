package com.tiborschneider.hangin.character;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.userInteraction.Controller;
import com.tiborschneider.hangin.userInteraction.InteractionHandler;
import com.tiborschneider.hangin.userInteraction.InterfaceElement;

/**
 * Created by Tibor Schneider on 20.06.2016.
 */
public abstract class GameObject {
    public static final int playerDisplayOffset = -8;
    protected Direction direction;
    protected int x;
    protected int y;
    protected int tmpX;
    protected int tmpY;
    protected int dx;
    protected int dy;
    protected int speed;
    protected Direction queue;
    protected Bitmap image;
    protected Context context;
    protected GamePanel gamePanel;
    protected int motionCounter = 0;
    protected Bitmap[][] imageArray = new Bitmap[4][9];
    protected int prevMotionCounter = 0;
    protected int updateTime = 2;
    protected final int numAnimations = 8;
    protected int animationSpeed = updateTime * numAnimations;

    public GameObject(GamePanel aGamePanel, Context aContext, Direction aDirection, int aX, int aY, int aSpeed)
    {
        x = aX;
        y = aY;
        tmpX = 0;
        tmpY = 0;
        dx = 0;
        dy = 0;
        direction = aDirection;
        speed = aSpeed;
        context = aContext;
        gamePanel = aGamePanel;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getTmpX() { return tmpX; }

    public int getTmpY() { return tmpY; }

    public Direction getDirection() { return direction; }

    public void updateMovement()
    {
        tmpX += dx;
        tmpY += dy;
        if ((dx > 0 && tmpX + speed > 0) || (dx < 0 && tmpX - speed < 0)) //(tmpX == 0)
        {
            dx = 0;
            tmpX = 0;
        }
        if ((dy > 0 && tmpY + speed > 0) || (dy < 0 && tmpY - speed < 0)) //(tmpY == 0)
        {
            dy = 0;
            tmpY = 0;
        }

    }

    public void update()
    {
        updateMovement();
    }

    public void draw(Canvas canvas, Paint stonedPaint)
    {

    }

    public void walk(Direction aDir)
    {
        if (dx == 0 && dy == 0 && tmpX == 0 && tmpY == 0) {
            Direction oldDirection = direction;
            direction = aDir;
            updateImage();
            if (oldDirection == direction) {
                switch (aDir) {
                    case UP:
                        if (gamePanel.isWalkable(x, y - 1) && gamePanel.canWalkUp(x, y) && gamePanel.canWalkDown(x, y - 1) && !gamePanel.isPlayerOn(x, y-1)) {
                            dx = 0;
                            dy = -speed;
                            tmpX = 0;
                            tmpY = InterfaceElement.tileSize;
                            y -= 1;
                        }
                        break;
                    case DOWN:
                        if (gamePanel.isWalkable(x, y + 1) && gamePanel.canWalkDown(x, y) && gamePanel.canWalkUp(x, y + 1) && !gamePanel.isPlayerOn(x, y+1)) {
                            dx = 0;
                            dy = speed;
                            tmpX = 0;
                            tmpY = -InterfaceElement.tileSize;
                            y += 1;
                        }
                        break;
                    case LEFT:
                        if (gamePanel.isWalkable(x - 1, y) && gamePanel.canWalkLeft(x, y) && gamePanel.canWalkRight(x - 1, y) && !gamePanel.isPlayerOn(x-1, y)) {
                            dx = -speed;
                            dy = 0;
                            tmpX = InterfaceElement.tileSize;
                            tmpY = 0;
                            x -= 1;
                        }
                        break;
                    case RIGHT:
                        if (gamePanel.isWalkable(x + 1, y) && gamePanel.canWalkRight(x, y) && gamePanel.canWalkLeft(x + 1, y) && !gamePanel.isPlayerOn(x+1, y)) {
                            dx = speed;
                            dy = 0;
                            tmpX = -InterfaceElement.tileSize;
                            tmpY = 0;
                            x += 1;
                        }
                        break;
                }
            }
        }
    }

    public void updateImage()
    {
        int imageNr = 0;
        if (tmpX != 0 || tmpY != 0 || continueWalking()) {
            imageNr = (motionCounter/updateTime) + 1;
            System.out.println("imageNr: " + imageNr);
        } else {
            System.out.println("Stop Animation");
        }
        switch (direction)
        {
            case UP:
                image = imageArray[0][imageNr];
                break;
            case DOWN:
                image = imageArray[2][imageNr];
                break;
            case LEFT:
                image = imageArray[1][imageNr];
                break;
            case RIGHT:
                image = imageArray[3][imageNr];
                break;
            default:
                image = imageArray[0][0];
        }
    }

    public static Bitmap[][] cutCharacterAnimation(String name, int numAnimations) {
        Bitmap[][] image = new Bitmap[4][numAnimations];
        int resID = GamePanel.getGamePanel().getContext().getResources().getIdentifier(name, "drawable", GamePanel.getGamePanel().getContext().getPackageName());
        Bitmap sprite = BitmapFactory.decodeResource(GamePanel.getGamePanel().getContext().getResources(), resID);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < numAnimations; j++) {
                image[i][j] = Bitmap.createBitmap(sprite, j * InterfaceElement.tileSize, i * InterfaceElement.tileSize, InterfaceElement.tileSize, InterfaceElement.tileSize);
            }
        }
        return image;
    }

    protected boolean continueWalking() {
        return true;
    }

    protected void updateStepCounter() {

        //update StepCounter
        prevMotionCounter = motionCounter;
        if (dx != 0 || dy != 0 || continueWalking()) {
            motionCounter++;
            if (motionCounter == animationSpeed) {
                motionCounter = 0;
            }
            updateImage();
        } else {
            motionCounter = 0;
            if (prevMotionCounter != 0)
                updateImage();
        }
    }
}
