package com.tiborschneider.hangin.character;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.tiborschneider.hangin.R;
import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.userInteraction.InterfaceElement;

/**
 * Created by tibor on 03.08.16.
 */
public class SmokeAnimation {
    private static final int numAnimations = 23;
    private static final int yOffset = -30;

    private Context context;
    private Bitmap[] images = new Bitmap[numAnimations];
    private int counter = 0;
    private int updateSpeed = 2;
    private int x;
    private int y;
    private boolean finished = true;

    public SmokeAnimation(Context context, int ix, int iy) {
        this.context = context;
        finished = true;
        setPosition(ix, iy);
        cutAnimation();
    }

    private void setPosition(int ix, int iy) {
        this.x = ix * InterfaceElement.tileSize + InterfaceElement.gameBorderSize;
        this.y = yOffset + iy* InterfaceElement.tileSize + 2*InterfaceElement.statusBarOuterMargin + InterfaceElement.statusBarHeight;
    }

    public void draw(Canvas canvas) {
        if (!finished) {
            canvas.drawBitmap(images[counter / updateSpeed], x, y, null);
        }
    }

    public void update() {
        if (!finished) {
            if (++counter == numAnimations * updateSpeed) {
                counter = 0;
                finished = true;
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void restart(int ix, int iy) {
        setPosition(ix, iy);
        counter = 0;
        finished = false;
    }

    private void cutAnimation() {
        Bitmap sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.smoke_animation);
        for (int i = 0; i < numAnimations; i++) {
            images[i] = Bitmap.createBitmap(sprite, i * InterfaceElement.tileSize, 0, InterfaceElement.tileSize, InterfaceElement.tileSize);
            InterfaceElement.resizeImage(images[i], InterfaceElement.tileSize, InterfaceElement.tileSize);
        }
    }
}
