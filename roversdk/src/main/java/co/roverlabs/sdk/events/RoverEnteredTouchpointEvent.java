package co.roverlabs.sdk.events;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

import co.roverlabs.sdk.models.RoverTouchpoint;

/**
 * Created by SherryYang on 2015-03-09.
 */
public class RoverEnteredTouchpointEvent extends RoverEvent {

    //JSON member
    @SerializedName("touchpoint") private String mTouchpointId;

    //Local members
    transient private RoverTouchpoint mTouchpoint;
    transient private boolean mVisited;
    
    public RoverEnteredTouchpointEvent(String id, RoverTouchpoint touchpoint) {

        mId = id;
        mObjectName = "touchpoint";
        mAction = "enter";
        mTimeStamp = Calendar.getInstance().getTime();
        mTouchpoint = touchpoint;
        mTouchpointId = touchpoint.getId();
    }
    
    public RoverTouchpoint getTouchpoint() { return mTouchpoint; }
    public boolean hasBeenVisited() { return mVisited; }

    public void setBeenVisited(boolean visited) { mVisited = visited; }
}
