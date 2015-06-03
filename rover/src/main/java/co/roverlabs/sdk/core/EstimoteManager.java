package co.roverlabs.sdk.core;

import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by arsent on 15-06-01.
 */
public class EstimoteManager implements ILocationManager {

    static final String TAG = EstimoteManager.class.getSimpleName();

    volatile boolean isLoggingEnabled = false;

    final Context context;
    private BeaconManager mEstimoteManager;
    private Handler mHandler;

    volatile boolean isMonitoringStarted = false;

    public EstimoteManager(Context context, Handler handler){
        this.context = context;
        this.mHandler = handler;

        mEstimoteManager = new BeaconManager(context);
        mEstimoteManager.setRangingListener(rangingListener);
        mEstimoteManager.setMonitoringListener(monitoringListener);

    }

    void setLoggingEnabled(boolean isEnabled){
        isLoggingEnabled = isEnabled;
    }

    /**
     * Initiates monitoring using estimote
     */
    @Override
    public void startMonitoring(String uuid){
        final Region region = new Region("Monitor Region", uuid, null, null);

        //not doing anything if monitoring has been started already
        if (isMonitoringStarted) {
            if (isLoggingEnabled){
                Log.d(TAG, "Monitoring is already in progress. Not doing anything.");
            }
            return;
        }

        isMonitoringStarted = true;

        //TODO: make customizable
        mEstimoteManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);

        mEstimoteManager.connect(new BeaconManager.ServiceReadyCallback() {

            @Override
            public void onServiceReady() {
                try {
                    mEstimoteManager.startMonitoring(region);
                    isMonitoringStarted = true;
                } catch (RemoteException e) {

                    isMonitoringStarted = false;
                    Log.e(TAG, "Cannot start monitoring", e);
                }
            }
        });
    }

    @Override
    public void stopMonitoring(){
        mEstimoteManager.disconnect();
    }

    @Override
    public boolean isMonitoringStarted() {
        return isMonitoringStarted;
    }

    @Override
    public void startRanging() {

    }

    @Override
    public void stopRanging() {

    }


    /**
     *
     *  Defining ranging and monitoring listeners
     *
     */
    BeaconManager.RangingListener rangingListener = new BeaconManager.RangingListener() {

        @Override
        public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {

            //TODO: process beacons one by one and start ranging
        }
    };


    BeaconManager.MonitoringListener monitoringListener = new BeaconManager.MonitoringListener() {

        @Override
        public void onEnteredRegion(Region region, List<Beacon> beacons) {

            if (beacons.size() != 0) {

                //TODO: why are we ignoring other beacons
                Beacon beacon = beacons.get(0);
                co.roverlabs.sdk.model.Region rangingRegion = new co.roverlabs.sdk.model.Region(beacon.getProximityUUID(), beacon.getMajor(), null);

                //let the visitmanager know that we entered a region
                mHandler.sendMessage(mHandler.obtainMessage(ENTERED_REGION, rangingRegion));

                if (isLoggingEnabled) {
                    Log.d(TAG, "Region entered - minor " + beacon.getMinor());
                }
            }
        }

        @Override
        public void onExitedRegion(Region region) {
            //let the visitmanager know that we exited a region
            mHandler.sendMessage(mHandler.obtainMessage(EXITED_REGION, region));

            if (isLoggingEnabled) {
                Log.d(TAG, "Region exited - main" + region.getMajor() + ", " + region.getMinor());
            }
        }
    };
}
