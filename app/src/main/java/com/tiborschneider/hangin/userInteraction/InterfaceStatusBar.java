package com.tiborschneider.hangin.userInteraction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;

import com.tiborschneider.hangin.mainGame.GamePanel;
import com.tiborschneider.hangin.R;
import com.tiborschneider.hangin.character.Player;

/**
 * Created by tibor on 05.07.16.
 */
public class InterfaceStatusBar extends InterfaceElement {
    private int statusBarStonedMeterOffsetX = statusBarInnerMargin + borderSmallWidth;
    private int statusBarMunchiesMeterOffsetX = statusBarWidth - statusBarInnerMargin  - borderSmallWidth - statusBarMeterMaximumWidth;
    private int statusBarMeterOffsetY = statusBarInnerMargin + borderSmallWidth;
    private int statusBarStonedTextOffsetX = 2*statusBarStonedMeterOffsetX + statusBarMeterMaximumWidth + 15;
    private int statusBarMunchiesTextOffsetX = statusBarMunchiesMeterOffsetX - statusBarStonedMeterOffsetX - statusBarTextLength;
    private int statusBarTextOffsetY = 5;

    private Bitmap imageBackground;
    private Bitmap imageStonedMeter;
    private Bitmap imageMunchiesMeter;

    private Context context;
    private Player player;

    public InterfaceStatusBar(Context aContext, Player aPlayer) {
        context = aContext;
        player = aPlayer;

        x = (GamePanel.screenWidth - statusBarWidth) / 2;
        y = statusBarOuterMargin;

        imageBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_status_bar);
        imageBackground = resizeImage(imageBackground, statusBarWidth, statusBarHeight);
        imageStonedMeter = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_status_bar_stoned_meter);
        imageStonedMeter = resizeImage(imageStonedMeter, 1, statusBarMeterHeight);
        imageMunchiesMeter = BitmapFactory.decodeResource(context.getResources(), R.drawable.ui_status_bar_munchies_meter);
        imageMunchiesMeter = resizeImage(imageMunchiesMeter, 1, statusBarMeterHeight);


    }

    public void draw(Canvas canvas) {
        TextPaint paint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        int textColor = context.getResources().getColor(R.color.interfaceWhiteText);
        paint.setColor(textColor);
        paint.setTextSize(normalTextSize);

        canvas.drawText("" + player.getStonedMeter(), x + statusBarStonedTextOffsetX, y + statusBarTextOffsetY + normalTextSize + textOffset, paint);
        canvas.drawText("" + player.getMunchiesMeter(), x + statusBarMunchiesTextOffsetX, y + statusBarTextOffsetY + normalTextSize + textOffset, paint);

        for (int i = 0; i < player.getStonedMeter()*statusBarMeterMaximumWidth/100; i++) {
            canvas.drawBitmap(imageStonedMeter, x + statusBarStonedMeterOffsetX + i, y + statusBarMeterOffsetY, null );
        }

        int tmpMunchiesMeterOffsetX = statusBarMunchiesMeterOffsetX + statusBarMeterMaximumWidth;
        for (int i = 0; i < player.getMunchiesMeter()*statusBarMeterMaximumWidth/100; i++) {
            canvas.drawBitmap(imageMunchiesMeter, x + tmpMunchiesMeterOffsetX - i, y + statusBarMeterOffsetY, null);
        }

        canvas.drawBitmap(imageBackground, x, y, null);
    }
}
