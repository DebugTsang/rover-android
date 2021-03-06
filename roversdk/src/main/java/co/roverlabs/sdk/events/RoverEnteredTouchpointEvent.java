package co.roverlabs.sdk.events;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

import co.roverlabs.sdk.models.RoverTouchPoint;

/**
 * Created by SherryYang on 2015-03-09.
 */
public class RoverEnteredTouchpointEvent extends RoverEvent {

    //JSON member
    @SerializedName("touchpoint") private String mTouchpointId;

    //Local members
    public static final String TAG = RoverEnteredTouchpointEvent.class.getSimpleName();
    transient private RoverTouchPoint mTouchpoint;
    transient private boolean mVisited;
    
    public RoverEnteredTouchpointEvent(String id, RoverTouchPoint touchpoint) {

        mId = id;
        mObjectName = "touchpoint";
        mAction = "enter";
        mTimeStamp = Calendar.getInstance().getTime();
        mTouchpoint = touchpoint;
        mTouchpointId = touchpoint.getId();
    }
    
    public RoverTouchPoint getTouchpoint() { return mTouchpoint; }
    public boolean hasBeenVisited() { return mVisited; }

    public void setBeenVisited(boolean visited) { mVisited = visited; }
}
