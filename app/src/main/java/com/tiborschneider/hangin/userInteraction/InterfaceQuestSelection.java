package com.tiborschneider.hangin.userInteraction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.TextPaint;

import com.tiborschneider.hangin.R;
import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.state.Quest;
import com.tiborschneider.hangin.state.QuestHandler;

/**
 * Created by tibor on 27.07.16.
 */
public class InterfaceQuestSelection extends InterfaceElement {
    private final String titleText = "Quests";
    public final int numQuestsOnScreen = 6;
    private int xPosTitleText;
    private int yPosTitleText;
    private int xPosQuestEntry;
    private int xPosSelection;
    private int yPosSelection;
    private int xPosUp;
    private int yPosUp;
    private int xPosDown;
    private int yPosDown;
    private int[] yPosQuestEntry = new int[numQuestsOnScreen];
    private Bitmap image;
    private Bitmap selectionImage;
    private Bitmap starImage;
    private Bitmap upImage;
    private Bitmap downImage;
    private Context context;
    private QuestHandler questHandler;
    private int currentIndex = 0;
    private int topEntryIndex = 0;
    private Quest[] listQuests;
    //private boolean[] listTrue;
    private boolean[] listChanged;
    private boolean[] listFinished;
    private int numQuestsToShow;
    private int numQuests;

    public InterfaceQuestSelection(Context context) {
        this.context = context;
        this.questHandler = GamePanel.getGamePanel().getQuestHandler();

        //update finished State of all Quests
        questHandler.checkFinished();

        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_quest_main_window);
        selectionImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_select);
        starImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_star);
        upImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_up);
        downImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_down);

        x = (GamePanel.screenWidth - questMainWidth) / 2;
        y = 2*statusBarOuterMargin + statusBarHeight - gameBorderSize + x;

        xPosUp = GamePanel.screenWidth/2 - navigationButtonSize/2;
        yPosUp = y + 2* borderWidth + 2*innerTextMargin + bigTextSize - navigationButtonSize + navigationButtonOffset;
        xPosDown = xPosUp;
        yPosDown = y + questMainHeight - borderWidth - navigationButtonOffset;

        xPosTitleText = x + borderWidth + innerTextMargin;
        yPosTitleText = y + borderWidth + innerTextMargin + bigTextSize + textOffset;

        xPosSelection = x + borderWidth + innerTextMargin;
        yPosSelection = y + 2*borderWidth + 2*innerTextMargin + bigTextSize + innerTextMargin + normalTextSize/2 - selectionHeight/2;

        xPosQuestEntry = x + borderWidth + 2*innerTextMargin + selectionWidth;
        for (int i = 0; i < numQuestsOnScreen; i++)
            yPosQuestEntry[i] = y + 2*borderWidth + 2*innerTextMargin + bigTextSize + innerTextMargin + normalTextSize + textOffset + i*(normalTextSize + 2*innerTextMargin + borderSmallWidth);

        updateQuestStatus();

    }

    public void updateQuestStatus() {
        numQuestsToShow = questHandler.getNumQuestsToShow();
        numQuests = questHandler.getNumQuests();
        listQuests = new Quest[numQuestsToShow];
        //listTrue = new boolean[numQuestsToShow];
        listChanged = new boolean[numQuestsToShow];
        listFinished = new boolean[numQuestsToShow];
        int index = 0;
        for (int i = 0; i < numQuests; i++) {
            Quest quest = questHandler.getQuest(i);
            if (quest != null && quest.isTrue()) {
                listQuests[index] = quest;
                listChanged[index] = quest.isChanged();
                listFinished[index] = quest.isFinished();
                index++;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        //draw background
        canvas.drawBitmap(image, x, y, null);

        //prepare Paint
        TextPaint paint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        int textColor = context.getResources().getColor(R.color.interfaceText);
        paint.setColor(textColor);
        paint.setTextSize(bigTextSize);
        TextPaint grayPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        textColor = context.getResources().getColor(R.color.interfaceGrayText);
        grayPaint.setColor(textColor);
        grayPaint.setTextSize(normalTextSize);

        //draw Title
        canvas.drawText(titleText, xPosTitleText, yPosTitleText, paint);

        //draw Selection
        canvas.drawBitmap(selectionImage, xPosSelection, yPosSelection + (currentIndex-topEntryIndex)*(borderSmallWidth + 2*innerTextMargin + normalTextSize), null);

        //draw Entries
        paint.setTextSize(normalTextSize);

        for (int i = 0; (i < numQuestsOnScreen && i < numQuestsToShow); i++) {
            if (listFinished[i + topEntryIndex]) {
                canvas.drawText(listQuests[i + topEntryIndex].getQuestName(), xPosQuestEntry, yPosQuestEntry[i], grayPaint);
            } else {
                canvas.drawText(listQuests[i + topEntryIndex].getQuestName(), xPosQuestEntry, yPosQuestEntry[i], paint);
                if (listChanged[i + topEntryIndex]) {
                    canvas.drawBitmap(starImage, xPosQuestEntry - innerTextMargin*3/2, yPosQuestEntry[i] - textOffset - normalTextSize - innerTextMargin*3/2, null);
                }
            }
        }

        //draw Navigation Buttons
        //draw Up:
        if (topEntryIndex != 0) {
            canvas.drawBitmap(upImage, xPosUp, yPosUp, null);
        }
        if (numQuestsToShow > topEntryIndex + numQuestsOnScreen) {
            canvas.drawBitmap(downImage, xPosDown, yPosDown, null);
        }
    }

    public void moveSelectionUp() {
        if (currentIndex > 0) {
            currentIndex--;
            if (currentIndex < topEntryIndex) {
                topEntryIndex--;
            }
        }
    }

    public void moveSelectionDown() {
        if (currentIndex < numQuestsToShow - 1) {
            currentIndex++;
            if (currentIndex >= topEntryIndex + numQuestsOnScreen) {
                topEntryIndex++;
            }
        }
    }

    public Quest getSelectedQuest() {
        return listQuests[getSelectedIndex()];
    }

    public int getSelectedIndex() {
        return currentIndex;
    }
}
