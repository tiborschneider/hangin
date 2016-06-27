package com.tiborschneider.hangin;

/**
 * Created by Tibor Schneider on 21.06.2016.
 */
public class GameJump {
    private int sceneIndexTarget;
    private int xOrigin;
    private int yOrigin;
    private int xTarget;
    private int yTarget;

    public GameJump(int aSceneIndexTarget, int aXOrigin, int aYOrigin, int aXTarget, int aYTarget)
    {
        sceneIndexTarget = aSceneIndexTarget;
        xOrigin = aXOrigin;
        yOrigin = aYOrigin;
        xTarget = aXTarget;
        yTarget = aYTarget;
    }

    public int getTargetScene() { return sceneIndexTarget; }
    public int getXOrigin() { return xOrigin; }
    public int getYOrigin() { return yOrigin; }
    public int getXTarget() { return xTarget; }
    public int getYTarget() { return yTarget; }
}
