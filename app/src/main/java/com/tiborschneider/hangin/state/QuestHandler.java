package com.tiborschneider.hangin.state;

import com.tiborschneider.hangin.mainGame.GamePanel;

/**
 * Created by tibor on 27.07.16.
 */
public class QuestHandler {
    public static final int maxNumQuests = 50;
    private int numQuests = 0;
    private Quest[] quests = new Quest[maxNumQuests];

    public void addQuest(Quest quest) {
        if (numQuests < maxNumQuests - 1)
            quests[numQuests++] = quest;
    }

    public int getNumQuests() {
        return numQuests;
    }

    public int getNumQuestsToShow() {
        int ret = 0;
        for (int i = 0; i < numQuests; i++) {
            if (quests[i].getNumTextToShow() != 0)
                ret++;
        }
        return ret;
    }

    public String[] getQuestList() {
        if (numQuests == 0) {
            String[] ret = new String[1];
            ret[0] = null;
            return ret;
        }

        String[] ret = new String[numQuests];
        int counter = 0;
        for (int i = 0; i < numQuests; i++) {
            if (quests[i].getTextToShow()[0] != null) {
                ret[counter++] = quests[i].getQuestName();
            }
        }
        return ret;
    }

    public Quest getQuest(int index) {
        if (index >= 0 && index < numQuests) {
            return quests[index];
        }
        return null;
    }

    public Quest getQuest(String questName) {
        for (int i = 0; i < numQuests; i++) {
            if (quests[i].getQuestName().equals(questName))
                return getQuest(i);
        }
        return null;
    }

    public void sortAll() {
        //sort Quests
        //bubble sort algorithm
        for (int i = numQuests-1; i >= 0; i--) {
            for (int j = 1; j <= i; j++) {
                if (quests[j-1].getQuestStatus() > quests[j].getQuestStatus()) {
                    Quest tmp  = quests[j-1];
                    quests[j-1] = quests[j];
                    quests[j]   = tmp;
                }
            }
        }

        //sort Text in Quests
        for (int i = 0; i < numQuests; i++) {
            quests[i].sort();
        }
    }

    public void checkFinished() {
        for (int i = 0; i < numQuests; i++) {
            quests[i].checkFinished();
        }
    }
}
