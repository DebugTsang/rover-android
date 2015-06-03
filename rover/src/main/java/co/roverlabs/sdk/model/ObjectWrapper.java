package co.roverlabs.sdk.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-23.
 */
public class ObjectWrapper {

    //JSON members
    @SerializedName("visit") private Visit mVisit;
    @SerializedName("touchpoint") private Touchpoint mTouchpoint;
    
    //Local members
    public static final String TAG = ObjectWrapper.class.getSimpleName();

    //Getters
    public Visit getVisit() { return mVisit; }
    public Touchpoint getTouchpoint() { return mTouchpoint; }
    
    public Object get() {

        if(mVisit != null) {
            return getVisit();
        }
        else if(mTouchpoint != null) {
            return getTouchpoint();
        }
        return null;
    }

    //Setters
    public void setVisit(Visit visit) { mVisit = visit; }
    public void setTouchpoint(Touchpoint touchpoint) { mTouchpoint = touchpoint; }
    
    public void set(Object object) {

        String objectType = object.getObjectName();
        
        switch (objectType) {

            case "visit":
                setVisit((Visit)object);
                break;

            case "touchpoint":
                setTouchpoint((Touchpoint)object);
                break;

            default:
                Log.e(TAG, "Object type cannot be wrapped");
        }
    }
}
