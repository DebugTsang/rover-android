package co.roverlabs.sdk.events;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import co.roverlabs.sdk.listeners.RoverEventSaveListener;
import co.roverlabs.sdk.network.RoverNetworkEventSaveListener;
import co.roverlabs.sdk.network.RoverNetworkManager;
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
    protected transient String mId;
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

        mNetworkManager.sendEventSaveRequest(this, new RoverNetworkEventSaveListener() {

            @Override
            public void onNetworkCallSuccess(Response response) {

                eventSaveListener.onSaveSuccess();
            }

            @Override
            public void onNetworkCallFailure() {

                eventSaveListener.onSaveFailure();
            }
        });
    }
}
