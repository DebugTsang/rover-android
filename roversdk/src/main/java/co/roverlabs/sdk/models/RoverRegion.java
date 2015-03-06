package co.roverlabs.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SherryYang on 2015-03-05.
 */
public class RoverRegion implements Parcelable {
    
    private String mId;
    private String mUuid;
    private Integer mMajor;
    private Integer mMinor;
    
    public RoverRegion(String id, String uuid, Integer major, Integer minor) {
        
        mId = id;
        mUuid = uuid;
        mMajor = major;
        mMinor = minor;
    }
    
    public String getId() { return mId; }
    public String getUuid() { return mUuid; }
    public Integer getMajor() { return mMajor; }
    public Integer getMinor() { return mMinor; }
    
    public void setId(String id) { mId = id; }
    public void setUuid(String uuid) { mUuid = uuid; }
    public void setMajor(Integer major) { mMajor = major; }
    public void setMinor(Integer minor) { mMinor = minor; }

    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null) {
            return false;
        }
        if(getClass() != o.getClass()) {
            return false;
        }
        
        RoverRegion region = (RoverRegion)o;
        
        if(mMajor != null ? !mMajor.equals(region.getMajor()) : region.getMajor() != null) {
            return false;
        }
        if(mMinor != null ? !mMinor.equals(region.getMinor()) : region.getMinor() != null) {
            return false;
        }
        if(mUuid != null ? !mUuid.equals(region.getUuid()) : region.getUuid() != null) {
            return false;
        }
        
        return true;
    }

    @Override
    public int describeContents() {
        
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mId);
        dest.writeString(mUuid);
        dest.writeInt(mMajor == null ? -1 : mMajor.intValue());
        dest.writeInt(mMinor == null ? -1 : mMinor.intValue());
    }

    @Override
    public String toString() {
        
        return "{REGION:{" +
                "ID:" + mId + "," +
                "UUID:" + mUuid + "," +
                "Major:" + mMajor + "," +
                "Minor:" + mMinor + "}}";
    }
}
