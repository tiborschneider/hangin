package com.tiborschneider.hangin.userInteraction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private int[] yPosQuestEntry = new int[numQuestsOnScreen];
    private Bitmap image;
    private Bitmap selectionImage;
    private Bitmap starImage;
    private Context context;
    private QuestHandler questHandler;
    private int currentIndex = 0;
    private int topEntryIndex = 0;
    private boolean[] listTrue;
    private int numQuestsToShow;
    private int numQuests;

    public InterfaceQuestSelection(Context context) {
        this.context = context;
        this.questHandler = GamePanel.getGamePanel().getQuestHandler();

        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_quest_main_window);
        selectionImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_quest_main_selection);
        starImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_star);

        x = (GamePanel.screenWidth - questMainWidth) / 2;
        y = 2*statusBarOuterMargin + statusBarHeight - gameBorderSize + x;

        xPosTitleText = x + borderWidth + innerTextMargin;
        yPosTitleText = y + borderWidth + innerTextMargin + bigTextSize + textOffset;

        xPosSelection = x + borderWidth - borderSmallWidth;
        yPosSelection = y + 2*borderWidth + 2*innerTextMargin + bigTextSize - borderSmallWidth;

        xPosQuestEntry = x + borderWidth + innerTextMargin;
        for (int i = 0; i < numQuestsOnScreen; i++)
            yPosQuestEntry[i] = y + 2*borderWidth + 2*innerTextMargin + bigTextSize + innerTextMargin + normalTextSize + textOffset + i*(normalTextSize + 2*innerTextMargin + borderSmallWidth);

        //init listTrue
        numQuestsToShow = questHandler.getNumQuestsToShow();
        numQuests = questHandler.getNumQuests();
        listTrue = new boolean[numQuests];
        for (int i = 0; i < numQuests; i++) {
            if (questHandler.getQuest(i) != null) {
                listTrue[i] = questHandler.getQuest(i).isTrue();
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
        int counter = 0;
        for (int i = 0; i < numQuestsOnScreen; i++) {
            if (listTrue[i + topEntryIndex] && questHandler.getQuest(i + topEntryIndex) != null) {
                if (questHandler.getQuest(i + topEntryIndex).isFinished()) {
                    canvas.drawText(questHandler.getQuest(i + topEntryIndex).getQuestName(), xPosQuestEntry, yPosQuestEntry[counter++], grayPaint);
                } else {
                    if (questHandler.getQuest(i + topEntryIndex).isChanged()) {
                        canvas.drawBitmap(starImage, xPosQuestEntry - innerTextMargin/2, yPosQuestEntry[counter], null);
                    }
                    canvas.drawText(questHandler.getQuest(i + topEntryIndex).getQuestName(), xPosQuestEntry, yPosQuestEntry[counter++], paint);
                }
            }
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
        return questHandler.getQuest(getSelectedIndex());
    }

    public int getSelectedIndex() {
        int counter = currentIndex + 1;
        for (int i = 0; i < numQuests; i++) {
            if (listTrue[i])
                counter--;
            if (counter == 0)
                return i;
        }
        return currentIndex;

    }
}
