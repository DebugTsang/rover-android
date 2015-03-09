package co.roverlabs.sdk.events;

import co.roverlabs.sdk.models.RoverTouchpoint;

/**
 * Created by SherryYang on 2015-03-09.
 */
public class RoverEnteredTouchpointEvent {
    
    private RoverTouchpoint mTouchpoint;
    
    public RoverEnteredTouchpointEvent(RoverTouchpoint touchpoint) { mTouchpoint = touchpoint; }
    
    public RoverTouchpoint getTouchpoint() { return mTouchpoint; }
}
