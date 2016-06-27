package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

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
        double corrFactor = GamePanel.screenWidth/ originalScreenWidth;
        borderWidth *= corrFactor;
        borderSmallWidth *= corrFactor;
        dialogueWidth *= corrFactor;
        dialogueHeight *= corrFactor;
        dialogueReplyWidth *= corrFactor;
        dialogueReply1Height *= corrFactor;
        dialogueReply2Height *= corrFactor;
        dialogueReply3Height *= corrFactor;
        dialogueReply4Height *= corrFactor;
        inventoryWidth *= corrFactor;
        inventoryHeight *= corrFactor;
        inventoryImageMargin *= corrFactor;
        inventoryTextMargin *= corrFactor;
        selectionWidth *= corrFactor;
        selectionHeight *= corrFactor;
        innerTextMargin *= corrFactor;
        textOffset *= corrFactor;
        normalTextSize *= corrFactor;
        bigTextSize *= corrFactor;
        itemSize *= corrFactor;
        lootboxItemMargin *= corrFactor;
        lootboxWidth *= corrFactor;
        lootboxHeight1 *= corrFactor;
        lootboxHeight2 *= corrFactor;
        lootboxHeight3 *= corrFactor;
        lootboxHeight4 *= corrFactor;
        lootboxHeight5 *= corrFactor;
        lootboxImageMargin *= corrFactor;
        lootboxTextMargin *= corrFactor;
        tileSize *= corrFactor;
        numTiles *= corrFactor;
        gameBorderSize *= corrFactor;
        InterfaceElement.gameBorderSize = (GamePanel.screenWidth - InterfaceElement.tileSize * InterfaceElement.numTiles)/2;
    }
}
