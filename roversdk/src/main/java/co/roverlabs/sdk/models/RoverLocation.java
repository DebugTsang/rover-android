package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverLocation extends RoverModel {
    
    @SerializedName("title")
    private String mTitle;

    @SerializedName("address")
    private String mAddress;

    @SerializedName("city")
    private String mCity;

    @SerializedName("province")
    private String mProvince;

    @SerializedName("postalCode")
    private String mPostalCode;

    @SerializedName("majorNumber")
    private int mMajorNumber;

    @SerializedName("longitude")
    private float mLongitude;

    @SerializedName("radius")
    private int mRadius;

    public String getTitle() {
        
        return mTitle;
    }
    
    public void setTitle(String title) {
        
        mTitle = title;
    }
    
    public String getAddress() {
        
        return mAddress;
    }
    
    public void setAddress(String address) {
        
        mAddress = address;
    }
    
    public String getCity() {
        
        return mCity;
    }
    
    public void setCity(String city) {
        
        mCity = city;
    }

    public String getProvince() {
        
        return mProvince;
    }
    
    public void setProvince(String province) {
        
       mProvince = province; 
    }

    public String getPostalCode() {
        
        return mPostalCode;
    }
    
    public void setPostalCode(String postalCode) {
        
        mPostalCode = postalCode;
    }

    public int getMajorNumber() {
        
        return mMajorNumber;
    }
    
    public void setMajorNumber(int majorNumber) {
        
        mMajorNumber = majorNumber;
    }

    public float getLongitude() {
        
        return mLongitude;
    }
    
    public void setLongitude(float longitude) {
        
        mLongitude = longitude;
    }
    
    @SerializedName("latitude")
    private float mLatitude;
    
    public float getLatitude() {
        
        return mLatitude;
    }
    
    public void setLatitude(float latitude) {
        
        mLatitude = latitude;
    }

    public int getRadius() {
        
        return mRadius;
    }
    
    public void setRadius(int radius) {
        
        mRadius = radius;
    }
}
