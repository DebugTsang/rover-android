package co.roverlabs.sdk;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by SherryYang on 2015-01-27.
 */
public class RoverUtils {
    
    private static final String TAG = RoverUtils.class.getName();
    private static final String SHARED_PREFS_NAME = "RoverPrefs";
    
    public static String readFromSharedPreferences(Context con, String key, String defaultValue) {

        SharedPreferences sharedPreferences = con.getSharedPreferences(SHARED_PREFS_NAME, 0);
        return sharedPreferences.getString(key, defaultValue);
    }
    
    public static void writeToSharedPreferences(Context con, String key, String value) {
        
        SharedPreferences sharedPreferences = con.getSharedPreferences(SHARED_PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    
    public static Object readFromSharedPreferences(Context con, Class customObjectClass, String defaultValue) {
        
        SharedPreferences sharedPreferences = con.getSharedPreferences(SHARED_PREFS_NAME, 0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(customObjectClass.getSimpleName(), defaultValue);
        return gson.fromJson(json, customObjectClass);
    }
    
    public static void writeToSharedPreferences(Context con, String key, Object customObject) {

        SharedPreferences sharedPreferences = con.getSharedPreferences(SHARED_PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customObject);
        editor.putString(key, json);
        editor.apply();
    }
}
