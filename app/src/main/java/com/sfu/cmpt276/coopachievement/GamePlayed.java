package com.sfu.cmpt276.coopachievement;

import android.content.Context;
import android.content.Intent;

public class GamePlayed {
    private int totalScore;
    private int numPlayers;
    private String achievement;
    private String [] achievements = {};
    //private GameType game;

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

    }


    public GamePlayed(int totalScore, int numPlayers, String achievement) {
        this.totalScore = totalScore;
        this.numPlayers = numPlayers;
        this.achievement = achievement;
    }



}
