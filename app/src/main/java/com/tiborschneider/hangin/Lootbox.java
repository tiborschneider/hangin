package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Tibor Schneider on 24.06.2016.
 */
public class Lootbox {
    public static int maxNumItem = 5;
    private Bitmap image;
    private Context context;
    private Item[] items = new Item[maxNumItem];
    private int numItems = 0;
    private int x;
    private int y;
    private int posX;
    private int posY;

    public Lootbox(Context aContext, int aX, int aY)
    {
        context = aContext;
        x = aX;
        y = aY;
        posX = InterfaceElement.gameBorderSize + x* InterfaceElement.tileSize;
        posY = InterfaceElement.gameBorderSize + y* InterfaceElement.tileSize;
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.lootbox);
        image = InterfaceElement.resizeImage(image, InterfaceElement.tileSize, InterfaceElement.tileSize);
    }

    public boolean addItem(Item aItem)
    {
        if (numItems < maxNumItem) {
            items[numItems++] = aItem;
            return true;
        } else {
            return false;
        }
    }

    public boolean addItem(ItemType aType)
    {
        if (numItems < maxNumItem) {
            items[numItems++] = new Item(context, aType);
            return true;
        } else {
            return false;
        }
    }

    public Item[] getItems() {
        return items;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getNumItems() {
        return numItems;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, posX, posY, null);
    }

    public Bitmap getImageOfItem(int index)
    {
        if (index >= 0 && index < numItems)
            return items[index].getImage();
        else return null;
    }

    public String getTextOfItem(int index)
    {
        if (index >= 0 && index < numItems)
            return items[index].getText();
        else return null;
    }

    public boolean addItemsToInventory(Inventory inventory)
    {
        boolean spaceLeft = true;
        for (int i = 0; i < numItems; i++) {
            spaceLeft = inventory.addItem(items[i]);
        }
        return spaceLeft;
    }
}
