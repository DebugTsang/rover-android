package co.roverlabs.sdk.managers;

import android.content.Context;

import java.util.Calendar;

import co.roverlabs.sdk.models.RoverRegion;
import co.roverlabs.sdk.models.RoverVisit;
import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class RoverVisitManager {
    
    private static final String TAG = RoverVisitManager.class.getName();
    private static RoverVisitManager sVisitManagerInstance;
    private Context mContext;
    private RoverVisit mLatestVisit;
    
    //Constructor
    private RoverVisitManager(Context con) { 
        
        mContext = con; 
        // listen for RoverDidEnterRegion -> didEnterLocation
    }

    public static RoverVisitManager getInstance(Context con) {

        if(sVisitManagerInstance == null) {
            sVisitManagerInstance = new RoverVisitManager(con);
        }
        return sVisitManagerInstance;
    }
    
    //Rename to didEnterRegion
    //Delete list of beacons argument
    public void didEnterRegion(RoverRegion region) {
        
        //Double check if region has all values passed in
        
        if (getLatestVisit() != null && mLatestVisit.isInRegion(region) && mLatestVisit.isAlive()) {
            
            /*
            if(latestvisit.currenttouchoint is not null || !latestvisit.currenttouchpoint.isinregion(region)) {
                movedToSubRegion(region)
            }
             */
            
            return;
        }
        Calendar now = Calendar.getInstance();
        mLatestVisit = new RoverVisit(mContext);
        mLatestVisit.setRegion(region);
        mLatestVisit.setEnteredTime(now.getTime());
        mLatestVisit.setLastBeaconDetection(now);
        mLatestVisit.save(); // success callback -> broadcast RoverDidEnterLocation (only after successful server call and mapping)
                                                /// after that -> movedToSubRegion(region)
    }
    
    /*
    void movedToSubRegion(region) {
        touchpoint = latestVisit.getTouchPoint(region)
        if(touchpoint is not null) {
          if (! latestvisit.visitedTouchpoints.contain(touchpoint)) {
            latestvisit.setcurrenttouchpoint(touchpoint)
            broadcast 'RoverDidEnterTouchpoint' - have not seen the touchpoint before
          
            return
          }
          
          latestvisit.setcurrenttouchpoint(touchpoint)
        } else {
            // log "invalid touchpoint"
            Does not correspond to any set up touchpoint on the server
        }
        
     */

    public void didExitRegion() {

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