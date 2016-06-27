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
        player.consumeItem(item);
    }

    private void plantSeed()
    {
        //TODO: plant seed
    }
}
