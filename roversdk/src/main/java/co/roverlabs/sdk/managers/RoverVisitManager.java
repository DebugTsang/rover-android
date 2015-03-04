package co.roverlabs.sdk.managers;

import android.content.Context;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;

import java.util.Calendar;
import java.util.List;

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
    private RoverVisitManager(Context con) { mContext = con; }

    public static RoverVisitManager getInstance(Context con) {

        if(sVisitManagerInstance == null) {
            sVisitManagerInstance = new RoverVisitManager(con);
        }
        return sVisitManagerInstance;
    }
    
    public void didEnterLocation(Region region, List<Beacon> beacons) {
        
        if (getLatestVisit() != null && mLatestVisit.isInRegion(region) && mLatestVisit.isAlive()) {
            return;
        }
        Calendar now = Calendar.getInstance();
        mLatestVisit = new RoverVisit(mContext);
        mLatestVisit.setRegion(region);
        mLatestVisit.setEnteredTime(now.getTime());
        mLatestVisit.setLastBeaconDetection(now);
        mLatestVisit.setBeacons(beacons);
        mLatestVisit.save();
    }

    public void didExitLocation() {

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