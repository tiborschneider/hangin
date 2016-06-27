package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Tibor Schneider on 19.06.2016.
 */
public class GameScene {
    public static int maxNumLootbox = 10;
    private int numLootbox = 0;
    private Context context;
    private GameTile[][] tiles = new GameTile[InterfaceElement.numTiles][InterfaceElement.numTiles];;
    private GameJumpHandler gameJumpHandler;
    private Bitmap jumpImage;
    private Lootbox[] lootboxes = new Lootbox[maxNumLootbox];
    private GamePanel gamePanel;


    public GameScene(GamePanel aGamePanel, Context aContext)
    {
        gamePanel = aGamePanel;
        context = aContext;
        gameJumpHandler = new GameJumpHandler();
        jumpImage = BitmapFactory.decodeResource(context.getResources(),R.drawable.jump);
    }

    public void createNewTile(int x, int y, TileType setType, boolean isInteractive)
    {
        if (isInteractive) {
            tiles[x][y] = new InteractiveTile(context, setType);
        }
        else {
            tiles[x][y] = new StandardTile(context, setType);
        }
    }

    public void changeTile(int x, int y, TileType setType)
    {
        tiles[x][y] = new StandardTile(context, setType);
    }

    public void draw(Canvas canvas)
    {
        for (int ix = 0; ix < 16; ix++)
        {
            for (int iy = 0; iy < 16; iy++)
            {
                canvas.drawBitmap(tiles[ix][iy].getBitmap(), ix* InterfaceElement.tileSize + InterfaceElement.gameBorderSize, iy* InterfaceElement.tileSize + InterfaceElement.gameBorderSize, null);
                if (isJumpTile(ix, iy))
                    canvas.drawBitmap(jumpImage, ix* InterfaceElement.tileSize + InterfaceElement.gameBorderSize, iy* InterfaceElement.tileSize + InterfaceElement.gameBorderSize, null);
            }
        }

        //draw Lootboxes
        for (int i = 0; i < numLootbox; i++) {
            lootboxes[i].draw(canvas);
        }
    }

    public boolean isWalkable(int aX, int aY)
    {
        boolean ret = false;
        if (aX >= 0 && aX < InterfaceElement.numTiles && aY >= 0 && aY < InterfaceElement.numTiles) {
            ret = tiles[aX][aY].isWalkable();
        }
        for (int i = 0; i < numLootbox; i++) {
            if (lootboxes[i] != null && lootboxes[i].getX() == aX && lootboxes[i].getY() == aY)
                ret = false;
        }
        if (gamePanel.getNpc(this, aX, aY) != null)
            ret = false;
        return ret;
    }

    public boolean isInteractive(int aX, int aY)
    {
        if (aX >= 0 && aX < InterfaceElement.numTiles && aY >= 0 && aY < InterfaceElement.numTiles)
            return (tiles[aX][aY].isInteractive());
        else return false;
    }

    public boolean canWalkUp(int aX, int aY)
    {
        if (aX >= 0 && aX < InterfaceElement.numTiles && aY >= 0 && aY < InterfaceElement.numTiles)
            return tiles[aX][aY].canWalkUp();
        else return false;
    }

    public boolean canWalkDown(int aX, int aY)
    {
        if (aX >= 0 && aX < InterfaceElement.numTiles && aY >= 0 && aY < InterfaceElement.numTiles)
            return tiles[aX][aY].canWalkDown();
        else return false;
    }

    public boolean canWalkLeft(int aX, int aY)
    {
        if (aX >= 0 && aX < InterfaceElement.numTiles && aY >= 0 && aY < InterfaceElement.numTiles)
            return tiles[aX][aY].canWalkLeft();
        else return false;
    }

    public boolean canWalkRight(int aX, int aY)
    {
        if (aX >= 0 && aX < InterfaceElement.numTiles && aY >= 0 && aY < InterfaceElement.numTiles)
            return tiles[aX][aY].canWalkRight();
        else return false;
    }

    public boolean addNewJump(int aSceneIndexTarget, int aXOrigin, int aYOrigin, int aXTarget, int aYTarget)
    {
        return gameJumpHandler.addNewJump(aSceneIndexTarget, aXOrigin, aYOrigin, aXTarget, aYTarget);
    }

    public boolean isJumpTile(int aX, int aY)
    {
        return gameJumpHandler.isJumpTile(aX, aY);
    }

