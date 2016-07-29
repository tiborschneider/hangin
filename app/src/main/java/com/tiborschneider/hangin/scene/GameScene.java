package com.tiborschneider.hangin.scene;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.R;
import com.tiborschneider.hangin.character.Direction;
import com.tiborschneider.hangin.character.Inventory;
import com.tiborschneider.hangin.character.NonPlayerCharacter;
import com.tiborschneider.hangin.character.Player;
import com.tiborschneider.hangin.dialogue.Dialogue;
import com.tiborschneider.hangin.item.Item;
import com.tiborschneider.hangin.item.Lootbox;
import com.tiborschneider.hangin.tile.AnimatedTile;
import com.tiborschneider.hangin.tile.DoorTile;
import com.tiborschneider.hangin.tile.GameTile;
import com.tiborschneider.hangin.tile.InteractiveTile;
import com.tiborschneider.hangin.tile.StandardTile;
import com.tiborschneider.hangin.tile.TileForegroundType;
import com.tiborschneider.hangin.tile.TileType;
import com.tiborschneider.hangin.userInteraction.InterfaceElement;

/**
 * Created by Tibor Schneider on 19.06.2016.
 */
public class GameScene {
    public static int maxNumLootbox = 10;
    private int numLootbox = 0;
    private Context context;
    private GameTile[][] tiles = new GameTile[InterfaceElement.numTiles][InterfaceElement.numTiles];
    private GameJumpHandler gameJumpHandler;
    private Bitmap jumpImage;
    private Lootbox[] lootboxes = new Lootbox[maxNumLootbox];
    private GamePanel gamePanel;
    private static Bitmap sceneImage = null;


