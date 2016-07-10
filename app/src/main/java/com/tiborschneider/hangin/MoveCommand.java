package com.tiborschneider.hangin;

/**
 * Created by tibor on 07.07.16.
 */
public class MoveCommand {
    private boolean isJump = false;
    private Direction direction = Direction.NDEF;
    private int jumpToScene = -1;
    private int jumpToX = -1;
    private int jumpToY = -1;

    public MoveCommand(Direction direction) {
        this.direction = direction;
    }

    public MoveCommand(int scene, int x, int y) {
        this.jumpToScene = scene;
        this.jumpToX = x;
        this.jumpToY = y;
        this.isJump = true;
    }

    public boolean isJump() {
        return isJump;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getJumpToScene() {
        return jumpToScene;
    }

    public int getJumpToX() {
        return jumpToX;
    }

    public int getJumpToY() {
        return jumpToY;
    }
}
