package com.tiborschneider.hangin.userInteraction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.tiborschneider.hangin.mainGame.GamePanel;

/**
 * Created by Tibor Schneider on 22.06.2016.
 */
public abstract class InterfaceElement {
    public static int originalScreenWidth = 1080;
    public static int tileSize = 64;
    public static int numTiles = 16;
    public static int gameBorderSize = 28;
    public static int borderWidth = 20;
    public static int borderSmallWidth = 8;
    public static int dialogueWidth = 800;
    public static int dialogueHeight = 200;
    public static int dialogueReplyWidth = 500;
    public static int dialogueReply1Height = 110;
    public static int dialogueReply2Height = 180;
    public static int dialogueReply3Height = 250;
    public static int dialogueReply4Height = 320;
    public static int inventoryWidth = 800;
    public static int inventoryHeight = 800;
    public static int inventorySelectionWidth = 136;
    public static int inventorySelectionHeight = 136;
    public static int inventoryImageMargin = 20;
    public static int inventoryTextMargin = 5;
    public static int selectionWidth = 30;
    public static int selectionHeight = 30;
    public static int innerTextMargin = 20;
    public static int textOffset = -5;
    public static int normalTextSize = (dialogueHeight - 2*borderWidth - 3*innerTextMargin)/2;
    public static int bigTextSize = 68;
    public static int itemSize = 80;
    public static int lootboxItemMargin = 20;
    public static int lootboxWidth = 800;
    public static int lootboxHeight1 = 250;
    public static int lootboxHeight2 = 358;
    public static int lootboxHeight3 = 466;
    public static int lootboxHeight4 = 574;
    public static int lootboxHeight5 = 682;
    public static int lootboxImageMargin = 10;
    public static int lootboxTextMargin = 25;
    public static int statusBarWidth = 1000;
    public static int statusBarHeight = 60;
    public static int statusBarInnerMargin = 10;
    public static int statusBarOuterMargin = 5;
    public static int statusBarInnerTextMargin = 5;
    public static int statusBarBorder = 10;
    public static int statusBarMeterMaximumWidth = 284;
    public static int statusBarMeterHeight = 24;
    public static int statusBarTextLength = 70;

    protected int x;
    protected int y;
    protected Context context;
    protected Bitmap backgroundImage;

    public void draw(Canvas canvas) {
        //must be overwritten
    }

    public static void initSizes()
    {
        //initialize tile display parameters
        float corrFactor = ((float) GamePanel.screenWidth)/((float) originalScreenWidth);
        System.out.println("scale all Distances with corr Factor: " + corrFactor);
        borderWidth *= corrFactor;
        borderWidth = (int)(0.5+corrFactor*borderWidth);
        borderSmallWidth = (int)(0.5+corrFactor*borderSmallWidth);
        dialogueWidth = (int)(0.5+corrFactor*dialogueWidth);
        dialogueHeight = (int)(0.5+corrFactor*dialogueHeight);
        dialogueReplyWidth = (int)(0.5+corrFactor*dialogueReplyWidth);
        dialogueReply1Height = (int)(0.5+corrFactor*dialogueReply1Height);
        dialogueReply2Height = (int)(0.5+corrFactor*dialogueReply2Height);
        dialogueReply3Height = (int)(0.5+corrFactor*dialogueReply3Height);
        dialogueReply4Height  = (int)(0.5+corrFactor*dialogueReply4Height);
        inventoryWidth  = (int)(0.5+corrFactor*inventoryWidth);
        inventoryHeight  = (int)(0.5+corrFactor*inventoryHeight);
        inventoryImageMargin  = (int)(0.5+corrFactor*inventoryImageMargin);
        inventoryTextMargin  = (int)(0.5+corrFactor*inventoryTextMargin);
        selectionWidth  = (int)(0.5+corrFactor*selectionWidth);
        selectionHeight  = (int)(0.5+corrFactor*selectionHeight);
        innerTextMargin  = (int)(0.5+corrFactor*innerTextMargin);
        textOffset  = (int)(0.5+corrFactor*textOffset);
        normalTextSize  = (int)(0.5+corrFactor*normalTextSize);
        bigTextSize  = (int)(0.5+corrFactor*bigTextSize);
        itemSize  = (int)(0.5+corrFactor*itemSize);
        lootboxItemMargin  = (int)(0.5+corrFactor*lootboxItemMargin);
        lootboxWidth  = (int)(0.5+corrFactor*lootboxWidth);
        lootboxHeight1  = (int)(0.5+corrFactor*lootboxHeight1);
        lootboxHeight2  = (int)(0.5+corrFactor*lootboxHeight2);
        lootboxHeight3  = (int)(0.5+corrFactor*lootboxHeight3);
        lootboxHeight4  = (int)(0.5+corrFactor*lootboxHeight4);
        lootboxHeight5  = (int)(0.5+corrFactor*lootboxHeight5);
        lootboxImageMargin  = (int)(0.5+corrFactor*lootboxImageMargin);
        lootboxTextMargin  = (int)(0.5+corrFactor*lootboxTextMargin);
        tileSize  = (int)(0.5+corrFactor*tileSize);
        gameBorderSize  = (int)(0.5+corrFactor*gameBorderSize);
        inventorySelectionHeight  = (int)(0.5+corrFactor*inventorySelectionHeight);
        inventorySelectionWidth  = (int)(0.5+corrFactor*inventorySelectionWidth);
        statusBarWidth = (int)(0.5+corrFactor*statusBarWidth);
        statusBarHeight = (int)(0.5+corrFactor*statusBarHeight);
        statusBarInnerMargin = (int)(0.5+corrFactor*statusBarInnerMargin);
        statusBarOuterMargin = (int)(0.5+corrFactor* statusBarOuterMargin);
        statusBarInnerTextMargin = (int)(0.5+corrFactor*statusBarInnerTextMargin);
        statusBarBorder = (int)(0.5+corrFactor*statusBarBorder);
        statusBarMeterMaximumWidth = (int)(0.5+corrFactor*statusBarMeterMaximumWidth);
        statusBarMeterHeight = (int)(0.5+corrFactor*statusBarMeterHeight);
        statusBarTextLength = (int)(0.5+corrFactor*statusBarTextLength);

        InterfaceElement.gameBorderSize = (GamePanel.screenWidth - InterfaceElement.tileSize * InterfaceElement.numTiles)/2;

        Controller.initSizes(corrFactor);
    }

    public static Bitmap resizeImage(Bitmap image, int newWidth, int newHeight)
    {
        //resize image
        float scaleWidth = ((float) newWidth) / image.getWidth();
        float scaleHeight = ((float) newHeight) / image.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
    }
}
