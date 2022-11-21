package com.sfu.cmpt276.coopachievement.model;

import android.content.Context;
import android.content.Intent;
import android.os.Build;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
/*
* GamePlayed Class is responsible for storing and printing the input of the total score
* and the number of player from New Game Activity while getting the
* corresponding achievement name from the string.xml
* and record its time stamp
*/

public class GamePlayed {
    private static final int MAX_ACHIEVEMENT = 9;
    private int totalScore;
    //Added list parameter to class
    private ArrayList<Integer> listScore;
    private int numPlayers;
    private String achievementName;
    private int difficulty;

    private static final String DATE_FORMAT = "MMM dd @ HH:mm";
    private String timePlayed;

    public GamePlayed() {
        this.totalScore = 0;
        this.numPlayers = 0;
        this.achievementName = "";
        this.difficulty = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime tmp = LocalDateTime.now();
        this.timePlayed = tmp.format(formatter);
    }

    public void setDifficulty(int level){
        difficulty = level;
    }
    public void setListScore(ArrayList<Integer> playerScoreArray) {
        this.listScore = playerScoreArray;
    }
    public void setTotalScore(ArrayList<Integer> listScore) {
        this.listScore = listScore;
        if(!isValidScoresList()){
            totalScore = -1;
        }
        else{
            totalScore = 0;
            for(int i = 0; i < numPlayers; i++){
                totalScore += listScore.get(i);
            }
        }

    }
    public void setNumPlayers(int numPlayers) {
        if(numPlayers < 0){
            throw new IllegalArgumentException("Player Number Out of Bounds");
        }
        this.numPlayers = numPlayers;
    }
    public void setAchievementString(String name){
        achievementName = name;
    }
    public void setAchievementLevel(ArrayList<Integer>boundariesList, String namesList[]){
        double averagePlayerScore = totalScore/numPlayers;

        if (averagePlayerScore < boundariesList.get(0)) {
            achievementName = namesList[0];
            return;
        }
        int i = 1;
        int level = 8;

        while(i < MAX_ACHIEVEMENT) {
            if(averagePlayerScore < boundariesList.get(i)){
                level = i;
                break;
            }
            i++;
        }
        achievementName = namesList[level];
    }

    public int getDifficulty(){
        return difficulty;
    }
    public String getDifficultyAsString(){
        if(difficulty == 0){
            return "Easy";
        }else if(difficulty == 1){
            return "Medium";
        }else{
            return "Hard";
        }
    }

    public int getTotalScore() {
        return totalScore;
    }
    public ArrayList<Integer> getListScore(){
        if(listScore != null){
            return listScore;
        }
        return new ArrayList<Integer>();
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public String getAchievementName(){
        return achievementName;
    }

    public String checkAchievementLevel(ArrayList<Integer>boundariesList, String namesList[], int numberPlayers, int combinedScore){
        double averagePlayerScore = combinedScore/numberPlayers;
        if (averagePlayerScore < boundariesList.get(0)) {
            return namesList[0];
        }
        int i = 1;
        int level = 8;
        while(i < MAX_ACHIEVEMENT) {
            if(averagePlayerScore < boundariesList.get(i)){
                level = i;
                break;
            }
            i++;
        }
        return namesList[level];
    }
    public String[] getParamsArray() {
        String[] params = {""+totalScore, ""+numPlayers, getDifficultyAsString(), ""+achievementName, ""+timePlayed };
        return params;
    }

    @Override
    public String toString() {
        return "GamePlayed{" +
                "totalScore=" + totalScore +
                ", numPlayers=" + numPlayers +
                ", difficulty=" + getDifficultyAsString() +
                ", achievement=" + achievementName + '\'' +
                ", timePlayed='" + timePlayed + '\'' +
                '}';
    }

    public boolean isValidScoresList(){
        for(int i = 0; i < numPlayers; i++){
            if(listScore.get(i) == -1){
                return false;
            }
        }
        return true;
    }

}