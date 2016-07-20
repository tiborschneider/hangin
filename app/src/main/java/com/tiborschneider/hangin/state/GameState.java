package com.tiborschneider.hangin.state;

/**
 * Created by Tibor Schneider on 26.06.2016.
 */
public class GameState {
    public String name;
    public int value;
    public boolean isSpecial = false;

    public GameState()
    {
        //do nothing
    }

    public GameState(String aName, int aValue)
    {
        name = aName;
        value = aValue;
    }

    public GameState(String aName, int aValue, boolean isSpecial)
    {
        name = aName;
        value = aValue;
        this.isSpecial = isSpecial;
    }
}
