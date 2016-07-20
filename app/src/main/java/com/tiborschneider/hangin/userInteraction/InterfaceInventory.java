package com.tiborschneider.hangin.userInteraction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;

import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.R;
import com.tiborschneider.hangin.character.Inventory;

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
    private Bitmap darkShadow;
    private int currentIndex = 0;

    public InterfaceInventory(Context aContext, Inventory aInventory)
    {
        context = aContext;
        inventory = aInventory;
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_inventory_window);
        image = resizeImage(image, InterfaceElement.inventoryWidth, InterfaceElement.inventoryHeight);

        selectionImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_inventory_select);
        selectionImage = resizeImage(selectionImage, InterfaceElement.inventorySelectionWidth, InterfaceElement.inventorySelectionHeight);

        darkShadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_inventory_dark_shadow);
        darkShadow = resizeImage(darkShadow, InterfaceElement.inventoryWidth, InterfaceElement.inventoryHeight);

        x = (GamePanel.screenWidth - inventoryWidth) / 2;
        y = 2*statusBarOuterMargin + statusBarHeight - gameBorderSize + x;
        xPosTitleText = borderWidth + innerTextMargin; // +x
        yPosTitleText = borderWidth + innerTextMargin + bigTextSize + textOffset; // +y
        for (int i = 0; i < numCols; i++)
            xPosImage[i] = borderWidth + inventoryImageMargin + i*(itemSize + 2*inventoryImageMargin + borderSmallWidth); // +x
        for (int i = 0; i < numRows; i++)
            yPosImage[i] = 2*borderWidth + 2*innerTextMargin + bigTextSize + inventoryImageMargin + i*(itemSize + 2*inventoryImageMargin + borderSmallWidth); // +y
        currentIndex = inventory.getEquippedEntry();
    }

    public void draw(Canvas canvas)
    {
        //create new temporary Canvas and Image
        Bitmap tempImage = Bitmap.createBitmap(InterfaceElement.inventoryWidth, InterfaceElement.inventoryHeight, Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(tempImage);

        //Paint background and Title
        tempCanvas.drawBitmap(image, 0, 0, null);
        TextPaint paint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        int textColor = context.getResources().getColor(R.color.interfaceText);
        paint.setColor(textColor);
        paint.setTextSize(bigTextSize);
        tempCanvas.drawText(infoText, xPosTitleText, yPosTitleText, paint);

        //paint Items and Text
        paint.setTextSize(normalTextSize);
        int row = 0;
        int col = -1;
        for (int i = 0; i < inventory.getNumEntries(); i++) {
            if (++col == numCols) {
                col = 0;
                row++;
            }
            tempCanvas.drawBitmap(inventory.getImage(i),xPosImage[col], yPosImage[row], null);
            int tmpX = xPosImage[col] - inventoryImageMargin + inventoryTextMargin;
            int tmpY = yPosImage[row] + inventoryImageMargin + itemSize - inventoryTextMargin + textOffset;
            tempCanvas.drawText("" + inventory.getItemUsages(i), tmpX, tmpY, paint);
        }

        //paint selection
        int tmpX = borderWidth - borderSmallWidth + getCurrentCol()*(borderSmallWidth + 2*inventoryImageMargin + itemSize); // + x
        int tmpY = 2*borderWidth + 2*innerTextMargin + bigTextSize - borderSmallWidth + getCurrentRow()*(borderSmallWidth + 2*inventoryImageMargin + itemSize); // + y
        tempCanvas.drawBitmap(selectionImage, tmpX, tmpY, null);

        //make darker if needed
        if (InteractionHandler.multipleInterfacesActive) {
            tempCanvas.drawBitmap(darkShadow, 0, 0, null);
        }

        //scale Bitmap
        tmpX = x;
        tmpY = y;
        if (InteractionHandler.multipleInterfacesActive) {
            tempImage = InterfaceElement.resizeImage(tempImage, inventoryWidth * 7 / 8, inventoryHeight * 7 / 8);
            tmpX += inventoryWidth / 16;
            tmpY += inventoryWidth / 16;
        }

        //draw Bitmap on Canvas
        canvas.drawBitmap(tempImage, tmpX, tmpY, null);

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
