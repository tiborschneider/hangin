package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.BitmapFactory;

/**
 * Created by tibor on 28.06.16.
 * Special Item which have a Counter (for example the number of matches left.
 */
public class SpecialItem extends Item {
    private int count = 0;

    public SpecialItem(Context aContext, ItemType aType)
    {
        super(aContext, aType);
        isSpecial = true;
        switch (itemType) {
            case WEED_BAG:
                count = 6;
                break;
        }
    }

    @Override
    public String getText()
    {
        switch (itemType) {
            case WEED_BAG:
                return "Small bag of weed";
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
}
