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
        
        final RoverRegion subRegion = event.getRegion();
        final RoverRegion mainRegion = new RoverRegion(subRegion.getUuid(), subRegion.getMajor(), null);
        
        Log.d(TAG, "subRegion is " + subRegion.toString());
        Log.d(TAG, "mainRegion is " + mainRegion.toString());

        Log.d(TAG, "the region grabbed from RoverEnteredRegionEvent is " + event.getRegion().toString());
        if(getLatestVisit() != null) {
            Log.d(TAG, "the latest visit region is " + getLatestVisit().getRegion().toString());
            Log.d(TAG, "getLatestVisit() != null is " + String.valueOf(getLatestVisit() != null));
            Log.d(TAG, "mLatestVisit.isInRegion(mainRegion) is " + String.valueOf(mLatestVisit.isInRegion(mainRegion)));
            Log.d(TAG, "mLatestVisit.isAlive() is " + String.valueOf(mLatestVisit.isAlive()));
        }
        
        if(getLatestVisit() != null && mLatestVisit.isInRegion(mainRegion) && mLatestVisit.isAlive()) {
            Log.d(TAG, "in the same mainRegion");
            if(mLatestVisit.getCurrentTouchpoint() == null || !mLatestVisit.getCurrentTouchpoint().isInSubRegion(subRegion)) {
                Log.d(TAG, "not in the same subRegion");
                didEnterSubRegion(subRegion);
            }
            else {
                Log.d(TAG, "in the same subRegion");
            }
            return;
        }
        
        Calendar now = Calendar.getInstance();
        mLatestVisit = new RoverVisit();
        mLatestVisit.setRegion(mainRegion);
        mLatestVisit.setEnteredTime(now.getTime());
        mLatestVisit.setLastBeaconDetection(now);
        
        mLatestVisit.save(new RoverObjectSaveListener() {
            
            @Override
            public void onSaveSuccess() {

                Log.d(TAG, "Object save is successful");
                didEnterSubRegion(subRegion);
                RoverEventBus.getInstance().post(new RoverEnteredLocationEvent(mLatestVisit));
                RoverUtils.writeObjectToSharedPreferences(mContext, "RoverVisit", mLatestVisit);
            }

            @Override
            public void onSaveFailure() {

                Log.d(TAG, "Object save has failed");
            }
        });
    }
    
    public void didEnterSubRegion(RoverRegion subRegion) {

        RoverTouchpoint touchpoint = mLatestVisit.getTouchpoint(subRegion);
        if(touchpoint != null) {
            if(!mLatestVisit.getVisitedTouchpoints().contains(touchpoint)) {
                Log.d(TAG, "has not seen this touchpoint yet");
                mLatestVisit.setCurrentTouchpoint(touchpoint);
                RoverEventBus.getInstance().post(new RoverEnteredTouchpointEvent(touchpoint));
                return;
            }
            else {
                Log.d(TAG, "seen this touchpoint already");
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
        RoverUtils.writeObjectToSharedPreferences(mContext, mLatestVisit.getObjectName(), mLatestVisit);
        Log.d(TAG, "The saved region is " + mLatestVisit.getRegion().toString());
        //TODO: mLatestVisit.save() to be tested
    }
    
    public RoverVisit getLatestVisit() {
        
        if(mLatestVisit == null) {
            mLatestVisit = (RoverVisit)RoverUtils.readObjectFromSharedPreferences(mContext, RoverVisit.class, null);
        }
        return mLatestVisit;
    }
}