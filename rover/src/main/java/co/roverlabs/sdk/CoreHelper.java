package co.roverlabs.sdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.Calendar;

import co.roverlabs.sdk.core.EstimoteManager;
import co.roverlabs.sdk.core.ILocationManager;
import co.roverlabs.sdk.network.listeners.RoverObjectSaveListener;
import co.roverlabs.sdk.model.Region;
import co.roverlabs.sdk.model.TouchPoint;
import co.roverlabs.sdk.model.Visit;
import co.roverlabs.sdk.util.SharedPrefUtils;

import static co.roverlabs.sdk.core.EstimoteManager.ENTERED_REGION;

/**
 * Created by ars on 15-06-02.
 */
public class CoreHelper {
    static final String TAG = EstimoteManager.class.getSimpleName();

    final Rover rover;
    private Visit mVisit;

    /**
     * Listen for beacon monitoring events
     */
    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENTERED_REGION: {
                    onRegionEnter((Region)msg.obj);
                    break;
                }
                default:
                    throw new AssertionError("Unknown handler message received: " + msg.what);
            }
        }
    };

    ILocationManager locationManager;
    CoreHelper(Rover rover){
        this.rover = rover;
        locationManager = Factory.getDefaultLocationManager(rover.context, handler);

    }

    void reStartMonitoring(){
        stopMonitoring();
        startMonitoring();
    }

    void stopMonitoring(){
        locationManager.stopMonitoring();
    }
    void startMonitoring(){
        locationManager.startMonitoring(rover.config.getUuid());
    }



    private void onRegionEnter(final Region region){
        //canceling the keepalive countdown
//        if(mRangeTimer != null && mRangeTimer.hasCountDownStarted()) {
//            mRangeTimer.cancel();
//            mRangeTimer.setCountDownStarted(false);
//        }


//        if(getLatestVisit() != null && mVisit.isInRegion(mainRegion) && mVisit.isAlive()) {
//            if(!mVisit.isInSubRegion(subRegion)) {
//                didEnterSubRegion(subRegion);
//            }
//            return;
//        }

        Calendar now = Calendar.getInstance();
        mVisit = new Visit();
        //mVisit.setSimulation(mSandBoxMode);
        mVisit.setCustomer(rover.customer);
        mVisit.setRegion(region);
        mVisit.setTimeStamp(now.getTime());
        mVisit.setLastBeaconDetectionTime(now);

        NetworkHelper.save(new RoverObjectSaveListener() {
            @Override
            public void onSaveSuccess() {
                if (rover.isLoggingEnabled) {
                    Log.d(TAG, "Visit saved");
                }

                //TODO: Analytics: entered location

                if (!mVisit.isInSubRegion(region)) {
                    onSubRegionEnter(region);
                }

                SharedPrefUtils.writeObjectToSharedPrefs(rover.context, mVisit);
            }

            @Override
            public void onSaveFailure() {
                Log.d(TAG, "Visit object save failed");
            }
        }, mVisit);

//        mVisit.save(new RoverObjectSaveListener() {
//
//            @Override
//            public void onSaveSuccess() {
//
//                Log.d(TAG, "Visit object save successful");
//                RoverEventBus.getInstance().post(new RoverEnteredLocationEvent(mVisit));
//                if (!mVisit.isInSubRegion(subRegion)) {
//                    didEnterSubRegion(subRegion);
//                }
//
//                RoverUtils.writeObjectToSharedPrefs(mContext, mVisit);
//            }
//
//            @Override
//            public void onSaveFailure() {
//                Log.d(TAG, "Visit object save failed");
//            }
//        });

    }


    public void onSubRegionEnter(Region subRegion) {

        if(!mVisit.currentlyContainsWildCardTouchpoints()) {
            for(TouchPoint wildCardTouchPoint : mVisit.getWildCardTouchpoints()) {

                boolean isBeenVisited = mVisit.getVisitedTouchpoints().contains(wildCardTouchPoint);
                mVisit.addToCurrentTouchpoints(wildCardTouchPoint);

                if (!isBeenVisited) {
                    rover.onEnterTouchPoint(wildCardTouchPoint);
                }

                if (rover.isLoggingEnabled) {
                    Log.d(TAG, "TouchPoint wild card (" + wildCardTouchPoint.getTitle() + ") - " + (isBeenVisited ? "" : "not") + " seen before");
                }
            }
        }

//        TouchPoint touchpoint = mVisit.getTouchpoint(subRegion);

//        if(touchpoint != null) {
//            RoverEnteredTouchpointEvent enteredTouchpointEvent = new RoverEnteredTouchpointEvent(mVisit.getId(), touchpoint);
//            if(!mVisit.getVisitedTouchpoints().contains(touchpoint)) {
//                Log.d(TAG, "Touchpoint minor " + touchpoint.getMinor() + " (" + touchpoint.getTitle() + ") - not seen before");
//                enteredTouchpointEvent.setBeenVisited(false);
//            }
//            else {
//                Log.d(TAG, "Touchpoint minor " + touchpoint.getMinor() + " (" + touchpoint.getTitle() + ") - seen before");
//                enteredTouchpointEvent.setBeenVisited(true);
//            }
//            mVisit.addToCurrentTouchpoints(touchpoint);
//            RoverEventBus.getInstance().post(enteredTouchpointEvent);
//        }
    }




}
