package com.tiborschneider.hangin;

import android.graphics.Bitmap;

/**
 * Created by Tibor Schneider on 25.06.2016.
 */
public class InventoryEntry {
    public Item item;
    public int count;

    public InventoryEntry(Item aItem, int aCount)
    {
        item = aItem;
        count = aCount;
    }

    public Bitmap getImage() {
        return item.getImage();
    }

    public boolean checkItem(Item aItem)
    {
        boolean ret = (item.getItemType() == aItem.getItemType());
        return ret;
    }

    public Item getItem()
    {
        return item;
    }
}