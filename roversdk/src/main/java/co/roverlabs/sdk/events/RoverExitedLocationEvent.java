package co.roverlabs.sdk.events;

import com.estimote.sdk.Region;

import co.roverlabs.sdk.models.RoverRegion;
import co.roverlabs.sdk.models.RoverVisit;

/**
 * Created by SherryYang on 2015-03-20.
 */
public class RoverExitedLocationEvent {

    private RoverVisit mVisit;

    public RoverExitedLocationEvent(RoverVisit visit) { mVisit = visit; }

    public RoverVisit getVisit() { return mVisit; }

    public Region getRangeRegion() {

        RoverRegion region = mVisit.getRegion();
        Region rangeRegion = new Region("Range Region", region.getUuid(), region.getMajor(), null);
        return rangeRegion;
    }
}
