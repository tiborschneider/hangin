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

/**
 * Created by tibor on 27.07.16.
 */
public class InterfaceQuestInfo extends InterfaceElement {
    private final String titleText;
    public final int numTextOnScreen = 8;
    private int xPosTitleText;
    private int yPosTitleText;
    private int xPosTextEntry;
    private int[] yPosTextEntry = new int[numTextOnScreen];
    private int xPosSelection;
    private int yPosSelection;
    private int xPosUp;
    private int yPosUp;
    private int xPosDown;
    private int yPosDown;
    private int textWindowWidth;
    private int textWindowHeight;
    private int xPosTextWindow;
    private int yPosTextWindow;
    private Bitmap image;
    private Bitmap downImage;
    private Bitmap upImage;
    private Context context;
    private Quest quest;
    private int topEntryIndex = 0;
    private int tmpY = 0;

    private int numText;
    private String[] text;

    public InterfaceQuestInfo(Context context, Quest quest) {
        this.context = context;
        this.quest = quest;

        titleText = quest.getQuestName();

        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_quest_single_window);
        upImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_up);
        downImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_down);

        x = (GamePanel.screenWidth - questMainWidth) / 2;
        y = 2*statusBarOuterMargin + statusBarHeight - gameBorderSize + x;

        xPosUp = GamePanel.screenWidth/2 - navigationButtonSize/2;
        yPosUp = y + 2* borderWidth + 2*innerTextMargin + bigTextSize - navigationButtonSize + navigationButtonOffset;
        xPosDown = xPosUp;
        yPosDown = y + questSingleHeight - borderWidth - navigationButtonOffset;

        xPosTitleText = x + borderWidth + innerTextMargin;
        yPosTitleText = y + borderWidth + innerTextMargin + bigTextSize + textOffset;

        xPosSelection = x + borderWidth - borderSmallWidth;
        yPosSelection = y + 2*borderWidth + 2*innerTextMargin + bigTextSize - borderSmallWidth;

        textWindowWidth = questSingleWidth - 2*borderWidth;
        textWindowHeight = questSingleHeight - 3*borderWidth - 2*innerTextMargin - bigTextSize;
        xPosTextWindow = x + borderWidth;
        yPosTextWindow = y + 2*borderWidth + 2*innerTextMargin + bigTextSize;

        xPosTextEntry = borderWidth + innerTextMargin;
        for (int i = 0; i < numTextOnScreen; i++)
            yPosTextEntry[i] = innerTextMargin + normalTextSize + textOffset + i*(normalTextSize + innerTextMargin);

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

        //create temp canvas
        Bitmap textImage = Bitmap.createBitmap(textWindowWidth, textWindowHeight, Bitmap.Config.ARGB_8888);
        textImage.eraseColor(Color.argb(0,0,0,0));
        Canvas textCanvas = new Canvas(textImage);

        //draw Entries
        paint.setTextSize(normalTextSize);
        //draw the text over and under current Window if necesary to animate!
        if (topEntryIndex > 0 && tmpY > 0 && text[topEntryIndex-1] != null) {
            textCanvas.drawText(text[topEntryIndex - 1], xPosTextEntry, yPosTextEntry[0] - normalTextSize - innerTextMargin + tmpY, paint);
        }
        if (tmpY < 0 && text[numTextOnScreen + topEntryIndex] != null) {
            textCanvas.drawText(text[numTextOnScreen + topEntryIndex], xPosTextEntry, yPosTextEntry[numTextOnScreen - 1] + innerTextMargin + normalTextSize + tmpY, paint);
        }
        for (int i = 0; i < numTextOnScreen; i++) {
            if (text[i + topEntryIndex] != null) {
                textCanvas.drawText(text[i + topEntryIndex], xPosTextEntry, yPosTextEntry[i] + tmpY, paint);
            }
        }
        canvas.drawBitmap(textImage, xPosTextWindow, yPosTextWindow, null);

        //draw Navigation Arrows
        if (topEntryIndex != 0) {
            canvas.drawBitmap(upImage, xPosUp, yPosUp, null);
        }
        if (numText > topEntryIndex + numTextOnScreen) {
            canvas.drawBitmap(downImage, xPosDown, yPosDown, null);
        }
    }

    public void moveSelectionUp() {
        if (topEntryIndex > 0) {
            topEntryIndex--;
            tmpY = (-1)*(innerTextMargin + normalTextSize);
        }
    }

    public void moveSelectionDown() {
        if (topEntryIndex < numText - numTextOnScreen) {
            topEntryIndex++;
            tmpY = innerTextMargin + normalTextSize;
        }
    }

    @Override
    public void update() {
        if (tmpY > 0) {
            tmpY -= 14;
            if (tmpY < 0) tmpY = 0;
        }
        if (tmpY < 0) {
            tmpY += 14;
            if (tmpY > 0) tmpY = 0;
        }
    }
}
