package com.tiborschneider.hangin;

/**
 * Created by Tibor Schneider on 25.06.2016.
 */
public class Action {
    private ActionType actionType;
    private GamePanel gamePanel;

    public Action(GamePanel aGamePanel, ActionType aActionType)
    {
        gamePanel = aGamePanel;
        actionType = aActionType;
    }

    public Action(GamePanel aGamePanel, String aActionName)
    {
        gamePanel = aGamePanel;
        actionType = ActionType.valueOf(aActionName);
    }

    public void run()
    {
        switch (actionType) {
            case DROP:
                drop();
                break;
            case CONSUME:
                consume();
                break;
            case PLANT_SEED:
                plantSeed();
                break;
            case ROLL:
                roll();
                break;
            case MOVE_NPC:
                moveNpc();
                break;
            case NULL:
                //do nothing
                break;
        }
    }

    private void drop()
    {
        Player player = gamePanel.getPlayer();
        Item item = player.getEquippedItem();
        player.useItem(item);
    }

    private void consume()
    {
        //check if smoking with an npc
        GameState smokeWithNpc = gamePanel.getStateHandler().getState("smokeWithNpc");
        if (smokeWithNpc.value != 0) {
            //smoke with NPC
            gamePanel.getPlayer().updateStonedMeter(smokeWithNpc.value);
            smokeWithNpc.value = 0;
            gamePanel.getStateHandler().setState(smokeWithNpc);
            return;
        }

        //check if smoking with an npc
        smokeWithNpc = gamePanel.getStateHandler().getState("smokeWithNpcUglyJoint");
        if (smokeWithNpc.value == 1) {
            //smoke with NPC
            gamePanel.getPlayer().updateStonedMeter(10);
            gamePanel.getPlayer().useItem(new Item(gamePanel.getContext(), ItemType.UGLY_JOINT));
            smokeWithNpc.value = 0;
            gamePanel.getStateHandler().setState(smokeWithNpc);
            return;
        }

        //check if smoking with an npc
        smokeWithNpc = gamePanel.getStateHandler().getState("smokeWithNpcJoint");
        if (smokeWithNpc.value == 1) {
            //smoke with NPC
            gamePanel.getPlayer().updateStonedMeter(15);
            gamePanel.getPlayer().useItem(new Item(gamePanel.getContext(), ItemType.JOINT));
            smokeWithNpc.value = 0;
            gamePanel.getStateHandler().setState(smokeWithNpc);
            return;
        }

        //check if smoking with an npc
        smokeWithNpc = gamePanel.getStateHandler().getState("smokeWithNpcBigJoint");
        if (smokeWithNpc.value == 1) {
            //smoke with NPC
            gamePanel.getPlayer().updateStonedMeter(30);
            gamePanel.getPlayer().useItem(new Item(gamePanel.getContext(), ItemType.BIG_JOINT));
            smokeWithNpc.value = 0;
            gamePanel.getStateHandler().setState(smokeWithNpc);
            return;
        }


        //check if smoking with an npc
        smokeWithNpc = gamePanel.getStateHandler().getState("smokeWithNpcWeed");
        if (smokeWithNpc.value == 1) {
            //smoke with NPC
            gamePanel.getPlayer().updateStonedMeter(30);
            Item item = gamePanel.getPlayer().getInventory().getItem(gamePanel.getPlayer().getInventory().getItemIndex(ItemType.WEED_BAG));

            //reduce Number of Item used
            if (!item.useOnce())
                gamePanel.getPlayer().getInventory().useItem(item);

            smokeWithNpc.value = 0;
            gamePanel.getStateHandler().setState(smokeWithNpc);
            return;
        }

        //check if smoke on Campfire
        GameState smokeAtCampfire = gamePanel.getStateHandler().getState("smokeAtCampfire");
        if (smokeAtCampfire.value != 0) {
            switch(smokeAtCampfire.value) {
                case 1:
                    //smoke Ugly Joint
                    gamePanel.getPlayer().updateStonedMeter(10);
                    gamePanel.getPlayer().useItem(new Item(gamePanel.getContext(), ItemType.UGLY_JOINT));
                    break;
                case 2:
                    //smoke  Joint
                    gamePanel.getPlayer().updateStonedMeter(20);
                    gamePanel.getPlayer().useItem(new Item(gamePanel.getContext(), ItemType.JOINT));
                    break;
                case 3:
                    //smoke Big Joint
                    gamePanel.getPlayer().updateStonedMeter(50);
                    gamePanel.getPlayer().useItem(new Item(gamePanel.getContext(), ItemType.BIG_JOINT));
                    break;
            }
            smokeAtCampfire.value = 0;
            gamePanel.getStateHandler().setState(smokeAtCampfire);
        }

        //Check if smoke on Campfire a Joint rolled before:
        smokeAtCampfire = gamePanel.getStateHandler().getState("smokeAtCampfireRolledJoint");
        if (smokeAtCampfire.value != 0) {
            GameState abilityRollJoints = gamePanel.getStateHandler().getState("abilityRollJoints");
            int stonedOffset = (smokeAtCampfire.value - 1) * 10;
            switch (abilityRollJoints.value) {
                case 1:
                    //smoke Ugly Joint
                    gamePanel.getPlayer().updateStonedMeter(20-stonedOffset);
                    gamePanel.getPlayer().useItem(new Item(gamePanel.getContext(), ItemType.UGLY_JOINT));
                    break;
                case 2:
                    //smoke  Joint
                    gamePanel.getPlayer().updateStonedMeter(30-stonedOffset);
                    gamePanel.getPlayer().useItem(new Item(gamePanel.getContext(), ItemType.JOINT));
                    break;
                case 3:
                    //smoke Big Joint
                    gamePanel.getPlayer().updateStonedMeter(60-stonedOffset);
                    gamePanel.getPlayer().useItem(new Item(gamePanel.getContext(), ItemType.BIG_JOINT));
                    break;
            }

            smokeAtCampfire.value = 0;
            gamePanel.getStateHandler().setState(smokeAtCampfire);
        }

        Player player = gamePanel.getPlayer();
        Item item = player.getEquippedItem();
        if (item.needsfire()) {
            Item lighter = Item.createNewItem(GamePanel.getGamePanel().getContext(), ItemType.valueOf("LIGHTER"));
            if (player.hasItem(lighter)) {
                lighter = player.getInventory().getItem(player.getInventory().getItemIndex(lighter));
                if (!lighter.useOnce()) {
                    //lighter went out! delete Lighter
                    player.getInventory().useItem(lighter);
                    //queue dialogue
                    gamePanel.getInteractionHandler().queueDialogue(gamePanel.getInteractionHandler().getDialogueFromDatabase("lighterOff"));
                }
                player.consumeItem(item);
            } else {
                //override Dialogue with new Dialogue
                gamePanel.getInteractionHandler().overrideDialogue(gamePanel.getInteractionHandler().getDialogueFromDatabase("noLighter"));
            }
        } else {
            player.consumeItem(item);
        }
    }

