package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;

/**
 * Created by Tibor Schneider on 25.06.2016.
 */
public class InterfaceInventory extends InterfaceElement {
    public static String infoText = "Inventory";
    public static int numCols = 6;
    public static int numRows = 5;
    private int xPosTitleText;
    private int yPosTitleText;
    private int[] xPosImage = new int[numCols];
    private int[] yPosImage = new int[numRows];
    private Inventory inventory;
    private Context context;
    private Bitmap image;
    private Bitmap selectionImage;
    private int currentIndex = 0;

    public InterfaceInventory(Context aContext, Inventory aInventory)
    {
        context = aContext;
        inventory = aInventory;
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_inventory_window);
        selectionImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_inventory_select);
        x = (GamePanel.screenWidth - inventoryWidth) / 2;
        y = x;
        xPosTitleText = x + borderWidth + innerTextMargin;
        yPosTitleText = y + borderWidth + innerTextMargin + bigTextSize + textOffset;
        for (int i = 0; i < numCols; i++)
            xPosImage[i] = x + borderWidth + inventoryImageMargin + i*(itemSize + 2*inventoryImageMargin + borderSmallWidth);
        for (int i = 0; i < numRows; i++)
            yPosImage[i] = y + 2*borderWidth + 2*innerTextMargin + bigTextSize + inventoryImageMargin + i*(itemSize + 2*inventoryImageMargin + borderSmallWidth);
        currentIndex = inventory.getEquippedEntry();
    }

    public void draw(Canvas canvas)
    {
        //Paint background and Title
        canvas.drawBitmap(image, x, y, null);
        TextPaint paint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        int textColor = context.getResources().getColor(R.color.interfaceText);
        paint.setColor(textColor);
        paint.setTextSize(bigTextSize);
        canvas.drawText(infoText, xPosTitleText, yPosTitleText, paint);

        //paint Items and Text
        paint.setTextSize(normalTextSize);
        int row = 0;
        int col = -1;
        for (int i = 0; i < inventory.getNumEntries(); i++) {
            if (++col == numCols) {
                col = 0;
                row++;
            }
            canvas.drawBitmap(inventory.getImage(i),xPosImage[col], yPosImage[row], null);
            int tmpX = xPosImage[col] - inventoryImageMargin + inventoryTextMargin;
            int tmpY = yPosImage[row] + inventoryImageMargin + itemSize - inventoryTextMargin + textOffset;
            canvas.drawText("" + inventory.getItemCount(i), tmpX, tmpY, paint);
        }

        //paint selection
        int tmpX = x + borderWidth - borderSmallWidth + getCurrentCol()*(borderSmallWidth + 2*inventoryImageMargin + itemSize);
        int tmpY = y + 2*borderWidth + 2*innerTextMargin + bigTextSize - borderSmallWidth + getCurrentRow()*(borderSmallWidth + 2*inventoryImageMargin + itemSize);
        canvas.drawBitmap(selectionImage, tmpX, tmpY, null);

    }

    private int getCurrentCol()
    {
        return currentIndex % numCols;
    }

    private int getCurrentRow()
    {
        return currentIndex / numCols;
    }

    public void moveSelectionUp()
    {
        if (getCurrentRow() > 0)
            currentIndex -= numCols;
    }

    public void moveSelectionDown()
    {
        if (getCurrentRow() < numRows - 1)
            currentIndex += numCols;
    }

    public void moveSelectionLeft()
    {
        if (getCurrentCol() > 0)
            currentIndex--;
    }

    public void moveSelectionRight()
    {
        if (getCurrentCol() < numCols - 1)
            currentIndex++;
    }

    public void selectItem()
    {
        inventory.setEquippedEntry(currentIndex);
    }
}
