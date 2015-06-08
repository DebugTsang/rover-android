package co.roverlabs.sdk.core;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.roverlabs.sdk.model.Region;
import co.roverlabs.sdk.util.Utils;

/**
 * Created by SherryYang on 2015-06-04.
 */
public class RegionManager {

    public static final String TAG = RegionManager.class.getSimpleName();
    public IRegionManagerListener mRegionManagerListener;
    private String mUuid;
    private BeaconManager mBeaconManager;
    private com.estimote.sdk.Region mMonitorRegion;
    private com.estimote.sdk.Region mRangeRegion;
    private Set<Region> mCurrentRegions;
    volatile boolean mBeaconServiceReady = false;
    volatile boolean mMonitoringStarted = false;
    volatile boolean mRangingStarted = false;

    public interface IRegionManagerListener {

        void onEnterRegions(RegionManager regionManager, Set<Region> enteredRegions);
        void onExitRegions(RegionManager regionManager, Set<Region> exitedRegions);
    }

    public RegionManager(Context context) {

        mBeaconManager = new BeaconManager(context.getApplicationContext());
        setMonitoringListeners();
        setRangingListeners();
    }

    public void setListener(IRegionManagerListener listener) {

        mRegionManagerListener = listener;
    }

    public void removeListener() {

        mRegionManagerListener = null;
    }

    public void setUuid(String uuid) {

        mUuid = uuid;
        mMonitorRegion = new com.estimote.sdk.Region("Monitor Region", uuid, null, null);
    }

    public void setMonitoringScanRate(long scanTimeMillis, long waitTimeMillis) {

        mBeaconManager.setBackgroundScanPeriod(scanTimeMillis, waitTimeMillis);
    }

    public void setRangingScanRate(long scanTimeMillis, long waitTimeMillis) {

        mBeaconManager.setForegroundScanPeriod(scanTimeMillis, waitTimeMillis);
    }

    public Set<Region> getCurrentRegions() {

        return mCurrentRegions;
    }

    public void setMonitoringListeners() {

        BeaconManager.MonitoringListener monitoringListener = new BeaconManager.MonitoringListener() {

            @Override
            public void onEnteredRegion(com.estimote.sdk.Region region, List<Beacon> beacons) {

                if(beacons.size() != 0) {
                    if(Utils.hasIdenticalBeaconMajors(beacons)) {
                        mRangeRegion = new com.estimote.sdk.Region("Range Region", mUuid, beacons.get(0).getMajor(), null);
                        mCurrentRegions = new HashSet<>();
                        startRanging();
                    }
                    else {
                        Log.e(TAG, "Rover has stopped - beacons with different major numbers detected at the same location.");
                    }
                }
            }

            @Override
            public void onExitedRegion(com.estimote.sdk.Region region) { }
        };

        mBeaconManager.setMonitoringListener(monitoringListener);
    }

    public void setRangingListeners() {

        final RegionManager self = this;

        BeaconManager.RangingListener rangingListener = new BeaconManager.RangingListener() {

            @Override
            public void onBeaconsDiscovered(com.estimote.sdk.Region region, List<Beacon> beacons) {

                if(Utils.hasIdenticalBeaconMajors(beacons)) {

                    if(mCurrentRegions == null) {
                        mCurrentRegions = new HashSet<>();
                    }

                    Set<Region> detectedRegions = new HashSet<>();
                    Set<Region> enteredRegions = new HashSet<>();
                    Set<Region> exitedRegions = new HashSet<>();

                    for(Beacon beacon : beacons) {
                        Region beaconRegion = new Region(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
                        detectedRegions.add(beaconRegion);
                    }

                    enteredRegions = Utils.subtractSet(mCurrentRegions, detectedRegions);
                    exitedRegions = Utils.subtractSet(detectedRegions, mCurrentRegions);
                    mCurrentRegions = detectedRegions;

                    if(exitedRegions.size() > 0) {
                        mRegionManagerListener.onExitRegions(self, exitedRegions);
                    }

                    if(enteredRegions.size() > 0) {
                        mRegionManagerListener.onEnterRegions(self, enteredRegions);
                    }
                }
                else {
                    Log.e(TAG, "Rover has stopped - beacons with different major numbers detected at the same location.");
                }
            }
        };

        mBeaconManager.setRangingListener(rangingListener);
    }

    public void startMonitoring() {

        if(!mBeaconServiceReady) {

            mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {

                @Override
                public void onServiceReady() {

                    mBeaconServiceReady = true;

                    if(!mMonitoringStarted && mMonitorRegion != null) {
                        try {
                            mBeaconManager.startMonitoring(mMonitorRegion);
                        }
                        catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        mMonitoringStarted = true;
                    }
                }
            });
        }
    }

    public void stopMonitoring() {

        if(mMonitoringStarted && mMonitorRegion != null) {
            try {
                mBeaconManager.stopMonitoring(mMonitorRegion);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
            mMonitoringStarted = false;
        }

        mBeaconManager.disconnect();
        mBeaconServiceReady = false;
    }

    public boolean isMonitoringStarted() {

        return mMonitoringStarted;
    }

    public void startRanging() {

        if(!mBeaconServiceReady) {

            mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {

                @Override
                public void onServiceReady() {

                    mBeaconServiceReady = true;

                    if(!mRangingStarted && mRangeRegion != null) {
                        try {
                            mBeaconManager.startRanging(mRangeRegion);
                        }
                        catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        mRangingStarted = true;
                    }
                }
            });
        }
    }

    public void stopRanging() {

        if(mRangingStarted && mRangeRegion != null) {
            try {
                mBeaconManager.stopRanging(mRangeRegion);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
            mRangingStarted = false;
            mRangeRegion = null;
        }
    }
}