package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

import co.roverlabs.sdk.Rover;

/**
 * Created by SherryYang on 2015-02-23.
 */
public class RoverVisitWrapper {
    
    @SerializedName("visit")
    private RoverVisit mVisit;

    public RoverVisit getVisit() {

        return mVisit;
    }
    
    public void setVisit(RoverVisit visit) {
        
        mVisit = visit;
    }
}
