package co.roverlabs.sdk.events;

import co.roverlabs.sdk.models.RoverRegion;

/**
 * Created by SherryYang on 2015-03-06.
 */
public class RoverExitedRegionEvent {

    private RoverRegion mRegion;
    private String mRegionType;

    public RoverExitedRegionEvent(RoverRegion region, String regionType) {

        mRegion = new RoverRegion(region.getUuid(), region.getMajor(), region.getMinor());
        mRegionType = regionType;
    }

    public RoverRegion getRegion() { return mRegion; }
    public String getRegionType() { return mRegionType; }
}