package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Tibor Schneider on 25.06.2016.
 */
public class Inventory {
    public static int maxNumEntries = 30;
    private int numEntries = 0;
    private Context context;
    private GamePanel gamePanel;
    private InventoryEntry[] entries = new InventoryEntry[maxNumEntries];
    private int equippedEntry = 0;

    public Inventory(Context aContext, GamePanel aGamePanel)
    {
        context = aContext;
        gamePanel = aGamePanel;
        getEntriesFromDB();
    }

    private void getEntriesFromDB()
    {
        //TODO: Create this method
    }

    public boolean addItem(Item aItem, boolean addToinventory)
    {
        boolean itemFound = false;
        int index = 0;
        for (int i = 0; i < numEntries; i++) {
            if (entries[i].checkItem(aItem)) {
                itemFound = true;
                index = i;
            }
        }

        if (itemFound) {
            entries[index].count++;
        } else {
            if (numEntries == maxNumEntries)
                return false;
            entries[numEntries++] = new InventoryEntry(aItem, 1);
        }
        if (addToinventory)
            gamePanel.getStateHandler().addItemToInventory(aItem);
        return true;
    }

    public boolean hasItem(Item aItem)
    {
        for (int i = 0; i < numEntries; i++)
            if (entries[i].checkItem(aItem))
                return true;
        return false;
    }


    public boolean useItem(Item aItem)
    {
        boolean itemFound = false;
        int index = 0;
        for (int i = 0; i < numEntries; i++) {
            if (entries[i].checkItem(aItem)) {
                itemFound = true;
                index = i;
            }
        }

        if (itemFound) {
            if (entries[index].count == 1)
                deleteEntry(index);
            else {
                entries[index].count--;
                entries[index].resetItem();
            }
            gamePanel.getStateHandler().deleteItemFromInventory(aItem);
            return true;
        } else return false;
    }

    public void deleteEntry(int index)
    {
        entries[index] = null;
        for (int i = (index+1); i < maxNumEntries; i++)
            entries[i-1] = entries[i];
        entries[maxNumEntries-1] = null;
        numEntries--;
    }

    public int getItemCount(int index)
    {
        if (index < numEntries && index >= 0) {
            return entries[index].count;
        }
        return 0;
    }

    public int getItemUsages(int index)
    {
        if (index < numEntries && index >= 0) {
            if (SpecialItem.isSpecialItem(entries[index].getItem().getItemType())) {
                return (entries[index].count - 1) * entries[index].getItem().getMaxNumUses() + entries[index].getItem().getNumUses();
            } else {
                return entries[index].count;
            }
        }
        return 0;
    }

    public int getItemIndex(Item aItem)
    {
        for (int i = 0; i < numEntries; i++)
            if (entries[i].checkItem(aItem))
                return i;

        return -1;
    }

    public Bitmap getImage(int index)
    {
        if (index < numEntries && index >= 0)
            return entries[index].getImage();
        return null;
    }

    public int getEquippedEntry() {
        return equippedEntry;
    }

    public void setEquippedEntry(int aEquippedEntry) {
        this.equippedEntry = aEquippedEntry;
    }

    public int getNumEntries() {
        return numEntries;
    }

    public InventoryEntry[] getEntries() {
        return entries;
    }

    public Item getItem(int index)
    {
        if (index < numEntries && index >= 0)
            return entries[index].getItem();
        return null;
    }

    public int getItemIndex(ItemType itemType) {
        for (int i = 0; i < numEntries; i++) {
            if (entries[i].getItem().getItemType() == itemType) {
                return i;
            }
        }
        return -1;
    }
}
