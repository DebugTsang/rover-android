package co.roverlabs.sdk.managers;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.List;

import co.roverlabs.sdk.events.RoverEnteredRegionEvent;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverExitedRegionEvent;
import co.roverlabs.sdk.models.RoverRegion;
import co.roverlabs.sdk.utilities.RoverConstants;
import co.roverlabs.sdk.utilities.RoverUtils;

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
    private List<Beacon> mCurrentBeacons;
    private boolean mMonitorStarted;
    private boolean mRangeStarted;
    
    private RoverRegionManager(Context con) {

        mBeaconManager = new BeaconManager(con);
        mCurrentBeacons = new ArrayList<>();
        mIsBeaconServiceReady = false;
        mMonitorStarted = false;
        mRangeStarted = false;
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

                Log.d(TAG, "Main region has been entered for major " + region.getMajor());
                Beacon beacon = beacons.get(0);
                mCurrentBeacons.add(beacon);
                mRangeRegion = new Region("Range Region", beacon.getProximityUUID(), beacon.getMajor(), null);
                RoverRegion enteredRegion = new RoverRegion(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
                RoverEventBus.getInstance().post(new RoverEnteredRegionEvent(enteredRegion));
            }

            @Override
            public void onExitedRegion(Region region) {
                
                Log.d(TAG, "Main region has been exited for major " + region.getMajor());
                RoverRegion exitedMainRegion = new RoverRegion(region.getProximityUUID(), region.getMajor(), region.getMinor());
                RoverEventBus.getInstance().post(new RoverExitedRegionEvent(exitedMainRegion, RoverConstants.REGION_TYPE_MAIN));
            }
        }));

        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            
            @Override 
            public void onServiceReady() {
                
                mIsBeaconServiceReady = true;
                
                try {
                    if(!mMonitorStarted) {
                        mBeaconManager.startMonitoring(mMonitorRegion);
                        mMonitorStarted = true;
                    }
                } 
                catch(RemoteException e) {
                    Log.e(TAG, "Cannot start monitoring", e);
                }
            }
        });
    }
    
    public void stopMonitoring() {

        try {
            if(mMonitorStarted) {
                mBeaconManager.stopMonitoring(mMonitorRegion);
                mMonitorStarted = false;
            }
        } 
        catch(RemoteException e) {
            Log.d(TAG, "Cannot stop monitoring", e);
        }
        
        mBeaconManager.disconnect();
        mIsBeaconServiceReady = false;
    }
    
    public void startRanging() {
        
        mBeaconManager.setRangingListener(new BeaconManager.RangingListener() {
            
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {

                List<Beacon> addedBeacons = new ArrayList<>();
                List<Beacon> subtractedBeacons = new ArrayList<>();
                addedBeacons = RoverUtils.subtractList(mCurrentBeacons, beacons);
                subtractedBeacons = RoverUtils.subtractList(beacons, mCurrentBeacons);
                mCurrentBeacons = beacons;
                for(Beacon beacon : addedBeacons) {
                    Log.d(TAG, "Region has been entered for minor " + beacon.getMinor());
                    RoverRegion enteredSubRegion = new RoverRegion(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
                    RoverEventBus.getInstance().post(new RoverEnteredRegionEvent(enteredSubRegion));
                }
                for(Beacon beacon : subtractedBeacons) {
                    Log.d(TAG, "Region has been exited for minor " + beacon.getMinor());
                    RoverRegion exitedSubRegion = new RoverRegion(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
                    RoverEventBus.getInstance().post(new RoverExitedRegionEvent(exitedSubRegion, RoverConstants.REGION_TYPE_SUB));
                }
            }
        });
        
        if(mIsBeaconServiceReady) {
            try {
                if(!mRangeStarted) {
                    mBeaconManager.startRanging(mRangeRegion);
                    Log.d(TAG, "It's ranging now");
                    mRangeStarted = true;
                }
            }
            catch(RemoteException e) {
                Log.e(TAG, "Cannot start ranging", e);
            }
        }
        else {
            mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                
                @Override
                public void onServiceReady() {

                    mIsBeaconServiceReady = true;

                    try {
                        if(!mRangeStarted) {
                            mBeaconManager.startRanging(mRangeRegion);
                            Log.d(TAG, "It's ranging now");
                            mRangeStarted = true;
                        }
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
            if(mRangeStarted) {
                mBeaconManager.stopRanging(mRangeRegion);
                Log.d(TAG, "It's not ranging anymore");
                mRangeStarted = false;
                mRangeRegion = null;
            }
        }
        catch (RemoteException e) {
            Log.d(TAG, "Cannot stop ranging", e);
        }
    }
}