    public GameScene(GamePanel aGamePanel, Context aContext)
    {
        gamePanel = aGamePanel;
        context = aContext;
        gameJumpHandler = new GameJumpHandler();
        jumpImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.jump);
        jumpImage = InterfaceElement.resizeImage(jumpImage, InterfaceElement.tileSize, InterfaceElement.tileSize);
        sceneImage = Bitmap.createBitmap(InterfaceElement.tileSize * InterfaceElement.numTiles, InterfaceElement.tileSize * InterfaceElement.numTiles, Bitmap.Config.ARGB_8888);
    }

    public void createNewTile(int x, int y, TileType aType, TileForegroundType aForegroundType, boolean isInteractive)
    {
        if (AnimatedTile.isAnimatedTile(aType)) {
            tiles[x][y] = new AnimatedTile(context, aType, aForegroundType);
        } else if (DoorTile.isDoorTile(aForegroundType)) {
            tiles[x][y] = new DoorTile(context, aType, aForegroundType);
        } else if (isInteractive) {
            tiles[x][y] = new InteractiveTile(context, aType, aForegroundType);
        } else {
            tiles[x][y] = new StandardTile(context, aType, aForegroundType);
        }
    }

    public void changeTile(int x, int y, TileType aType, TileForegroundType aForegroundType)
    {
        tiles[x][y] = new StandardTile(context, aType, aForegroundType);
    }

    public void draw(Canvas canvas, Paint stonedPaint)
    {
        canvas.drawBitmap(sceneImage, InterfaceElement.gameBorderSize, InterfaceElement.statusBarHeight + 2*InterfaceElement.statusBarOuterMargin, null);

        //draw animated Tiles
        for (int x = 0; x < InterfaceElement.numTiles; x++) {
            for (int y = 0; y < InterfaceElement.numTiles; y++) {
                if (DoorTile.isDoorTile(tiles[x][y].getForegroundType())) {
                    canvas.drawBitmap(tiles[x][y].getDoorImage(), x* InterfaceElement.tileSize + InterfaceElement.gameBorderSize, y* InterfaceElement.tileSize + 2*InterfaceElement.statusBarOuterMargin + InterfaceElement.statusBarHeight, stonedPaint);
                } else if (tiles[x][y].isAnimationTile()) {
                    canvas.drawBitmap(tiles[x][y].getAnimationImage(), x* InterfaceElement.tileSize + InterfaceElement.gameBorderSize + tiles[x][y].getOffsetX(), y* InterfaceElement.tileSize + 2*InterfaceElement.statusBarOuterMargin + InterfaceElement.statusBarHeight + tiles[x][y].getOffsetY(), stonedPaint);
                }
            }
        }
    }

    public void drawOnScreenImage(Canvas canvas, Paint stonedPaint)
    {
        for (int ix = 0; ix < InterfaceElement.numTiles; ix++)
        {
            for (int iy = 0; iy < InterfaceElement.numTiles; iy++)
            {
                canvas.drawBitmap(tiles[ix][iy].getBitmap(), ix* InterfaceElement.tileSize, iy* InterfaceElement.tileSize, stonedPaint);
                if (isJumpTile(ix, iy) && !DoorTile.isDoorTile(tiles[ix][iy].getForegroundType()))
                    canvas.drawBitmap(jumpImage, ix* InterfaceElement.tileSize, iy* InterfaceElement.tileSize, stonedPaint);
            }
        }

        Paint shadowPaint = new Paint();
        shadowPaint.setAlpha(99);

        //draw Shadow of houses
        for (int ix = 0; ix < InterfaceElement.numTiles; ix++) {
            for (int iy = 0; iy < InterfaceElement.numTiles; iy++) {
                if (!tiles[ix][iy].isHouseOutside()) {
                    //check for horizontal shadow
                    if (iy < InterfaceElement.numTiles - 1 && tiles[ix][iy + 1].isHouseOutside()) {
                        //draw horizontal shadow
                        Bitmap shadow;
                        if (ix > 0 && !tiles[ix - 1][iy + 1].isHouseOutside()) {
                            shadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_shadow_tl);
                        } else {
                            shadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_shadow_tc);
                        }
                        canvas.drawBitmap(shadow, ix * InterfaceElement.tileSize, iy * InterfaceElement.tileSize, shadowPaint);
                    } else if (ix > 0 && tiles[ix - 1][iy].isHouseOutside()) {
                        //draw vertical shadow
                        Bitmap shadow;
                        if (iy < InterfaceElement.numTiles - 1 && !tiles[ix - 1][iy + 1].isHouseOutside()) {
                            shadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_shadow_rb);
                        } else {
                            shadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_shadow_rc);
                        }
                        canvas.drawBitmap(shadow, ix * InterfaceElement.tileSize, iy * InterfaceElement.tileSize, shadowPaint);
                    } else if (ix > 0 && iy < InterfaceElement.numTiles - 1 && tiles[ix - 1][iy + 1].isHouseOutside()) {
                        //draw corner shadow
                        Bitmap shadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_shadow_tr);
                        canvas.drawBitmap(shadow, ix * InterfaceElement.tileSize, iy * InterfaceElement.tileSize, shadowPaint);
                    }
                }
            }
        }

        //draw Lootboxes
        for (int i = 0; i < numLootbox; i++) {
            lootboxes[i].draw(canvas, stonedPaint);
        }
    }

    public void createSceneImage(Paint stonedPaint) {
        sceneImage = Bitmap.createBitmap(InterfaceElement.tileSize * InterfaceElement.numTiles, InterfaceElement.tileSize * InterfaceElement.numTiles, Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(sceneImage);
        drawOnScreenImage(tempCanvas, stonedPaint);

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
        if (aX >= 0 && aX < InterfaceElement.numTiles && aY >= 0 && aY < InterfaceElement.numTiles) {
            return tiles[aX][aY].canWalkUp();
        } else return false;
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

    public boolean addDialogueToTile(int aX, int aY, String aDialogue)
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

    public boolean isLootboxInView(int x, int y, Direction aDir)
    {
        switch (aDir) {
            case UP:
                if (y <= 0)
                    return false;
                if (!tiles[x][y].canWalkUp() || !tiles[x][y-1].canWalkDown())
                    return false;
                y--;
                break;
            case DOWN:
                if (y >= InterfaceElement.numTiles - 1)
                    return false;
                if (!tiles[x][y].canWalkDown() || !tiles[x][y+1].canWalkUp())
                    return false;
                y++;
                break;
            case LEFT:
                if (x <= 0)
                    return false;
                if (!tiles[x][y].canWalkLeft() || !tiles[x-1][y].canWalkRight())
                    return false;
                x--;
                break;
            case RIGHT:
                if (x >= InterfaceElement.numTiles - 1)
                    return false;
                if (!tiles[x][y].canWalkRight() || !tiles[x+1][y].canWalkLeft())
                    return false;
                x++;
                break;
        }
        return checkLootbox(x, y);
    }

    public boolean isNpcInView(int x, int y, Direction aDir)
    {
        switch (aDir) {
            case UP:
                if (y <= 0)
                    return false;
                if (!tiles[x][y].canWalkUp() || !tiles[x][y-1].canWalkDown())
                    return false;
                y--;
                break;
            case DOWN:
                if (y >= InterfaceElement.numTiles - 1)
                    return false;
                if (!tiles[x][y].canWalkDown() || !tiles[x][y+1].canWalkUp())
                    return false;
                y++;
                break;
            case LEFT:
                if (x <= 0)
                    return false;
                if (!tiles[x][y].canWalkLeft() || !tiles[x-1][y].canWalkRight())
                    return false;
                x--;
                break;
            case RIGHT:
                if (x >= InterfaceElement.numTiles - 1)
                    return false;
                if (!tiles[x][y].canWalkRight() || !tiles[x+1][y].canWalkLeft())
                    return false;
                x++;
                break;
            default:
                return false;
        }
        return checkNpc(x, y);
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
            default:
                return null;
        }
        return getNpc(aX, aY);
    }

    public void update() {
        for (int x = 0; x < InterfaceElement.numTiles; x++) {
            for (int y = 0; y < InterfaceElement.numTiles; y++) {
                tiles[x][y].update();
            }
        }
        Player player = GamePanel.getGamePanel().getPlayer();
        if (DoorTile.isDoorTile(tiles[player.getX()][player.getY()].getForegroundType())) {
            tiles[player.getX()][player.getY()].openDoor();
        } else if (player.getY() > 0 && DoorTile.isDoorTile(tiles[player.getX()][player.getY()-1].getForegroundType())) {
            tiles[player.getX()][player.getY()-1].closeDoor();
        }
    }

    public NonPlayerCharacter getNpc(int aX, int aY)
    {
        return gamePanel.getNpc(this, aX, aY);
    }

    public boolean isInteractiveInView(int x, int y, Direction direction) {
        switch (direction) {
            case UP:
                if (y <= 0)
                    return false;
                if (!tiles[x][y].canWalkUp() || !tiles[x][y-1].canWalkDown())
                    return false;
                y--;
                break;
            case DOWN:
                if (y >= InterfaceElement.numTiles - 1)
                    return false;
                if (!tiles[x][y].canWalkDown() || !tiles[x][y+1].canWalkUp())
                    return false;
                y++;
                break;
            case LEFT:
                if (x <= 0)
                    return false;
                if (!tiles[x][y].canWalkLeft() || !tiles[x-1][y].canWalkRight())
                    return false;
                x--;
                break;
            case RIGHT:
                if (x >= InterfaceElement.numTiles - 1)
                    return false;
                if (!tiles[x][y].canWalkRight() || !tiles[x+1][y].canWalkLeft())
                    return false;
                x++;
                break;
            default:
                return false;
        }
        return isInteractive(x, y);
    }

    public Dialogue getDialogueFromTileInView(int x, int y, Direction direction) {
        switch (direction) {
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
            default:
                return null;
        }
        return getDialogueFromTile(x, y);
    }
}
