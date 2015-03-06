package co.roverlabs.sdk.events;

import co.roverlabs.sdk.models.RoverRegion;

/**
 * Created by SherryYang on 2015-03-06.
 */
public class RoverEnteredRegionEvent {
    
    private RoverRegion mRegion;
    
    public RoverEnteredRegionEvent(RoverRegion region) {
        
        mRegion = new RoverRegion(region.getId(), region.getUuid(), region.getMajor(), region.getMinor());
    }
    
    public RoverRegion getRegion() { return mRegion; }
}
