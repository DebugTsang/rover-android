package co.roverlabs.sdk.events;

import co.roverlabs.sdk.models.RoverTouchpoint;

/**
 * Created by SherryYang on 2015-03-25.
 */
public class RoverExitedTouchpointEvent {

    private RoverTouchpoint mTouchpoint;

    public RoverExitedTouchpointEvent(RoverTouchpoint touchpoint) { mTouchpoint = touchpoint; }

    public RoverTouchpoint getTouchpoint() { return mTouchpoint; }
}
