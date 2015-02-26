package co.roverlabs.sdk.managers;

import android.content.Context;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.roverlabs.sdk.CardActivity;
import co.roverlabs.sdk.Rover;
import co.roverlabs.sdk.models.RoverObjectWrapper;
import co.roverlabs.sdk.models.RoverVisit;
import co.roverlabs.sdk.networks.RoverNetworkManager;
import co.roverlabs.sdk.utilities.RoverUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class RoverVisitManager {
    
    private static final String TAG = RoverVisitManager.class.getName();
    private static RoverVisitManager sVisitManagerInstance;
    private Context mContext;
    private RoverVisit mLatestVisit;
    //private Region mRegion;
    //private ArrayList<Beacon> mBeacons;
    
    //Constructor
    private RoverVisitManager(Context con) { mContext = con; }

    public static RoverVisitManager getInstance(Context con) {

        if(sVisitManagerInstance == null) {
            sVisitManagerInstance = new RoverVisitManager(con);
        }
        return sVisitManagerInstance;
    }
    
    public void didEnterLocation(Region region, List<Beacon> beacons) {

        if(getLatestVisit() != null && mLatestVisit.isInRegion(region) && mLatestVisit.isAlive()) {
            return;
        }
        mLatestVisit = new RoverVisit(region);
        mLatestVisit.setEnteredTime(Calendar.getInstance().getTime());
        mLatestVisit.save();

//        RoverObjectWrapper visit = new RoverObjectWrapper();
//        RoverVisit innerVisit = new RoverVisit(mRegion);
//        
//        innerVisit.customer_id ="1234";
//        innerVisit.major = mBeacons.get(0).getMajor();
//        Log.d(TAG, "the major I got is " + innerVisit.major);
//        innerVisit.uuid = "F352DB29-6A05-4EA2-A356-9BFAC2BB3316";
//
//        visit.setVisit(innerVisit);
//        
//        mNetWorkManager.makeCall().createVisit(Rover.getInstance(mContext).getAuthToken(), visit, new Callback<RoverObjectWrapper>() {
//
//                    @Override
//                    public void success(RoverObjectWrapper roverObjectWrapper, Response response) {
//                        Log.d(TAG, roverObjectWrapper.getVisit().getEnteredTime().toString());
//                        RoverNotificationManager notificationsManager = new RoverNotificationManager(mContext);
//                        String meta = roverObjectWrapper.getVisit().getOrganization().getMetaData().toString();
//                        Log.d(TAG, "meta data is " + meta);
//                        String title = roverObjectWrapper.getVisit().getTouchPoints().get(0).getTitle();
//                        String message = roverObjectWrapper.getVisit().getTouchPoints().get(0).getNotification();
//                        notificationsManager.sendNotification(Rover.getInstance(mContext).getIconResourceId(), 1, title, message, CardActivity.class);
//                        Log.d(TAG, "response is good");
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//
//
//                        Log.d(TAG, error.toString());
//                        Log.d(TAG, "response is bad and you should feel bad");
//                    }
//                }
//        );
        
//        RoverNotificationManager notificationsManager = new RoverNotificationManager(mContext);
//        notificationsManager.sendNotification(Rover.getInstance(mContext).getIconResourceId(), 1, "Rover Notification", "Welcome", CardActivity.class);
    }

    public void didExitLocation(Region region) {
        
        Calendar now = Calendar.getInstance();
        mLatestVisit.setLastBeaconDetection(now);
        mLatestVisit.setExitedTime(now.getTime());
        RoverUtils.writeToSharedPreferences(mContext, "RoverVisit", mLatestVisit);

        RoverNotificationManager notificationsManager = new RoverNotificationManager(mContext);
        notificationsManager.sendNotification(Rover.getInstance(mContext).getIconResourceId(), 1, "Good bye", "You have exited", CardActivity.class);
    }
    
    public RoverVisit getLatestVisit() {
        
        if(mLatestVisit == null) {
            Log.d(TAG, "the latest visit is null");
            mLatestVisit = (RoverVisit)RoverUtils.readFromSharedPreferences(mContext, RoverVisit.class, null);
        }
        return mLatestVisit;
    }
}