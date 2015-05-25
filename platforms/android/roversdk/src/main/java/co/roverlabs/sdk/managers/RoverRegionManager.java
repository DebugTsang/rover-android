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
import co.roverlabs.sdk.events.RoverRangeResultEvent;
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
        //mCurrentBeacons = new ArrayList<>();
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

        mBeaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {

            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {

                Log.d(TAG, "Region entered - main");
                if(beacons.size() != 0) {
                    Beacon beacon = beacons.get(0);
                    mCurrentBeacons = new ArrayList<>();
                    mCurrentBeacons.add(beacon);
                    mRangeRegion = new Region("Range Region", beacon.getProximityUUID(), beacon.getMajor(), null);
                    Log.d(TAG, "Region entered - minor " + beacon.getMinor());

                    RoverRegion enteredRegion = new RoverRegion(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
                    RoverEventBus.getInstance().post(new RoverEnteredRegionEvent(enteredRegion));
                }
            }

            @Override
            public void onExitedRegion(Region region) {

                Log.d(TAG, "Region exited - main");
                RoverRegion exitedMainRegion = new RoverRegion(region.getProximityUUID(), null, null);
                RoverEventBus.getInstance().post(new RoverExitedRegionEvent(exitedMainRegion, RoverConstants.REGION_TYPE_MAIN));
            }
        });

        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            
            @Override 
            public void onServiceReady() {
                
                mIsBeaconServiceReady = true;
                
                try {
                    if(!mMonitorStarted) {
                        mBeaconManager.startMonitoring(mMonitorRegion);
                        Log.d(TAG, "Monitoring now");
                        mMonitorStarted = true;
                    }
                    else {
                        Log.d(TAG, "Monitoring has already started - do nothing");
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
                Log.d(TAG, "Not monitoring anymore");
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

                //TODO: Remove after testing
                RoverEventBus.getInstance().post(new RoverRangeResultEvent(beacons));

                List<Beacon> addedBeacons;
                List<Beacon> subtractedBeacons;
                addedBeacons = RoverUtils.subtractList(mCurrentBeacons, beacons);
                subtractedBeacons = RoverUtils.subtractList(beacons, mCurrentBeacons);
                mCurrentBeacons = beacons;
                for(Beacon beacon : addedBeacons) {
                    Log.d(TAG, "Region entered - minor " + beacon.getMinor());
                    RoverRegion enteredSubRegion = new RoverRegion(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
                    RoverEventBus.getInstance().post(new RoverEnteredRegionEvent(enteredSubRegion));
                }
                for(Beacon beacon : subtractedBeacons) {
                    Log.d(TAG, "Region exited - minor " + beacon.getMinor());
                    RoverRegion exitedSubRegion = new RoverRegion(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
                    RoverEventBus.getInstance().post(new RoverExitedRegionEvent(exitedSubRegion, RoverConstants.REGION_TYPE_SUB));
                }
            }
        });
        
        if(mIsBeaconServiceReady) {
            try {
                if(!mRangeStarted) {
                    // TODO: this is ugly remove this,...pass region along instead of using iVars
                    if(mRangeRegion == null) {
                        mRangeRegion = new Region("Range Region", "7931D3AA-299B-4A12-9FCC-D66F2C5D2462", 18347, null);
                    }
                    mBeaconManager.startRanging(mRangeRegion);
                    Log.d(TAG, "Ranging now");
                    mRangeStarted = true;
                }
                else {
                    Log.d(TAG, "Ranging has already started - do nothing");
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
                            // TODO: this is ugly remove this,...pass region along instead of using iVars
                            if(mRangeRegion == null) {
                                mRangeRegion = new Region("Range Region", "7931D3AA-299B-4A12-9FCC-D66F2C5D2462", 18347, null);
                            }
                            mBeaconManager.startRanging(mRangeRegion);
                            Log.d(TAG, "Ranging now");
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
                Log.d(TAG, "Not ranging anymore");
                mRangeStarted = false;
                mRangeRegion = null;
            }
        }
        catch (RemoteException e) {
            Log.d(TAG, "Cannot stop ranging", e);
        }
    }
}
