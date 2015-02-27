package co.roverlabs.sdk;

import android.content.Context;

import co.roverlabs.sdk.managers.RoverRegionManager;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class Rover {
    
    private static final String TAG = Rover.class.getName();
    private static Rover sRoverInstance;
    private Context mContext;
    private static String mUuid;
    private static String mAppId;
    private static int mIconResourceId;

    //Constructor
    private Rover(Context con) { mContext = con; }
    
    public static Rover getInstance(Context con) {
        
        if(sRoverInstance == null) {
            sRoverInstance = new Rover(con);
        }
        return sRoverInstance;
    }
    
    //Getters
    public static String getAppId() { return mAppId; }
    public static String getAuthToken() { return "Bearer " + mAppId; }
    public static String getUuid() { return mUuid; }
    public static int getIconResourceId() { return mIconResourceId; }
    
    //Setters
    public void setAppId(String appId) { mAppId = appId; }
    public void setUuid(String uuid) { mUuid = uuid; }
    public void setIconResourceId(int resourceId) { mIconResourceId = resourceId; }
    
    public void startMonitoring() { 
        
        RoverRegionManager.getInstance(mContext).startMonitoring(); 
    }
    
    public void stopMonitoring() { 
        
        RoverRegionManager.getInstance(mContext).stopMonitoring(); 
    }
}
