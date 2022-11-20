package com.sfu.cmpt276.coopachievement.model;

import java.util.ArrayList;

/*
 * The GameHistory Class is responsible for storing all the instances of GamesPlayed per configured game. This facilitates instances of each config's
 * game history being stored in the Singleton GameType class.
 */

public class GameHistory {

    ArrayList<GamePlayed> gamesPlayedList = new ArrayList<GamePlayed>();

    String configName;

    public GameHistory(String configName){
        this.configName = configName;
    }

    public ArrayList<GamePlayed> getGameHistoryList() {
        return gamesPlayedList;
    }

    public void addPlayedGame(GamePlayed game) {
        gamesPlayedList.add(game);
    }

    public void removePlayedGame(int index) {gamesPlayedList.remove(index);}

    public void setGamePlayed(int index, GamePlayed game){
        gamesPlayedList.set(index, game);
    }
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigName() { return configName; }

    public ArrayList<String[]> getParamsArrayList() {
        ArrayList<String[]> paramsList = new ArrayList<>();
        for(int i = 0; i < gamesPlayedList.size(); i++){
            paramsList.add(gamesPlayedList.get(i).getParamsArray());
        }
        return paramsList;
    }

}
