package co.roverlabs.sdk;

import android.content.Context;
import android.util.Log;

import com.squareup.otto.Subscribe;

import co.roverlabs.sdk.events.RoverEnteredLocationEvent;
import co.roverlabs.sdk.events.RoverEnteredTouchpointEvent;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverNotificationEvent;
import co.roverlabs.sdk.managers.RoverNotificationManager;
import co.roverlabs.sdk.managers.RoverRegionManager;
import co.roverlabs.sdk.managers.RoverVisitManager;
import co.roverlabs.sdk.models.RoverTouchpoint;
import co.roverlabs.sdk.networks.RoverNetworkManager;
import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class Rover {

    public static final String TAG = Rover.class.getSimpleName();
    private static Rover sRoverInstance;
    private Context mContext;
    private String mLaunchActivityName;
    private RoverRegionManager mRegionManager;
    private RoverVisitManager mVisitManager;
    private RoverNetworkManager mNetworkManager;
    private RoverNotificationManager mNotificationManager;
    private String mUuid;
    private String mAppId;
    private int mNotificationIconId;
    //TODO: Get rid of temp fix
    private boolean mSetUp = false;
    private boolean mMonitoringStarted = false;

    //Constructor
    private Rover(Context con) { mContext = con; }

    public static Rover getInstance(Context con) {

        if (sRoverInstance == null) {
            Log.d(TAG, "Rover is null");
            sRoverInstance = new Rover(con);
        }
        else {
            Log.d(TAG, "Rover is not null");
        }
        return sRoverInstance;
    }
    
    public void completeSetUp() {
        
        if(!mSetUp) {
            Log.d(TAG, "Setting Rover up for the first time");
            RoverEventBus.getInstance().register(this);
            setRegionManager();
            setVisitManager();
            setNetworkManager();
            setNotificationManager();
            mSetUp = true;
        }
        else {
            Log.d(TAG, "Rover has already been set up");
        }
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
            if(mNotificationIconId == 0) {
                mNotificationIconId = R.drawable.rover_icon;
            }
        }
        return mNotificationIconId;
    }

    public String getLaunchActivityName() {

        if (mLaunchActivityName == null) {
            mLaunchActivityName = RoverUtils.readStringFromSharedPreferences(mContext, "LaunchActivityName", null);
        }
        return mLaunchActivityName;
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

    public void setLaunchActivityName(String launchActivityName) {

        mLaunchActivityName = launchActivityName;
        RoverUtils.writeStringToSharedPreferences(mContext, "LaunchActivityName", launchActivityName);
    }

    public void startMonitoring() {

        if(!mMonitoringStarted) {
            Log.d(TAG, "Rover is causing monitoring to start");
            mRegionManager.startMonitoring();
            mMonitoringStarted = true;
        }
        else {
            Log.d(TAG, "Monitoring has already started - do nothing");
        }
    }

    public void stopMonitoring() {

        mRegionManager.stopMonitoring();
    }
    
    @Subscribe
    public void onEnteredLocation(RoverEnteredLocationEvent event) {

        //TODO: Update open time
        mRegionManager.startRanging();
    }

    @Subscribe
    public void onEnteredTouchpoint(RoverEnteredTouchpointEvent event) {

        Log.d(TAG, "sending notification");
        //TODO: Filter which touchpoint to use for notification based on server result
        RoverTouchpoint touchpoint = event.getTouchpoint();
        int id = touchpoint.getMinor();
        String title = touchpoint.getTitle();
        String message = touchpoint.getNotification();
        //TODO: Better system for notification IDs
        RoverNotificationEvent notificationEvent = null;
        try {
            notificationEvent = new RoverNotificationEvent(id, title, message, Class.forName(mLaunchActivityName));
        }
        catch (ClassNotFoundException e) {
            Log.e(TAG, "Cannot find launch activity name", e);
        }
        RoverEventBus.getInstance().post(notificationEvent);
    }
}
