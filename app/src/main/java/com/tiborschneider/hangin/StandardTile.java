package com.tiborschneider.hangin;

import android.content.Context;

/**
 * Created by Tibor Schneider on 21.06.2016.
 */
public class StandardTile extends GameTile {
    public StandardTile (Context context, TileType setType)
    {
        super(context, setType);
        isInteractive = false;
    }
}
