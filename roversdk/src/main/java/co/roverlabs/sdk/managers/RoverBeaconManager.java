package co.roverlabs.sdk.managers;

import android.content.Context;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-03-19.
 */
public class RoverBeaconManager {

    public static final String TAG = RoverBeaconManager.class.getSimpleName();
    private static RoverBeaconManager sBeaconManager;
    private BeaconManager mBeaconManager;
    private Context mContext;
    private Region mMonitorRegion;
    private boolean mBeaconManagerConnected;

    private RoverBeaconManager(Context con) { mContext = con; }

    public static RoverBeaconManager getInstance(Context con) {

        if(sBeaconManager == null) {
            sBeaconManager = new RoverBeaconManager(con);
        }
        return sBeaconManager;
    }

    public void setMonitorRegion(String uuid) {

        mMonitorRegion = new Region("Monitor Region", uuid, null, null);
        RoverUtils.writeObjectToSharedPreferences(mContext, mMonitorRegion);
    }

    public void setBeaconManagerConnected(boolean connected) { mBeaconManagerConnected = connected; }

    public BeaconManager getBeaconManager() {

        if(mBeaconManager == null) {
            mBeaconManager = new BeaconManager(mContext);
        }
        return mBeaconManager;
    }

    public Region getMonitorRegion() {

        if(mMonitorRegion == null) {
            mMonitorRegion = (Region)RoverUtils.readObjectFromSharedPreferences(mContext, Region.class, null);
        }
        return mMonitorRegion;
    }

    public boolean isBeaconManagerConnected() { return mBeaconManagerConnected; }
}
