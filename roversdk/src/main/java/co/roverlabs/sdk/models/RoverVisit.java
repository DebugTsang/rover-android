package co.roverlabs.sdk.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverNotificationEvent;
import co.roverlabs.sdk.ui.CardListActivity;

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

    //New members
    //variable - currentTouchpoint
    //variable - Array<touchpoints> visitedTouchpoints

    /*
    setCurrentTouchpoint(touchpoint) {
        if(!this.visitedTouchpoint.contains(touchpoints) {
            add to visitedTouchpoint
        }
        this.currentTouchpoint = touchpoint;
    }

    getTouchpoint(region) {
        returns the touchpoint associated with the region passed in
    }
     */

    //Local members
    private static final String TAG = RoverVisit.class.getName();
    private RoverRegion mRegion;
    private Calendar mLastBeaconDetectionTime;
    //private List<Beacon> mBeacons;

    //TODO: Get rid of these members
    public String customer_id;
    public String uuid;
    public int major;
    
    //Constructor
    public RoverVisit(Context con) { 
        
        super(con);
        mObjectName = "visit"; 
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
    //public List<Beacon> getBeacons() { return mBeacons; }
    public RoverRegion getRegion() { return mRegion; }
    
    //Setters
    public void setCustomer(RoverCustomer customer) { mCustomer = customer; }
    public void setEnteredTime(Date enteredTime) { mEnteredTime = enteredTime; }
    public void setExitedTime(Date exitedTime) { mExitedTime = exitedTime; }
    public void setKeepAliveTime(long keepAliveTime) { mKeepAliveTime = keepAliveTime; }
    public void setLocation(RoverLocation location) { mLocation = location; }
    public void setOrganization(RoverOrganization organization) { mOrganization = organization; }
    public void setTouchpoints(List<RoverTouchpoint> touchpoints) { mTouchpoints = touchpoints; }
    public void setLastBeaconDetection(Calendar time) { mLastBeaconDetectionTime = time; }
    //public void setBeacons(List<Beacon> beacons) { mBeacons = beacons; }
    public void setRegion(RoverRegion region) { mRegion = region; }
    
    public void save() {

        //TODO: Change these hard coded values, should be grabbed from the list of beacons (mBeacons)
        this.customer_id ="1234";
        this.major = 52643;
        this.uuid = "F352DB29-6A05-4EA2-A356-9BFAC2BB3316";
        super.save();
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
        //TODO: Better place to put this call?
        sendNotification();
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
    
    public void sendNotification() {

        //TODO: Filter which touchpoint to use for notification based on server result
        RoverTouchpoint touchpoint = mTouchpoints.get(0);
        String title = touchpoint.getTitle();
        String message = touchpoint.getNotification();
        //TODO: Better system for notification IDs
        RoverNotificationEvent notificationEvent = new RoverNotificationEvent(1, title, message, CardListActivity.class);
        RoverEventBus.getInstance().post(notificationEvent);
    }
}
