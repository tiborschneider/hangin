package com.tiborschneider.hangin;

import android.content.Context;

/**
 * Created by Tibor Schneider on 22.06.2016.
 */
public class Dialogue {
    private int currentText = 0;
    private int numText = 0;
    private int numReplies = 0;
    public static int maxNumText = 20;
    public static int maxNumReplies = 4;
    private String[][] text = new String[maxNumText][2];
    private Dialogue[] followingDialogue = new Dialogue[maxNumReplies];
    private String[] replyText = new String[maxNumReplies];
    private Action[] replyAction = new Action[maxNumReplies];
    private String[] replySetState = new String[maxNumReplies];
    private int[] replySetValue = new int[maxNumReplies];
    private DialoguePosition dialoguePosition;
    private GamePanel gamePanel;

    public Dialogue(GamePanel aGamePanel, DialoguePosition aDialoguePosition) {
        dialoguePosition = aDialoguePosition;
        gamePanel = aGamePanel;
    }

    public boolean addTextElement(String line1, String line2)
    {
        if (numText < maxNumText) {
            text[numText][0] = line1;
            text[numText][1] = line2;
            numText++;
            return true;
        } else {
            return false;
        }
    }

    public String[] getNextText()
    {
        return text[currentText++];
    }

    public boolean isFinished()
    {
        if (currentText == numText)
        {
            resetDialogue();
            return true;
        } else {
            return false;
        }
    }

    public boolean mustSelectOption()
    {
        return (currentText == numText && numReplies > 0);
    }

    public int getNumReplies()
    {
        return numReplies;
    }

    public void resetDialogue()
    {
        currentText = 0;
    }

    public DialoguePosition getDialoguePosition() { return dialoguePosition; }

    public int getReplyBackgroundId(Context context) {
        if (numReplies > 0) {
            String pos;
            if (dialoguePosition == DialoguePosition.BOTTOM)
                pos = "bot";
            else
                pos = "top";
            String imageName = "ui_dialogue_reply_" + numReplies + "_" + pos;
            return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        } else {
            return 0;
        }
    }

    public String[] getReplyText()
    {
        String[] ret = new String[numReplies];
        for (int i = 0; i < numReplies; i++)
            ret[i] = replyText[i];
        return ret;
    }

    public Dialogue getNextDialogue(int i)
    {
        return followingDialogue[i];
    }

    public boolean addReply(String aReplyText, Dialogue aDialogue, String aReplyAction, String aState, int aValue)
    {
        if (numReplies < (maxNumReplies-1)) {
            replyText[numReplies] = aReplyText;
            followingDialogue[numReplies] = aDialogue;
            replyAction[numReplies] = new Action(gamePanel, aReplyAction);
            replySetState[numReplies] = aState;
            replySetValue[numReplies] = aValue;
            numReplies++;
            return true;
        } else {
            return false;
        }
    }

    public void runAction(int index)
    {
        replyAction[index].run();
    }

    public int getNumText()
    {
        return numText;
    }

    public String getStateNameToChange(int index) {
        return replySetState[index];
    }

    public int getStateValueToChange(int index) {
        return replySetValue[index];
    }
}
