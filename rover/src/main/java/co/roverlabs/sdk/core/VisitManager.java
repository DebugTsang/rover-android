package co.roverlabs.sdk.core;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import co.roverlabs.sdk.model.Location;
import co.roverlabs.sdk.model.Region;
import co.roverlabs.sdk.model.Touchpoint;
import co.roverlabs.sdk.model.Visit;
import co.roverlabs.sdk.util.SharedPrefUtils;
import co.roverlabs.sdk.util.Utils;

/**
 * Created by ars on 15-06-02.
 */
public class VisitManager implements RegionManager.IRegionManagerListener {

    public static final String TAG = VisitManager.class.getSimpleName();
    public IVisitManagerListener mVisitManagerListener;
    private Context mContext;
    private RegionManager mRegionManager;
    private Visit mLatestVisit;

    public interface IVisitManagerListener {

        boolean shouldCreateVisit(VisitManager manager, Visit visit);
        void onEnterLocation(VisitManager manager, Visit visit);
        void onPotentiallyExitLocation(VisitManager manager, Location location, Visit visit);
        void onExpireVisit(VisitManager manager, Visit visit);
        void onEnterTouchpoints(VisitManager manager, List<Touchpoint> touchpoints, Visit visit);
        void onExitTouchpoints(VisitManager manager, List<Touchpoint> touchpoints, Visit visit);
    }

    public VisitManager(Context context){

        mContext = context;
        mRegionManager = new RegionManager(context);
        mRegionManager.setListener(this);
    }

    public RegionManager getRegionManager() {

        return mRegionManager;
    }

    public void setListener(IVisitManagerListener listener){

        mVisitManagerListener = listener;
    }

    public void removeListener() {

        mVisitManagerListener = null;
    }

    public Visit getLatestVisit() {

        if(mLatestVisit == null) {
            mLatestVisit = (Visit)SharedPrefUtils.readObjectFromSharedPrefs(mContext, Visit.class, null);
        }
        return mLatestVisit;
    }

    @Override
    public void onEnterRegions(RegionManager regionManager, Set<Region> enteredRegions) {

        //if (self.latestVisit && [self.latestVisit isInLocationRegion:aRegion] && (self.latestVisit.currentTouchpoints.count > 0 || self.latestVisit.isAlive)) {
        if(Utils.hasIdenticalRegionMajors(enteredRegions)) {
            Region region = Utils.getRandomRegion(enteredRegions);
            if(getLatestVisit() != null && mLatestVisit.isInMajorRegion(region) && (mLatestVisit.getCurrentTouchpoints().size() > 0 || mLatestVisit.isAlive())) {

            }

        }
        else {
            Log.e(TAG, "Rover has stopped - beacons with different major numbers detected at the same location.");
        }
    }

    @Override
    public void onExitRegions(RegionManager regionManager, Set<Region> exitedRegions) {

    }

    private class RangeTimer extends CountDownTimer {

        public final String TAG = RangeTimer.class.getSimpleName();
        private VisitManager mVisitManager;
        private boolean mCountDownStarted = false;

        public RangeTimer(VisitManager visitManager, long keepAliveTimeInMinutes, long countDownInterval) {

            super(TimeUnit.MINUTES.toMillis(keepAliveTimeInMinutes), countDownInterval);
            mVisitManager = visitManager;
        }

        public void setCountDownStarted(boolean started) {

            mCountDownStarted = started;
        }

        public boolean hasCountDownStarted() {

            return mCountDownStarted;
        }

        @Override
        public void onTick(long millisUntilFinished) {

            Log.d(TAG, "Time left for ranging - " + TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) + " minute(s)");
        }

        @Override
        public void onFinish() {

            Log.d(TAG, "Range Timer has expired - ranging will now be stopped");
            mRegionManager.stopRanging();
            mVisitManagerListener.onExpireVisit(mVisitManager, mLatestVisit);
        }
    }

//    @Override
//    public void onEnteredRegions(RegionManager manager, List<Region> regions){
//        // TO BE FILLED IN
//    }
//
//    @Override
//    public void onExitedRegions(RegionManager manager, List<Region> regions) {
//        // TO BE FILLED IN
//    }

}
