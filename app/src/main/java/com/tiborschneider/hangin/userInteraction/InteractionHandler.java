package com.tiborschneider.hangin.userInteraction;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.tiborschneider.hangin.item.Lootbox;
import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.scene.GameScene;
import com.tiborschneider.hangin.mainGame.MainThread;
import com.tiborschneider.hangin.character.Direction;
import com.tiborschneider.hangin.character.NonPlayerCharacter;
import com.tiborschneider.hangin.character.Player;
import com.tiborschneider.hangin.dialogue.Dialogue;
import com.tiborschneider.hangin.dialogue.DialogueQueue;
import com.tiborschneider.hangin.state.QuestHandler;

/**
 * Created by Tibor Schneider on 22.06.2016.
 */
public class InteractionHandler {
    private Context context;
    private Player player;
    private Controller controller;
    public static boolean interfaceActive = false;
    public static boolean interfaceSelectionActive = false;
    public static boolean multipleInterfacesActive = false;
    private InterfaceDialogue interfaceDialogue;
    private InterfaceLootbox interfaceLootbox;
    private InterfaceInventory interfaceInventory;
    private InterfaceStatusBar interfaceStatusBar;
    private InterfaceQuestSelection interfaceQuestSelection;
    private InterfaceQuestInfo interfaceQuestInfo;
    private MainThread thread;
    private GamePanel gamePanel;
    private DialogueQueue dialogueQueue;
    private Lootbox queueLootbox = null;
    private QuestHandler questHandler;

    public InteractionHandler(Context aContext, Player aPlayer, MainThread aThread, GamePanel aGamePanel, QuestHandler questHandler)
    {
        gamePanel = aGamePanel;
        controller = new Controller(aContext, aPlayer, this);
        context = aContext;
        player = aPlayer;
        thread = aThread;
        dialogueQueue = new DialogueQueue();
        interfaceStatusBar = new InterfaceStatusBar(context, player);
        this.questHandler = questHandler;
    }

