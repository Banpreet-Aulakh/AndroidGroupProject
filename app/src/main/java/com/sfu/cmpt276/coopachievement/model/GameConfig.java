package com.sfu.cmpt276.coopachievement.model;

import java.util.ArrayList;

public class GameConfig {
    String gameName;
    public GameHistory gameHistory;

    int greatScore;
    int poorScore;
    ArrayList<Integer> achievement_Thresholds=new ArrayList<Integer>();
    ArrayList<GameHistory> gameHistories = new ArrayList<>();

    public ArrayList<Integer> calculateAchievementThreshold(int greatScore, int poorScore) {
        // math function for calculating achievement threshold
        // push each threshold into an int array
        return achievement_Thresholds;
    }

    public GameHistory getGameHistories() {
        return gameHistory;
    }

    public String[] getStringArray() {
        String[] tempArray = new String[gameHistories.size()];
        for (int i = 0; i < gameHistories.size(); i++) {
            tempArray[i] = gameHistories.get(i).toString();
        }
        return tempArray;
    }
}
