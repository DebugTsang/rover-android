package co.roverlabs.sdk;

import android.util.Log;

/**
 * Created by SherryYang on 2015-04-08.
 */
public class RoverConfigs {

    public static final String TAG = RoverConfigs.class.getSimpleName();
    private String mUuid;
    private String mAppId;
    private int mNotificationIconId;
    private int mRoverHeadIconId;
    private String mLaunchActivityName;
    private boolean mSandBoxMode;

    public RoverConfigs() { }

    public String getAppId() { return mAppId; }
    public String getAuthToken() { return "Bearer " + getAppId(); }
    public String getUuid() { return mUuid; }
    public String getLaunchActivityName() { return mLaunchActivityName; }
    public boolean getSandBoxMode() { return mSandBoxMode; }

    public int getNotificationIconId() {

        if(mNotificationIconId == 0) {
            mNotificationIconId = R.drawable.rover_icon;
        }
        return mNotificationIconId;
    }
    public int getRoverHeadIconId(){
        return mRoverHeadIconId;
    }

    public void setAppId(String appId) { mAppId = appId; }
    public void setUuid(String uuid) { mUuid = uuid; }
    public void setNotificationIconId(int resourceId) { mNotificationIconId = resourceId; }
    public void setRoverHeadIconId(int resourceId) { mRoverHeadIconId = resourceId; }

    public void setLaunchActivityName(String launchActivityName) { mLaunchActivityName = launchActivityName; }
    public void setSandBoxMode(boolean sandBoxMode) { mSandBoxMode = sandBoxMode; }

    public boolean isComplete() {

        String missing = "Missing configuration: ";

        if(mUuid == null) {
            missing += "UUID";
        }
        if(mAppId == null) {
            missing += ", App ID";
        }
        if(mNotificationIconId == 0) {
            missing += ", Notification Icon ID";
        }
        if(mLaunchActivityName == null) {
            missing += ", Launch Activity Name";
        }

        if(mUuid != null && mAppId != null && mNotificationIconId != 0 && mLaunchActivityName != null) {
            return true;
        }
        else {
            Log.d(TAG, missing);
            return false;
        }
    }
}
