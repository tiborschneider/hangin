package com.tiborschneider.hangin;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Tibor Schneider on 26.06.2016.
 */
public class StateHandler {
    private DatabaseHelper databaseHelper;
    private GamePanel gamePanel;
    public static int bestNpcPlayerRelation = 2;
    public static int worstNpcPlayerRelation = -2;
    public static int neutralNpcPlayerRelation = 0;

    public StateHandler(GamePanel aGamePanel, DatabaseHelper aDatabaseHelper)
    {
        gamePanel = aGamePanel;
        databaseHelper = aDatabaseHelper;
    }

    public GameState getState(String aState)
    {
        return databaseHelper.getState(aState);
    }

    public int getStateValue(String aState)
    {
        return databaseHelper.getState(aState).value;
    }

    public void setState(GameState aState)
    {
        if (aState.name != "NULL") {
            System.out.println("Set State on Database: " + aState.name + ", value: " + aState.value);
            databaseHelper.setState(aState);
        }
    }

    public Dialogue getNpcDialogue(String aName) {
        return databaseHelper.getDialogueFromDB(databaseHelper.getNpcDialogueName(aName));
    }

    public void deleteLootbox(int aX, int aY)
    {
        databaseHelper.deleteLootbox(aX, aY);
    }

    public void deleteItemFromInventory(Item aItem)
    {
        Inventory inventory = gamePanel.getPlayer().getInventory();
        if (!inventory.hasItem(aItem))
            databaseHelper.deleteInventoryItem(aItem.getItemType().name());
        else
            databaseHelper.setInventoryItemCount(aItem.getItemType().name(), inventory.getItemCount(inventory.getItemIndex(aItem)));
    }

    public void addItemToInventory(Item aItem)
    {
        Inventory inventory = gamePanel.getPlayer().getInventory();
        int newItemCount = inventory.getItemCount(inventory.getItemIndex(aItem));
        if (newItemCount != 1)
            databaseHelper.setInventoryItemCount(aItem.getItemType().name(), newItemCount);
        else
            databaseHelper.addNewItemToInventory(aItem.getItemType().name(), 1);
    }

    public void savePlayerState()
    {
        databaseHelper.savePlayerStatus();
    }
}
