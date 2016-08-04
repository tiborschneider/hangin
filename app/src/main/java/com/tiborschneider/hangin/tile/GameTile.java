package com.tiborschneider.hangin.tile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.tiborschneider.hangin.dialogue.Dialogue;
import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.userInteraction.InterfaceElement;

import java.util.Random;



/**
 * Created by Tibor Schneider on 19.06.2016.
 * Abstract GameTile class to initialize the correct image
 */
public abstract class GameTile {
    protected boolean walkable = true;
    protected boolean canGoUp = true;
    protected boolean canGoDown = true;
    protected boolean canGoLeft = true;
    protected boolean canGoRight = true;
    protected boolean isInteractive = false;
    protected boolean isAnimation = false;
    protected Bitmap image;
    protected Bitmap accentImage = null;
    protected Bitmap foregroundImage = null;
    protected int accentOffsetX = 0;
    protected int accentOffsetY = 0;
    protected TileType type;
    protected TileForegroundType foregroundType;
    protected ImageHandler imageHandler;

    public GameTile(Context context, TileType setType, TileForegroundType setForegroundType)
    {
        imageHandler = GamePanel.getGamePanel().getImageHandler();

        type = setType;
        foregroundType = setForegroundType;

        //to overlay an additional image
        Bitmap accent;

        //set Image for all and let dem override if necessary
        String imageName = type.name().toLowerCase();
        if (!imageName.equals("grass") && !imageName.matches("sign(.*)") && !imageName.equals("stone")) {
            image = imageHandler.getImage(imageName);
        }

        switch (type)
        {
            case GRASS:
                setRandomGrassImage(context);
                //randomize accent
                if (randInt(1,4) == 1)
                {
                    String accentName = "grass_accent" + randInt(1, 6);
                    accentImage = imageHandler.getImage(accentName);
                    accentOffsetX = randInt(-10,10+(InterfaceElement.tileSize/2));
                    accentOffsetY = randInt(-10,10+(InterfaceElement.tileSize/2));
                }
                break;
            case STONE:
                setRandomStoneImage(context);
            case GRASS_RISE_T:
                canGoUp = false;
                break;
            case GRASS_RISE_B:
                canGoDown = false;
                break;
            case GRASS_RISE_L:
                canGoLeft = false;
                break;
            case GRASS_RISE_R:
                canGoRight = false;
                break;
            case GRASS_RISE_TL:
                canGoUp = false;
                canGoLeft = false;
                break;
            case GRASS_RISE_TR:
                canGoUp = false;
                canGoRight = false;
                break;
            case GRASS_RISE_BL:
                canGoLeft = false;
                canGoDown = false;
                break;
            case GRASS_RISE_BR:
                canGoRight = false;
                canGoDown = false;
                break;
            case CAMPFIRE_GRASS:
                walkable = false;
                isInteractive = true;
                isAnimation = true;
                break;
            case GRASS_WATER_T:
                canGoUp = false;
                break;
            case GRASS_WATER_B:
                canGoDown = false;
                break;
            case GRASS_WATER_L:
                canGoLeft = false;
                break;
            case GRASS_WATER_R:
                canGoRight = false;
                break;
            case GRASS_WATER_TL:
                canGoUp = false;
                canGoLeft = false;
                break;
            case GRASS_WATER_TR:
                canGoUp = false;
                canGoRight = false;
                break;
            case GRASS_WATER_BL:
                canGoDown = false;
                canGoLeft = false;
                break;
            case GRASS_WATER_BR:
                canGoDown = false;
                canGoRight = false;
                break;
            case HOUSE_BLANK:
                walkable = false;
                break;
            default:
        }

        //mark not walkable if tile starts with water:
        if (type.name().matches("WATER(.*)")) {
            walkable = false;
        }

        //mark all House Walls with non walkable
        if (type.name().matches("HOUSE_WALL(.*)")) {
            walkable = false;
        }

        //mark all Furniture with non walkable
        if (foregroundType.name().matches("FURNITURE(.*)")) {
            walkable = false;
        }

        //mark all House Walls with non walkable
        if (type.name().matches("HOUSE_OUTSIDE_WALL(.*)")) {
            walkable = false;
        }

        //mark all House Tops with non walkable
        if (type.name().matches("HOUSE_OUTSIDE_TOP(.*)")) {
            walkable = false;
        }

        //mark all trees with non walkable
        if (foregroundType.name().matches("TREE(.*)")) {
            walkable = false;
        }

        //mark all trees with non walkable
        if (foregroundType.name().matches("POT(.*)")) {
            walkable = false;
        }

        //mark all stones with walkable
        if (type.name().matches("STONE(.*)")) {
            walkable = true;
            canGoUp = true;
            canGoDown = true;
            canGoRight = true;
            canGoLeft = true;
        }

        //add ForegroundTile
        if (foregroundType != TileForegroundType.NULL && !foregroundType.name().matches("HOUSE_OUTSIDE_DECO_DOOR(.*)")) {
            imageName = foregroundType.name().toLowerCase();
            foregroundImage = imageHandler.getImage(imageName);

            //change Flags
            switch (foregroundType) {
                case FENCE_HORIZONTAL_FRONT_LEFT:
                    walkable = false;
                    break;
                case FENCE_HORIZONTAL_FRONT_CENTER:
                    walkable = false;
                    break;
                case FENCE_HORIZONTAL_FRONT_RIGHT:
                    walkable = false;
                    break;
                case FENCE_HORIZONTAL_BACK_LEFT:
                    canGoUp = false;
                    break;
                case FENCE_HORIZONTAL_BACK_CENTER:
                    canGoUp = false;
                    break;
                case FENCE_HORIZONTAL_BACK_RIGHT:
                    canGoUp = false;
                    break;
                case FENCE_VERTICAL_TOP:
                    walkable = false;
                    break;
                case FENCE_VERTICAL_CENTER:
                    walkable = false;
                    break;
                case FENCE_VERTICAL_BOTTOM:
                    walkable = false;
                    break;
                case FENCE_CORNER_TOP_LEFT:
                    walkable = false;
                    break;
                case FENCE_CORNER_TOP_RIGHT:
                    walkable = false;
                    break;
                case FENCE_CORNER_BOTTOM_LEFT:
                    walkable = false;
                    break;
                case FENCE_CORNER_BOTTOM_RIGHT:
                    break;
                case SIGN_INFO:
                    isInteractive = true;
                    break;
                case BARREL_1:
                    walkable = false;
                    break;
                case BARREL_2:
                    walkable = false;
                    break;
                case HOUSE_OUTSIDE_DECO_DOOR_B:
                    walkable = true;
                    break;
                case BRIDGE_HORIZONTAL_C:
                case BRIDGE_HORIZONTAL_LC:
                case BRIDGE_HORIZONTAL_RC:
                    walkable = true;
                    canGoLeft = true;
                    canGoRight = true;
                    break;
                case BRIDGE_VERTICAL_C:
                case BRIDGE_VERTICAL_TC:
                case BRIDGE_VERTICAL_BC:
                    walkable = true;
                    canGoUp = true;
                    canGoDown = true;

            }
        }

        //resize Image
        //float scale = ((float) InterfaceElement.tileSize) / image.getWidth();
        //Matrix matrix = new Matrix();
        //matrix.postScale(scale,scale);
        //image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
    }

