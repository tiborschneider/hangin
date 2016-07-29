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
public class InterfaceQuestInfo extends InterfaceElement {
    private final String titleText;
    public final int numTextOnScreen = 9;
    private int xPosTitleText;
    private int yPosTitleText;
    private int xPosTextEntry;
    private int[] yPosTextEntry = new int[numTextOnScreen];
    private int xPosSelection;
    private int yPosSelection;
    private Bitmap image;
    private Context context;
    private Quest quest;
    private int topEntryIndex = 0;

    private int numText;
    private String[] text;

    public InterfaceQuestInfo(Context context, Quest quest) {
        this.context = context;
        this.quest = quest;

        titleText = quest.getQuestName();

        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_quest_single_window);

        x = (GamePanel.screenWidth - questMainWidth) / 2;
        y = 2*statusBarOuterMargin + statusBarHeight - gameBorderSize + x;

        xPosTitleText = x + borderWidth + innerTextMargin;
        yPosTitleText = y + borderWidth + innerTextMargin + bigTextSize + textOffset;

        xPosSelection = x + borderWidth - borderSmallWidth;
        yPosSelection = y + 2*borderWidth + 2*innerTextMargin + bigTextSize - borderSmallWidth;

        xPosTextEntry = x + borderWidth + innerTextMargin;
        for (int i = 0; i < numTextOnScreen; i++)
            yPosTextEntry[i] = y + 2*borderWidth + 2*innerTextMargin + bigTextSize + innerTextMargin + normalTextSize + textOffset + i*(normalTextSize + innerTextMargin);

        //init listTrue
        numText = quest.getNumEntries();
        text = quest.getTextToShow();
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

        //draw Title
        canvas.drawText(titleText, xPosTitleText, yPosTitleText, paint);

        //draw Entries
        paint.setTextSize(normalTextSize);
        for (int i = 0; i < numTextOnScreen; i++) {
            if (text[i] != null) {
                canvas.drawText(text[i + topEntryIndex], xPosTextEntry, yPosTextEntry[i + topEntryIndex], paint);
            }
        }
    }

    public void moveSelectionUp() {
        if (topEntryIndex > 0) {
            topEntryIndex--;
        }
    }

    public void moveSelectionDown() {
        if (topEntryIndex < numText - numTextOnScreen) {
        }
    }
}
