package com.sfu.cmpt276.coopachievement.model;

import java.util.ArrayList;

/*
    Game config class is responsible for storing all data needed for configuration of a game
    including calculating and storing achievement thresholds
*/

public class GameConfig {
    String gameName;
    public GameHistory gameHistory;

    int greatScore;
    int poorScore;
    ArrayList<Integer> achievement_Thresholds=new ArrayList<Integer>();


    public ArrayList<Integer> calculateAchievementThreshold(int greatScore, int poorScore) {
        // math function for calculating achievement threshold
        // push each threshold into an int array
        return achievement_Thresholds;
    }

    //call instance from game history
    public GameHistory getGameHistory() {
        return gameHistory;
    }

}
