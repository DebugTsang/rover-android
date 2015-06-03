package co.roverlabs.sdk.events;

import java.util.Calendar;

import co.roverlabs.sdk.model.RoverVisit;

/**
 * Created by SherryYang on 2015-03-25.
 */
public class RoverExitedLocationEvent extends RoverEvent {

    public static final String TAG = RoverExitedLocationEvent.class.getSimpleName();
    transient private RoverVisit mVisit;

    public RoverExitedLocationEvent(RoverVisit visit) {

        mObjectName = "location";
        mAction = "exit";
        mTimeStamp = Calendar.getInstance().getTime();
        mVisit = visit;
        mId = visit.getId();
    }

    public RoverVisit getVisit() { return mVisit; }
}
