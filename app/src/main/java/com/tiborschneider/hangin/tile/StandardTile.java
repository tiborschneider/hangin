package com.tiborschneider.hangin.tile;

import android.content.Context;

/**
 * Created by Tibor Schneider on 21.06.2016.
 */
public class StandardTile extends GameTile {
    public StandardTile (Context context, TileType aType, TileForegroundType aForegroundType)
    {
        super(context, aType, aForegroundType);
        isInteractive = false;
    }
}
