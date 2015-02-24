package co.roverlabs.sdk.managers;

import android.content.Context;

import com.estimote.sdk.Region;

import java.util.Calendar;

import co.roverlabs.sdk.CardActivity;
import co.roverlabs.sdk.Rover;
import co.roverlabs.sdk.RoverUtils;
import co.roverlabs.sdk.Visit;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class VisitManager {
    
    private static final String TAG = VisitManager.class.getName();
    private static VisitManager sVisitManagerInstance;
    private Context mContext;
    private Visit mLatestVisit;
    private Region mRegion;
    
    private VisitManager(Context con, Region region) {
        
        mContext = con;
        mRegion = region;
    }

    public static VisitManager getInstance(Context con, Region region) {

        if(sVisitManagerInstance == null) {
            sVisitManagerInstance = new VisitManager(con, region);
        }
        return sVisitManagerInstance;
    }
    
    public void didEnterLocation() {

        if(getLatestVisit() != null && mLatestVisit.isInRegion(mRegion) && mLatestVisit.isAlive()) {
            return;
        }
        mLatestVisit = new Visit(mRegion);
        mLatestVisit.setEnteredTime(Calendar.getInstance());
        NotificationsManager notificationsManager = new NotificationsManager(mContext);
        notificationsManager.sendNotification(Rover.getInstance(mContext).getIconResourceId(), 1, "Rover Notification", "Welcome", CardActivity.class);
    }

    public void didExitLocation() {
        
        Calendar now = Calendar.getInstance();
        mLatestVisit.setLastBeaconDetection(now);
        mLatestVisit.setExitedTime(now);
        RoverUtils.writeToSharedPreferences(mContext, "Visit", mLatestVisit);
    }
    
    public Visit getLatestVisit() {
        
        if(mLatestVisit == null) {
            mLatestVisit = (Visit)RoverUtils.readFromSharedPreferences(mContext, Visit.class, null);
        }
        return mLatestVisit;
    }
}