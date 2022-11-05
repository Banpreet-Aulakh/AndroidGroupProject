package com.sfu.cmpt276.coopachievement.model;

import java.util.ArrayList;
/*
    Singleton class is responsible for storing one singleton data for game config.
*/

public class Singleton {
    ArrayList<GameConfig> gameConfigs = new ArrayList<>();

    //set up singleton support
    private static Singleton instance;
    private Singleton(){

    }
    public static Singleton getInstance(){
        if(instance==null){
            instance = new Singleton();
        }
        return instance;
    }

    public ArrayList<GameConfig> getGameConfigList(){
        return gameConfigs;
    }

    public void addConfig(GameConfig config){
        gameConfigs.add(config);
    }
    public void removeConfig(int index) {
        gameConfigs.remove(index);
    }

    public void setGameConfigs(ArrayList<GameConfig> arr){
        //shared preference call
        this.gameConfigs = arr;
    }

    //print listview of game configs

    public String[] getStringArray() {
        String[] tempArray = new String[gameConfigs.size()];
        for (int i = 0; i < gameConfigs.size(); i++) {
            tempArray[i] = gameConfigs.get(i).toString();
        }
        return tempArray;
    }
}
