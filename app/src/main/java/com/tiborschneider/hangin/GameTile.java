package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import java.util.Random;



/**
 * Created by Tibor Schneider on 19.06.2016.
 */
public abstract class GameTile {
    protected boolean walkable = true;
    protected boolean canGoUp = true;
    protected boolean canGoDown = true;
    protected boolean canGoLeft = true;
    protected boolean canGoRight = true;
    protected boolean isInteractive = false;
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
            //System.out.println(imageName);
            int imageId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            image = BitmapFactory.decodeResource(context.getResources(), imageId);
        }

        switch (type)
        {
            case GRASS:
                setRandomGrassImage(context);
                //randomize accent
                if (randInt(1,6) == 1)
                {
                    String accentName = "grass_accent" + randInt(1, 6);
                    int accentId = context.getResources().getIdentifier(accentName, "drawable", context.getPackageName());
                    accent = BitmapFactory.decodeResource(context.getResources(), accentId);
                    image = overlay(image, accent);
                }
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

            default:
        }
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
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
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public boolean setDialogue(Dialogue aDialogue)
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
}
