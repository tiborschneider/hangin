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
        Item item = gamePanel.getPlayer().getEquippedItem();

        //get Type of Joint you can create
        GameState abilityRollJoints = gamePanel.getStateHandler().getState("abilityRollJoints");


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
}
