package co.roverlabs.sdk.events;

import com.estimote.sdk.Beacon;

import java.util.List;

/**
 * Created by SherryYang on 2015-05-19.
 */
public class RoverRangeResultEvent {

    public static final String TAG = RoverRangeResultEvent.class.getSimpleName();
    private List<Beacon> mBeacons;

    public RoverRangeResultEvent(List<Beacon> beacons) {

        mBeacons = beacons;
    }

    public List<Beacon> getBeaconsInRange() {

        return mBeacons;
    }

    public String displayBeaconsInRange() {

        String beaconsInRange = "Beacons in range: ";

        for(Beacon beacon : mBeacons) {
            beaconsInRange += beacon.getMinor() + " ";
        }

        return beaconsInRange;
    }
}
