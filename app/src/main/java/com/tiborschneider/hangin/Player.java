package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Tibor Schneider on 19.06.2016.
 */
public class Player extends GameObject {
    private int motionCounter = 0;
    private int prevMotionCounter = 0;
    private int animationSpeed = 20;
    private static int meterMaximum = 100;
    private Inventory inventory;
    private int munchiesMeter = 100;
    private int stonedMeter = 100;

    public Player(GamePanel aGamePanel, Context aContext, int aX, int aY)
    {
        super(aGamePanel, aContext, Direction.DOWN, aX, aY, 4);
        context = aContext;
        updateImage();
        inventory = new Inventory(context, gamePanel);
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
    }


    @Override public int getImageId()
    {
        String imageName;
        int imageNr;
        if (motionCounter < animationSpeed / 2 )
            imageNr = 1;
        else
            imageNr = 2;
        switch (direction)
        {
            case UP:
                imageName = "player_up" + imageNr;
                break;
            case DOWN:
                imageName = "player_down" + imageNr;
                break;
            case LEFT:
                imageName = "player_left" + imageNr;
                break;
            case RIGHT:
                imageName = "player_right" + imageNr;
                break;
            default:
                imageName = "player_down" + imageNr;
        }
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
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
        if (stonedMeter + change < meterMaximum) {
            if (stonedMeter + change > 0) {
                stonedMeter += change;
            } else {
                stonedMeter = 0;
                return false;
            }
        } else {
            stonedMeter = meterMaximum;
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
            image = BitmapFactory.decodeResource(context.getResources(), getImageId());
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
}
