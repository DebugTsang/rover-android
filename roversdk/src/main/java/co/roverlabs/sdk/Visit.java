package co.roverlabs.sdk;

import com.estimote.sdk.Region;

import java.util.Calendar;

/**
 * Created by SherryYang on 2015-01-21.
 */
public class Visit {
    
    private static final String TAG = Visit.class.getName();
    private Region mRegion;
    private long mAliveTimeInMillis;
    private Calendar mEnteredTime;
    private Calendar mExitedTime;
    private Calendar mLastBeaconDetectionTime;
    
    public Visit(Region region) {
        
        mRegion = region;
        //Temporary (5 min)
        mAliveTimeInMillis = 300000;
    }
    
    //If the region instance being held is the same as the current region or not
    public boolean isInRegion(Region region) {
        
        return mRegion.equals(region);
    }
    
    public boolean isAlive() {
  
        Calendar now = Calendar.getInstance();
        long elapsedTime = now.getTimeInMillis() - mLastBeaconDetectionTime.getTimeInMillis();
        return elapsedTime < mAliveTimeInMillis;
    }
    
    public void setLastBeaconDetection(Calendar time) {
        
        mLastBeaconDetectionTime = time;
    }
    
    public void setEnteredTime(Calendar time) {
        
        mEnteredTime = time;
    }
    
    public void setExitedTime(Calendar time) {
        
        mExitedTime = time;
    }
    
    public Calendar getEnteredTime() {
        
        return mEnteredTime;
    }
    
    public Calendar getExitedTime() {
        
        return mExitedTime;
    }
    
    public Region getRegion() {
        
        return mRegion;
    }
}
