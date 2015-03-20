package co.roverlabs.sdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;

import co.roverlabs.sdk.events.RoverEnteredRegionEvent;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverExitedRegionEvent;
import co.roverlabs.sdk.managers.RoverBeaconManager;
import co.roverlabs.sdk.models.RoverRegion;

/**
 * Created by SherryYang on 2015-03-19.
 */
public class RoverMonitorService extends Service {

    public static final String TAG = RoverMonitorService.class.getSimpleName();
    private Rover mRover;
    private RoverBeaconManager mBeaconManager;
    private BeaconManager mEstimoteBeaconManager;
    private Region mMonitorRegion;

    @Override
    public void onCreate() {

        Log.d(TAG, "RoverService onCreate() is being called");
        mRover = Rover.getInstance(this.getApplicationContext());
        mRover.completeSetUp();
        mBeaconManager = RoverBeaconManager.getInstance(this.getApplicationContext());
        mEstimoteBeaconManager = mBeaconManager.getBeaconManager();
        mMonitorRegion = mBeaconManager.getMonitorRegion();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "RoverService onStartCommand() is being called");

        mEstimoteBeaconManager.setMonitoringListener((new BeaconManager.MonitoringListener() {

            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {

                Log.d(TAG, "Region has been entered");
                Beacon beacon = beacons.get(0);
                RoverRegion roverRegion = new RoverRegion(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
                RoverEventBus.getInstance().post(new RoverEnteredRegionEvent(roverRegion));
            }

            @Override
            public void onExitedRegion(Region region) {

                Log.d(TAG, "Region has been exited");
                RoverEventBus.getInstance().post(new RoverExitedRegionEvent());
            }
        }));

        if(!mBeaconManager.isBeaconManagerConnected()) {
            connectBeaconManager();
        }

        return START_STICKY;
    }

    private void connectBeaconManager() {

        mEstimoteBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {

            @Override
            public void onServiceReady() {

                try {
                    mBeaconManager.setBeaconManagerConnected(true);
                    mEstimoteBeaconManager.startMonitoring(mMonitorRegion);
                    Log.d(TAG, "Estimote Beacon Manager connected");
                    Log.d(TAG, "Monitor region is " + mMonitorRegion.toString());
                }
                catch(RemoteException e) {
                    Log.e(TAG, "Cannot start monitoring", e);
                }
            }
        });
    }

   private void disconnectBeaconManager() {

       try {
           mEstimoteBeaconManager.stopMonitoring(mMonitorRegion);
           mBeaconManager.setBeaconManagerConnected(false);
           Log.d(TAG, "Estimote Beacon Manager disconnected");
       }
       catch(RemoteException e) {
           Log.d(TAG, "Cannot stop monitoring", e);
       }

       mEstimoteBeaconManager.disconnect();
   }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Log.d(TAG, "RoverService onTaskRemoved() is being called");
        if(mBeaconManager.isBeaconManagerConnected()) {
            disconnectBeaconManager();
        }
    }

    @Override
    public void onDestroy() {

        Log.d(TAG, "RoverService onDestroy() is being called");
        if(mBeaconManager.isBeaconManagerConnected()) {
            disconnectBeaconManager();
        }
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}
