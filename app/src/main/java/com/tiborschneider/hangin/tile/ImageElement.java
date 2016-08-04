package com.tiborschneider.hangin.tile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tiborschneider.hangin.R;
import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.userInteraction.InterfaceElement;

/**
 * Created by tibor on 04.08.16.
 */
public class ImageElement {
    Bitmap image;
    String imageName;

    public ImageElement(String imageName) {
        this.imageName = imageName;
        int imageId = GamePanel.getGamePanel().getContext().getResources().getIdentifier(imageName, "drawable", GamePanel.getGamePanel().getContext().getPackageName());
        image = BitmapFactory.decodeResource(GamePanel.getGamePanel().getContext().getResources(), imageId);
        image = InterfaceElement.resizeImage(image, (int)(0.5+image.getWidth()*InterfaceElement.getCorrFactor()), (int)(0.5+image.getHeight()*InterfaceElement.getCorrFactor()));
    }

    public boolean equals(String name) {
        if (imageName.equals(name))
            return true;
        return false;
    }

    public Bitmap getImage() {
        return image;
    }
}
