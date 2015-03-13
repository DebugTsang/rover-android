package co.roverlabs.sdk.managers;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;

import co.roverlabs.sdk.events.RoverEnteredRegionEvent;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverExitedRegionEvent;
import co.roverlabs.sdk.models.RoverRegion;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class RoverRegionManager {
    
    public static final String TAG = RoverRegionManager.class.getSimpleName();
    private static RoverRegionManager sRegionManagerInstance;
    private BeaconManager mBeaconManager;
    private boolean mIsBeaconServiceReady;
    private Region mMonitorRegion;
    private Region mRangeRegion;
    private Beacon mNearestBeacon;
    
    private RoverRegionManager(Context con) {

        mBeaconManager = new BeaconManager(con);
        mIsBeaconServiceReady = false;
    }

    public static RoverRegionManager getInstance(Context con) {

        if(sRegionManagerInstance == null) {
            sRegionManagerInstance = new RoverRegionManager(con);
        }
        return sRegionManagerInstance;
    }
    
    public void setMonitorRegion(String uuid) {

        mMonitorRegion = new Region("Monitor Region", uuid, null, null);
    }
    
    public void startMonitoring() {

        mBeaconManager.setMonitoringListener((new BeaconManager.MonitoringListener() {
            
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {

                Log.d(TAG, "region has been entered");
                mNearestBeacon = beacons.get(0);
                mRangeRegion = new Region("Range Region", mNearestBeacon.getProximityUUID(), mNearestBeacon.getMajor(), null);
                RoverRegion roverRegion = new RoverRegion(mNearestBeacon.getProximityUUID(), mNearestBeacon.getMajor(), mNearestBeacon.getMinor());
                RoverEventBus.getInstance().post(new RoverEnteredRegionEvent(roverRegion));
                // stop monitoring
                //startRanging();
            }

            @Override
            public void onExitedRegion(Region region) {
                
                Log.d(TAG, "region has been exited");
                stopRanging();
                RoverEventBus.getInstance().post(new RoverExitedRegionEvent());
            }
        }));

        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            
            @Override 
            public void onServiceReady() {
                
                mIsBeaconServiceReady = true;
                
                try {
                    mBeaconManager.startMonitoring(mMonitorRegion);
                    Log.d(TAG, "Monitor region is " + mMonitorRegion.toString());
                } 
                catch(RemoteException e) {
                    Log.e(TAG, "Cannot start monitoring", e);
                }
            }
        });
    }
    
    public void stopMonitoring() {

        try {
            mBeaconManager.stopMonitoring(mMonitorRegion);
        } 
        catch(RemoteException e) {
            Log.d(TAG, "Cannot stop monitoring", e);
        }
        
        mBeaconManager.disconnect();
        mIsBeaconServiceReady = false;
    }
    
    public void startRanging() {
        
        //TODO: Remove, for testing
        //mBeaconManager.setForegroundScanPeriod(TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(5));
        
        RoverRegionManager self = this;
        
        mBeaconManager.setRangingListener(new BeaconManager.RangingListener() {
            
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                
                Log.d(TAG, "it's ranging now");              
                //This should be the closest beacon
//                if(!beacons.isEmpty()) {
//                    Beacon beacon = beacons.get(0);
//                    if (!mSeenBeacons.contains(beacon)) {
//                        Log.d(TAG, "Seeing a new beacon " + beacon);
//                        mSeenBeacons.add(beacon);
//                        RoverRegion roverRegion = new RoverRegion(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
//                        RoverEventBus.getInstance().post(new RoverEnteredRegionEvent(roverRegion));
//                    }
//                }
                if(!beacons.isEmpty()) {
                    Beacon beacon = beacons.get(0);
                    if(mNearestBeacon != null && mNearestBeacon.equals(beacon)) {
                        return;
                    }
                    mNearestBeacon = beacon;
                    RoverRegion roverRegion = new RoverRegion(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
                    RoverEventBus.getInstance().post(new RoverEnteredRegionEvent(roverRegion));
                } 
                else {
                    //mNearestBeacon = null;
                    return;
                    //TODO: Broadcast RoverExitedRegionEvent?
                }
            }
        });
        
        if(mIsBeaconServiceReady) {
            try {
                mBeaconManager.startRanging(mRangeRegion);
            }
            catch(RemoteException e) {
                Log.e(TAG, "Cannot start ranging", e);
            }
        }
        else {
            mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                
                @Override
                public void onServiceReady() {

                    try {
                        mBeaconManager.startRanging(mRangeRegion);
                    }
                    catch(RemoteException e) {
                        Log.e(TAG, "Cannot start ranging", e);
                    }
                }
            });
        }
    }
    
    public void stopRanging() {
        
        try {
            Log.d(TAG, "it's not ranging anymore");
            mBeaconManager.stopRanging(mRangeRegion);
            mRangeRegion = null;
        }
        catch (RemoteException e) {
            Log.d(TAG, "Cannot stop ranging", e);
        }
    }
}
