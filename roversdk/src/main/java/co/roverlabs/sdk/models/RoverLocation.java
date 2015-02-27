package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverLocation extends RoverObject {
    
    //JSON members
    @SerializedName("title") private String mTitle;
    @SerializedName("address") private String mAddress;
    @SerializedName("city") private String mCity;
    @SerializedName("province") private String mProvince;
    @SerializedName("postalCode") private String mPostalCode;
    @SerializedName("majorNumber") private int mMajorNumber;
    @SerializedName("longitude") private float mLongitude;
    @SerializedName("latitude") private float mLatitude;
    @SerializedName("radius") private int mRadius;
    
    //Constructor
    public RoverLocation() { mObjectName = "location"; }

    //Getters
    public String getTitle() { return mTitle; }
    public String getAddress() { return mAddress; }
    public String getCity() { return mCity; }
    public String getProvince() { return mProvince; }
    public String getPostalCode() { return mPostalCode; }
    public int getMajorNumber() { return mMajorNumber; }
    public float getLongitude() { return mLongitude; }
    public float getLatitude() { return mLatitude; }
    public int getRadius() { return mRadius; }
    
    //Setters
    public void setTitle(String title) { mTitle = title; }
    public void setAddress(String address) { mAddress = address; }
    public void setCity(String city) { mCity = city; }
    public void setProvince(String province) { mProvince = province; }
    public void setPostalCode(String postalCode) { mPostalCode = postalCode; }
    public void setMajorNumber(int majorNumber) { mMajorNumber = majorNumber; }
    public void setLongitude(float longitude) { mLongitude = longitude; }
    public void setLatitude(float latitude) { mLatitude = latitude; }
    public void setRadius(int radius) { mRadius = radius; }
}
