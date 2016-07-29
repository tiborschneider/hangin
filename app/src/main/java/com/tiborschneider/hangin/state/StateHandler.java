package com.tiborschneider.hangin.state;

import com.tiborschneider.hangin.mainGame.DatabaseHelper;
import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.character.Inventory;
import com.tiborschneider.hangin.character.NonPlayerCharacter;
import com.tiborschneider.hangin.dialogue.Dialogue;
import com.tiborschneider.hangin.item.Item;
import com.tiborschneider.hangin.item.ItemType;

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
        GameState state = databaseHelper.getState(aState);
        if (state.isSpecial) {
            switch (state.name) {
                case "playerHasWeed":
                    if (gamePanel.getPlayer().hasItem(new Item(gamePanel.getContext(), ItemType.WEED_BAG)))
                        state.value = 1;
                    else
                        state.value = 0;
                    break;
                case "playerHasJoint":
                    if (gamePanel.getPlayer().hasItem(new Item(gamePanel.getContext(), ItemType.BIG_JOINT)))
                        state.value = 3;
                    else if (gamePanel.getPlayer().hasItem(new Item(gamePanel.getContext(), ItemType.JOINT)))
                        state.value = 2;
                    else if (gamePanel.getPlayer().hasItem(new Item(gamePanel.getContext(), ItemType.UGLY_JOINT)))
                        state.value = 1;
                    else
                        state.value = 0;
                    break;
                case "playerHasLighter":
                    if (gamePanel.getPlayer().hasItem(ItemType.LIGHTER))
                        state.value = 1;
                    else
                        state.value = 0;
                    break;
                case "playerHasSomethingToSmoke":
                    System.out.println("check Something to smoke");
                    if (gamePanel.getPlayer().hasItem(new Item(gamePanel.getContext(), ItemType.BIG_JOINT)))
                        state.value = 1;
                    else if (gamePanel.getPlayer().hasItem(new Item(gamePanel.getContext(), ItemType.JOINT)))
                        state.value = 1;
                    else if (gamePanel.getPlayer().hasItem(new Item(gamePanel.getContext(), ItemType.UGLY_JOINT)))
                        state.value = 1;
                    else
                        state.value = 0;
                    break;
            }
        }

        //¨System.out.println("get State: " + state.name + ", " + state.value);
        return state;
    }

    public void setState(GameState aState)
    {
        if (aState.name != "NULL") {
            //System.out.println("Set State on Database: " + aState.name + ", value: " + aState.value);
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
            databaseHelper.setInventoryItemCount(aItem.getItemType().name(), inventory.getItemCount(inventory.getItemIndex(aItem)), aItem.getNumUses());
    }

    public void addItemToInventory(Item aItem)
    {
        Inventory inventory = gamePanel.getPlayer().getInventory();
        int newItemCount = inventory.getItemCount(inventory.getItemIndex(aItem));
        if (newItemCount != 1)
            databaseHelper.setInventoryItemCount(aItem.getItemType().name(), newItemCount, aItem.getNumUses());
        else
            databaseHelper.addNewItemToInventory(aItem.getItemType().name(), 1, aItem.getNumUses());
    }

    public void updateDatabaseInventory()
    {
        Inventory inventory = gamePanel.getPlayer().getInventory();
        for (int i = 0; i < inventory.getNumEntries(); i++) {
            Item item = inventory.getItem(i);
            if (item.getItemType() == ItemType.WEED_BAG) {
                databaseHelper.setInventoryItemCount(item.getItemType().name(), inventory.getItemCount(i), item.getNumUses());
            }
        }

    }

    public void savePlayerState()
    {
        databaseHelper.savePlayerStatus();
    }

    public static boolean isSpecialState(String stateName) {
        switch (stateName) {
            case "playerHasWeed":
            case "playerHasJoint":
            case "playerHasLighter":
            case "playerHasSomethingToSmoke":
                return true;
            default:
                return false;
        }
    }

    public boolean commandNpc(NonPlayerCharacter npc) {
        return databaseHelper.getCommandFromDatabase(npc);
    }

    public void updateTimeToPass() {
        int numTTP = 1;
        String[] stateName = new String[numTTP];

        stateName[0] = "strangeGuyTTP";

        for (int i = 0; i < numTTP; i++) {
            GameState state = getState(stateName[i]);
            state.value--;
            if (state.value < 0)
                state.value = 0;
            else
                setState(state);
        }
    }
}

