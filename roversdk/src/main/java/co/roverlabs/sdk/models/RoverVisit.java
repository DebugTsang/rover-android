package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverVisit extends RoverModel {
    
    @SerializedName("customer")
    private RoverCustomer mCustomer;
    
    @SerializedName("enteredAt")
    private Date mEnteredTime;
    
    @SerializedName("keepAlive")
    private long mKeepAliveTime;
    
    @SerializedName("location")
    private RoverLocation mLocation;
    
    @SerializedName("organization")
    private RoverOrganization mOrganization;
    
    @SerializedName("touchpoints")
    private List<RoverTouchPoint> mTouchPoints;
    
    public RoverCustomer getCustomer() {
        
        return mCustomer;
    }
    
    public void setCustomer(RoverCustomer customer) {
        mCustomer = customer;
    }
    
    public Date getEnteredTime() {
        
        return mEnteredTime;
    }
    
    public void setEnteredTime(Date enteredTime) {
        
        mEnteredTime = enteredTime;
    }
    
    public long getKeepAliveTime() {
        
        return mKeepAliveTime;
    }
    
    public void setKeepAliveTime(long keepAliveTime) {
        
        mKeepAliveTime = keepAliveTime;
    }
    
    public RoverLocation getLocation() {
        
        return mLocation;
    }
    
    public void setLocation(RoverLocation location) {
        
        mLocation = location;
    }
    
    public RoverOrganization getOrganization() {
        
        return mOrganization;
    }
    
    public void setOrganization(RoverOrganization organization) {
        
        mOrganization = organization;
    }
    
    public List<RoverTouchPoint> getTouchPoints() {
        
        return mTouchPoints;
    }
    
    public void setTouchPoints(List<RoverTouchPoint> touchPoints) {
        
        mTouchPoints = touchPoints;
    }
}
