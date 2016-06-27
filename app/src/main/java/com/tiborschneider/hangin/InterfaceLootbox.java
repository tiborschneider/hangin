package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;

/**
 * Created by Tibor Schneider on 24.06.2016.
 */
public class InterfaceLootbox extends InterfaceElement {
    public static String infoText = "You have found a Lootbox!";
    private int xPosInfoText;
    private int yPosInfoText;
    private int xPosImage;
    private int[] yPosImage = new int[5];
    private int xPosText;
    private int[] yPosText = new int[5];
    private Lootbox lootbox;
    private Bitmap image;
    private Context context;

    public InterfaceLootbox(Context aContext, Lootbox aLootbox)
    {
        lootbox = aLootbox;
        context = aContext;
        x = (GamePanel.screenWidth - lootboxWidth) / 2;
        y = x;
        xPosInfoText = x + borderWidth + innerTextMargin;
        yPosInfoText = y + borderWidth + innerTextMargin + normalTextSize + textOffset;
        xPosImage = x + borderWidth + lootboxImageMargin;
        xPosText = xPosImage + itemSize + lootboxItemMargin;
        for (int i = 0; i < lootbox.getNumItems(); i++) {
            yPosImage[i] = y + borderWidth + 2*innerTextMargin + normalTextSize + borderWidth + lootboxImageMargin + i*(2*lootboxImageMargin + borderSmallWidth + itemSize);
            yPosText[i] =  y + borderWidth + 2*innerTextMargin + normalTextSize + borderWidth + lootboxTextMargin  + i*(2*lootboxTextMargin  + borderSmallWidth + normalTextSize) + normalTextSize + textOffset;
        }
        switch (lootbox.getNumItems()) {
            case 1:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_lootbox1);
                break;
            case 2:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_lootbox2);
                break;
            case 3:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_lootbox3);
                break;
            case 4:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_lootbox4);
                break;
            case 5:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_lootbox5);
                break;
        }
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y, null);
        TextPaint paint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        int textColor = context.getResources().getColor(R.color.interfaceText);
        paint.setColor(textColor);
        paint.setTextSize(normalTextSize);
        canvas.drawText(infoText, xPosInfoText, yPosInfoText, paint);
        for (int i = 0; i < lootbox.getNumItems(); i++) {
            canvas.drawBitmap(lootbox.getImageOfItem(i), xPosImage, yPosImage[i], null);
            canvas.drawText(lootbox.getTextOfItem(i), xPosText, yPosText[i], paint);
        }
    }
}