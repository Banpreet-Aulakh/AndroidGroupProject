package com.sfu.cmpt276.coopachievement.model;

import java.util.ArrayList;

/*
The GameHistory Class is responsible for storing all the instances of GamesPlayed per configured game. This facilitates instances of each config's
game history being stored in the Singleton GameType class.
*/

public class GameHistory {

    ArrayList<GamePlayed> gamesPlayedList = new ArrayList<GamePlayed>();

    String configName;

    public GameHistory(String configName){
        this.configName = configName;
    }

    public ArrayList<GamePlayed> getGameHistory() {
        return gamesPlayedList;
    }

    public void addPlayedGame(GamePlayed game) {
        gamesPlayedList.add(game);
    }

    public void removePlayedGame(int index) {
        gamesPlayedList.remove(index);
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigName() { return configName; }

    public String[] getStringArray() {
        String[] arr = new String[gamesPlayedList.size()];
        for (int i = 0; i < gamesPlayedList.size(); i++) {
            arr[i] = gamesPlayedList.get(i).toString(); // Calls to String method of GamesPLayed NEED TO ADD
        }
        return arr;
    }

}
