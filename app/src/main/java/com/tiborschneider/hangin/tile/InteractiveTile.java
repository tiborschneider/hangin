package com.tiborschneider.hangin.tile;

import android.content.Context;

import com.tiborschneider.hangin.dialogue.Dialogue;
import com.tiborschneider.hangin.character.Direction;
import com.tiborschneider.hangin.mainGame.GamePanel;

/**
 * Created by Tibor Schneider on 22.06.2016.
 */
public class InteractiveTile extends GameTile {
    protected Direction interactiveFromNeighbour = Direction.NDEF;
    protected String dialogue;

    public InteractiveTile(Context context, TileType setType, TileForegroundType setForegroundType)
    {
        super(context, setType, setForegroundType);
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
