package co.roverlabs.sdk.managers;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.events.RoverEnteredLocationEvent;
import co.roverlabs.sdk.events.RoverEnteredRegionEvent;
import co.roverlabs.sdk.events.RoverEnteredTouchpointEvent;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverExitedLocationEvent;
import co.roverlabs.sdk.events.RoverExitedRegionEvent;
import co.roverlabs.sdk.events.RoverExitedTouchpointEvent;
import co.roverlabs.sdk.events.RoverRangeEvent;
import co.roverlabs.sdk.listeners.RoverObjectSaveListener;
import co.roverlabs.sdk.models.RoverCustomer;
import co.roverlabs.sdk.models.RoverRegion;
import co.roverlabs.sdk.models.RoverTouchpoint;
import co.roverlabs.sdk.models.RoverVisit;
import co.roverlabs.sdk.ui.CardListActivity;
import co.roverlabs.sdk.utilities.RoverConstants;
import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class RoverVisitManager {
    
    public static final String TAG = RoverVisitManager.class.getSimpleName();
    private final long COUNT_DOWN_INTERVAL = 60000;
    private static RoverVisitManager sVisitManagerInstance;
    private Context mContext;
    private RoverVisit mLatestVisit;
    private RoverTimer mRangeTimer;
    private boolean mSandBoxMode;
    
    //Constructor
    private RoverVisitManager(Context con) { 
        
        mContext = con;
        RoverEventBus.getInstance().register(this);
    }

    public static RoverVisitManager getInstance(Context con) {

        if(sVisitManagerInstance == null) {
            sVisitManagerInstance = new RoverVisitManager(con);
        }
        return sVisitManagerInstance;
    }

    public void setSandBoxMode(boolean sandBoxMode) {

        mSandBoxMode = sandBoxMode;
    }

    public void resetVisit() {

        mLatestVisit = null;
        RoverUtils.removeObjectFromSharedPrefs(mContext, RoverVisit.class);
    }

    //TODO: Remove after testing
    public void showCards() {

        if(mLatestVisit != null) {
            Intent intent = new Intent(mContext, CardListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        else {
            Toast.makeText(mContext, R.string.no_cards_text, Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void didEnterRegion(RoverEnteredRegionEvent event) {

        if(mRangeTimer != null && mRangeTimer.hasCountDownStarted()) {
            mRangeTimer.cancel();
            mRangeTimer.setCountDownStarted(false);
        }

        final RoverRegion subRegion = event.getRegion();
        final RoverRegion mainRegion = new RoverRegion(subRegion.getUuid(), subRegion.getMajor(), null);

        if(getLatestVisit() != null && mLatestVisit.isInRegion(mainRegion) && mLatestVisit.isAlive()) {
            if(!mLatestVisit.isInSubRegion(subRegion)) {
                didEnterSubRegion(subRegion);
            }
            return;
        }

        Calendar now = Calendar.getInstance();
        mLatestVisit = new RoverVisit();
        mLatestVisit.setSimulation(mSandBoxMode);
        mLatestVisit.setCustomer((RoverCustomer)RoverUtils.readObjectFromSharedPrefs(mContext, RoverCustomer.class, null));
        mLatestVisit.setRegion(mainRegion);
        mLatestVisit.setTimeStamp(now.getTime());
        mLatestVisit.setLastBeaconDetectionTime(now);

        mLatestVisit.save(new RoverObjectSaveListener() {

            @Override
            public void onSaveSuccess() {

                Log.d(TAG, "Visit object save successful");
                RoverEventBus.getInstance().post(new RoverEnteredLocationEvent(mLatestVisit));
                if(!mLatestVisit.isInSubRegion(subRegion)) {
                    didEnterSubRegion(subRegion);
                }
                RoverEventBus.getInstance().post(new RoverRangeEvent(RoverConstants.RANGE_ACTION_START));
                RoverUtils.writeObjectToSharedPrefs(mContext, mLatestVisit);
            }

            @Override
            public void onSaveFailure() {

                Log.d(TAG, "Visit object save failed");
            }
        });
    }

    @Subscribe
    public void didExitRegion(RoverExitedRegionEvent event) {

        mLatestVisit.setLastBeaconDetectionTime(Calendar.getInstance());

        if(event.getRegionType().equals(RoverConstants.REGION_TYPE_MAIN)) {
            if(mLatestVisit.currentlyContainsWildCardTouchpoints()) {
                exitAllWildCardTouchpoints();
            }
            if(mLatestVisit.currentlyContainsTouchpoints()) {
                exitAllTouchpoints();
            }
            RoverUtils.writeObjectToSharedPrefs(mContext, mLatestVisit);
            RoverEventBus.getInstance().post(new RoverExitedLocationEvent(mLatestVisit));
            if(mRangeTimer == null) {
                mRangeTimer = new RoverTimer(mLatestVisit.getKeepAliveTime(), COUNT_DOWN_INTERVAL);
            }
            mRangeTimer.start();
            mRangeTimer.setCountDownStarted(true);
        }
        else if(event.getRegionType().equals(RoverConstants.REGION_TYPE_SUB)) {
            RoverRegion subRegion = event.getRegion();
            didExitSubRegion(subRegion);
        }
    }

    public void exitAllWildCardTouchpoints() {

        Iterator<RoverTouchpoint> iterator = mLatestVisit.getCurrentTouchpoints().iterator();
        while(iterator.hasNext()) {
            RoverTouchpoint touchpoint = iterator.next();
            if(touchpoint.getTrigger().equals(RoverConstants.WILD_CARD_TOUCHPOINT_TRIGGER)) {
                RoverEventBus.getInstance().post(new RoverExitedTouchpointEvent(mLatestVisit.getId(), touchpoint));
                iterator.remove();
            }
        }
    }

    public void exitAllTouchpoints() {

        Iterator<RoverTouchpoint> iterator = mLatestVisit.getCurrentTouchpoints().iterator();
        while(iterator.hasNext()) {
            RoverEventBus.getInstance().post(new RoverExitedTouchpointEvent(mLatestVisit.getId(), iterator.next()));
            iterator.remove();
        }
    }

    public void didEnterSubRegion(RoverRegion subRegion) {

        if(!mLatestVisit.currentlyContainsWildCardTouchpoints()) {
            for(RoverTouchpoint wildCardTouchpoint : mLatestVisit.getWildCardTouchpoints()) {
                RoverEnteredTouchpointEvent enteredTouchpointEvent = new RoverEnteredTouchpointEvent(mLatestVisit.getId(), wildCardTouchpoint);
                if(!mLatestVisit.getVisitedTouchpoints().contains(wildCardTouchpoint)) {
                    Log.d(TAG, "Touchpoint wild card (" + wildCardTouchpoint.getTitle() + ") - not seen before");
                    enteredTouchpointEvent.setBeenVisited(false);
                }
                else {
                    Log.d(TAG, "Touchpoint wild card (" + wildCardTouchpoint.getTitle() + ") - seen before");
                    enteredTouchpointEvent.setBeenVisited(true);
                }
                mLatestVisit.addToCurrentTouchpoints(wildCardTouchpoint);
                RoverEventBus.getInstance().post(enteredTouchpointEvent);
            }
        }

        RoverTouchpoint touchpoint = mLatestVisit.getTouchpoint(subRegion);

        if(touchpoint != null) {
            RoverEnteredTouchpointEvent enteredTouchpointEvent = new RoverEnteredTouchpointEvent(mLatestVisit.getId(), touchpoint);
            if(!mLatestVisit.getVisitedTouchpoints().contains(touchpoint)) {
                Log.d(TAG, "Touchpoint minor " + touchpoint.getMinor() + " (" + touchpoint.getTitle() + ") - not seen before");
                enteredTouchpointEvent.setBeenVisited(false);
            }
            else {
                Log.d(TAG, "Touchpoint minor " + touchpoint.getMinor() + " (" + touchpoint.getTitle() + ") - seen before");
                enteredTouchpointEvent.setBeenVisited(true);
            }
            mLatestVisit.addToCurrentTouchpoints(touchpoint);
            RoverEventBus.getInstance().post(enteredTouchpointEvent);
        }
    }

    public void didExitSubRegion(RoverRegion subRegion) {

        RoverTouchpoint touchpoint = mLatestVisit.getTouchpoint(subRegion);
        if(touchpoint != null) {
            mLatestVisit.removeFromCurrentTouchpoints(touchpoint);
            RoverEventBus.getInstance().post(new RoverExitedTouchpointEvent(mLatestVisit.getId(), touchpoint));
        }
    }
    
    public RoverVisit getLatestVisit() {
        
        if(mLatestVisit == null) {
            mLatestVisit = (RoverVisit)RoverUtils.readObjectFromSharedPrefs(mContext, RoverVisit.class, null);
        }
        return mLatestVisit;
    }

    private class RoverTimer extends CountDownTimer {

        public final String TAG = RoverTimer.class.getSimpleName();
        private boolean mCountDownStarted = false;

        public RoverTimer(long keepAliveTimeInMinutes, long countDownInterval) {

            super(TimeUnit.MINUTES.toMillis(keepAliveTimeInMinutes), countDownInterval);
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

            Log.d(TAG, "RoverRangeTimer has expired - ranging will now be stopped");
            RoverEventBus.getInstance().post(new RoverRangeEvent(RoverConstants.RANGE_ACTION_STOP));
        }
    }
}