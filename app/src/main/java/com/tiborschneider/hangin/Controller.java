package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import java.lang.Math;

/**
 * Created by Tibor Schneider on 20.06.2016.
 */
public class Controller {
    private Bitmap imageButtonUp;
    private Bitmap imageButtonDown;
    private Bitmap imageButtonLeft;
    private Bitmap imageButtonRight;
    private Bitmap imageButtonInteract;
    private Bitmap imageButtonDrop;
    private Bitmap imageButtonInventory;
    private Context context;
    private Player player;
    private InteractionHandler interactionHandler;
    private boolean waitForInteractionRelease = false;
    private static int borderSize = 48;
    private static int baseSpace1 = 100;
    private static int baseSpace2 = 170;
    private static int spread = 15;
    private static int buttonArrowLength = 150;
    private static int buttonArrowWidth  = 122;
    private static int arrowRadius = 250;
    private static int actionRadius = 110;
    private static int actionButtonWidth = 160;
    private static int actionButtonSpace = 160;
    private static int buttonInventoryWidth = 96;
    private static int inventoryRadius = buttonInventoryWidth;
    private static int equippedItemMargin = 20;

    public Controller(Context aContext, Player aPlayer, InteractionHandler aInteractionHandler)
    {
        context = aContext;
        player  = aPlayer;
        interactionHandler  = aInteractionHandler;
        imageButtonUp        = BitmapFactory.decodeResource(context.getResources(), R.drawable.button_up);
        imageButtonDown      = BitmapFactory.decodeResource(context.getResources(), R.drawable.button_down);
        imageButtonLeft      = BitmapFactory.decodeResource(context.getResources(), R.drawable.button_left);
        imageButtonRight     = BitmapFactory.decodeResource(context.getResources(), R.drawable.button_right);
        imageButtonInteract  = BitmapFactory.decodeResource(context.getResources(), R.drawable.button_interact);
        imageButtonDrop      = BitmapFactory.decodeResource(context.getResources(), R.drawable.button_drop);
        imageButtonInventory = BitmapFactory.decodeResource(context.getResources(), R.drawable.button_inventory);
    }

    public void draw(Canvas canvas)
    {
        //arrow buttons
        int startX = InterfaceElement.gameBorderSize + borderSize;
        int startY = InterfaceElement.numTiles * InterfaceElement.tileSize + 2 * InterfaceElement.gameBorderSize + borderSize;
        canvas.drawBitmap(imageButtonUp, startX + baseSpace1 + spread, startY, null);
        canvas.drawBitmap(imageButtonLeft, startX, startY + baseSpace1 + spread, null);
        canvas.drawBitmap(imageButtonDown, startX + baseSpace1 + spread, startY + baseSpace2 + 2*spread, null);
        canvas.drawBitmap(imageButtonRight, startX + baseSpace2 + 2*spread, startY + baseSpace1 + spread, null);

        //action buttons
        startX = GamePanel.screenWidth - InterfaceElement.gameBorderSize - borderSize - actionButtonWidth;
        canvas.drawBitmap(imageButtonInteract, startX, startY, null);
        canvas.drawBitmap(imageButtonDrop, startX-actionButtonSpace, startY+actionButtonSpace, null);

        //inventory buttons
        startX = (GamePanel.screenWidth - buttonInventoryWidth)/2;
        startY += baseSpace2 + 2*spread + buttonArrowLength;
        canvas.drawBitmap(imageButtonInventory, startX, startY, null);

        //Equipped Item
        if (player.hasEquippedItem()) {
            startX += buttonInventoryWidth + equippedItemMargin;
            startY += (buttonInventoryWidth - InterfaceElement.itemSize)/2;
            canvas.drawBitmap(player.getEquippedItemImage(), startX, startY, null);
        }
    }

    public boolean onTouch(MotionEvent e)
    {
        if (waitForInteractionRelease) {
            if (e.getAction() == MotionEvent.ACTION_UP)
                waitForInteractionRelease = false;
        } else {
            Button pressedButton = detectButton(e.getX(), e.getY());
            if (InteractionHandler.interfaceActive && e.getAction() == MotionEvent.ACTION_UP) {
                waitForInteractionRelease = false;
                interactionHandler.onButtonPress(pressedButton);
            } else if (!InteractionHandler.interfaceActive) {
                if (pressedButton == Button.INTERACT)
                    waitForInteractionRelease = true;
                interactionHandler.onButtonPress(pressedButton);
            }
        }

        return true;
    }

    public void setWaitForInteractionRelease(boolean b)
    {
        waitForInteractionRelease = b;
    }

    private Button detectButton(float aX, float aY)
    {
        //check, if pressed arrow area

        int centerX = InterfaceElement.gameBorderSize + borderSize + buttonArrowLength;
        int centerY = InterfaceElement.numTiles * InterfaceElement.tileSize + 2 * InterfaceElement.gameBorderSize + borderSize + buttonArrowLength;

        float xPrim = aX - centerX;
        float yPrim = aY - centerY;

        if (Math.abs(xPrim*xPrim + yPrim*yPrim) < arrowRadius*arrowRadius)
        {
            //touch event was in arrow area
            if (yPrim > xPrim && yPrim < -xPrim)
                return Button.LEFT;
            if (yPrim > xPrim && yPrim > -xPrim)
                return Button.DOWN;
            if (yPrim < xPrim && yPrim > -xPrim)
                return Button.RIGHT;
            if (yPrim < xPrim && yPrim < -xPrim)
                return Button.UP;
        }

        //check interact button
        centerX = GamePanel.screenWidth - InterfaceElement.gameBorderSize - borderSize - actionButtonWidth/2;
        centerY = InterfaceElement.numTiles * InterfaceElement.tileSize + 2 * InterfaceElement.gameBorderSize + borderSize + actionButtonWidth/2;
        xPrim = aX - centerX;
        yPrim = aY - centerY;

        if (Math.abs(xPrim*xPrim + yPrim*yPrim) < actionRadius*actionRadius)
            return Button.INTERACT;

        //check drop Button
        centerX -= actionButtonSpace;
        centerY += actionButtonSpace;
        xPrim = aX - centerX;
        yPrim = aY - centerY;

        if (Math.abs(xPrim*xPrim + yPrim*yPrim) < actionRadius*actionRadius)
            return Button.DROP;

        //check InventoryButton
        centerX = GamePanel.screenWidth / 2;
        centerY = InterfaceElement.numTiles * InterfaceElement.tileSize + 2 * InterfaceElement.gameBorderSize + borderSize + baseSpace2 + 2*spread + buttonArrowLength + buttonInventoryWidth/2;
        xPrim = aX - centerX;
        yPrim = aY - centerY;

        if (Math.abs(xPrim*xPrim + yPrim*yPrim) < inventoryRadius * inventoryRadius)
            return Button.INVENTORY;

        return Button.NDEF;
    }
}
