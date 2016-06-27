package com.tiborschneider.hangin;

import android.content.Context;

/**
 * Created by Tibor Schneider on 22.06.2016.
 */
public class InteractiveTile extends GameTile{
    private Direction interactiveFromNeighbour = Direction.NDEF;
    private Dialogue dialogue;

    public InteractiveTile(Context context, TileType setType)
    {
        super(context, setType);
        isInteractive = true;
    }

    @Override public boolean setDialogue(Dialogue aDialogue)
    {
        dialogue = aDialogue;
        return true;
    }

    @Override public Dialogue getDialogue()
    {
        return dialogue;
    }
}
