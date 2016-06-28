package com.tiborschneider.hangin;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Tibor Schneider on 22.06.2016.
 */
public class InteractionHandler {
    private Context context;
    private Player player;
    private Controller controller;
    public static boolean interfaceActive = false;
    public static boolean interfaceSelectionActive = false;
    private InterfaceDialogue interfaceDialogue;
    private InterfaceLootbox interfaceLootbox;
    private InterfaceInventory interfaceInventory;
    private MainThread thread;
    private GamePanel gamePanel;

    public InteractionHandler(Context aContext, Player aPlayer, MainThread aThread, GamePanel aGamePanel)
    {
        gamePanel = aGamePanel;
        controller = new Controller(aContext, aPlayer, this);
        context = aContext;
        player = aPlayer;
        thread = aThread;
    }

    public void onButtonPress(Button button)
    {
        if (!interfaceActive)
        {
            //currently in normal mode
            switch (button)
            {
                case LEFT:
                    player.walk(Direction.LEFT);
                    break;
                case UP:
                    player.walk(Direction.UP);
                    break;
                case RIGHT:
                    player.walk(Direction.RIGHT);
                    break;
                case DOWN:
                    player.walk(Direction.DOWN);
                    break;
                case INTERACT:
                    interact();
                    break;
                case INVENTORY:
                    startInventory();
                    break;
                case DROP:
                    useEquippedItem();
                    break;
            }
        } else if (interfaceDialogue != null) {
            //currently displaying a dialogue
            if (interfaceSelectionActive) {
                switch (button) {
                    case UP:
                        interfaceDialogue.currentSelectionUp();
                        break;
                    case DOWN:
                        interfaceDialogue.currentSelectionDown();
                        break;
                    case INTERACT:
                        selectReply();
                        break;
                }
            } else {
                switch (button) {
                    case INTERACT:
                        nextDialogueText();
                        break;
                }
            }
        } else if (interfaceLootbox != null) {
            switch (button) {
                case INTERACT:
                    closeLootbox();
                    break;
            }
        } else if (interfaceInventory != null) {
            switch (button) {
                case UP:
                    interfaceInventory.moveSelectionUp();
                    break;
                case DOWN:
                    interfaceInventory.moveSelectionDown();
                    break;
                case LEFT:
                    interfaceInventory.moveSelectionLeft();
                    break;
                case RIGHT:
                    interfaceInventory.moveSelectionRight();
                    break;
                case DROP:
                    closeInventory();
                    break;
                case INTERACT:
                    interfaceInventory.selectItem();
                    closeInventory();
                    break;
            }
        }
    }

    public void drawController(Canvas aCanvas)
    {
        controller.draw(aCanvas);
    }

    public void drawInterface(Canvas canvas)
    {
        if (interfaceActive)
        {
            if (interfaceDialogue != null) {
                interfaceDialogue.draw(canvas);
            } else if (interfaceLootbox != null) {
                interfaceLootbox.draw(canvas);
            } else if (interfaceInventory != null) {
                interfaceInventory.draw(canvas);
            }

        }
    }

    public boolean onTouch(MotionEvent e)
    {
        return controller.onTouch(e);
    }

    private void interact()
    {
        GameScene scene = gamePanel.getScene();
        if (scene.isInteractive(player.getX(), player.getY())) {
            createDialogue(scene.getDialogueFromTile(player.getX(), player.getY()));
        }
        if (scene.isLootboxInView(player.getX(), player.getY(), player.getDirection())) {
            openLootbox(scene);
        }
        if(scene.isNpcInView(player.getX(), player.getY(), player.getDirection())) {
            NonPlayerCharacter npc = scene.getNpc(player.getX(), player.getY(), player.getDirection());
            Direction newNpcDirection = Direction.NDEF;
            switch(player.getDirection())
            {
                case UP:
                    newNpcDirection = Direction.DOWN;
                    break;
                case DOWN:
                    newNpcDirection = Direction.UP;
                    break;
                case LEFT:
                    newNpcDirection = Direction.RIGHT;
                    break;
                case RIGHT:
                    newNpcDirection = Direction.LEFT;
                    break;
            }
            npc.turn(newNpcDirection);
            createDialogue(npc.getCurrentDialogue());
        }
    }

    public void createDialogue(Dialogue aDialogue)
    {
        gamePanel.redrawScene();
        interfaceDialogue = new InterfaceDialogue(gamePanel, context, aDialogue);
        interfaceActive = true;
        if (interfaceDialogue.mustSelectOption()) {
            interfaceSelectionActive = true;
        }
    }

    private void nextDialogueText()
    {
        if(!interfaceDialogue.nextText()) {
            interfaceDialogue = null;
            controller.setWaitForInteractionRelease(false);
            interfaceActive = false;
            gamePanel.redrawScene();
        } else if (interfaceDialogue.mustSelectOption()) {
            interfaceSelectionActive = true;
            gamePanel.redrawScene();
        }
    }

    private void selectReply()
    {
        interfaceSelectionActive = false;
        interfaceDialogue.selectReply();
        if (interfaceDialogue.mustSelectOption()) {
            interfaceSelectionActive = true;
        }
        gamePanel.redrawScene();
    }

    public Dialogue getDialogueFromDatabase(String dialogueName)
    {
        return gamePanel.getDatabaseHelper().getDialogueFromDB(dialogueName);
    }

    private void openLootbox(GameScene scene)
    {
        int tmpX = player.getX();
        int tmpY = player.getY();
        switch (player.getDirection()) {
            case UP:
                tmpY--;
                break;
            case DOWN:
                tmpY++;
                break;
            case LEFT:
                tmpX--;
                break;
            case RIGHT:
                tmpY++;
                break;
        }
        if (scene.checkLootbox(tmpX, tmpY)) {
            interfaceLootbox = new InterfaceLootbox(context, scene.getLootbox(tmpX, tmpY));
            interfaceActive = true;
        }
    }

    private void closeLootbox()
    {
        GameScene scene = gamePanel.getScene();
        int tmpX = player.getX();
        int tmpY = player.getY();
        switch (player.getDirection()) {
            case UP:
                tmpY--;
                break;
            case DOWN:
                tmpY++;
                break;
            case LEFT:
                tmpX--;
                break;
            case RIGHT:
                tmpY++;
                break;
        }
        if (scene.checkLootbox(tmpX, tmpY)) {
            scene.addLootboxToInventory(player.getInventory(), tmpX, tmpY);
            scene.deleteLootbox(tmpX, tmpY);
            gamePanel.getStateHandler().deleteLootbox(tmpX, tmpY);
            interfaceLootbox = null;
            controller.setWaitForInteractionRelease(false);
            interfaceActive = false;
            gamePanel.redrawScene();
        }
    }

    private void startInventory()
    {
        if (!interfaceActive) {
            interfaceInventory = new InterfaceInventory(context, player.getInventory());
            interfaceActive = true;
        }
    }

    private void closeInventory()
    {
        interfaceInventory = null;
        controller.setWaitForInteractionRelease(false);
        interfaceActive = false;
        gamePanel.redrawScene();
    }

    private void useEquippedItem()
    {
        if (player.getEquippedItem() != null)
            player.getEquippedItem().use(this);
    }

    public void closeAllWindows()
    {
        interfaceInventory = null;
        interfaceLootbox = null;
        interfaceDialogue = null;
        interfaceActive = false;
        interfaceSelectionActive = false;
        controller.setWaitForInteractionRelease(false);
    }
}
