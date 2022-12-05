package com.sfu.cmpt276.coopachievement.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Map;

/*
* Game config class is responsible for storing all data needed for configuration of a game
* including calculating and storing achievement thresholds
*/

public class GameConfig {
    private final int MAX_THRESHOLD = 8;
    private String gameName;
    private GameHistory gameHistory;
    private final static double EASY = 0.75;
    private final static double MEDIUM = 1.0;
    private final static double HARD = 1.25;
    private int greatScore;
    private int poorScore;
    private String boxImage;
    //TODO store a reference to the image in storage instead of a bitmap
    private ArrayList<Integer> achievement_Thresholds = new ArrayList<Integer>();
    private int[] achievementCounter;

    public GameConfig(){
        achievementCounter = new int[9];
    }

    public int[] getAchievementCounter(){
        return achievementCounter;
    }

    public String achievementCounterString(){
        String str = "";
        for(int i = 0; i < achievementCounter.length; i++){
            str += achievementCounter[i] + ", ";
        }
        return str;
    }

    //difficulty: easy:0, medium:1, hard:2
    private ArrayList<Integer> calculateAchievementThreshold(int greatScore, int poorScore, int difficulty) {
        achievement_Thresholds.clear();
        int range = greatScore - poorScore ;
        int remainder = range % MAX_THRESHOLD;
        int lowerBound = poorScore;
        double multiplier;

        if(difficulty == 0){
            multiplier = EASY;
        }else if (difficulty == 1){
            multiplier = MEDIUM;
        }else{
            multiplier = HARD;
        }

        int boundary = (int) Math.floor((range/MAX_THRESHOLD));
        achievement_Thresholds.add(lowerBound);

        //adding threshold corresponding to the remainder value
        for (int i = 0; i < remainder ;i++){
            lowerBound += boundary+1;
            achievement_Thresholds.add(lowerBound);
        }

        //adding leftover threshold
        for(int i = 0; i< (MAX_THRESHOLD - remainder);i++){
            lowerBound += boundary;
            achievement_Thresholds.add(lowerBound);
        }
        for (int i = 0; i< MAX_THRESHOLD;i++){
            achievement_Thresholds.set(i, (int) (achievement_Thresholds.get(i)*multiplier));
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
    public void setBoxImage(String image){
        boxImage = image;
    }
    public String getBoxImage(){
        return boxImage;
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

    public void setAchievement_Thresholds(int difficulty){
        this.achievement_Thresholds = calculateAchievementThreshold(greatScore, poorScore, difficulty);
    }

    public ArrayList<Integer> getAchievement_Thresholds(){
        return achievement_Thresholds;
    }

}
