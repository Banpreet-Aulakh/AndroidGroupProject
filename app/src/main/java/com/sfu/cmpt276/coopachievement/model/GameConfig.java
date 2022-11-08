package com.sfu.cmpt276.coopachievement.model;

import java.util.ArrayList;

/*
* Game config class is responsible for storing all data needed for configuration of a game
* including calculating and storing achievement thresholds
*/

public class GameConfig {
    private final int MAX_THRESHOLD = 8;
    private String gameName;
    private GameHistory gameHistory;

    private int greatScore;
    private int poorScore;
    private ArrayList<Integer> achievement_Thresholds = new ArrayList<Integer>();


    private ArrayList<Integer> calculateAchievementThreshold(int greatScore, int poorScore) {
        int range = greatScore - poorScore ;
        int remainder = range % MAX_THRESHOLD;
        int lowerBound = poorScore;

        int boundary = (int) Math.floor(range/MAX_THRESHOLD);
        achievement_Thresholds.add(lowerBound);

        //adding threshold corresponding to the remainder value
        for (int i = 0; i < remainder ;i++){
            lowerBound +=boundary+1;
            achievement_Thresholds.add(lowerBound);

        }
        //adding leftover threshold
        for(int i = 0; i< (MAX_THRESHOLD - remainder);i++){
            lowerBound += boundary;
            achievement_Thresholds.add(lowerBound);

        }

        return achievement_Thresholds;
    }

    //call instance from game history
    public GameHistory getGameHistory() {
        return gameHistory;
    }
    public void setGameHistory(GameHistory history){
        this.gameHistory = history;
    }

    public String getGameName(){
        return gameName;
    }
    public int getGreatScore(){
        return greatScore;
    }
    public int getPoorScore(){
        return poorScore;
    }
    public void setGameName(String gameName){
        this.gameName = gameName;
    }
    public void setGreatScore(int greatScore){
        this.greatScore = greatScore;
    }
    public void setPoorScore(int poorScore){
        this.poorScore = poorScore;
    }

    @Override
    public String toString(){
        return gameName;
    }

    public void setAchievement_Thresholds(){
        this.achievement_Thresholds = calculateAchievementThreshold(greatScore, poorScore);
    }

    public ArrayList<Integer> getAchievement_Thresholds(){
        return achievement_Thresholds;
    }

}
