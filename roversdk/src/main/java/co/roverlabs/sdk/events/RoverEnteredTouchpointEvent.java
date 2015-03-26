package co.roverlabs.sdk.events;

import co.roverlabs.sdk.models.RoverTouchpoint;

/**
 * Created by SherryYang on 2015-03-09.
 */
public class RoverEnteredTouchpointEvent {
    
    private RoverTouchpoint mTouchpoint;
    private boolean mVisited;
    
    public RoverEnteredTouchpointEvent(RoverTouchpoint touchpoint) {

        mTouchpoint = touchpoint;
    }
    
    public RoverTouchpoint getTouchpoint() { return mTouchpoint; }
    public boolean hasBeenVisited() { return mVisited; }

    public void setBeenVisited(boolean visited) { mVisited = visited; }
}
