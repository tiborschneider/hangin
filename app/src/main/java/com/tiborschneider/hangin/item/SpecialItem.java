package com.tiborschneider.hangin.item;

import android.content.Context;

import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.state.GameState;
import com.tiborschneider.hangin.userInteraction.InteractionHandler;

/**
 * Created by tibor on 28.06.16.
 * Special Item which have a Counter (for example the number of matches left.
 */
public class SpecialItem extends Item {
    private int count = 0;
    private int currentUsedJointUnit = 0;
    private static int maxNumJointsFirstStage = 6;
    private static int maxLighterUsages = 3; // 20

    public SpecialItem(Context aContext, ItemType aType)
    {
        super(aContext, aType);
        isSpecial = true;
        switch (itemType) {
            case WEED_BAG:
                count = maxNumJointsFirstStage;
                currentUsedJointUnit = 1;
                break;
            case LIGHTER:
                count = maxLighterUsages;
                break;
        }
        getNumUses();
    }

    @Override
    public String getText()
    {
        switch (itemType) {
            case WEED_BAG:
                return "Small bag of weed";
            case LIGHTER:
                return "Lighter";
            default:
                return "none";
        }
    }

    @Override
    public void use(InteractionHandler interactionHandler)
    {
        String dialogueName;
        switch (itemType) {
            case WEED_BAG:
                dialogueName = "rollOne";
                break;
            case LIGHTER:
                dialogueName = "useLighter";
                break;
            default:
                return;
        }
        interactionHandler.createDialogue(interactionHandler.getDialogueFromDatabase(dialogueName));
    }

    public static boolean isSpecialItem(ItemType aType)
    {
        switch (aType) {
            case WEED_BAG:
                return true;
            case LIGHTER:
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getCount()
    {
        return count;
    }

    @Override
    public void setCount(int count)
    {
        this.count = count;
        if (itemType == ItemType.WEED_BAG) {
            currentUsedJointUnit = GamePanel.getGamePanel().getStateHandler().getState("abilityRollJoints").value;
        }
    }

    @Override
    public boolean useOnce()
    {
        if (count > 1) {
            count--;
            return true;
        } else {
            count = 0;
            return false;
        }
    }

    @Override
    public int getNumUses()
    {
        switch(itemType) {
            case WEED_BAG:
                    GameState state = GamePanel.getGamePanel().getStateHandler().getState("abilityRollJoints");
                    if (state.value == currentUsedJointUnit || (state.value == 0 && currentUsedJointUnit == 1)) {
                        return count;
                    } else {
                        if (state.value == 0) {
                            currentUsedJointUnit = 1;
                            return 0;
                        } else if (state.value == 2 && currentUsedJointUnit == 1) {
                            currentUsedJointUnit = state.value;
                            count = count * 3 / 2;
                            return count;
                        } else if (state.value == 3 && currentUsedJointUnit == 2) {
                            currentUsedJointUnit = state.value;
                            count = count * 4 / 5;
                            return count;
                        } else if (state.value == 3 && currentUsedJointUnit == 1) {
                            currentUsedJointUnit = state.value;
                            count = count * 3 * 4 / 2 / 5;
                            return count;
                        }
                    }
                break;
        }
        return count;
    }

    @Override
    public int getMaxNumUses()
    {
        switch(itemType) {
            case WEED_BAG:
                GameState state = GamePanel.getGamePanel().getStateHandler().getState("abilityRollJoints");
                switch (state.value) {
                    case 2:
                        return maxNumJointsFirstStage * 3 / 2;
                    case 3:
                        return maxNumJointsFirstStage * 3 * 4 / 2 / 5;
                    default:
                        return maxNumJointsFirstStage;
                }
            case LIGHTER:
                return maxLighterUsages;
        }
        return 0;
    }
}
