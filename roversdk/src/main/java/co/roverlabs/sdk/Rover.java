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
import co.roverlabs.sdk.ui.CardListActivity;
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
    private boolean mSetUp = false;
    private boolean mMonitoringStarted = false;

    //Constructor
    private Rover(Context con) { mContext = con; }

    public static Rover getInstance(Context con) {

        if (sRoverInstance == null) {
            sRoverInstance = new Rover(con);
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
        mNotificationManager.clearNotificationEvents();
        mRegionManager.startRanging();
    }

    @Subscribe
    public void onEnteredTouchpoint(RoverEnteredTouchpointEvent event) {

        Log.d(TAG, "sending notification");
        RoverTouchpoint touchpoint = event.getTouchpoint();
        String id = touchpoint.getId();
        String title = touchpoint.getTitle();
        String message = touchpoint.getNotification();
        RoverNotificationEvent notificationEvent = new RoverNotificationEvent(id, title, message, CardListActivity.class);
        if(!mNotificationManager.getNotificationEvents().contains(notificationEvent)) {
            mNotificationManager.addNotificationEvent(notificationEvent);
        }
        RoverEventBus.getInstance().post(notificationEvent);
    }
}
