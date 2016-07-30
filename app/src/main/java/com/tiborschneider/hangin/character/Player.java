package com.tiborschneider.hangin.character;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.tiborschneider.hangin.scene.GameJumpHandler;
import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.userInteraction.InterfaceElement;
import com.tiborschneider.hangin.item.Item;
import com.tiborschneider.hangin.item.ItemType;
import com.tiborschneider.hangin.R;

/**
 * Created by Tibor Schneider on 19.06.2016.
 */
public class Player extends GameObject {
    private int motionCounter = 0;
    private int prevMotionCounter = 0;
    private int animationSpeed = 16;
    private static int meterMaximum = 100;
    private Inventory inventory;
    private int munchiesMeter = 0;
    private int stonedMeter = 0;
    private int nextMunchiesMeter = 0;
    private int nextStonedMeter = 0;
    private int stonedMeterCounter = 0;
    private int munchiesMeterCounter = 0;
    private static int maxTimeToDecayStoned = 160;
    private static int minTimeToDecayStoned = 80;
    private static int maxTimeToDecayMunchies = 320;
    private static int minTimeToDecayMunchies = 80;
    private Bitmap[][] imageArray = new Bitmap[4][9];

    public Player(GamePanel aGamePanel, Context aContext, int aX, int aY)
    {
        super(aGamePanel, aContext, Direction.RIGHT, aX, aY, 8);
        context = aContext;
        inventory = new Inventory(context, gamePanel);

        //get all Images
        imageArray = GameObject.cutCharacterAnimation("player", 9);

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

    @Override public void update()
    {
        updateMovement();
        if (!GameJumpHandler.jumpsAllowed && (tmpX != 0 || tmpY != 0))
            GameJumpHandler.jumpsAllowed = true;

        //update Stoned meter
        stonedMeterCounter++;
        int currentDecayTime = maxTimeToDecayStoned - stonedMeter*(maxTimeToDecayStoned - minTimeToDecayStoned)/100;
        if (stonedMeterCounter >= currentDecayTime) {
            stonedMeterCounter = 0;
            updateStonedMeter(-1);
        }

        //update Munchies meter
        munchiesMeterCounter++;
        currentDecayTime = maxTimeToDecayMunchies - munchiesMeter*(maxTimeToDecayMunchies - minTimeToDecayMunchies)/100;
        if (munchiesMeterCounter >= currentDecayTime) {
            munchiesMeterCounter = 0;
            updateMunchiesMeter(1);
        }

        //update StepCounter
        prevMotionCounter = motionCounter;
        if (dx != 0 || dy != 0) {
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
        if (nextStonedMeter > stonedMeter) {
            //gamePanel.redrawScene();
            stonedMeter++;
        } else if (nextStonedMeter < stonedMeter) {
            //gamePanel.redrawScene();
            stonedMeter--;
        }
    }

    public boolean hasItem(Item aItem)
    {
        return inventory.hasItem(aItem);
    }

    public boolean hasItem(ItemType type) {
        return inventory.hasItem(type);
    }

    public boolean useItem(Item aItem)
    {
        if (!hasItem(aItem)) return false;
            inventory.useItem(aItem);
        return true;
    }

    public boolean addItem(Item aItem)
    {
        return inventory.addItem(aItem, true);
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
        int imageNr = 0;
        if (tmpX != 0 || tmpY != 0) {
            imageNr = (motionCounter/2) + 1;
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
