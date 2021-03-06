package co.roverlabs.sdk.models;

import android.os.Build;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import co.roverlabs.sdk.events.RoverCardDeliveredEvent;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.listeners.RoverObjectSaveListener;
import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverVisit extends RoverObject {

    //JSON members
    @SerializedName("keepAlive") private long mKeepAliveTimeInMinutes;
    @SerializedName("organization") private RoverOrganization mOrganization;
    @SerializedName("location") private RoverLocation mLocation;
    @SerializedName("customer") private RoverCustomer mCustomer;
    @SerializedName("touchpoints") private List<RoverTouchPoint> mTouchpoints;
    @SerializedName("uuid") private String mUuid;
    @SerializedName("majorNumber") private Integer mMajor;
    @SerializedName("device") private String mDevice;
    @SerializedName("operatingSystem") private String mOperatingSystem;
    @SerializedName("osVersion") private String mOsVersion;
    @SerializedName("sdkVersion") private String mSdkVersion;
    @SerializedName("timestamp") private Date mTimeStamp;
    @SerializedName("simulate") private boolean mIsSimulation;

    //Local members
    public static final String TAG = RoverVisit.class.getSimpleName();
    private RoverRegion mRegion;
    private Calendar mLastBeaconDetectionTime;
    private List<RoverTouchPoint> mCurrentTouchpoints;
    private List<RoverTouchPoint> mVisitedTouchpoints;
    
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
    public long getKeepAliveTime() { return mKeepAliveTimeInMinutes; }
    public RoverOrganization getOrganization() { return mOrganization; }
    public RoverLocation getLocation() { return mLocation; }
    public RoverCustomer getCustomer() { return mCustomer; }
    public List<RoverTouchPoint> getTouchpoints() { return mTouchpoints; }
    public String getUuid() { return mUuid; }
    public Integer getMajor() { return mMajor; }
    public String getDevice() { return mDevice; }
    public String getOperatingSystem() { return mOperatingSystem; }
    public String getOsVersion() { return mOsVersion; }
    public String getSdkVersion() { return mSdkVersion; }
    public Date getTimeStamp() { return mTimeStamp; }
    public RoverRegion getRegion() { return mRegion; }
    public Calendar getLastBeaconDetectionTime() { return mLastBeaconDetectionTime; }
    public List<RoverTouchPoint> getCurrentTouchpoints() { return mCurrentTouchpoints; }
    public List<RoverTouchPoint> getVisitedTouchpoints() { return mVisitedTouchpoints; }
    public boolean getIsSimulation() { return mIsSimulation; }

    public List<RoverTouchPoint> getWildCardTouchpoints() {

        List<RoverTouchPoint> wildCardTouchpoints = new ArrayList<>();
        if(mTouchpoints != null) {
            for(RoverTouchPoint touchpoint : mTouchpoints) {
                if(touchpoint.getTrigger().equals(RoverConstants.WILD_CARD_TOUCHPOINT_TRIGGER)) {
                    wildCardTouchpoints.add(touchpoint);
                }
            }
        }
        return wildCardTouchpoints;
    }
    
    public RoverTouchPoint getTouchpoint(RoverRegion region) {

        if(mTouchpoints != null) {
            for(RoverTouchPoint touchpoint : mTouchpoints) {
                if(touchpoint.getMinor() != null) {
                    if ((touchpoint.getMinor()).equals(region.getMinor())) {
                        Log.d(TAG, "Minor " + region.getMinor() + " corresponds to a valid touchpoint");
                        return touchpoint;
                    }
                }
            }
        }
        Log.d(TAG, "Minor " + region.getMinor() + " does not correspond to a valid touchpoint");
        return null;
    }

    public List<RoverCard> getAccumulatedCards() {

        List<RoverCard> accumulatedCards = new ArrayList<>();
        if(mVisitedTouchpoints != null) {
            for(RoverTouchPoint touchpoint : mVisitedTouchpoints) {
                if(touchpoint.getCards() != null) {
                    for(RoverCard card : touchpoint.getCards()) {
                        if(!card.hasBeenDismissed()) {
                            accumulatedCards.add(card);
                        }
                    }
                }
            }
        }
        return accumulatedCards;
    }

    //Setter
    public void setKeepAliveTime(long keepAliveTime) { mKeepAliveTimeInMinutes = keepAliveTime; }
    public void setOrganization(RoverOrganization organization) { mOrganization = organization; }
    public void setLocation(RoverLocation location) { mLocation = location; }
    public void setCustomer(RoverCustomer customer) { mCustomer = customer; }
    public void setTouchpoints(List<RoverTouchPoint> touchpoints) { mTouchpoints = touchpoints; }
    public void setUuid(String uuid) { mUuid = uuid; }
    public void setMajor(Integer major) { mMajor = major; }
    public void setDevice(String device) { mDevice = device; }
    public void setOperatingSystem(String operatingSystem) { mOperatingSystem = operatingSystem; }
    public void setOsVersion(String osVersion) { mOsVersion = osVersion; }
    public void setSdkVersion(String sdkVersion) { mSdkVersion = sdkVersion; }
    public void setTimeStamp(Date timeStamp) { mTimeStamp = timeStamp; }
    public void setRegion(RoverRegion region) { mRegion = region; }
    public void setLastBeaconDetectionTime(Calendar lastBeaconDetectionTime) { mLastBeaconDetectionTime = lastBeaconDetectionTime; }
    public void setSimulation(boolean isSimulation) { mIsSimulation = isSimulation; }
    
    public void addToCurrentTouchpoints(RoverTouchPoint touchpoint) {

        mCurrentTouchpoints.add(touchpoint);
        if(!mVisitedTouchpoints.contains(touchpoint)) {
            mVisitedTouchpoints.add(touchpoint);
            if(touchpoint.getCards() != null) {
                List<RoverCard> cards = touchpoint.getCards();
                for(int i = cards.size() - 1; i >= 0; i--) {
                    RoverEventBus.getInstance().post(new RoverCardDeliveredEvent(mId, cards.get(i)));
                }
            }
        }
    }

    public void removeFromCurrentTouchpoints(RoverTouchPoint touchpoint) {

        mCurrentTouchpoints.remove(touchpoint);
    }

    public boolean isInRegion(RoverRegion region) {

        return mRegion.equals(region);
    }

    public boolean isInSubRegion(RoverRegion region) {

        for(RoverTouchPoint touchpoint : mCurrentTouchpoints) {
            if(touchpoint.getMinor() != null) {
                if(touchpoint.getMinor().equals(region.getMinor())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAlive() {

        long keepAliveTimeInMillis = TimeUnit.MINUTES.toMillis(mKeepAliveTimeInMinutes);
        Calendar now = Calendar.getInstance();
        long elapsedTime = now.getTimeInMillis() - mLastBeaconDetectionTime.getTimeInMillis();
        return elapsedTime < keepAliveTimeInMillis;
    }

    public boolean currentlyContainsWildCardTouchpoints() {

        for(RoverTouchPoint touchpoint : mCurrentTouchpoints) {
            if(touchpoint.getTrigger().equals(RoverConstants.WILD_CARD_TOUCHPOINT_TRIGGER)) {
                return true;
            }
        }
        return false;
    }

    public boolean currentlyContainsTouchpoints() {

        return mCurrentTouchpoints.isEmpty();
    }

    public void save(RoverObjectSaveListener saveListener) {

        mUuid = mRegion.getUuid();
        mMajor = mRegion.getMajor();
        super.save(saveListener);
    }

    public void update(RoverObject object) {

        RoverVisit visit = (RoverVisit)object;
        super.update(visit);
        //TODO: Change keep alive time back to regular after testing is done
        mKeepAliveTimeInMinutes = visit.getKeepAliveTime();
        //mKeepAliveTimeInMinutes = 0;
        mOrganization = visit.getOrganization();
        mLocation = visit.getLocation();
        mCustomer = visit.getCustomer();
        mTouchpoints = visit.getTouchpoints();
    }
}
