package com.sfu.cmpt276.coopachievement.model;

import android.content.Context;
import android.content.Intent;
import android.os.Build;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GamePlayed {

    private int totalScore;
    private int numPlayers;
    private String achievement;
    private static final String DATE_FORMAT = "MMM dd @ HH:mm";
    private String timePlayed;

    public int getTotalScore() {
        return totalScore;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    private void setAchievementLevel(){
        int i = 0;
    }

    public GamePlayed() {
        this.totalScore = 0;
        this.numPlayers = 0;
        this.achievement = "";
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
                ", achievement='" + achievement + '\'' +
                ", timePlayed='" + timePlayed + '\'' +
                '}';
    }
}