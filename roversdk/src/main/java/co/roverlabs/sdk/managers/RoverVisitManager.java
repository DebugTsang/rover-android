package co.roverlabs.sdk.managers;

import android.content.Context;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.Calendar;
import java.util.Map;

import co.roverlabs.sdk.events.RoverEnteredLocationEvent;
import co.roverlabs.sdk.events.RoverEnteredRegionEvent;
import co.roverlabs.sdk.events.RoverEnteredTouchpointEvent;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverExitedLocationEvent;
import co.roverlabs.sdk.events.RoverExitedRegionEvent;
import co.roverlabs.sdk.events.RoverExitedTouchpointEvent;
import co.roverlabs.sdk.events.RoverRangeEvent;
import co.roverlabs.sdk.listeners.RoverObjectSaveListener;
import co.roverlabs.sdk.models.RoverCustomer;
import co.roverlabs.sdk.models.RoverRegion;
import co.roverlabs.sdk.models.RoverTouchpoint;
import co.roverlabs.sdk.models.RoverVisit;
import co.roverlabs.sdk.utilities.RoverConstants;
import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class RoverVisitManager {
    
    public static final String TAG = RoverVisitManager.class.getSimpleName();
    private static RoverVisitManager sVisitManagerInstance;
    private Context mContext;
    private RoverVisit mLatestVisit;
    private RoverCustomer mCustomer;
    
    //Constructor
    private RoverVisitManager(Context con) { 
        
        mContext = con;
        RoverEventBus.getInstance().register(this);
    }

    public static RoverVisitManager getInstance(Context con) {

        if(sVisitManagerInstance == null) {
            sVisitManagerInstance = new RoverVisitManager(con);
        }
        return sVisitManagerInstance;
    }

    public void setCustomer(String id, String name, String email, Map<String, Object> traits) {

        mCustomer = new RoverCustomer();
        mCustomer.setId(id);
        mCustomer.setName(name);
        mCustomer.setEmail(email);
        mCustomer.setTraits(traits);
    }

    @Subscribe
    public void didEnterRegion(RoverEnteredRegionEvent event) {

        final RoverRegion subRegion = event.getRegion();
        final RoverRegion mainRegion = new RoverRegion(subRegion.getUuid(), subRegion.getMajor(), null);

        if(getLatestVisit() != null && mLatestVisit.isInRegion(mainRegion) && mLatestVisit.isAlive()) {
            if(mLatestVisit.isValidTouchpoint(subRegion) && !mLatestVisit.isInTouchpoint(subRegion)) {
                didEnterTouchpoint(subRegion);
            }
            RoverEventBus.getInstance().post(new RoverRangeEvent(RoverConstants.RANGE_ACTION_START));
            return;
        }

        Calendar now = Calendar.getInstance();
        mLatestVisit = new RoverVisit();
        mLatestVisit.setCustomer(mCustomer);
        mLatestVisit.setRegion(mainRegion);
        mLatestVisit.setTimeStamp(now.getTime());
        mLatestVisit.setLastBeaconDetectionTime(now);

        mLatestVisit.save(new RoverObjectSaveListener() {

            @Override
            public void onSaveSuccess() {

                Log.d(TAG, "Visit object save is successful");
                if(mLatestVisit.isValidTouchpoint(subRegion)) {
                    didEnterTouchpoint(subRegion);
                }
                RoverEventBus.getInstance().post(new RoverEnteredLocationEvent(mLatestVisit));
                RoverEventBus.getInstance().post(new RoverRangeEvent(RoverConstants.RANGE_ACTION_START));
                RoverUtils.writeObjectToSharedPrefs(mContext, mLatestVisit);
            }

            @Override
            public void onSaveFailure() {

                Log.d(TAG, "Visit object save has failed");
            }
        });
    }

    @Subscribe
    public void didExitRegion(RoverExitedRegionEvent event) {

        if(event.getRegionType().equals(RoverConstants.REGION_TYPE_MAIN)) {
            Calendar now = Calendar.getInstance();
            mLatestVisit.setLastBeaconDetectionTime(now);
            RoverUtils.writeObjectToSharedPrefs(mContext, mLatestVisit);
            RoverEventBus.getInstance().post(new RoverExitedLocationEvent(mLatestVisit));
            RoverEventBus.getInstance().post(new RoverRangeEvent(RoverConstants.RANGE_ACTION_STOP));
        }
        else if(event.getRegionType().equals(RoverConstants.REGION_TYPE_SUB)) {
            RoverRegion subRegion = event.getRegion();
            if(mLatestVisit.isValidTouchpoint(subRegion) && mLatestVisit.isInTouchpoint(subRegion)) {
                didExitTouchpoint(subRegion);
            }
        }
    }

    public void didEnterTouchpoint(RoverRegion subRegion) {

        RoverTouchpoint touchpoint = mLatestVisit.getTouchpoint(subRegion);
        RoverEnteredTouchpointEvent enteredTouchpointEvent = new RoverEnteredTouchpointEvent(touchpoint);
        if(!mLatestVisit.getVisitedTouchpoints().contains(touchpoint)) {
            Log.d(TAG, "Has not seen touchpoint " + touchpoint.getMinor() + " (" + touchpoint.getTitle() + ") yet");
            mLatestVisit.addToCurrentTouchpoints(touchpoint);
            enteredTouchpointEvent.setBeenVisited(false);
        }
        else {
            Log.d(TAG, "Has seen touchpoint " + touchpoint.getMinor() + " (" + touchpoint.getTitle() + ")");
            enteredTouchpointEvent.setBeenVisited(true);
        }
        RoverEventBus.getInstance().post(enteredTouchpointEvent);
    }

    public void didExitTouchpoint(RoverRegion subRegion) {

        RoverTouchpoint touchpoint = mLatestVisit.getTouchpoint(subRegion);
        mLatestVisit.removeFromCurrentTouchpoints(touchpoint);
        RoverEventBus.getInstance().post(new RoverExitedTouchpointEvent(touchpoint));
    }
    
    public RoverVisit getLatestVisit() {
        
        if(mLatestVisit == null) {
            mLatestVisit = (RoverVisit)RoverUtils.readObjectFromSharedPrefs(mContext, RoverVisit.class, null);
        }
        return mLatestVisit;
    }
}