package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

import co.roverlabs.sdk.Rover;

/**
 * Created by SherryYang on 2015-02-23.
 */
public class RoverObjectWrapper {

    //JSON members
    @SerializedName("visit") private RoverVisit mVisit;
    @SerializedName("touchpoint") private RoverTouchPoint mTouchPoint;

    //Getters
    public RoverVisit getVisit() { return mVisit; }
    public RoverTouchPoint getTouchPoint() { return mTouchPoint; }

    //Setters
    public void setVisit(RoverVisit visit) { mVisit = visit; }
    public void setTouchPoint(RoverTouchPoint touchPoint) { mTouchPoint = touchPoint; }
}
