package co.roverlabs.sdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Calendar;

import co.roverlabs.sdk.models.Region;
import co.roverlabs.sdk.models.Visit;

import static co.roverlabs.sdk.EstimoteHelper.ENTERED_REGION;

/**
 * Created by ars on 15-06-02.
 */
public class VisitManager {
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

    public VisitManager(Rover rover){
        this.rover = rover;
    }

    private void onRegionEnter(Region region){
        //canceling the keepalive countdown
//        if(mRangeTimer != null && mRangeTimer.hasCountDownStarted()) {
//            mRangeTimer.cancel();
//            mRangeTimer.setCountDownStarted(false);
//        }


//        if(getLatestVisit() != null && mLatestVisit.isInRegion(mainRegion) && mLatestVisit.isAlive()) {
//            if(!mLatestVisit.isInSubRegion(subRegion)) {
//                didEnterSubRegion(subRegion);
//            }
//            return;
//        }

        Calendar now = Calendar.getInstance();
        mVisit = new Visit();
        //mLatestVisit.setSimulation(mSandBoxMode);
        mVisit.setCustomer(rover.customer);
        mVisit.setRegion(region);
        mVisit.setTimeStamp(now.getTime());
        mVisit.setLastBeaconDetectionTime(now);

//        mLatestVisit.save(new RoverObjectSaveListener() {
//
//            @Override
//            public void onSaveSuccess() {
//
//                Log.d(TAG, "Visit object save successful");
//                RoverEventBus.getInstance().post(new RoverEnteredLocationEvent(mLatestVisit));
//                if (!mLatestVisit.isInSubRegion(subRegion)) {
//                    didEnterSubRegion(subRegion);
//                }
//
//                RoverUtils.writeObjectToSharedPrefs(mContext, mLatestVisit);
//            }
//
//            @Override
//            public void onSaveFailure() {
//                Log.d(TAG, "Visit object save failed");
//            }
//        });

    }





}
