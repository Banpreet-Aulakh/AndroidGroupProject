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
    private int numPlayers;
    private String achievementName;

    private static final String DATE_FORMAT = "MMM dd @ HH:mm";
    private String timePlayed;


    public int getTotalScore() {
        return totalScore;
    }

    public int getNumPlayers() {
        return numPlayers;
    }


    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    public void setAchievementString(String name){
        achievementName = name;
    }
    public String getAchievementName(){
        return achievementName;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
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

    public GamePlayed() {
        this.totalScore = 0;
        this.numPlayers = 0;
        this.achievementName = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime tmp = LocalDateTime.now();
        this.timePlayed = tmp.format(formatter);
        System.out.println("created Game played class instance");
    }

    @Override
    public String toString() {
        return "GamePlayed{" +
                "totalScore=" + totalScore +
                ", numPlayers=" + numPlayers +
                ", achievement=" + achievementName + '\'' +
                ", timePlayed='" + timePlayed + '\'' +
                '}';
    }
}