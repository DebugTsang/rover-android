package co.roverlabs.sdk;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.Map;
import java.util.UUID;

import co.roverlabs.sdk.events.RoverCardClickedEvent;
import co.roverlabs.sdk.events.RoverCardDeliveredEvent;
import co.roverlabs.sdk.events.RoverCardDiscardedEvent;
import co.roverlabs.sdk.events.RoverCardViewedEvent;
import co.roverlabs.sdk.events.RoverEnteredLocationEvent;
import co.roverlabs.sdk.events.RoverEnteredRegionEvent;
import co.roverlabs.sdk.events.RoverEnteredTouchpointEvent;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverExitedLocationEvent;
import co.roverlabs.sdk.events.RoverExitedRegionEvent;
import co.roverlabs.sdk.events.RoverExitedTouchpointEvent;
import co.roverlabs.sdk.events.RoverRangeEvent;
import co.roverlabs.sdk.events.RoverVisitExpiredEvent;
import co.roverlabs.sdk.listeners.RoverEventSaveListener;
import co.roverlabs.sdk.managers.RoverNotificationManager;
import co.roverlabs.sdk.managers.RoverRegionManager;
import co.roverlabs.sdk.managers.RoverVisitManager;
import co.roverlabs.sdk.models.RoverCustomer;
import co.roverlabs.sdk.models.RoverRegion;
import co.roverlabs.sdk.models.RoverTouchPoint;
import co.roverlabs.sdk.networks.RoverNetworkManager;
import co.roverlabs.sdk.utilities.Factory;
import co.roverlabs.sdk.utilities.RoverConstants;
import co.roverlabs.sdk.utilities.RoverUtils;

public class Rover {

    public static final String TAG = Rover.class.getSimpleName();
    private static Rover sRoverInstance;
    private Context mContext;
    private RoverRegionManager mRegionManager;
    private RoverVisitManager mVisitManager;
    private RoverNetworkManager mNetworkManager;
    private RoverNotificationManager mNotificationManager;
    private RoverConfigs mConfigs;
    private RoverCustomer mCustomer;
    //TODO: Get rid of temp fix
    private boolean mSetUp = false;
    private boolean mMonitorStarted = false;

    private Rover(Context con) { mContext = con.getApplicationContext(); }

    public static Rover getInstance(Context con) {

        if(sRoverInstance == null) {
            Log.d(TAG, "Rover is null");
            sRoverInstance = new Rover(con);
        }
        else {
            Log.d(TAG, "Rover is not null");
        }
        return sRoverInstance;
    }

    public RoverCustomer resetCustomer() {

        mCustomer = new RoverCustomer();
        mCustomer.setId(createCustomerId());
        RoverUtils.writeObjectToSharedPrefs(mContext, mCustomer);
        if(mVisitManager != null) {
            mVisitManager.resetVisit();
        }
        return mCustomer;
    }

    public void resetVisit() {

        if(mVisitManager != null) {
            mVisitManager.resetVisit();
        }
    }

    public void setCustomer(RoverCustomer customer) {

        String name = customer.getName();
        String email = customer.getEmail();
        Map<String, Object> traits = customer.getTraits();

        getCustomer();

        if(name != null && !TextUtils.isEmpty(name)) {
            mCustomer.setName(name);
        }
        if(email != null && !TextUtils.isEmpty(email)) {
            mCustomer.setEmail(email);
        }
        if(traits != null && !traits.isEmpty()) {
            mCustomer.setTraits(traits);
        }

        RoverUtils.writeObjectToSharedPrefs(mContext, mCustomer);
    }

    public void setConfigurations(RoverConfigs configs) {

        if(!configs.isComplete()) {
            Log.d(TAG, "Rover cannot be set up - configurations incomplete");
            return;
        }
        mConfigs = configs;
        RoverUtils.writeObjectToSharedPrefs(mContext, configs);
    }

    private void completeSetUp() {

        Log.d(TAG, "Setting up Rover");
        if(mCustomer == null) {
            getCustomer();
        }
        if(mConfigs == null) {
            getConfigurations();
        }
        RoverEventBus.getInstance().register(this);
        setRegionManager(mConfigs.getUuid());
        setVisitManager(mConfigs.getSandBoxMode());
        setNetworkManager(mConfigs.getAuthToken());
        setNotificationManager(mConfigs.getNotificationIconId());
        mSetUp = true;
    }

