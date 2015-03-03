package co.roverlabs.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import co.roverlabs.sdk.managers.RoverRegionManager;
import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class Rover {

    private static final String TAG = Rover.class.getName();
    private static Rover sRoverInstance;
    private Context mContext;
    private String mUuid;
    private String mAppId;
    private int mNotificationIconId;

    //Constructor
    private Rover(Context con) { mContext = con; }

    public static Rover getInstance(Context con) {

        if (sRoverInstance == null) {
            sRoverInstance = new Rover(con);
        }
        return sRoverInstance;
    }

    //Getters
    public String getAppId() { 
        
        if(mAppId == null) {
            mAppId = RoverUtils.readStringFromSharedPreferences(mContext, "AppId", null);
        }
        return mAppId; 
    }
    
    public String getAuthToken() {

        if(mAppId == null) {
            mAppId = RoverUtils.readStringFromSharedPreferences(mContext, "AppId", null);
        }
        return "Bearer " + mAppId; 
    }
    
    public String getUuid() {

        if(mUuid == null) {
            mUuid = RoverUtils.readStringFromSharedPreferences(mContext, "UUID", null);
        }
        return mUuid;
    }
    
    public int getNotificationIconId() { 
        
        if(mNotificationIconId == 0) {
            mNotificationIconId = RoverUtils.readIntFromSharedPreferences(mContext, "NotificationIconId", 0);
        }
        return mNotificationIconId;
    }

    //Setters
    public void setAppId(String appId) { 
        
        mAppId = appId;
        RoverUtils.writeStringToSharedPreferences(mContext, "AppId", appId);
    }
    
    public void setUuid(String uuid) { 
        
        mUuid = uuid;
        RoverUtils.writeStringToSharedPreferences(mContext, "UUID", uuid);
    }
    
    public void setNotificationIconId(int resourceId) { 
        
        mNotificationIconId = resourceId;
        RoverUtils.writeIntToSharedPreferences(mContext, "NotificationIconId", resourceId);
    }

    public void startMonitoring() {

        RoverRegionManager.getInstance(mContext).startMonitoring();
    }

    public void stopMonitoring() {

        RoverRegionManager.getInstance(mContext).stopMonitoring();
    }
}
