package com.tiborschneider.hangin.character;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.tiborschneider.hangin.scene.GameJumpHandler;
import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.userInteraction.InterfaceElement;
import com.tiborschneider.hangin.item.Item;
import com.tiborschneider.hangin.item.ItemType;

/**
 * Created by Tibor Schneider on 19.06.2016.
 */
public class Player extends GameObject {
    private static int meterMaximum = 100;
    private Inventory inventory;
    private int munchiesMeter = 0;
    private int stonedMeter = 0;
    private int nextMunchiesMeter = 0;
    private int nextStonedMeter = 0;
    private int stonedMeterCounter = 0;
    private int munchiesMeterCounter = 0;
    private SmokeAnimation smokeAnimation = null;
    private static int maxTimeToDecayStoned = 160;
    private static int minTimeToDecayStoned = 80;
    private static int maxTimeToDecayMunchies = 320;
    private static int minTimeToDecayMunchies = 80;
    //private Bitmap[][] imageArray = new Bitmap[4][9];


    public Player(GamePanel aGamePanel, Context aContext, int aX, int aY)
    {
        super(aGamePanel, aContext, Direction.RIGHT, aX, aY, 8);
        context = aContext;
        inventory = new Inventory(context, gamePanel);

        //get all Images
        imageArray = GameObject.cutCharacterAnimation("player", 9);

        smokeAnimation = new SmokeAnimation(context, 0, 0);

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
        updateStepCounter();

        if (nextStonedMeter > stonedMeter) {
            //gamePanel.redrawScene();
            stonedMeter++;
        } else if (nextStonedMeter < stonedMeter) {
            //gamePanel.redrawScene();
            stonedMeter--;
        }

        //update Smoke Animaiton
        if (!smokeAnimation.isFinished()) {
                smokeAnimation.update();
        }
    }

    @Override
    public void draw(Canvas canvas, Paint stonedPaint)
    {
        canvas.drawBitmap(image, x* InterfaceElement.tileSize + tmpX + InterfaceElement.gameBorderSize, y* InterfaceElement.tileSize + tmpY + 2*InterfaceElement.statusBarOuterMargin + InterfaceElement.statusBarHeight + playerDisplayOffset, stonedPaint);
        smokeAnimation.draw(canvas);
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
        if (aItem.getItemType() == ItemType.JOINT ||
                aItem.getItemType() == ItemType.UGLY_JOINT ||
                aItem.getItemType() == ItemType.BIG_JOINT) {
            startSmokeAnimation();
        }
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

    @Override
    protected boolean continueWalking() {
        if ( GamePanel.getGamePanel().getInteractionHandler() != null) {
            if (GamePanel.getGamePanel().getInteractionHandler().getController() != null) {
                return GamePanel.getGamePanel().getInteractionHandler().getController().isDirectionButtonPressed();
            }
        }
        return false;
    }

    public void startSmokeAnimation() {
        if (smokeAnimation.isFinished())
            smokeAnimation.restart(x, y);
    }
}
