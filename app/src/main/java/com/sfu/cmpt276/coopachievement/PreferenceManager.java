package com.sfu.cmpt276.coopachievement;

import static android.preference.PreferenceManager.getDefaultSharedPreferencesName;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static  PreferenceManager INSTANCE;
    private  static SharedPreferences preferences;

    synchronized public static PreferenceManager getInstance(Context context){
        if(INSTANCE==null){
            INSTANCE=new PreferenceManager();
            preferences=context.getSharedPreferences("userinfo3",Context.MODE_PRIVATE);
        }
        return INSTANCE;
    }

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getSharedPreferences(getDefaultSharedPreferencesName(context),
                getDefaultSharedPreferencesMode());
    }
    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    private static int getDefaultSharedPreferencesMode() {
        return Context.MODE_PRIVATE;
    }

    public void setString(String key,String value){
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key){
        return preferences.getString(key,"");
    }
}