    public int getTargetScene(int aX, int aY)
    {
        return gameJumpHandler.getTargetScene(aX, aY);
    }

    public int getXTarget(int aX, int aY)
    {
        return gameJumpHandler.getXTarget(aX, aY);
    }

    public int getYTarget(int aX, int aY)
    {
        return gameJumpHandler.getYTarget(aX, aY);
    }

    public boolean addDialogueToTile(int aX, int aY, Dialogue aDialogue)
    {
        return tiles[aX][aY].setDialogue(aDialogue);
    }

    public Dialogue getDialogueFromTile(int aX, int aY)
    {
        return tiles[aX][aY].getDialogue();
    }

    public boolean addLootbox(Lootbox aLootbox)
    {
        if (numLootbox < maxNumLootbox) {
            lootboxes[numLootbox++] = aLootbox;
            return true;
        } else {
            return false;
        }
    }

    public boolean checkLootbox(int aX, int aY)
    {
        for (int i = 0; i < numLootbox; i++)
            if (lootboxes[i] != null && lootboxes[i].getX() == aX && lootboxes[i].getY() == aY)
                return true;
        return false;
    }

    public Lootbox getLootbox(int aX, int aY)
    {
        for (int i = 0; i < numLootbox; i++)
            if (lootboxes[i] != null && lootboxes[i].getX() == aX && lootboxes[i].getY() == aY)
                return lootboxes[i];
        return null;
    }

    public int getLootboxItemNum(int aX, int aY)
    {
        for (int i = 0; i < numLootbox; i++)
            if (lootboxes[i] != null && lootboxes[i].getX() == aX && lootboxes[i].getY() == aY)
                return lootboxes[i].getNumItems();
        return 0;
    }

    public Item[] getLootboxContent(int aX, int aY)
    {
        for (int i = 0; i < numLootbox; i++)
            if (lootboxes[i] != null && lootboxes[i].getX() == aX && lootboxes[i].getY() == aY)
                return lootboxes[i].getItems();
        return null;
    }

    public boolean addLootboxToInventory(Inventory inventory, int aX, int aY)
    {
        for (int i = 0; i < numLootbox; i++)
            if (lootboxes[i] != null && lootboxes[i].getX() == aX && lootboxes[i].getY() == aY)
                return lootboxes[i].addItemsToInventory(inventory);
        return false;
    }

    public boolean deleteLootbox(int aX, int aY)
    {
        int freeIndex = maxNumLootbox;
        for (int i = 0; i < numLootbox; i++) {
            if (lootboxes[i] != null && lootboxes[i].getX() == aX && lootboxes[i].getY() == aY) {
                lootboxes[i] = null;
                freeIndex = i;
                numLootbox--;
            }
        }
        for (int i = freeIndex + 1; i < maxNumLootbox; i++) {
            lootboxes[i-1] = lootboxes[i];
        }

        return false;
    }

    public boolean isLootboxInView(int aX, int aY, Direction aDir)
    {
        switch (aDir) {
            case UP:
                aY--;
                break;
            case DOWN:
                aY++;
                break;
            case LEFT:
                aX--;
                break;
            case RIGHT:
                aX++;
                break;
            case NDEF:
                return false;
        }
        return checkLootbox(aX, aY);
    }

    public boolean isNpcInView(int aX, int aY, Direction aDir)
    {
        switch (aDir) {
            case UP:
                aY--;
                break;
            case DOWN:
                aY++;
                break;
            case LEFT:
                aX--;
                break;
            case RIGHT:
                aX++;
                break;
            case NDEF:
                return false;
        }
        return checkNpc(aX, aY);
    }

    public boolean checkNpc(int aX, int aY)
    {
        return (gamePanel.getNpc(this, aX, aY) != null);
    }

    public NonPlayerCharacter getNpc(int aX, int aY, Direction aDir)
    {
        switch (aDir) {
            case UP:
                aY--;
                break;
            case DOWN:
                aY++;
                break;
            case LEFT:
                aX--;
                break;
            case RIGHT:
                aX++;
                break;
            case NDEF:
                return null;
        }
        return getNpc(aX, aY);
    }

    public NonPlayerCharacter getNpc(int aX, int aY)
    {
        return gamePanel.getNpc(this, aX, aY);
    }
}
