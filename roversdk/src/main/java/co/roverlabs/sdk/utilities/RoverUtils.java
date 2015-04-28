package co.roverlabs.sdk.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by SherryYang on 2015-01-27.
 */
public class RoverUtils {
    
    public static final String TAG = RoverUtils.class.getSimpleName();

    public static boolean readBoolFromSharedPrefs(Context con, String key, boolean defaultValue) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(RoverConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(key, defaultValue);
    }

    public static void writeBoolToSharedPrefs(Context con, String key, boolean value) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(RoverConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String readStringFromSharedPrefs(Context con, String key, String defaultValue) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(RoverConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(key, defaultValue);
    }
    
    public static void writeStringToSharedPrefs(Context con, String key, String value) {
        
        SharedPreferences sharedPrefs = con.getSharedPreferences(RoverConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }
    
    public static int readIntFromSharedPrefs(Context con, String key, int defaultValue) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(RoverConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getInt(key, defaultValue);
    }

    public static void writeIntToSharedPrefs(Context con, String key, int value) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(RoverConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static Map<String, Object> readMapFromSharedPrefs(Context con, String key) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(RoverConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(key, null);
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static void writeMapToSharedPrefs(Context con, String key, Map<String, Object> value) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(RoverConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.apply();
    }
    
    public static Object readObjectFromSharedPrefs(Context con, Class customObjectClass, String defaultValue) {
        
        SharedPreferences sharedPrefs = con.getSharedPreferences(RoverConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(customObjectClass.getSimpleName(), defaultValue);
        return gson.fromJson(json, customObjectClass);
    }
    
    public static void writeObjectToSharedPrefs(Context con, Object customObject) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(RoverConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customObject);
        editor.putString(customObject.getClass().getSimpleName(), json);
        editor.apply();
    }

    public static void removeObjectFromSharedPrefs(Context con, Class customObjectClass) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(RoverConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(customObjectClass.getSimpleName());
        editor.apply();
    }

    public static void clearSharedPrefs(Context con) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(RoverConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.apply();
    }

    public static List subtractList(List list1, List list2) {

        List result = new ArrayList(list2);
        result.removeAll(list1);
        return result;
    }

    public static int convertDpToPx(Context con, float dp) {

        final float scale = con.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static Bitmap getScaledBitmap(Context con, Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = convertDpToPx(con, width);
        int newHeight = convertDpToPx(con, height);
        float scaleWidth = ((float)newWidth) / width;
        float scaleHeight = ((float)newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }

    public static void setImageMode(Context con, Bitmap bitmap, ImageView imageView, String imageMode) {

        Bitmap resizedBitmap = RoverUtils.getScaledBitmap(con, bitmap);
        BitmapDrawable backgroundDrawable = new BitmapDrawable(con.getResources(), resizedBitmap);

        switch(imageMode) {

            case RoverConstants.IMAGE_MODE_STRETCH:
                imageView.setImageDrawable(backgroundDrawable);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                break;

            case RoverConstants.IMAGE_MODE_TILE:
                backgroundDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                imageView.setBackground(backgroundDrawable);
                break;

            case RoverConstants.IMAGE_MODE_FILL:
                imageView.setImageDrawable(backgroundDrawable);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;

            case RoverConstants.IMAGE_MODE_FIT:
                imageView.setImageDrawable(backgroundDrawable);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                break;

            default:
                imageView.setImageDrawable(backgroundDrawable);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
        }
    }
}
