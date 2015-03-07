package co.roverlabs.sdk;

import android.content.Context;

import co.roverlabs.sdk.managers.RoverNotificationManager;
import co.roverlabs.sdk.managers.RoverRegionManager;
import co.roverlabs.sdk.managers.RoverVisitManager;
import co.roverlabs.sdk.networks.RoverNetworkManager;
import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class Rover {

    public static final String TAG = Rover.class.getSimpleName();
    private static Rover sRoverInstance;
    private Context mContext;
    private RoverRegionManager mRegionManager;
    private RoverVisitManager mVisitManager;
    private RoverNetworkManager mNetworkManager;
    private RoverNotificationManager mNotificationManager;
    private String mUuid;
    private String mAppId;
    private int mNotificationIconId;
    //TODO: Get rid of temp fix
    private static boolean sSetUp = false;

    //Constructor
    private Rover(Context con) { 
        
        mContext = con;
        setRegionManager();
        setVisitManager();
        setNetworkManager();
        setNotificationManager();
        sSetUp = true;
    }

    public static Rover getInstance(Context con) {

        if (sRoverInstance == null) {
            sRoverInstance = new Rover(con);
        }
        return sRoverInstance;
    }
    
    private void setRegionManager() {
        
        mRegionManager = RoverRegionManager.getInstance(mContext);
        mRegionManager.setMonitorRegion(getUuid());
    }
    
    private void setVisitManager() {
        
        mVisitManager = RoverVisitManager.getInstance(mContext);
    }
    
    private void setNetworkManager() {
        
        mNetworkManager = RoverNetworkManager.getInstance();
        mNetworkManager.setAuthToken(getAuthToken());
    }
    
    private void setNotificationManager() {
        
        mNotificationManager = RoverNotificationManager.getInstance(mContext);
        mNotificationManager.setNotificationIconId(getNotificationIconId());
    }
    
    public static boolean isSetUp() {
        
        return sSetUp;
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

        mRegionManager.startMonitoring();
    }

    public void stopMonitoring() {

        mRegionManager.stopMonitoring();
    }
}
