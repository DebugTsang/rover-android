package co.roverlabs.sdk.events;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import co.roverlabs.sdk.listeners.RoverEventSaveListener;
import co.roverlabs.sdk.networks.RoverNetworkEventSaveListener;
import co.roverlabs.sdk.networks.RoverNetworkManager;
import retrofit.client.Response;

/**
 * Created by SherryYang on 2015-03-27.
 */
public class RoverEvent {

    //JSON members
    @SerializedName("object") protected String mObjectName;
    @SerializedName("action") protected String mAction;
    @SerializedName("timestamp") protected Date mTimeStamp;

    //Local members
    public static final String TAG = RoverEvent.class.getSimpleName();
    protected String mId;
    protected transient RoverNetworkManager mNetworkManager;

    //Constructor
    RoverEvent() { mNetworkManager = RoverNetworkManager.getInstance(); }

    //Getters
    public String getId() { return mId; }
    public String getObjectName() { return mObjectName; }
    public String getAction() { return mAction; }
    public Date getTimeStamp() { return mTimeStamp; }

    //Setters
    public void setId(String id) { mId = id; }
    public void setObjectName(String objectName) { mObjectName = objectName; }
    public void setAction(String action) { mAction = action; }
    public void setTimeStamp(Date timeStamp) { mTimeStamp = timeStamp; }

    public void send(final RoverEventSaveListener eventSaveListener) {

        final RoverEvent self = this;

        mNetworkManager.sendEventSaveRequest(this, new RoverNetworkEventSaveListener() {

            @Override
            public void onNetworkCallSuccess(Response response) {

                Log.d(TAG, "Network call for " + self.getAction() + " " + self.getObjectName() + " event has succeeded");
                eventSaveListener.onSaveSuccess();
            }

            @Override
            public void onNetworkCallFailure() {

                Log.d(TAG, "Network call for " + self.getAction() + " " + self.getObjectName() + " event has failed");
                eventSaveListener.onSaveFailure();
            }
        });
    }
}
