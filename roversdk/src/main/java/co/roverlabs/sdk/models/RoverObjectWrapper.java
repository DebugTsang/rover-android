package co.roverlabs.sdk.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-23.
 */
public class RoverObjectWrapper {

    //JSON members
    @SerializedName("visit") private RoverVisit mVisit;
    @SerializedName("touchpoint") private RoverTouchpoint mTouchpoint;
    
    //Local members
    public static final String TAG = RoverObjectWrapper.class.getSimpleName();

    //Getters
    public RoverVisit getVisit() { return mVisit; }
    public RoverTouchpoint getTouchpoint() { return mTouchpoint; }
    
    public RoverObject get() {

        if(mVisit != null) {
            return mVisit;
        }
        else if(mTouchpoint != null) {
            return mTouchpoint;
        }
        return null;
    }

    //Setters
    public void setVisit(RoverVisit visit) { mVisit = visit; }
    public void setTouchpoint(RoverTouchpoint touchpoint) { mTouchpoint = touchpoint; }
    
    public void set(RoverObject object) {

        String objectType = object.getObjectName();
        
        switch (objectType) {

            case "visit":
                setVisit((RoverVisit)object);
                break;

            case "touchpoint":
                setTouchpoint((RoverTouchpoint)object);
                break;

            default:
                Log.e(TAG, "Object type cannot be wrapped");
        }
    }
}
