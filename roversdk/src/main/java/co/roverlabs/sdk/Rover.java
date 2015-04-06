package co.roverlabs.sdk;

import android.content.Context;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

import co.roverlabs.sdk.events.RoverEnteredLocationEvent;
import co.roverlabs.sdk.events.RoverEnteredTouchpointEvent;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverExitedLocationEvent;
import co.roverlabs.sdk.events.RoverExitedTouchpointEvent;
import co.roverlabs.sdk.events.RoverNotificationEvent;
import co.roverlabs.sdk.events.RoverRangeEvent;
import co.roverlabs.sdk.listeners.RoverEventSaveListener;
import co.roverlabs.sdk.managers.RoverNotificationManager;
import co.roverlabs.sdk.managers.RoverRegionManager;
import co.roverlabs.sdk.managers.RoverVisitManager;
import co.roverlabs.sdk.models.RoverTouchpoint;
import co.roverlabs.sdk.networks.RoverNetworkManager;
import co.roverlabs.sdk.utilities.RoverConstants;
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
    private String mLaunchActivityName;
    private String mCustomerId;
    private String mCustomerName;
    private String mCustomerEmail;
    private Map<String, Object> mCustomerTraits;
    private boolean mIsSimulation;
    //TODO: Get rid of temp fix
    private boolean mSetUp = false;
    private boolean mMonitorStarted = false;

    private Rover(Context con) { mContext = con; }

    public void setSimulation(boolean isSimulation) { mIsSimulation = isSimulation; }

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
        mVisitManager.setCustomer(getCustomerId(), getCustomerName(), getCustomerEmail(), getCustomerTraits());
        mVisitManager.setSimulation(mIsSimulation);
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
            mAppId = RoverUtils.readStringFromSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_APP_ID, null);
        }
        return mAppId; 
    }
    
    public String getAuthToken() {

        if(mAppId == null) {
            mAppId = RoverUtils.readStringFromSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_APP_ID, null);
        }
        return "Bearer " + mAppId; 
    }
    
    public String getUuid() {

        if(mUuid == null) {
            mUuid = RoverUtils.readStringFromSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_UUID, null);
        }
        return mUuid;
    }
    
    public int getNotificationIconId() { 
        
        if(mNotificationIconId == 0) {
            mNotificationIconId = RoverUtils.readIntFromSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_NOTIFICATION_ICON_ID, 0);
            if(mNotificationIconId == 0) {
                mNotificationIconId = R.drawable.rover_icon;
            }
        }
        return mNotificationIconId;
    }

    public String getLaunchActivityName() {

        if (mLaunchActivityName == null) {
            mLaunchActivityName = RoverUtils.readStringFromSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_LAUNCH_ACTIVITY_NAME, null);
        }
        return mLaunchActivityName;
    }

    public String getCustomerId() {

        if(mCustomerId == null) {
            mCustomerId = RoverUtils.readStringFromSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_CUSTOMER_ID, null);
        }
        return mCustomerId;
    }

    public String getCustomerName() {

        if(mCustomerName == null) {
            mCustomerName = RoverUtils.readStringFromSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_CUSTOMER_NAME, null);
        }
        return mCustomerName;
    }

    public String getCustomerEmail() {

        if(mCustomerEmail == null) {
            mCustomerEmail = RoverUtils.readStringFromSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_CUSTOMER_EMAIL, null);
        }
        return mCustomerEmail;
    }

    public Map<String, Object> getCustomerTraits() {

        if(mCustomerTraits == null) {
            mCustomerTraits = new HashMap<>();
            mCustomerTraits = RoverUtils.readMapFromSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_CUSTOMER_TRAITS);
        }
        return mCustomerTraits;
    }

    //Setters
    public void setAppId(String appId) { 
        
        mAppId = appId;
        RoverUtils.writeStringToSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_APP_ID, appId);
    }
    
    public void setUuid(String uuid) { 
        
        mUuid = uuid;
        RoverUtils.writeStringToSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_UUID, uuid);
    }
    
    public void setNotificationIconId(int resourceId) { 
        
        mNotificationIconId = resourceId;
        RoverUtils.writeIntToSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_NOTIFICATION_ICON_ID, resourceId);
    }

    public void setLaunchActivityName(String launchActivityName) {

        mLaunchActivityName = launchActivityName;
        RoverUtils.writeStringToSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_LAUNCH_ACTIVITY_NAME, launchActivityName);
    }

    public void setCustomerId(String customerId) {

        mCustomerId = customerId;
        RoverUtils.writeStringToSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_CUSTOMER_ID, customerId);
    }

    public void setCustomerName(String customerName) {

        mCustomerName = customerName;
        RoverUtils.writeStringToSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_CUSTOMER_NAME, customerName);
    }

    public void setCustomerEmail(String customerEmail) {

        mCustomerEmail = customerEmail;
        RoverUtils.writeStringToSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_CUSTOMER_EMAIL, customerEmail);
    }

    public void setCustomerTraits(Map<String, Object> customerTraits) {

        mCustomerTraits = new HashMap<String, Object>();
        mCustomerTraits = customerTraits;
        RoverUtils.writeMapToSharedPrefs(mContext, RoverConstants.SHARED_PREFS_NAME_CUSTOMER_TRAITS, customerTraits);
    }

    public void startMonitoring() {

        if(!mMonitorStarted) {
            Log.d(TAG, "Monitoring is being started by Rover");
            mRegionManager.startMonitoring();
            mMonitorStarted = true;
        }
        else {
            Log.d(TAG, "Monitoring was already started by Rover - do nothing");
        }
    }

    public void stopMonitoring() {

        if(mMonitorStarted) {
            Log.d(TAG, "Monitoring is being stopped by Rover");
            mRegionManager.stopMonitoring();
            mMonitorStarted = false;
        }
        else {
            Log.d(TAG, "Monitoring was already stopped by Rover - do nothing");
        }
    }
    
    @Subscribe
    public void onEnteredLocation(final RoverEnteredLocationEvent event) {

        event.send(new RoverEventSaveListener() {

            @Override
            public void onSaveSuccess() {

                Log.d(TAG, "Event sent successfully - enter location");
            }

            @Override
            public void onSaveFailure() {

                Log.d(TAG, "Event sent unsuccessfully - enter location");
            }
        });
    }

    @Subscribe
    public void onExitedLocation(final RoverExitedLocationEvent event) {

        event.send(new RoverEventSaveListener() {

            @Override
            public void onSaveSuccess() {

                Log.d(TAG, "Event sent successfully - exit location");
            }

            @Override
            public void onSaveFailure() {

                Log.d(TAG, "Event sent unsuccessfully - exit location");
            }
        });
    }

    @Subscribe
    public void onRangeAction(RoverRangeEvent event) {

        if(event.getAction().equals(RoverConstants.RANGE_ACTION_START)) {
            mRegionManager.startRanging();
        }
        else if(event.getAction().equals(RoverConstants.RANGE_ACTION_STOP)) {
            mRegionManager.stopRanging();
        }
    }

    @Subscribe
    public void onEnteredTouchpoint(final RoverEnteredTouchpointEvent event) {

        if(!event.hasBeenVisited()) {
            RoverTouchpoint touchpoint = event.getTouchpoint();
            String touchpointId = touchpoint.getId();
            String numberOnlyId = touchpointId.replaceAll("[^0-9]", "");
            numberOnlyId = numberOnlyId.substring(Math.max(0, numberOnlyId.length() - 7));
            int id = Integer.valueOf(numberOnlyId);
            String title = touchpoint.getTitle();
            String message = touchpoint.getNotification();
            RoverNotificationEvent notificationEvent = null;
            try {
                notificationEvent = new RoverNotificationEvent(id, title, message, Class.forName(getLaunchActivityName()));
                if(touchpoint.getMinor() != null) {
                    Log.d(TAG, "Sending notification - touchpoint minor " + touchpoint.getMinor() + " (" + touchpoint.getTitle() + ")");
                }
                else {
                    Log.d(TAG, "Sending notification - touchpoint wild card (" + touchpoint.getTitle() + ")");
                }
                RoverEventBus.getInstance().post(notificationEvent);
            }
            catch (ClassNotFoundException e) {
                Log.e(TAG, "Cannot send notification - cannot find launch activity name", e);
            }
        }

        event.send(new RoverEventSaveListener() {

            @Override
            public void onSaveSuccess() {

                if(event.getTouchpoint().getMinor() != null) {
                    Log.d(TAG, "Event sent successfully - enter touchpoint minor " + event.getTouchpoint().getMinor() + " (" + event.getTouchpoint().getTitle() + ")");
                }
                else {
                    Log.d(TAG, "Event sent successfully - enter touchpoint wild card (" + event.getTouchpoint().getTitle() + ")");
                }
            }

            @Override
            public void onSaveFailure() {

                if(event.getTouchpoint().getMinor() != null) {
                    Log.d(TAG, "Event sent unsuccessfully - enter touchpoint minor " + event.getTouchpoint().getMinor() + " (" + event.getTouchpoint().getTitle() + ")");
                }
                else {
                    Log.d(TAG, "Event sent unsuccessfully - enter touchpoint wild card (" + event.getTouchpoint().getTitle() + ")");
                }
            }
        });

    }

    @Subscribe
    public void onExitedTouchpoint(final RoverExitedTouchpointEvent event) {

        event.send(new RoverEventSaveListener() {

            @Override
            public void onSaveSuccess() {

                if(event.getTouchpoint().getMinor() != null) {
                    Log.d(TAG, "Event sent successfully - exit touchpoint minor " + event.getTouchpoint().getMinor() + " (" + event.getTouchpoint().getTitle() + ")");
                }
                else {
                    Log.d(TAG, "Event sent successfully - exit touchpoint wild card (" + event.getTouchpoint().getTitle() + ")");
                }
            }

            @Override
            public void onSaveFailure() {

                if(event.getTouchpoint().getMinor() != null) {
                    Log.d(TAG, "Event sent unsuccessfully - exit touchpoint minor " + event.getTouchpoint().getMinor() + " (" + event.getTouchpoint().getTitle() + ")");
                }
                else {
                    Log.d(TAG, "Event sent unsuccessfully - exit touchpoint wild card (" + event.getTouchpoint().getTitle() + ")");
                }
            }
        });
    }
}
