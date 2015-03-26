package co.roverlabs.sdk.events;

import co.roverlabs.sdk.models.RoverVisit;

/**
 * Created by SherryYang on 2015-03-25.
 */
public class RoverExitedLocationEvent {

    private RoverVisit mVisit;

    public RoverExitedLocationEvent(RoverVisit visit) { mVisit = visit; }

    public RoverVisit getVisit() { return mVisit; }
}
