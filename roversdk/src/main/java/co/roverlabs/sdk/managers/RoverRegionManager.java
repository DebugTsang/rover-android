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
    
    private static final String TAG = RoverRegionManager.class.getName();
    private static RoverRegionManager sRegionManagerInstance;
    private BeaconManager mBeaconManager;
    private RoverRegion mRegion;
    private Region mEstimoteRegion;
    
    private RoverRegionManager(Context con) {

        mBeaconManager = new BeaconManager(con);
    }

    public static RoverRegionManager getInstance(Context con) {

        if(sRegionManagerInstance == null) {
            sRegionManagerInstance = new RoverRegionManager(con);
        }
        return sRegionManagerInstance;
    }
    
    public void setMonitorRegion(String uuid) {
        
        mRegion = new RoverRegion("Current Region", uuid, null, null);
        mEstimoteRegion = new Region("Monitor Region", uuid, null, null);
    }
    
    public void startMonitoring() {

        mBeaconManager.setMonitoringListener((new BeaconManager.MonitoringListener() {
            
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {

                mRegion.setUuid(region.getProximityUUID());
                //TODO: Grab the actual nearest beacon instead of just the first in the list of beacons
                mRegion.setMajor(beacons.get(0).getMajor());
                mRegion.setMinor(beacons.get(0).getMinor());
                RoverEventBus.getInstance().post(new RoverEnteredRegionEvent(mRegion));
            }

            @Override
            public void onExitedRegion(Region region) {
                
                RoverEventBus.getInstance().post(new RoverExitedRegionEvent());
            }
        }));

        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            
            @Override 
            public void onServiceReady() {
                
                try {
                    mBeaconManager.startMonitoring(mEstimoteRegion);
                } 
                catch (RemoteException e) {
                    Log.e(TAG, "Cannot start monitoring", e);
                }
            }
        });
    }
    
    public void stopMonitoring() {

        try {
            mBeaconManager.stopMonitoring(mEstimoteRegion);
        } 
        catch (RemoteException e) {
            Log.d(TAG, "Cannot stop monitoring", e);
        }
        mBeaconManager.disconnect();
    }
}
