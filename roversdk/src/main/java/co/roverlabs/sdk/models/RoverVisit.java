package co.roverlabs.sdk.models;

import android.util.Log;

import com.estimote.sdk.Region;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverVisit extends RoverModel {
    
    //JSON members
    @SerializedName("customer") private RoverCustomer mCustomer;
    @SerializedName("enteredAt") private Date mEnteredTime;
    @SerializedName("exitedAt") private Date mExitedTime;
    @SerializedName("keepAlive") private long mKeepAliveTime;
    @SerializedName("location") private RoverLocation mLocation;
    @SerializedName("organization") private RoverOrganization mOrganization;
    @SerializedName("touchpoints") private List<RoverTouchPoint> mTouchPoints;

    //Local members
    private static final String TAG = RoverVisit.class.getName();
    private Region mRegion;
    private Calendar mLastBeaconDetectionTime;

    //Temp, for sending to server
    public String customer_id;
    public String uuid;
    public int major;
    
    //Constructor
    public RoverVisit(Region region) {
        
        mModelName = "visit";
        mRegion = region;
    }
    
    //Getters
    public RoverCustomer getCustomer() { return mCustomer; }
    public Date getEnteredTime() { return mEnteredTime; }
    public Date getExitedTime() { return mExitedTime; }
    public long getKeepAliveTime() { return mKeepAliveTime; }
    public RoverLocation getLocation() { return mLocation; }
    public RoverOrganization getOrganization() { return mOrganization; }
    public List<RoverTouchPoint> getTouchPoints() { return mTouchPoints; }
    public Calendar getLastBeaconDetectionTime() { return mLastBeaconDetectionTime; }
    
    //Setters
    public void setCustomer(RoverCustomer customer) { mCustomer = customer; }
    public void setEnteredTime(Date enteredTime) { mEnteredTime = enteredTime; }
    public void setExitedTime(Date exitedTime) { mExitedTime = exitedTime; }
    public void setKeepAliveTime(long keepAliveTime) { mKeepAliveTime = keepAliveTime; }
    public void setLocation(RoverLocation location) { mLocation = location; }
    public void setOrganization(RoverOrganization organization) { mOrganization = organization; }
    public void setTouchPoints(List<RoverTouchPoint> touchPoints) { mTouchPoints = touchPoints; }
    public void setLastBeaconDetection(Calendar time) { mLastBeaconDetectionTime = time; }

    public boolean isInRegion(Region region) {

        return mRegion.equals(region);
    }

    public boolean isAlive() {

        //Temp, 5 minutes
        mKeepAliveTime = 300000;
        Calendar now = Calendar.getInstance();
        long elapsedTime = now.getTimeInMillis() - mLastBeaconDetectionTime.getTimeInMillis();
        return elapsedTime < mKeepAliveTime;
    }
}
