package co.roverlabs.sdk.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.roverlabs.sdk.listeners.RoverObjectSaveListener;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverVisit extends RoverObject {
    
    //JSON members
    @SerializedName("customer") private RoverCustomer mCustomer;
    @SerializedName("enteredAt") private Date mEnteredTime;
    @SerializedName("exitedAt") private Date mExitedTime;
    @SerializedName("keepAlive") private long mKeepAliveTime;
    @SerializedName("location") private RoverLocation mLocation;
    @SerializedName("organization") private RoverOrganization mOrganization;
    @SerializedName("touchpoints") private List<RoverTouchpoint> mTouchpoints;

    //Local members
    public static final String TAG = RoverVisit.class.getSimpleName();
    private RoverRegion mRegion;
    private Calendar mLastBeaconDetectionTime;
    private RoverTouchpoint mCurrentTouchpoint;
    private List<RoverTouchpoint> mVisitedTouchpoints;
    //TODO: Get rid of these members
    public String customer_id;
    public String uuid;
    public int major;
    
    //Constructor
    public RoverVisit(Context con) { 
        
        super(con);
        mObjectName = "visit"; 
        mVisitedTouchpoints = new ArrayList<>();
    }
    
    //Getters
    public RoverCustomer getCustomer() { return mCustomer; }
    public Date getEnteredTime() { return mEnteredTime; }
    public Date getExitedTime() { return mExitedTime; }
    public long getKeepAliveTime() { return mKeepAliveTime; }
    public RoverLocation getLocation() { return mLocation; }
    public RoverOrganization getOrganization() { return mOrganization; }
    public List<RoverTouchpoint> getTouchpoints() { return mTouchpoints; }
    public Calendar getLastBeaconDetectionTime() { return mLastBeaconDetectionTime; }
    public RoverRegion getRegion() { return mRegion; }
    public RoverTouchpoint getCurrentTouchpoint() { return mCurrentTouchpoint; }
    public List<RoverTouchpoint> getVisitedTouchpoints() { return mVisitedTouchpoints; }
    
    public RoverTouchpoint getTouchpoint(RoverRegion region) {
        
        for(RoverTouchpoint touchpoint : mTouchpoints) {
            if((touchpoint.getMinor()).equals(region.getMinor())) {
                return touchpoint;
            }
        }
        return null;
    }
    
    //Setters
    public void setCustomer(RoverCustomer customer) { mCustomer = customer; }
    public void setEnteredTime(Date enteredTime) { mEnteredTime = enteredTime; }
    public void setExitedTime(Date exitedTime) { mExitedTime = exitedTime; }
    public void setKeepAliveTime(long keepAliveTime) { mKeepAliveTime = keepAliveTime; }
    public void setLocation(RoverLocation location) { mLocation = location; }
    public void setOrganization(RoverOrganization organization) { mOrganization = organization; }
    public void setTouchpoints(List<RoverTouchpoint> touchpoints) { mTouchpoints = touchpoints; }
    public void setLastBeaconDetection(Calendar time) { mLastBeaconDetectionTime = time; }
    public void setRegion(RoverRegion region) { mRegion = region; }
    
    public void setCurrentTouchpoint(RoverTouchpoint touchpoint) {
        
        if(!mVisitedTouchpoints.contains(touchpoint)) {
            mVisitedTouchpoints.add(touchpoint);
        }
        mCurrentTouchpoint = touchpoint;
    }
    
    public void save(RoverObjectSaveListener saveListener) {

        //TODO: Change these hard coded values, should be grabbed from the list of beacons (mBeacons)
        this.customer_id ="1234";
        this.major = 52643;
        this.uuid = "F352DB29-6A05-4EA2-A356-9BFAC2BB3316";
        super.save(saveListener);
    }
    
    public void update(RoverObject object) {
        
        RoverVisit visit = (RoverVisit)object;
        super.update(visit);
        mCustomer = visit.getCustomer();
        mEnteredTime = visit.getEnteredTime();
        mExitedTime = visit.getExitedTime();
        mKeepAliveTime = visit.getKeepAliveTime();
        mLocation = visit.getLocation();
        mOrganization = visit.getOrganization();
        mTouchpoints = visit.getTouchpoints();
    }

    public boolean isInRegion(RoverRegion region) {

        return mRegion.equals(region);
    }

    public boolean isAlive() {

        //TODO: Get rid of temporary keep alive time of 5 minutes
        mKeepAliveTime = 300000;
        Calendar now = Calendar.getInstance();
        long elapsedTime = now.getTimeInMillis() - mLastBeaconDetectionTime.getTimeInMillis();
        return elapsedTime < mKeepAliveTime;
    }
}
