package co.roverlabs.sdk.events;

import java.util.Calendar;

import co.roverlabs.sdk.models.RoverVisit;

/**
 * Created by SherryYang on 2015-03-09.
 */
public class RoverEnteredLocationEvent extends RoverEvent {
    
    transient private RoverVisit mVisit;
    
    public RoverEnteredLocationEvent(RoverVisit visit) {

        mObjectName = "location";
        mAction = "enter";
        mTimeStamp = Calendar.getInstance().getTime();
        mVisit = visit;
        mId = visit.getId();
    }
    
    public RoverVisit getVisit() { return mVisit; }
}
