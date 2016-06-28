package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Created by Tibor Schneider on 19.06.2016.
 */
public class Player extends GameObject {
    public static int numImages = 8;
    private int motionCounter = 0;
    private int prevMotionCounter = 0;
    private int animationSpeed = 20;
    private static int meterMaximum = 100;
    private Inventory inventory;
    private int munchiesMeter = 100;
    private int stonedMeter = 0;
    private int nextMunchiesMeter = 100;
    private int nextStonedMeter = 0;
    private Bitmap[] imageArray = new Bitmap[numImages];

    public Player(GamePanel aGamePanel, Context aContext, int aX, int aY)
    {
        super(aGamePanel, aContext, Direction.DOWN, aX, aY, 4);
        context = aContext;
        inventory = new Inventory(context, gamePanel);

        //get all Images
        imageArray[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_up1);
        imageArray[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_up2);
        imageArray[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_down1);
        imageArray[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_down2);
        imageArray[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_left1);
        imageArray[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_left2);
        imageArray[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_right1);
        imageArray[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_right2);

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
        updateMovement();
        if (!GameJumpHandler.jumpsAllowed && (tmpX != 0 || tmpY != 0))
            GameJumpHandler.jumpsAllowed = true;

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
        if (nextStonedMeter > stonedMeter) {
            gamePanel.redrawScene();
            stonedMeter++;
        } else if (nextStonedMeter < stonedMeter) {
            gamePanel.redrawScene();
            stonedMeter--;
        }
    }

    public boolean hasItem(Item aItem)
    {
        return inventory.hasItem(aItem);
    }

    public boolean useItem(Item aItem)
    {
        if (!hasItem(aItem)) return false;
        inventory.useItem(aItem);
        return true;
    }

    public boolean addItem(Item aItem)
    {
        return inventory.addItem(aItem);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean hasEquippedItem()
    {
        return (inventory.getItemCount(inventory.getEquippedEntry()) != 0);
    }

    public Bitmap getEquippedItemImage()
    {
        return inventory.getImage(inventory.getEquippedEntry());
    }

    public Item getEquippedItem()
    {
        return inventory.getItem(inventory.getEquippedEntry());
    }

    public void consumeItem(Item aItem)
    {
        updateMunchiesMeter(aItem.getMunchiesChange());
        updateStonedMeter(aItem.getStonedChange());
        useItem(aItem);
    }

    public boolean updateMunchiesMeter(int change)
    {
        if (munchiesMeter + change < meterMaximum) {
            if (munchiesMeter + change > 0) {
                munchiesMeter += change;
            } else {
                munchiesMeter = 0;
                return false;
            }
        } else {
            munchiesMeter = meterMaximum;
        }
        return true;
    }

    public boolean updateStonedMeter(int change)
    {
        if (nextStonedMeter + change < meterMaximum) {
            if (nextStonedMeter + change > 0) {
                nextStonedMeter += change;
            } else {
                nextStonedMeter = 0;
                return false;
            }
        } else {
            nextStonedMeter = meterMaximum;
        }
        return true;
    }

    public void teleport(int aX, int aY)
    {
        x = aX;
        y = aY;
        dx = 0;
        dy = 0;
        tmpX = 0;
        tmpY = 0;
    }

    public void teleport(int aX, int aY, Direction aDirection)
    {
        direction = aDirection;
        teleport(aX, aY);
    }

    @Override
    public void walk(Direction aDirection)
    {
        if (dx == 0 && dy == 0 && tmpX == 0 && tmpY == 0) {
            direction = aDirection;
            updateImage();
            switch (aDirection) {
                case UP:
                    if (gamePanel.isWalkable(x, y - 1) && gamePanel.canWalkUp(x, y) && gamePanel.canWalkDown(x, y-1)) {
                        dx = 0;
                        dy = -speed;
                        tmpX = 0;
                        tmpY = InterfaceElement.tileSize;
                        y -= 1;
                    }
                    break;
                case DOWN:
                    if (gamePanel.isWalkable(x, y + 1) && gamePanel.canWalkDown(x, y) && gamePanel.canWalkUp(x, y+1)) {
                        dx = 0;
                        dy = speed;
                        tmpX = 0;
                        tmpY = -InterfaceElement.tileSize;
                        y += 1;
                    }
                    break;
                case LEFT:
                    if (gamePanel.isWalkable(x - 1, y) && gamePanel.canWalkLeft(x, y) && gamePanel.canWalkRight(x-1, y)) {
                        dx = -speed;
                        dy = 0;
                        tmpX = InterfaceElement.tileSize;
                        tmpY = 0;
                        x -= 1;
                    }
                    break;
                case RIGHT:
                    if (gamePanel.isWalkable(x + 1, y) && gamePanel.canWalkRight(x, y) && gamePanel.canWalkLeft(x+1, y)) {
                        dx = speed;
                        dy = 0;
                        tmpX = -InterfaceElement.tileSize;
                        tmpY = 0;
                        x += 1;
                    }
                    break;
            }
            gamePanel.getStateHandler().savePlayerState();
        }
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

    public int getStonedMeter() {
        return stonedMeter;
    }

    public int getMunchiesMeter() {
        return munchiesMeter;
    }

    public void setMunchiesMeter(int munchiesMeter) {
        this.munchiesMeter = munchiesMeter;
    }

    public void setStonedMeter(int stonedMeter) {
        this.stonedMeter = stonedMeter;
        this.nextStonedMeter = stonedMeter;
    }
}