    private void setRegionManager(String uuid) {

        mRegionManager = RoverRegionManager.getInstance(mContext);
        mRegionManager.setMonitorRegion(uuid);
    }

    private void setVisitManager(boolean sandBoxMode) {

        mVisitManager = RoverVisitManager.getInstance(mContext);
        mVisitManager.setSandBoxMode(sandBoxMode);
    }

    private void setNetworkManager(String authToken) {

        mNetworkManager = RoverNetworkManager.getInstance();
        mNetworkManager.setAuthToken(authToken);
    }

    private void setNotificationManager(int notificationIconId) {

        mNotificationManager = RoverNotificationManager.getInstance(mContext);
        mNotificationManager.setNotificationIconId(notificationIconId);
    }

    public RoverConfigs getConfigurations() {

        mConfigs = (RoverConfigs)RoverUtils.readObjectFromSharedPrefs(mContext, RoverConfigs.class, null);
        return mConfigs;
    }

    public void stopHeadService(){
        RoverEventBus.getInstance().post(new RoverVisitExpiredEvent());
    }

    public RoverCustomer getCustomer() {

        mCustomer = (RoverCustomer)RoverUtils.readObjectFromSharedPrefs(mContext, RoverCustomer.class, null);
        if(mCustomer == null) {
            Log.d(TAG, "Creating a new customer");
            mCustomer = new RoverCustomer();
            mCustomer.setId(createCustomerId());
            RoverUtils.writeObjectToSharedPrefs(mContext, mCustomer);
            Log.d(TAG, "Customer ID is " + mCustomer.getId());
        }
        return mCustomer;
    }

    private String createCustomerId() {

        return UUID.randomUUID().toString();
    }

    public boolean isMonitoring() {

        return mMonitorStarted;
    }

    public void startMonitoring() {

        if(!mSetUp) {
            Log.d(TAG, "Setting up Rover before monitoring can start");
            completeSetUp();
        }
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

        if(mSetUp) {
            if(mMonitorStarted) {
                mRegionManager.stopMonitoring();
                mRegionManager.stopRanging();
                mMonitorStarted = false;
            }
            else {
                Log.d(TAG, "Monitoring was already stopped by Rover - do nothing");
            }
        }
        else {
            Log.d(TAG, "Rover has not been set up yet - do nothing");
        }
    }

    //TODO: Remove, used for testing
    public void showCards() {

        Log.d(TAG, "Monitoring was already started by Rover - do nothing");

        if(mVisitManager != null) {
            mVisitManager.showCards();
        }
        else {
            Toast.makeText(mContext, R.string.no_cards_text, Toast.LENGTH_SHORT).show();
        }
    }

    //TODO: Remove, used for testing
    public void simulate() {

        stopMonitoring();

        Log.d(TAG, "Simulating");

        RoverRegion enteredRegion = new RoverRegion("7931D3AA-299B-4A12-9FCC-D66F2C5D2462", 18347, 11111);
        RoverEventBus.getInstance().post(new RoverEnteredRegionEvent(enteredRegion));
        RoverRegion exitedMainRegion = new RoverRegion("7931D3AA-299B-4A12-9FCC-D66F2C5D2462", null, null);
        RoverEventBus.getInstance().post(new RoverExitedRegionEvent(exitedMainRegion, RoverConstants.REGION_TYPE_MAIN));
    }

