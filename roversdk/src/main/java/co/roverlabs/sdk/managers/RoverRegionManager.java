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
import co.roverlabs.sdk.models.RoverRegion;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class RoverRegionManager {
    
    public static final String TAG = RoverRegionManager.class.getSimpleName();
    private static RoverRegionManager sRegionManagerInstance;
    private RoverBeaconManager mBeaconManager;
    private BeaconManager mEstimoteBeaconManager;
    private Beacon mNearestBeacon;
    
    private RoverRegionManager(Context con) {

        mBeaconManager = RoverBeaconManager.getInstance(con);
        mEstimoteBeaconManager = mBeaconManager.getBeaconManager();
    }

    public static RoverRegionManager getInstance(Context con) {

        if(sRegionManagerInstance == null) {
            sRegionManagerInstance = new RoverRegionManager(con);
        }
        return sRegionManagerInstance;
    }

    public void startRanging(final Region rangeRegion) {

        Log.d(TAG, "startRanging() is called");

        mEstimoteBeaconManager.setRangingListener(new BeaconManager.RangingListener() {

            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {

            Log.d(TAG, "It's ranging now");
            if(!beacons.isEmpty()) {
                Beacon beacon = beacons.get(0);
                if(mNearestBeacon != null && mNearestBeacon.equals(beacon)) {
                    return;
                }
                mNearestBeacon = beacon;
                RoverRegion roverRegion = new RoverRegion(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
                RoverEventBus.getInstance().post(new RoverEnteredRegionEvent(roverRegion));
            }
            }
        });

        if(mBeaconManager.isBeaconManagerConnected()) {
            try {
                mEstimoteBeaconManager.startRanging(rangeRegion);
            } catch (RemoteException e) {
                Log.e(TAG, "Cannot start ranging", e);
            }
        }
        else {
            mEstimoteBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {

                @Override
                public void onServiceReady() {

                    try {
                        mBeaconManager.setBeaconManagerConnected(true);
                        mEstimoteBeaconManager.startRanging(rangeRegion);
                    } catch (RemoteException e) {
                        Log.e(TAG, "Cannot start ranging", e);
                    }
                }
            });
        }
    }

    public void stopRanging(Region rangeRegion) {

        try {
            Log.d(TAG, "It's not ranging anymore");
            mEstimoteBeaconManager.stopRanging(rangeRegion);
        }
        catch (RemoteException e) {
            Log.d(TAG, "Cannot stop ranging", e);
        }
    }
}
