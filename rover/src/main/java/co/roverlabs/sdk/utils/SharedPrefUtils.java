package co.roverlabs.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by SherryYang on 2015-01-27.
 */
public class SharedPrefUtils {
    
    public static final String TAG = SharedPrefUtils.class.getSimpleName();

    public static boolean readBoolFromSharedPrefs(Context con, String key, boolean defaultValue) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(key, defaultValue);
    }

    public static void writeBoolToSharedPrefs(Context con, String key, boolean value) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String readStringFromSharedPrefs(Context con, String key, String defaultValue) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(key, defaultValue);
    }
    
    public static void writeStringToSharedPrefs(Context con, String key, String value) {
        
        SharedPreferences sharedPrefs = con.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }
    
    public static int readIntFromSharedPrefs(Context con, String key, int defaultValue) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getInt(key, defaultValue);
    }

    public static void writeIntToSharedPrefs(Context con, String key, int value) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static Map<String, Object> readMapFromSharedPrefs(Context con, String key) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(key, null);
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static void writeMapToSharedPrefs(Context con, String key, Map<String, Object> value) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.apply();
    }
    
    public static Object readObjectFromSharedPrefs(Context con, Class customObjectClass, String defaultValue) {
        
        SharedPreferences sharedPrefs = con.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(customObjectClass.getSimpleName(), defaultValue);
        return gson.fromJson(json, customObjectClass);
    }
    
    public static void writeObjectToSharedPrefs(Context con, Object customObject) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customObject);
        editor.putString(customObject.getClass().getSimpleName(), json);
        editor.apply();
    }

    public static void removeObjectFromSharedPrefs(Context con, Class customObjectClass) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(customObjectClass.getSimpleName());
        editor.apply();
    }

    public static void clearSharedPrefs(Context con) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
    }



}


