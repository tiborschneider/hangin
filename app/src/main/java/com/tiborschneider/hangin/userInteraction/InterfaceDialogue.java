package com.tiborschneider.hangin.userInteraction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;

import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.R;
import com.tiborschneider.hangin.dialogue.Dialogue;
import com.tiborschneider.hangin.dialogue.DialoguePosition;
import com.tiborschneider.hangin.state.GameState;

/**
 * Created by Tibor Schneider on 22.06.2016.
 */
public class InterfaceDialogue extends InterfaceElement {
    private Dialogue dialogue;
    private int xPosTextLine1;
    private int yPosTextLine1;
    private int xPosTextLine2;
    private int yPosTextLine2;
    private int numReplies;
    private int dialogueReplyHeight;
    private int xPosReply;
    private int yPosReply;
    private int xPosReplyText;
    private int[] yPosReplyText = new int[Dialogue.maxNumReplies];
    private int xPosArrow;
    private int[] yPosArrow = new int[Dialogue.maxNumReplies];
    private String[] currentText = new String[2];
    private String[] replyText = new String[Dialogue.maxNumReplies];
    private Context context;
    private GamePanel gamePanel;
    private int currentSelectionIndex = 0;
    private Bitmap selectionBackground;
    private Bitmap selectionArrow;



    public InterfaceDialogue(GamePanel aGamePanel, Context aContext, Dialogue aDialogue)
    {
        context = aContext;
        gamePanel = aGamePanel;
        dialogue = aDialogue;
        x = (GamePanel.screenWidth - dialogueWidth) / 2;
        switch (dialogue.getDialoguePosition()) {
            case TOP:
                y = 2*statusBarOuterMargin + statusBarHeight - gameBorderSize + x;
                break;
            case BOTTOM:
                y = 2*statusBarOuterMargin + statusBarHeight + gameBorderSize + numTiles * tileSize - x - dialogueHeight;
                break;
            default:
                y = 2*statusBarOuterMargin + statusBarHeight - gameBorderSize + x;
        }

        xPosTextLine1 = x + borderWidth + innerTextMargin;
        xPosTextLine2 = xPosTextLine1;
        yPosTextLine1 = y + borderWidth + innerTextMargin + normalTextSize +textOffset;
        yPosTextLine2 = yPosTextLine1 + innerTextMargin + normalTextSize;

        initializeReplyDialogue();

        //dialogue.resetDialogue();
        currentText = dialogue.getNextText();

        backgroundImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_dialog_window);
        backgroundImage = resizeImage(backgroundImage, dialogueWidth, dialogueHeight);
    }

    private void initializeReplyDialogue()
    {
        numReplies = dialogue.getNumReplies();

        if (numReplies != 0)
        {
            switch (numReplies) {
                case 1:
                    dialogueReplyHeight = dialogueReply1Height;
                    break;
                case 2:
                    dialogueReplyHeight = dialogueReply2Height;
                    break;
                case 3:
                    dialogueReplyHeight = dialogueReply3Height;
                    break;
                case 4:
                    dialogueReplyHeight = dialogueReply4Height;
                    break;
            }

            //initialize reply window
            selectionArrow = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_select);
            selectionArrow = resizeImage(selectionArrow, InterfaceElement.selectionWidth, InterfaceElement.selectionHeight);

            selectionBackground = BitmapFactory.decodeResource(context.getResources(), dialogue.getReplyBackgroundId(context));
            selectionBackground = resizeImage(selectionBackground, InterfaceElement.dialogueReplyWidth, dialogueReplyHeight);

            xPosReply = x + dialogueWidth - dialogueReplyWidth;
            xPosReplyText = xPosReply + borderWidth + 2*innerTextMargin + selectionWidth;
            xPosArrow = xPosReply + borderWidth + innerTextMargin;
            replyText = dialogue.getReplyText();

            int tmpBorderWidth = 0;
            if (dialogue.getDialoguePosition() == DialoguePosition.BOTTOM) {
                tmpBorderWidth = borderWidth;
                yPosReply = y - dialogueReplyHeight;
            } else {
                yPosReply = y + dialogueHeight;
            }

            for (int i = 0; i < numReplies; i++) {
                yPosReplyText[i] = yPosReply + tmpBorderWidth + (i+1) * (innerTextMargin + normalTextSize) + textOffset;
                yPosArrow[i] = yPosReply + tmpBorderWidth + innerTextMargin + i * (innerTextMargin + normalTextSize) + (normalTextSize - selectionHeight)/2;
            }
        }
    }

    public boolean nextText()
    {
        if (dialogue.isFinished()) {
            return false;
        } else {
            currentText = dialogue.getNextText();
            return true;
        }
    }

    public boolean mustSelectOption()
    {
        return dialogue.mustSelectOption();
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(backgroundImage, x, y, null);
        TextPaint paint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        int textColor = context.getResources().getColor(R.color.interfaceText);
        paint.setColor(textColor);
        paint.setTextSize(50);
        canvas.drawText(currentText[0], xPosTextLine1, yPosTextLine1, paint);
        canvas.drawText(currentText[1], xPosTextLine2, yPosTextLine2, paint);

        if (InteractionHandler.interfaceSelectionActive) {
            canvas.drawBitmap(selectionBackground, xPosReply, yPosReply, null);
            for (int i = 0; i < numReplies; i++)
                canvas.drawText(replyText[i], xPosReplyText, yPosReplyText[i], paint);
            canvas.drawBitmap(selectionArrow, xPosArrow, yPosArrow[currentSelectionIndex], null);
        }
    }

    public void currentSelectionUp()
    {
        if (currentSelectionIndex > 0)
            currentSelectionIndex--;
    }

    public void currentSelectionDown()
    {
        if (currentSelectionIndex < (numReplies-1))
            currentSelectionIndex++;
    }

    public void selectReply()
    {
        //set State
        GameState state = new GameState(dialogue.getReplyStateNameToChange(currentSelectionIndex), dialogue.getReplyStateValueToChange(currentSelectionIndex));
        if (state.name != "NULL")
            gamePanel.getStateHandler().setState(state);

        //run Action
        dialogue.runReplyAction(currentSelectionIndex);


        Dialogue nextDialogue = dialogue.getNextDialogue(currentSelectionIndex);
        dialogue = nextDialogue;
        initializeReplyDialogue();
        currentSelectionIndex = 0;


        dialogue.resetDialogue();
        currentText = dialogue.getNextText();
    }

    public Dialogue getDialogue() {
        return dialogue;
    }
}
