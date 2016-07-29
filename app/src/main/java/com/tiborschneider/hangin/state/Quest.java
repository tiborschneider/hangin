package com.tiborschneider.hangin.state;

/**
 * Created by tibor on 27.07.16.
 */
public class Quest {
    public static final int maxNumEntries = 20;
    private int numEntries = 0;
    private QuestEntry[] questEntry = new QuestEntry[maxNumEntries];
    private StateHandler stateHandler;
    private GameState finishCondition;
    private String questName;

    public Quest(StateHandler stateHandler, String questName, GameState finishCondition) {
        this.stateHandler = stateHandler;
        this.questName = questName;
        this.finishCondition = finishCondition;

    }

    public void addQuestEntry(QuestEntry questEntry) {
        if (numEntries < maxNumEntries -1) {
            this.questEntry[numEntries++] = questEntry;
        }
    }

    public String[] getTextToShow() {
        String[] ret = new String[maxNumEntries];
        int counter = 0;
        for (int i = 0; i < numEntries; i++) {
            if (questEntry[i].isTrue())
                ret[counter++] = questEntry[i].getText();
        }
        return ret;
    }

    public int getNumTextToShow() {
        int counter = 0;
        for (int i = 0; i < numEntries; i++) {
            if (questEntry[i].isTrue())
                counter++;
        }
        return counter;
    }

    public void sort() {
        if (numEntries <= 2)
            return;
        //bubble sort algorithm
        for (int i = numEntries-1; i >= 0; i--) {
            for (int j = 1; j <= i; j++) {
                if (questEntry[j-1].getOrder() > questEntry[j].getOrder()) {
                    QuestEntry tmp  = questEntry[j-1];
                    questEntry[j-1] = questEntry[j];
                    questEntry[j]   = tmp;
                }
            }
        }
    }

    public String getQuestName() {
        return questName;
    }

    public boolean isTrue() {
        return getTextToShow()[0] != null;
    }

    public int getNumEntries() {
        return numEntries;
    }

    public boolean isNew() {
        if (getQuestState().value == 0)
            return true;
        return false;
    }

    public boolean isChanged() {
        return (isNew() || isQuestUpdated());
    }

    public boolean isFinished() {
        if (getQuestState().value == 2)
            return true;
        return false;
    }

    public void setFinished() {
        GameState questState = getQuestState();
        questState.value = 2;
        stateHandler.setState(questState);
    }

    public void setNormal() {
        if (isNew()) {
            GameState questState = getQuestState();
            questState.value = 1;
            stateHandler.setState(questState);
        }
        GameState state = getLastShownState();
        state.value = getNumTextToShow();
        stateHandler.setState(state);
    }

    public void checkFinished() {
        if (stateHandler.getState(finishCondition.name).value == finishCondition.value)
            setFinished();
    }

    public int getQuestStatus() {
        return getQuestState().value;
    }

    public boolean isQuestUpdated() {
        if ((getLastShownState().value != getNumTextToShow()))
            return true;
        else
            return false;
    }

    private GameState getQuestState() {
        return stateHandler.getState(getQuestStateName());
    }

    public String getQuestStateName() {
        return "_questState_" + questName;
    }

    private GameState getLastShownState() {
        return stateHandler.getState(getLastShownStateName());
    }

    public String getLastShownStateName() {
        return "_questLastShown_" + questName;
    }
}
