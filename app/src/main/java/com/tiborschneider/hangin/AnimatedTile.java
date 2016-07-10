package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

/**
 * Created by tibor on 09.07.16.
 */
public class AnimatedTile extends InteractiveTile {
    private int numAnimations = 0;
    private Bitmap[] animationImages;
    private int updateTime = 3;
    private int counter = 0;
    private int currentImage = 0;
    private int offsetX = 0;
    private int offsetY = 0;

    public AnimatedTile(Context context, TileType tileType) {
        super(context, tileType);
        isAnimation = true;
        Bitmap fullImage;

        if (tileType == TileType.CAMPFIRE_GRASS) {
            numAnimations = 5;
            animationImages = new Bitmap[numAnimations];
            updateTime = 3;
            fullImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.campfire_animation);
        } else if (tileType.name().matches("WATER(.*)")) {
            numAnimations = 8;
            animationImages = new Bitmap[numAnimations];
            updateTime = 4;
            currentImage = randInt(0,7);
            offsetX = randInt(-5,5);
            offsetY = randInt(-5,5);
            fullImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.water_animation);
        } else {
            return;
        }

        for (int i = 0; i < numAnimations; i++) {
            animationImages[i] = Bitmap.createBitmap(fullImage, i * InterfaceElement.tileSize, 0, InterfaceElement.tileSize, InterfaceElement.tileSize);
        }
    }

    @Override
    public boolean update() {
        if (++counter == updateTime) {
            counter = 0;
            if (type.name().matches("WATER(.*)")) {
                offsetX += randInt(-2, 2) / 2;
                offsetY += randInt(-2, 2) / 2;
                if (offsetX >= 16) {
                    offsetX = 15;
                }
                if (offsetY >= 16) {
                    offsetY = 15;
                }
                if (offsetX <= -16) {
                    offsetX = -15;
                }
                if (offsetY <= -16) {
                    offsetY = -15;
                }
            }
            if (++currentImage == numAnimations) {
                currentImage = 0;
            }
        }
        return true;
    }

    @Override
    public Bitmap getAnimationImage() {
        return animationImages[currentImage];
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    private int randInt(int min, int max)
    {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static boolean isAnimatedTile(TileType setType) {
        if (setType == TileType.CAMPFIRE_GRASS) {
            return true;
        }
        if (setType.name().matches("WATER(.*)")) {
            return true;
        }
        return false;
    }
}