    private void plantSeed()
    {
        //TODO: plant seed
    }

    private void roll()
    {

        GameState abilityRollJoints = gamePanel.getStateHandler().getState("abilityRollJoints");
        Item item = null;
        GameState smokeAtCampfire = gamePanel.getStateHandler().getState("smokeAtCampfire");
        if (smokeAtCampfire.value == 4) {
            item = gamePanel.getPlayer().getInventory().getItem(gamePanel.getPlayer().getInventory().getItemIndex(ItemType.WEED_BAG));
        } else {
            item = gamePanel.getPlayer().getEquippedItem();
        }

        if (SpecialItem.isSpecialItem(item.getItemType()) && item.getNumUses() > 0 && abilityRollJoints.value > 0) {
            //add a Joint to Inventory
            switch (abilityRollJoints.value) {
                case 1:
                    gamePanel.getPlayer().getInventory().addItem(Item.createNewItem(gamePanel.getContext(), ItemType.UGLY_JOINT),true);
                    break;
                case 2:
                    gamePanel.getPlayer().getInventory().addItem(Item.createNewItem(gamePanel.getContext(), ItemType.JOINT),true);
                    break;
                case 3:
                    gamePanel.getPlayer().getInventory().addItem(Item.createNewItem(gamePanel.getContext(), ItemType.BIG_JOINT),true);
                    break;
            }


            //reduce Number of Item used
            if (!item.useOnce()) {
                //delete Item
                gamePanel.getPlayer().getInventory().useItem(item);
            }
            gamePanel.getStateHandler().deleteItemFromInventory(item);
        }
    }

    private void moveNpc()
    {
        //check which NPC to move
        GameState moveNpc = gamePanel.getStateHandler().getState("moveKatia");
        if (moveNpc.value == 1) {
            moveNpc.value = 0;
            gamePanel.getStateHandler().setState(moveNpc);
            NonPlayerCharacter npc = gamePanel.getNpc("katia");
            gamePanel.getStateHandler().commandNpc(npc);
        } else {
            return;
        }

        //get NPC movement from Database

    }
}
