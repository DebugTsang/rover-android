package co.roverlabs.sdk.core;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import co.roverlabs.sdk.model.Customer;
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
    private final long COUNT_DOWN_INTERVAL = 60000;
    private Context mContext;
    private RegionManager mRegionManager;
    private Visit mLatestVisit;
    private RangeTimer mRangeTimer;

    public interface IVisitManagerListener {

        boolean shouldCreateVisit(VisitManager manager, Visit visit);
        void onEnterLocation(VisitManager manager, Visit visit);
        void onPotentiallyExitLocation(VisitManager manager, Visit visit);
        void onVisitExpire(VisitManager manager, Visit visit);
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

        if(Utils.hasIdenticalRegionMajors(enteredRegions)) {

            Region anyRegion = Utils.getRandomRegion(enteredRegions);

            if(getLatestVisit() != null && mLatestVisit.isInMajorRegion(anyRegion) && (mLatestVisit.getCurrentTouchpoints().size() > 0 || mLatestVisit.isAlive())) {

                Set<Region> newTouchpointRegions = new HashSet<>();

                for(Region enteredRegion : enteredRegions) {
                    if(!getLatestVisit().isInMinorRegion(enteredRegion)) {
                        newTouchpointRegions.add(enteredRegion);
                    }
                }

                if(newTouchpointRegions.size() > 0 ) {
                    moveToRegions(newTouchpointRegions);
                }

                if(mRangeTimer != null && mRangeTimer.hasCountDownStarted()) {
                    mRangeTimer.cancel();
                    mRangeTimer.setCountDownStarted(false);
                }

                return;
            }

            mRangeTimer = null;

            createVisitWithRegions(enteredRegions);

        }
        else {
            Log.e(TAG, "Rover has stopped - beacons with different major numbers detected at the same location.");
        }
    }

    @Override
    public void onExitRegions(RegionManager regionManager, Set<Region> exitedRegions) {

        if(Utils.hasIdenticalRegionMajors(exitedRegions)) {

            Region anyRegion = Utils.getRandomRegion(exitedRegions);

            if(getLatestVisit() != null && mLatestVisit.isInMajorRegion(anyRegion)) {

                List<Touchpoint> exitedTouchpoints = new ArrayList<>();

                for(Region region : exitedRegions) {
                    Touchpoint touchpoint = mLatestVisit.getTouchpoint(region);
                    if(touchpoint != null) {
                        mLatestVisit.removeFromCurrentTouchpoints(touchpoint);
                        exitedTouchpoints.add(touchpoint);
                    }
                }

                if(regionManager.getCurrentRegions().isEmpty()) {

                    Calendar now = Calendar.getInstance();
                    mLatestVisit.setLastBeaconDetectionTime(now);
                    SharedPrefUtils.writeObjectToSharedPrefs(mContext, mLatestVisit);

                    if(mLatestVisit.currentlyContainsWildCardTouchpoints()) {
                        for(Touchpoint wildCardTouchpoint : mLatestVisit.getCurrentWildCardTouchpoints()) {
                            mLatestVisit.removeFromCurrentTouchpoints(wildCardTouchpoint);
                            exitedTouchpoints.add(wildCardTouchpoint);
                        }
                    }

                    //Start timer for ranging
                    if(mRangeTimer == null) {
                        mRangeTimer = new RangeTimer(this, mLatestVisit.getKeepAliveTime(), COUNT_DOWN_INTERVAL);
                    }
                    mRangeTimer.start();
                    mRangeTimer.setCountDownStarted(true);
                }

                if(exitedTouchpoints.size() > 0) {
                    mVisitManagerListener.onExitTouchpoints(this, exitedTouchpoints, mLatestVisit);
                }

                if(regionManager.getCurrentRegions().isEmpty()) {
                    mVisitManagerListener.onPotentiallyExitLocation(this, mLatestVisit);
                }
            }
        }
        else {
            Log.e(TAG, "Rover has stopped - beacons with different major numbers detected at the same location.");
        }

    }

    public void moveToRegions(Set<Region> regions) {

        List<Touchpoint> enteredTouchpoints = new ArrayList<>();

        if(!mLatestVisit.currentlyContainsWildCardTouchpoints()) {
            for(Touchpoint touchpoint : mLatestVisit.getWildCardTouchpoints()) {
                mLatestVisit.addToCurrentTouchpoints(touchpoint);
                enteredTouchpoints.add(touchpoint);
            }
        }
    }

    public void createVisitWithRegions(Set<Region> regions) {

        Region anyRegion = Utils.getRandomRegion(regions);

        Calendar now = Calendar.getInstance();
        Visit newVisit = new Visit();
        newVisit.setUuid(anyRegion.getUuid());
        newVisit.setMajor(anyRegion.getMajor());
        newVisit.setCustomer((Customer) SharedPrefUtils.readObjectFromSharedPrefs(mContext, Customer.class, null));
        newVisit.setTimeStamp(now.getTime());
        newVisit.setLastBeaconDetectionTime(now);

        boolean shouldCreateVisit = mVisitManagerListener.shouldCreateVisit(this, newVisit);

        if(shouldCreateVisit) {
            mLatestVisit = newVisit;
            mVisitManagerListener.onEnterLocation(this, mLatestVisit);
            moveToRegions(regions);
            SharedPrefUtils.writeObjectToSharedPrefs(mContext, mLatestVisit);
        }
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
            mVisitManagerListener.onVisitExpire(mVisitManager, mLatestVisit);
        }
    }
}
