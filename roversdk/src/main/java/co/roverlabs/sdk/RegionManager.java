package co.roverlabs.sdk;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class RegionManager {
    
    private static final String TAG = RegionManager.class.getName();
    private static RegionManager sRegionManagerInstance;
    private Context mContext;
    private BeaconManager mBeaconManager;
    private Region mRegion;
    
    public RegionManager(Context con) {

        mContext = con;
        mBeaconManager = new BeaconManager(con);
        //mRegion = new Region("ID", Rover.getInstance(con).getUUID(), null, null);
        
        //Test
        mRegion = new Region("ID", Rover.getInstance(con).getUUID(), RoverConstants.TEST_MAJOR_ESTIMOTE, RoverConstants.TEST_MINOR_ESTIMOTE);
    }

    public static RegionManager getInstance(Context con) {

        if(sRegionManagerInstance == null) {
            sRegionManagerInstance = new RegionManager(con);
        }
        return sRegionManagerInstance;
    }
    
    public void startMonitoring() {

        mBeaconManager.setMonitoringListener((new BeaconManager.MonitoringListener() {
            
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                VisitManager.getInstance(mContext, mRegion).didEnterLocation();
            }

            @Override
            public void onExitedRegion(Region region) {
                VisitManager.getInstance(mContext, mRegion).didExitLocation();
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
