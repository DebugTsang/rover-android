package co.roverlabs.sdk.core;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.roverlabs.sdk.model.Location;
import co.roverlabs.sdk.model.Region;
import co.roverlabs.sdk.model.Touchpoint;
import co.roverlabs.sdk.model.Visit;

/**
 * Created by ars on 15-06-02.
 */
public class VisitManager {
    static final String TAG = EstimoteHelper.class.getSimpleName();

    public interface IVisitListener {
        boolean shouldCreateVisit(Visit visit);
        void onEnterLocation(Visit visit);
        void onPotentiallyExitLocation(Location location);
        void onExpireVisit(Visit visit);
        void onEnterTouchpoint(List<Touchpoint> touchPoints);
        void onExitTouchpoints(List<Touchpoint> touchPoints);
    }

    final Context context;

    private Visit mVisit;
    private IVisitListener mVisitListener;

    volatile boolean isLoggingEnabled;

    IBeaconHelper beaconHelper;
    public VisitManager(Context context){
        this.context = context;
        beaconHelper = getDefaultBeaconHelper();

    }

    public void setVisitListener(IVisitListener visitListener){
        this.mVisitListener = visitListener;
    }
    public void removeVisitListener(){
        this.mVisitListener = null;
    }

    public void reStartMonitoring(String uuid){
        stopMonitoring();
        startMonitoring(uuid);
    }

    public void stopMonitoring(){
        beaconHelper.stopMonitoring();
    }
    public void startMonitoring(String uuid){
        beaconHelper.startMonitoring(uuid);
    }


    void enteredRegion(final Region region){

        if(mVisit != null && mVisit.isInRegion(region) && mVisit.isAlive()) {
            if(!mVisit.isInSubRegion(region)) {
                onSubRegionEnter(region);
            }
            return;
        }

        Calendar now = Calendar.getInstance();
        mVisit = new Visit();
        //mVisit.setSimulation(mSandBoxMode);
        //TODO: make customer statically accessible from Customer class
        //mVisit.setCustomer((Customer)RoverUtils.readObjectFromSharedPrefs(mContext, RoverCustomer.class, null));
        mVisit.setRegion(region);
        mVisit.setTimeStamp(now.getTime());
        mVisit.setLastBeaconDetectionTime(now);

        if (mVisitListener != null){
            mVisitListener.onEnterLocation(mVisit);
        }

        if (!mVisit.isInSubRegion(region)) {
            onSubRegionEnter(region);
        }
    }

    public void onSubRegionEnter(Region subRegion) {

        List<Touchpoint> enteredTouchPoints = new ArrayList<>();
        if(!mVisit.currentlyContainsWildCardTouchpoints()) {
            for(Touchpoint wildCardTouchPoint : mVisit.getWildCardTouchpoints()) {
                boolean doesContain = mVisit.getCurrentTouchpoints().contains(wildCardTouchPoint);
                if (!doesContain){
                    mVisit.addToCurrentTouchpoints(wildCardTouchPoint);
                    enteredTouchPoints.add(wildCardTouchPoint);

                }
                if (isLoggingEnabled) {
                    Log.d(TAG, "TouchPoint wild card (" + wildCardTouchPoint.getTitle() + ") - " + (doesContain ? "" : "not") + " seen before");
                }
            }
        }

        Touchpoint touchpoint = mVisit.getTouchpoint(subRegion);

        if(touchpoint != null) {
            boolean doesContain = mVisit.getCurrentTouchpoints().contains(touchpoint);
            if (!doesContain){
                mVisit.addToCurrentTouchpoints(touchpoint);
                enteredTouchPoints.add(touchpoint);
            }
        }

        if (mVisitListener != null) {
            mVisitListener.onEnterTouchpoint(enteredTouchPoints);
        }
    }


    public IBeaconHelper getDefaultBeaconHelper(){
        return new EstimoteHelper(this);
    }

}
