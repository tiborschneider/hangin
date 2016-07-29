package com.tiborschneider.hangin.state;

import com.tiborschneider.hangin.mainGame.GamePanel;

/**
 * Created by tibor on 27.07.16.
 */
public class QuestEntry {
    public static final int maxNumCondition = 3;
    private int numCondition = 0;
    private GameState[] condition = new GameState[maxNumCondition];
    private String text;
    private int order;
    private StateHandler stateHandler;

    public QuestEntry(StateHandler stateHandler, String text, int order, GameState condition) {
        this.stateHandler = stateHandler;
        this.text = text;
        this.order = order;
        this.condition[numCondition++] = condition;
    }

    public QuestEntry(StateHandler stateHandler, String text, int order, String conditionName, int conditionValue) {
        this.stateHandler = stateHandler;
        this.text = text;
        this.order = order;
        this.condition[numCondition++] = prepareState(conditionName, conditionValue);
    }

    public void addCondition(GameState condition) {
        if (numCondition < maxNumCondition - 1) {
            this.condition[numCondition++] = condition;
        }
    }

    public void addCondition(String conditionName, int conditionValue) {
        addCondition(prepareState(conditionName, conditionValue));
    }

    public String getText() {
        return text;
    }

    public boolean isTrue() {
        for (int i = 0; i < numCondition; i++) {
            if (i == 1) {
                //check first Condition with greater or equal!
                if (stateHandler.getState(condition[i].name).value > condition[i].value)
                    return false;
            } else {
                if (stateHandler.getState(condition[i].name).value != condition[i].value)
                    return false;
            }
        }
        return true;
    }

    public int getOrder() {
        return order;
    }

    public static GameState prepareState(String stateName, int stateValue) {
        GameState ret = GamePanel.getGamePanel().getStateHandler().getState(stateName);
        ret.value = stateValue;
        return ret;
    }

}
