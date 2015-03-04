package co.roverlabs.sdk.managers;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;

import co.roverlabs.sdk.Rover;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class RoverRegionManager {
    
    private static final String TAG = RoverRegionManager.class.getName();
    private static RoverRegionManager sRegionManagerInstance;
    private Context mContext;
    private BeaconManager mBeaconManager;
    private Region mRegion;
    
    private RoverRegionManager(Context con) {

        mContext = con;
        mBeaconManager = new BeaconManager(con);
        mRegion = new Region("General Region", Rover.getInstance(mContext).getUuid(), null, null);
    }

    public static RoverRegionManager getInstance(Context con) {

        if(sRegionManagerInstance == null) {
            sRegionManagerInstance = new RoverRegionManager(con);
        }
        return sRegionManagerInstance;
    }
    
    public void startMonitoring() {

        mBeaconManager.setMonitoringListener((new BeaconManager.MonitoringListener() {
            
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {

                RoverVisitManager.getInstance(mContext).didEnterLocation(region, beacons);
            }

            @Override
            public void onExitedRegion(Region region) {
                
                RoverVisitManager.getInstance(mContext).didExitLocation();
            }
        }));

        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            
            @Override 
            public void onServiceReady() {
                
                try {
                    mBeaconManager.startMonitoring(mRegion);
                } 
                catch (RemoteException e) {
                    Log.e(TAG, "Cannot start monitoring", e);
                }
            }
        });
    }
    
    public void stopMonitoring() {

        try {
            mBeaconManager.stopMonitoring(mRegion);
        } 
        catch (RemoteException e) {
            Log.d(TAG, "Cannot stop monitoring", e);
        }
        mBeaconManager.disconnect();
    }
}
