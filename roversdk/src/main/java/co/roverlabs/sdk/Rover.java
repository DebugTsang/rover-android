package co.roverlabs.sdk;

import android.content.Context;

import co.roverlabs.sdk.managers.RoverRegionManager;
import co.roverlabs.sdk.utilities.RoverConstants;
import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class Rover {
    
    private static final String TAG = Rover.class.getName();
    private static Rover sRoverInstance;
    private Context mContext;
    private int mIconResourceId;

    private Rover(Context con) {
        
        mContext = con;
    }
    
    public static Rover getInstance(Context con) {
        
        if(sRoverInstance == null) {
            sRoverInstance = new Rover(con);
        }
        return sRoverInstance;
    }
    
    public void setAppId(String appId) {

        RoverUtils.writeToSharedPreferences(mContext, "appId", appId);
    }
    
    public String getAppId() {

        return RoverUtils.readFromSharedPreferences(mContext, "appId", null);
    }
    
    public String getAuthToken() {
        
        return "Bearer " + getAppId();
    }
    
    public void setUUID(String uuid) {

        RoverUtils.writeToSharedPreferences(mContext, "UUID", uuid);
    }
    
    public String getUUID() {

        return RoverUtils.readFromSharedPreferences(mContext, "UUID", RoverConstants.ESTIMOTE_DEFAULT_UUID);
    }
    
    public void setIconResourceId(int resourceId) {

        mIconResourceId = resourceId;
    }
    
    public int getIconResourceId() {
        
        return mIconResourceId;
    }
    
    public void startMonitoring() {
        
        RoverRegionManager.getInstance(mContext).startMonitoring();
    }
    
    public void stopMonitoring() {
        
        RoverRegionManager.getInstance(mContext).stopMonitoring();
    }
}
