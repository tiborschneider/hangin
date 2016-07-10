package com.tiborschneider.hangin;

import android.content.Context;

/**
 * Created by Tibor Schneider on 22.06.2016.
 */
public class InteractiveTile extends GameTile{
    protected Direction interactiveFromNeighbour = Direction.NDEF;
    protected String dialogue;

    public InteractiveTile(Context context, TileType setType)
    {
        super(context, setType);
        isInteractive = true;
        switch (type) {
            case CAMPFIRE_GRASS:
                interactiveFromNeighbour = Direction.ANY;
                break;
        }
    }

    @Override public boolean setDialogue(String aDialogue)
    {
        dialogue = aDialogue;
        return true;
    }

    @Override public Dialogue getDialogue()
    {
        return GamePanel.getGamePanel().getDatabaseHelper().getDialogueFromDB(dialogue);
    }

    public static boolean isInteractiveTile(TileType setType) {
        if (setType == TileType.CAMPFIRE_GRASS) {
            return true;
        }
        return false;
    }

    public Direction isInteractiveFromNeighbour() {
        return interactiveFromNeighbour;
    }
}