    public Bitmap getBitmap()
    {
        return image;
    }

    public boolean isWalkable()
    {
        return walkable;
    }

    public boolean canWalkUp() { return canGoUp;}

    public boolean canWalkDown() { return canGoDown;}

    public boolean canWalkLeft() { return canGoLeft;}

    public boolean canWalkRight() { return canGoRight;}

    private int randInt(int min, int max)
    {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public boolean setDialogue(String aDialogue)
    {
        return false;
    }

    public Dialogue getDialogue()
    {
        return null;
    }

    public boolean isInteractive()
    {
        return isInteractive;
    }

    private void setRandomGrassImage(Context context)
    {
        String imageName = "grass" + randInt(1, 10);
        image = imageHandler.getImage(imageName);
    }

    private void setRandomStoneImage(Context context)
    {
        String imageName = "stone" + randInt(1, 6);
        image = imageHandler.getImage(imageName);
    }

    public boolean update() {
        return false;
    }

    public Bitmap getAnimationImage() {
        return null;
    }

    public Bitmap getDoorImage() {
        return null;
    }

    public void drawAnimation(Canvas canvas, Paint stonedPaint) {
        return;
    }

    public boolean isAnimationTile() {
        return isAnimation;
    }

    public int getOffsetX() {
        return 0;
    }

    public int getOffsetY() {
        return 0;
    }

    public void openDoor() {}

    public void closeDoor() {}

    public TileForegroundType getForegroundType() {
        return foregroundType;
    }

    public TileType getType() {
        return type;
    }

    public boolean isHouseOutside() {
        return type.name().matches("HOUSE_OUTSIDE(.*)");
    }

    public void draw(Canvas canvas, int x, int y, Paint stonedPaint) {
        canvas.drawBitmap(image, x, y, stonedPaint);
        if (accentImage != null)
            canvas.drawBitmap(accentImage, x + accentOffsetX, y + accentOffsetY, stonedPaint);
        if (foregroundImage != null) {
            canvas.drawBitmap(foregroundImage, x, y, stonedPaint);
        }
    }
}
