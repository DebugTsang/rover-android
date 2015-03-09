package co.roverlabs.sdk.events;

import co.roverlabs.sdk.models.RoverVisit;

/**
 * Created by SherryYang on 2015-03-09.
 */
public class RoverEnteredLocationEvent {
    
    private RoverVisit mVisit;
    
    public RoverEnteredLocationEvent(RoverVisit visit) { mVisit = visit; }
    
    public RoverVisit getVisit() { return mVisit; }
}
