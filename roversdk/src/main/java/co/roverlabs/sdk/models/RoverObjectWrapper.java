package co.roverlabs.sdk.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import co.roverlabs.sdk.Rover;

/**
 * Created by SherryYang on 2015-02-23.
 */
public class RoverObjectWrapper {

    //JSON members
    @SerializedName("visit") private RoverVisit mVisit;
    @SerializedName("touchpoint") private RoverTouchPoint mTouchPoint;
    
    //Local members
    public static final String TAG = RoverObjectWrapper.class.getName();

    //Getters
    public RoverVisit getVisit() { return mVisit; }
    public RoverTouchPoint getTouchPoint() { return mTouchPoint; }
    
    public RoverModel get() {

        if(mVisit != null) {
            return mVisit;
        }
        else if(mTouchPoint != null) {
            return mTouchPoint;
        }
        return null;
    }

    //Setters
    public void setVisit(RoverVisit visit) { mVisit = visit; }
    public void setTouchPoint(RoverTouchPoint touchPoint) { mTouchPoint = touchPoint; }
    
    public void set(RoverModel object) {

        String objectType = object.getModelName();
        
        switch (objectType) {

            case "visit":
                setVisit((RoverVisit)object);
                break;

            case "touchpoint":
                setTouchPoint((RoverTouchPoint)object);
                break;

            default:
                Log.e(TAG, "Object type cannot be wrapped");
        }
    }
}
