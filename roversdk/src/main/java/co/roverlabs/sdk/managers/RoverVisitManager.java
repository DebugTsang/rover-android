package co.roverlabs.sdk.managers;

import android.content.Context;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.Calendar;

import co.roverlabs.sdk.events.RoverEnteredLocationEvent;
import co.roverlabs.sdk.events.RoverEnteredRegionEvent;
import co.roverlabs.sdk.events.RoverEnteredTouchpointEvent;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverExitedRegionEvent;
import co.roverlabs.sdk.listeners.RoverObjectSaveListener;
import co.roverlabs.sdk.models.RoverRegion;
import co.roverlabs.sdk.models.RoverTouchpoint;
import co.roverlabs.sdk.models.RoverVisit;
import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class RoverVisitManager {
    
    public static final String TAG = RoverVisitManager.class.getSimpleName();
    private static RoverVisitManager sVisitManagerInstance;
    private Context mContext;
    private RoverVisit mLatestVisit;
    
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

    @Subscribe
    public void didEnterRegion(RoverEnteredRegionEvent event) {
        
        final RoverRegion region = event.getRegion();

        if(getLatestVisit() != null && mLatestVisit.isInRegion(region) && mLatestVisit.isAlive()) {
            if(mLatestVisit.getCurrentTouchpoint() == null || !mLatestVisit.getCurrentTouchpoint().isInSubRegion(region)) {
                didEnterSubRegion(region);
            }
            return;
        }
        
        Calendar now = Calendar.getInstance();
        mLatestVisit = new RoverVisit();
        mLatestVisit.setRegion(region);
        mLatestVisit.setEnteredTime(now.getTime());
        mLatestVisit.setLastBeaconDetection(now);
        
        mLatestVisit.save(new RoverObjectSaveListener() {
            
            @Override
            public void onSaveSuccess() {

                Log.d(TAG, "Object save is successful");
                RoverEventBus.getInstance().post(new RoverEnteredLocationEvent(mLatestVisit));
                
                // tell the regionmanager, start monitoring for these uuid, major, and minor
                
                didEnterSubRegion(region);
            }

            @Override
            public void onSaveFailure() {

                Log.d(TAG, "Object save has failed");
            }
        });
    }
    
    public void didEnterSubRegion(RoverRegion region) {

        RoverTouchpoint touchpoint = mLatestVisit.getTouchpoint(region);
        if(touchpoint != null) {
            if(!mLatestVisit.getVisitedTouchpoints().contains(touchpoint)) {
                mLatestVisit.setCurrentTouchpoint(touchpoint);
                RoverEventBus.getInstance().post(new RoverEnteredTouchpointEvent(touchpoint));
                return;
            }
            mLatestVisit.setCurrentTouchpoint(touchpoint);
        }
        else {
            Log.d(TAG, "Invalid touchpoint - does not correspond to any set up touchpoints");
        }
    }

    @Subscribe
    public void didExitRegion(RoverExitedRegionEvent event) {

        Calendar now = Calendar.getInstance();
        mLatestVisit.setLastBeaconDetection(now);
        mLatestVisit.setExitedTime(now.getTime());
        RoverUtils.writeObjectToSharedPreferences(mContext, "RoverVisit", mLatestVisit);
        //TODO: mLatestVisit.save() to be tested
    }
    
    public RoverVisit getLatestVisit() {
        
        if(mLatestVisit == null) {
            mLatestVisit = (RoverVisit)RoverUtils.readObjectFromSharedPreferences(mContext, RoverVisit.class, null);
        }
        return mLatestVisit;
    }
}