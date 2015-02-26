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
    private RoverNetworkManager mNetWorkManager;
    private Context mContext;
    private RoverVisit mLatestVisit;
    private Region mRegion;
    private ArrayList<Beacon> mBeacons;
    
    private RoverVisitManager(Context con, Region region, List<Beacon> beacons) {
        
        mNetWorkManager = new RoverNetworkManager();
        mContext = con;
        mRegion = region;
        mBeacons = new ArrayList<Beacon>(beacons);
    }

    public static RoverVisitManager getInstance(Context con, Region region, List<Beacon> beacons) {

        if(sVisitManagerInstance == null) {
            sVisitManagerInstance = new RoverVisitManager(con, region, beacons);
        }
        return sVisitManagerInstance;
    }
   
    public static RoverVisitManager getInstance(Context con, Region region) {

        if(sVisitManagerInstance == null) {
            sVisitManagerInstance = new RoverVisitManager(con, region, new ArrayList<Beacon>());
        }
        return sVisitManagerInstance;
    }
    
    public void didEnterLocation() {

        if(getLatestVisit() != null && mLatestVisit.isInRegion(mRegion) && mLatestVisit.isAlive()) {
            return;
        }
        mLatestVisit = new RoverVisit(mRegion);
        mLatestVisit.setEnteredTime(Calendar.getInstance().getTime());
        
        
//        JSONObject json = new JSONObject();
//        JSONObject mainjson = new JSONObject();
//        try {
//            json.put("uuid", "F352DB29-6A05-4EA2-A356-9BFAC2BB3316");
//            json.put("major", 52643);
//            json.put("customer_id", "1234");
//            mainjson.put("visit", json);
//        } 
//        catch (JSONException e) {
//            e.printStackTrace();
//        }

        RoverObjectWrapper visit = new RoverObjectWrapper();
        RoverVisit innerVisit = new RoverVisit(mRegion);
        
        innerVisit.customer_id ="1234";
        innerVisit.major = mBeacons.get(0).getMajor();
        Log.d(TAG, "the major I got is " + innerVisit.major);
        innerVisit.uuid = "F352DB29-6A05-4EA2-A356-9BFAC2BB3316";

        visit.setVisit(innerVisit);
        
        mNetWorkManager.makeCall().createVisit(Rover.getInstance(mContext).getAuthToken(), visit, new Callback<RoverObjectWrapper>() {

                    @Override
                    public void success(RoverObjectWrapper roverObjectWrapper, Response response) {
                        Log.d(TAG, roverObjectWrapper.getVisit().getEnteredTime().toString());
                        RoverNotificationManager notificationsManager = new RoverNotificationManager(mContext);
                        String meta = roverObjectWrapper.getVisit().getOrganization().getMetaData().toString();
                        Log.d(TAG, "meta data is " + meta);
                        String title = roverObjectWrapper.getVisit().getTouchPoints().get(0).getTitle();
                        String message = roverObjectWrapper.getVisit().getTouchPoints().get(0).getNotification();
                        notificationsManager.sendNotification(Rover.getInstance(mContext).getIconResourceId(), 1, title, message, CardActivity.class);
                        Log.d(TAG, "response is good");
                    }

                    @Override
                    public void failure(RetrofitError error) {


                        Log.d(TAG, error.toString());
                        Log.d(TAG, "response is bad and you should feel bad");
                    }
                }
        );
        
//        RoverNotificationManager notificationsManager = new RoverNotificationManager(mContext);
//        notificationsManager.sendNotification(Rover.getInstance(mContext).getIconResourceId(), 1, "Rover Notification", "Welcome", CardActivity.class);
    }

    public void didExitLocation() {
        
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