package co.roverlabs.sdk.events;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

import co.roverlabs.sdk.models.RoverTouchPoint;

/**
 * Created by SherryYang on 2015-03-25.
 */
public class RoverExitedTouchpointEvent extends RoverEvent {

    //JSON member
    @SerializedName("touchpoint") private String mTouchpointId;

    //Local members
    public static final String TAG = RoverExitedTouchpointEvent.class.getSimpleName();
    transient private RoverTouchPoint mTouchpoint;

    public RoverExitedTouchpointEvent(String id, RoverTouchPoint touchpoint) {

        mId = id;
        mObjectName = "touchpoint";
        mAction = "exit";
        mTimeStamp = Calendar.getInstance().getTime();
        mTouchpoint = touchpoint;
        mTouchpointId = touchpoint.getId();
    }

    public RoverTouchPoint getTouchpoint() { return mTouchpoint; }
}