    public boolean isInterfaceActive() {
        return interfaceActive;
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
                case QUESTS:
                    System.out.println("Open Quest UI");
                    openQuestSelection();
                    break;
            }
        } else if (interfaceDialogue != null) {
            //currently displaying a dialogue
            System.out.println("Button Pressed and Interface Dialogue active: " + button.name());
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
                    interfaceInventory.selectItem();
                    useEquippedItem();
                    break;
                case INTERACT:
                    interfaceInventory.selectItem();
                    closeInventory();
                    break;
                case INVENTORY:
                    closeInventory();
                    break;
            }

        } else if (interfaceLootbox != null) {
            switch (button) {
                case INTERACT:
                    closeLootbox();
                    break;
            }
        } else if (interfaceQuestInfo != null) {
            switch(button) {
                case UP:
                    interfaceQuestInfo.moveSelectionUp();
                    break;
                case DOWN:
                    interfaceQuestInfo.moveSelectionDown();
                    break;
                case DROP:
                case INVENTORY:
                case QUESTS:
                    closeQuestInfo();
                    break;
            }
        } else if (interfaceQuestSelection != null) {
            switch (button) {
                case UP:
                    interfaceQuestSelection.moveSelectionUp();
                    break;
                case DOWN:
                    interfaceQuestSelection.moveSelectionDown();
                    break;
                case INTERACT:
                    selectQuest();
                    break;
                case DROP:
                case INVENTORY:
                case QUESTS:
                    closeQuest();
                    break;
            }
        }
    }

    private void closeQuestInfo() {
        interfaceQuestInfo = null;
        interfaceQuestSelection.updateQuestStatus();
        //gamePanel.redrawScene();
    }

    private void openQuestSelection() {
        if (!interfaceActive) {
            questHandler.sortAll();
            interfaceQuestSelection = new InterfaceQuestSelection(context);
            interfaceActive = true;
        }
    }

    private void selectQuest() {
        interfaceQuestSelection.getSelectedQuest().setNormal();
        interfaceQuestInfo = new InterfaceQuestInfo(context, interfaceQuestSelection.getSelectedQuest());
        controller.setWaitForPressRelease(false);
        //gamePanel.redrawScene();
    }

    private void closeQuest() {
        interfaceQuestSelection = null;
        controller.setWaitForPressRelease(false);
        interfaceActive = false;
        gamePanel.redrawScene();
    }

    public void drawController(Canvas canvas)
    {
        interfaceStatusBar.draw(canvas);
        controller.draw(canvas);
    }

    public void drawInterface(Canvas canvas)
    {
        if (interfaceActive)
        {
            if (interfaceInventory != null)
                interfaceInventory.draw(canvas);
            if (interfaceDialogue != null)
                interfaceDialogue.draw(canvas);
            if (interfaceLootbox != null)
                interfaceLootbox.draw(canvas);
            if (interfaceQuestSelection != null && interfaceQuestInfo == null)
                interfaceQuestSelection.draw(canvas);
            else if (interfaceQuestInfo != null)
                interfaceQuestInfo.draw(canvas);

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
        if (scene.isInteractiveInView(player.getX(), player.getY(), player.getDirection())) {
            createDialogue(scene.getDialogueFromTileInView(player.getX(), player.getY(), player.getDirection()));
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
        //System.out.println("Dialogue: Replies: " + aDialogue.getNumReplies());
        gamePanel.redrawScene();
        interfaceDialogue = new InterfaceDialogue(gamePanel, context, aDialogue);
        if (interfaceActive) {
            multipleInterfacesActive = true;
            //System.out.println("multiple interfaces active");
        } else {
            interfaceActive = true;
        }
        if (interfaceDialogue.mustSelectOption()) {
            interfaceSelectionActive = true;
        }
    }

    private void nextDialogueText()
    {
        if(!interfaceDialogue.nextText()) {
            interfaceDialogue = null;
            controller.setWaitForPressRelease(false);
            if (multipleInterfacesActive) {
                multipleInterfacesActive = false;
            } else {
                interfaceActive = false;
            }

            if (!checkQueue() && queueLootbox != null)
                dequeueLootbox();

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
        gamePanel.getStateHandler().updateDatabaseInventory();
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
                tmpX++;
                break;
        }
        if (scene.checkLootbox(tmpX, tmpY)) {
            Lootbox lootbox = scene.getLootbox(tmpX, tmpY);
            if (!lootbox.isVisible()) {
                createDialogue(gamePanel.getDatabaseHelper().getDialogueFromDB("hiddenLootboxFound"));
                queueLootbox(lootbox);
            } else {
                interfaceLootbox = new InterfaceLootbox(context, lootbox);
                interfaceActive = true;
            }
        }
    }

    private void queueLootbox(Lootbox lootbox) {
        queueLootbox = lootbox;
    }

    private void dequeueLootbox() {
        if (queueLootbox != null) {
            interfaceLootbox = new InterfaceLootbox(context, queueLootbox);
            queueLootbox = null;
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
                tmpX++;
                break;
        }
        if (scene.checkLootbox(tmpX, tmpY)) {
            scene.addLootboxToInventory(player.getInventory(), tmpX, tmpY);
            scene.deleteLootbox(tmpX, tmpY);
            gamePanel.getStateHandler().deleteLootbox(tmpX, tmpY);
            interfaceLootbox = null;
            controller.setWaitForPressRelease(false);
            interfaceActive = false;
            gamePanel.redrawScene();
        }
        interfaceLootbox = null;
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
        controller.setWaitForPressRelease(false);
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
        dialogueQueue.clear();
        controller.setWaitForPressRelease(false);
    }

    public void overrideDialogue(Dialogue dialogue)
    {
        if(!interfaceActive || interfaceDialogue == null)
            return; //do not allow to override Dialogue when no dialogue is active
        interfaceActive = false;
        interfaceDialogue = null;
        createDialogue(dialogue);
    }

    public void queueDialogue(Dialogue dialogue)
    {
        dialogueQueue.enqueue(dialogue);
    }

    private boolean checkQueue()
    {
        if (!dialogueQueue.isEmpty()) {
            createDialogue(dialogueQueue.getNext());
            return true;
        }
        return false;
    }

    public void saveDialogue() {
        if (interfaceDialogue != null) {
            gamePanel.getDatabaseHelper().saveDialogue(interfaceDialogue.getDialogue());
        }
    }

    public void restoreSavedDialogue() {
        if (gamePanel.getDatabaseHelper().isDialogueSaved())
            this.createDialogue(gamePanel.getDatabaseHelper().getSavedDialogue());
    }

    public void update() {
        if (interfaceQuestInfo != null) {
            interfaceQuestInfo.update();
        }
    }

    public boolean isDialogueActive() {
        return (interfaceDialogue != null);
    }
}
