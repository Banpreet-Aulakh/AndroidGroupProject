package com.sfu.cmpt276.coopachievement.model;

import java.util.ArrayList;

/*
The GameHistory Class is responsible for storing all the instances of GamesPlayed per configured game. This facilitates instances of each config's
game history being stored in the Singleton GameType class.
*/

public class GameHistory {

    ArrayList<GamesPlayed> gamesPlayedList = new ArrayList<GamesPlayed>();

    String configName;

    public GameHistory(String configName){
        this.configName = configName;
    }

    public ArrayList<GamesPlayed> getGameHistory() {
        return gamesPlayedList;
    }

    public void addPlayedGame(GamesPlayed game) {
        gamesPlayedList.add(game);
    }

    public void removePlayedGame(int index) {
        gamesPlayedList.remove(index);
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }


}
