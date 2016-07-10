package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

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
    protected TileType type;

    public GameTile(Context context, TileType setType)
    {
        type = setType;

        //to overlay an additional image
        Bitmap accent;

        //set Image for all and let dem override if necessary
        String imageName = type.name().toLowerCase();
        if (!imageName.equals("grass") && !imageName.matches("sign(.*)")) {
            int imageId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            image = BitmapFactory.decodeResource(context.getResources(), imageId);
        }

        switch (type)
        {
            case GRASS:
                setRandomGrassImage(context);
                //randomize accent
                image = drawAccent(image);

                break;
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

            //backup

            case FENCE_HORIZONTAL_FRONT_LEFT:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_horizontal_front_left);
                image = overlay(image, accent);
                walkable = false;
                break;
            case FENCE_HORIZONTAL_FRONT_CENTER:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_horizontal_front_center);
                image = overlay(image, accent);
                walkable = false;
                break;
            case FENCE_HORIZONTAL_FRONT_RIGHT:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_horizontal_front_right);
                image = overlay(image, accent);
                walkable = false;
                break;
            case FENCE_HORIZONTAL_BACK_LEFT:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_horizontal_back_left);
                image = overlay(image, accent);
                canGoUp = false;
                break;
            case FENCE_HORIZONTAL_BACK_CENTER:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_horizontal_back_center);
                image = overlay(image, accent);
                canGoUp = false;
                break;
            case FENCE_HORIZONTAL_BACK_RIGHT:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_horizontal_back_right);
                image = overlay(image, accent);
                canGoUp = false;
                break;
            case FENCE_VERTICAL_TOP:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_vertical_top);
                image = overlay(image, accent);
                walkable = false;
                break;
            case FENCE_VERTICAL_CENTER:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_vertical_center);
                image = overlay(image, accent);
                walkable = false;
                break;
            case FENCE_VERTICAL_BOTTOM:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_vertical_bottom);
                image = overlay(image, accent);
                walkable = false;
                break;
            case FENCE_CORNER_TOP_LEFT:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_corner_top_left);
                image = overlay(image, accent);
                walkable = false;
                break;
            case FENCE_CORNER_TOP_RIGHT:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_corner_top_right);
                image = overlay(image, accent);
                walkable = false;
                break;
            case FENCE_CORNER_BOTTOM_LEFT:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_corner_bottom_left);
                image = overlay(image, accent);
                walkable = false;
                break;
            case FENCE_CORNER_BOTTOM_RIGHT:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.fence_corner_bottom_right);
                image = overlay(image, accent);
                walkable = false;
                break;
            case TREE_1_TL:
                walkable = false;
                break;
            case TREE_1_TR:
                walkable = false;
                break;
            case TREE_1_BR:
                walkable = false;
                break;
            case TREE_1_BL:
                walkable = false;
                break;
            case TREE_2_TL:
                walkable = false;
                break;
            case TREE_2_TR:
                walkable = false;
                break;
            case TREE_2_BR:
                walkable = false;
                break;
            case TREE_2_BL:
                walkable = false;
                break;
            case TREE_3_TL:
                walkable = false;
                break;
            case TREE_3_TR:
                walkable = false;
                break;
            case TREE_3_BR:
                walkable = false;
                break;
            case TREE_3_BL:
                walkable = false;
                break;
            case TREE_4_TL:
                walkable = false;
                break;
            case TREE_4_TR:
                walkable = false;
                break;
            case TREE_4_BR:
                walkable = false;
                break;
            case TREE_4_BL:
                walkable = false;
                break;
            case SIGN_INFO_GRASS:
                setRandomGrassImage(context);
                accent = BitmapFactory.decodeResource(context.getResources(), R.drawable.sign_info);
                image = overlay(image, accent);
                isInteractive = true;
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
            default:
        }

        //mark not walkable if tile starts with water:
        if (type.name().matches("WATER(.*)")) {
            walkable = false;
        }

        //resize Image
        float scale = ((float) InterfaceElement.tileSize) / image.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale);
        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }

    private Bitmap drawAccent(Bitmap image) {
        Bitmap bmOverlay = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(image, 0, 0, null);
        if (randInt(1,4
        ) == 1)
        {
            String accentName = "grass_accent" + randInt(1, 6);
            int accentId = GamePanel.getGamePanel().getContext().getResources().getIdentifier(accentName, "drawable", GamePanel.getGamePanel().getContext().getPackageName());
            Bitmap accent = BitmapFactory.decodeResource(GamePanel.getGamePanel().getContext().getResources(), accentId);
            accent = InterfaceElement.resizeImage(accent, accent.getWidth()/2, accent.getHeight()/2);
            canvas.drawBitmap(accent, randInt(-10,10+(InterfaceElement.tileSize/2)), randInt(-10,10+(InterfaceElement.tileSize/2)), null);
        }
        return bmOverlay;

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
        int imageId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        image = BitmapFactory.decodeResource(context.getResources(), imageId);
    }

    public boolean update() {
        return false;
    }

    public Bitmap getAnimationImage() {
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
}
