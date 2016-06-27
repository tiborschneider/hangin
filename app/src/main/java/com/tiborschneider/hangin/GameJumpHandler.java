package com.tiborschneider.hangin;

/**
 * Created by Tibor Schneider on 21.06.2016.
 */
public class GameJumpHandler {
    public static int maxJumps = 10;
    private GameJump[] jumps = new GameJump[maxJumps];
    private int jumpCounter = 0;
    public static boolean jumpsAllowed = true;

    public GameJumpHandler () {}

    public boolean addNewJump(int aSceneIndexTarget, int aXOrigin, int aYOrigin, int aXTarget, int aYTarget)
    {
        if (jumpCounter < maxJumps) {
            jumps[jumpCounter] = new GameJump(aSceneIndexTarget, aXOrigin, aYOrigin, aXTarget, aYTarget);
            jumpCounter++;
        } else
            return false;
        return true;
    }

    public boolean isJumpTile(int aX, int aY)
    {
        for (int i = 0; i < jumpCounter; i++)
        {
            if (jumps[i].getXOrigin() == aX && jumps[i].getYOrigin() == aY)
                return true;
        }
        return false;
    }

    public int getTargetScene(int aX, int aY)
    {
        for (int i = 0; i < jumpCounter; i++)
        {
            if (jumps[i].getXOrigin() == aX && jumps[i].getYOrigin() == aY)
                return jumps[i].getTargetScene();
        }
        return -1;
    }

    public int getXTarget(int aX, int aY)
    {
        for (int i = 0; i < jumpCounter; i++)
        {
            if (jumps[i].getXOrigin() == aX && jumps[i].getYOrigin() == aY)
                return jumps[i].getXTarget();
        }
        return -1;
    }

    public int getYTarget(int aX, int aY)
    {
        for (int i = 0; i < jumpCounter; i++)
        {
            if (jumps[i].getXOrigin() == aX && jumps[i].getYOrigin() == aY)
                return jumps[i].getYTarget();
        }
        return -1;
    }
}