    @Subscribe
    public void onEnteredLocation(final RoverEnteredLocationEvent event) {

        if(!mConfigs.getSandBoxMode()) {
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
    }

    @Subscribe
    public void onExitedLocation(final RoverExitedLocationEvent event) {

        if(!mConfigs.getSandBoxMode()) {
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
            RoverTouchPoint touchpoint = event.getTouchpoint();
            String touchpointId = touchpoint.getId();
            String numberOnlyId = touchpointId.replaceAll("[^0-9]", "");
            numberOnlyId = numberOnlyId.substring(Math.max(0, numberOnlyId.length() - 7));
            int id = Integer.valueOf(numberOnlyId);
            String title = touchpoint.getTitle();
            String message = touchpoint.getNotification();

            //prefetch images before notifying the user
            Factory.getDefaultImageLoader(mContext.getApplicationContext()).fetchAll();

            mNotificationManager.showStickyNotification(id, title, message);
        }

        if(!mConfigs.getSandBoxMode()) {
            event.send(new RoverEventSaveListener() {

                @Override
                public void onSaveSuccess() {

                    if (event.getTouchpoint().getMinor() != null) {
                        Log.d(TAG, "Event sent successfully - enter touchpoint minor " + event.getTouchpoint().getMinor() + " (" + event.getTouchpoint().getTitle() + ")");
                    }
                    else {
                        Log.d(TAG, "Event sent successfully - enter touchpoint wild card (" + event.getTouchpoint().getTitle() + ")");
                    }
                }

                @Override
                public void onSaveFailure() {

                    if (event.getTouchpoint().getMinor() != null) {
                        Log.d(TAG, "Event sent unsuccessfully - enter touchpoint minor " + event.getTouchpoint().getMinor() + " (" + event.getTouchpoint().getTitle() + ")");
                    }
                    else {
                        Log.d(TAG, "Event sent unsuccessfully - enter touchpoint wild card (" + event.getTouchpoint().getTitle() + ")");
                    }
                }
            });
        }

    }

    @Subscribe
    public void onExitedTouchpoint(final RoverExitedTouchpointEvent event) {

        if(!mConfigs.getSandBoxMode()) {
            event.send(new RoverEventSaveListener() {

                @Override
                public void onSaveSuccess() {

                    if (event.getTouchpoint().getMinor() != null) {
                        Log.d(TAG, "Event sent successfully - exit touchpoint minor " + event.getTouchpoint().getMinor() + " (" + event.getTouchpoint().getTitle() + ")");
                    }
                    else {
                        Log.d(TAG, "Event sent successfully - exit touchpoint wild card (" + event.getTouchpoint().getTitle() + ")");
                    }
                }

                @Override
                public void onSaveFailure() {

                    if (event.getTouchpoint().getMinor() != null) {
                        Log.d(TAG, "Event sent unsuccessfully - exit touchpoint minor " + event.getTouchpoint().getMinor() + " (" + event.getTouchpoint().getTitle() + ")");
                    }
                    else {
                        Log.d(TAG, "Event sent unsuccessfully - exit touchpoint wild card (" + event.getTouchpoint().getTitle() + ")");
                    }
                }
            });
        }
    }

    @Subscribe
    public void onCardDelivered(final RoverCardDeliveredEvent event) {

        if(!mConfigs.getSandBoxMode()) {
            event.send(new RoverEventSaveListener() {

                @Override
                public void onSaveSuccess() {

                    Log.d(TAG, "Event sent successfully - deliver card " + event.getCardId());
                }

                @Override
                public void onSaveFailure() {

                    Log.d(TAG, "Event sent unsuccessfully - deliver card " + event.getCardId());
                }
            });
        }
    }

    @Subscribe
    public void onCardViewed(final RoverCardViewedEvent event) {

        if(!mConfigs.getSandBoxMode()) {
            event.send(new RoverEventSaveListener() {

                @Override
                public void onSaveSuccess() {

                    Log.d(TAG, "Event sent successfully - view card " + event.getCardId());
                }

                @Override
                public void onSaveFailure() {

                    Log.d(TAG, "Event sent unsuccessfully - view card " + event.getCardId());
                }
            });
        }
    }

    @Subscribe
    public void onCardClicked(final RoverCardClickedEvent event) {

        if(!mConfigs.getSandBoxMode()) {
            event.send(new RoverEventSaveListener() {

                @Override
                public void onSaveSuccess() {

                    Log.d(TAG, "Event sent successfully - click card " + event.getCardId());
                }

                @Override
                public void onSaveFailure() {

                    Log.d(TAG, "Event sent unsuccessfully - click card " + event.getCardId());
                }
            });
        }
    }

    @Subscribe
    public void onCardDiscarded(final RoverCardDiscardedEvent event) {

        if(!mConfigs.getSandBoxMode()) {
            event.send(new RoverEventSaveListener() {

                @Override
                public void onSaveSuccess() {

                    Log.d(TAG, "Event sent successfully - discard card " + event.getCardId());
                }

                @Override
                public void onSaveFailure() {

                    Log.d(TAG, "Event sent unsuccessfully - discard card " + event.getCardId());
                }
            });
        }
    }
}