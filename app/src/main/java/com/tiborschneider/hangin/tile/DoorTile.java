package com.tiborschneider.hangin.tile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tiborschneider.hangin.R;

/**
 * Created by tibor on 09.07.16.
 */
public class DoorTile extends InteractiveTile {
    private int numAnimations = 6;
    private Bitmap[] animationImages;
    private int updateTime = 2;
    private int counter = 0;
    private int currentImage = 0;
    public boolean running = false;
    public boolean isClosed = true;

    public DoorTile(Context context, TileType tileType, TileForegroundType foregroundType) {
        super(context, tileType, foregroundType);
        isAnimation = true;
        Bitmap fullImage;

        if (foregroundType == TileForegroundType.HOUSE_OUTSIDE_DECO_DOOR_B) {
            animationImages = new Bitmap[numAnimations];
            walkable = true;
            animationImages[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_outside_deco_door_b1);
            animationImages[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_outside_deco_door_b2);
            animationImages[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_outside_deco_door_b3);
            animationImages[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_outside_deco_door_b4);
            animationImages[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_outside_deco_door_b5);
            animationImages[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_outside_deco_door_b6);
        } else if (foregroundType == TileForegroundType.HOUSE_OUTSIDE_DECO_DOOR_T) {
            animationImages = new Bitmap[numAnimations];
            walkable = false;
            animationImages[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_outside_deco_door_t1);
            animationImages[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_outside_deco_door_t2);
            animationImages[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_outside_deco_door_t3);
            animationImages[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_outside_deco_door_t4);
            animationImages[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_outside_deco_door_t5);
            animationImages[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_outside_deco_door_t6);
        }
    }

    @Override
    public boolean update() {
        if (running) {
            if (++counter == updateTime) {
                counter = 0;
                if (isClosed) {
                    if (++currentImage == numAnimations) {
                        currentImage--;
                        running = false;
                        isClosed = false;
                    }
                } else {
                    if (--currentImage < 0) {
                        currentImage = 0;
                        running = false;
                        isClosed = true;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public Bitmap getDoorImage() {
        if (animationImages == null) {
            return null;
        }
        return animationImages[currentImage];
    }

    @Override
    public void openDoor() {
        if (!running && isClosed) {
            running = true;
        }
    }

    @Override
    public void closeDoor() {
        if (!running && !isClosed) {
            running = true;
        }
    }

    public static boolean isDoorTile(TileForegroundType typeForeground) {
        if (typeForeground == TileForegroundType.HOUSE_OUTSIDE_DECO_DOOR_B) {
            return true;
        } else if (typeForeground == TileForegroundType.HOUSE_OUTSIDE_DECO_DOOR_T) {
            return true;
        } else {
            return false;
        }
    }
}
