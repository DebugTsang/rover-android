package co.roverlabs.sdk.models;

import android.os.Build;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import co.roverlabs.sdk.listeners.RoverObjectSaveListener;
import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverVisit extends RoverObject {

    //JSON members
    @SerializedName("keepAlive") private long mKeepAliveTime;
    @SerializedName("organization") private RoverOrganization mOrganization;
    @SerializedName("location") private RoverLocation mLocation;
    @SerializedName("customer") private RoverCustomer mCustomer;
    @SerializedName("touchpoints") private List<RoverTouchpoint> mTouchpoints;
    @SerializedName("uuid") private String mUuid;
    @SerializedName("majorNumber") private Integer mMajor;
    @SerializedName("device") private String mDevice;
    @SerializedName("operatingSystem") private String mOperatingSystem;
    @SerializedName("osVersion") private String mOsVersion;
    @SerializedName("sdkVersion") private String mSdkVersion;
    @SerializedName("timestamp") private Date mTimeStamp;

    //Local members
    public static final String TAG = RoverVisit.class.getSimpleName();
    private RoverRegion mRegion;
    private Calendar mLastBeaconDetectionTime;
    private List<RoverTouchpoint> mCurrentTouchpoints;
    private List<RoverTouchpoint> mVisitedTouchpoints;
    
    //Constructor
    public RoverVisit() {
        
        mObjectName = "visit";
        mCurrentTouchpoints = new ArrayList<>();
        mVisitedTouchpoints = new ArrayList<>();
        mDevice = Build.MODEL;
        mOperatingSystem = RoverConstants.OPERATING_SYSTEM;
        mOsVersion = Build.VERSION.RELEASE;
        mSdkVersion = RoverConstants.SDK_VERSION;
    }
    
    //Getters
    public long getKeepAliveTime() { return mKeepAliveTime; }
    public RoverOrganization getOrganization() { return mOrganization; }
    public RoverLocation getLocation() { return mLocation; }
    public RoverCustomer getCustomer() { return mCustomer; }
    public List<RoverTouchpoint> getTouchpoints() { return mTouchpoints; }
    public String getUuid() { return mUuid; }
    public Integer getMajor() { return mMajor; }
    public String getDevice() { return mDevice; }
    public String getOperatingSystem() { return mOperatingSystem; }
    public String getOsVersion() { return mOsVersion; }
    public String getSdkVersion() { return mSdkVersion; }
    public Date getTimeStamp() { return mTimeStamp; }
    public RoverRegion getRegion() { return mRegion; }
    public Calendar getLastBeaconDetectionTime() { return mLastBeaconDetectionTime; }
    public List<RoverTouchpoint> getCurrentTouchpoints() { return mCurrentTouchpoints; }
    public List<RoverTouchpoint> getVisitedTouchpoints() { return mVisitedTouchpoints; }
    
    public RoverTouchpoint getTouchpoint(RoverRegion region) {

        if(mTouchpoints != null) {
            for(RoverTouchpoint touchpoint : mTouchpoints) {
                //TODO: Add logic to handle wild cards
                if(touchpoint.getMinor() != null) {
                    if ((touchpoint.getMinor()).equals(region.getMinor())) {
                        return touchpoint;
                    }
                }
            }
        }
        return null;
    }

    //Setter
    public void setKeepAliveTime(long keepAliveTime) { mKeepAliveTime = keepAliveTime; }
    public void setOrganization(RoverOrganization organization) { mOrganization = organization; }
    public void setLocation(RoverLocation location) { mLocation = location; }
    public void setCustomer(RoverCustomer customer) { mCustomer = customer; }
    public void setTouchpoints(List<RoverTouchpoint> touchpoints) { mTouchpoints = touchpoints; }
    public void setUuid(String uuid) { mUuid = uuid; }
    public void setMajor(Integer major) { mMajor = major; }
    public void setDevice(String device) { mDevice = device; }
    public void setOperatingSystem(String operatingSystem) { mOperatingSystem = operatingSystem; }
    public void setOsVersion(String osVersion) { mOsVersion = osVersion; }
    public void setSdkVersion(String sdkVersion) { mSdkVersion = sdkVersion; }
    public void setTimeStamp(Date timeStamp) { mTimeStamp = timeStamp; }
    public void setRegion(RoverRegion region) { mRegion = region; }
    public void setLastBeaconDetectionTime(Calendar lastBeaconDetectionTime) { mLastBeaconDetectionTime = lastBeaconDetectionTime; }
    
    public void addToCurrentTouchpoints(RoverTouchpoint touchpoint) {

        mCurrentTouchpoints.add(touchpoint);
        if(!mVisitedTouchpoints.contains(touchpoint)) {
            mVisitedTouchpoints.add(touchpoint);
        }
    }

    public void removeFromCurrentTouchpoints(RoverTouchpoint touchpoint) {

        mCurrentTouchpoints.remove(touchpoint);
    }

    public boolean isInRegion(RoverRegion region) {

        return mRegion.equals(region);
    }

    public boolean isValidTouchpoint(RoverRegion region) {

        if(mTouchpoints != null) {
            for(RoverTouchpoint touchpoint : mTouchpoints) {
                //TODO: Add wildcard support
                if(touchpoint.getMinor() != null) {
                    if (region.getMinor().equals(touchpoint.getMinor())) {
                        Log.d(TAG, "Beacon minor " + region.getMinor() + " is a valid touchpoint");
                        return true;
                    }
                }
            }
        }
        Log.d(TAG, "Invalid touchpoint - does not correspond to any set up touchpoints");
        return false;
    }

    public boolean isInTouchpoint(RoverRegion region) {

        for(RoverTouchpoint touchpoint : mCurrentTouchpoints) {
            if(touchpoint.getMinor().equals(region.getMinor())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlive() {

        long keepAliveTimeInMillis = TimeUnit.MINUTES.toMillis(mKeepAliveTime);
        //Log.d(TAG, "Keep alive time is " + String.valueOf(mKeepAliveTime));
        Calendar now = Calendar.getInstance();
        long elapsedTime = now.getTimeInMillis() - mLastBeaconDetectionTime.getTimeInMillis();
        //Log.d(TAG, "Elapsed time is " + String.valueOf(TimeUnit.MILLISECONDS.toMinutes(elapsedTime)));
        return elapsedTime < keepAliveTimeInMillis;
    }

    public void save(RoverObjectSaveListener saveListener) {

        mUuid = mRegion.getUuid();
        mMajor = mRegion.getMajor();
        super.save(saveListener);
    }

    public void update(RoverObject object) {

        RoverVisit visit = (RoverVisit)object;
        super.update(visit);
        mKeepAliveTime = visit.getKeepAliveTime();
        mOrganization = visit.getOrganization();
        mLocation = visit.getLocation();
        mCustomer = visit.getCustomer();
        mTouchpoints = visit.getTouchpoints();
    }
}
