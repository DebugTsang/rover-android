package co.roverlabs.sdk.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import co.roverlabs.sdk.listeners.RoverObjectSaveListener;
import co.roverlabs.sdk.networks.RoverNetworkListener;
import co.roverlabs.sdk.networks.RoverNetworkManager;

/**
 * Created by SherryYang on 2015-02-20.
 */
public abstract class RoverObject {

    //JSON members
    @SerializedName("id") protected String mId;
    @SerializedName("meta") protected Map<String, Object> mMeta;
    
    //Local members
    public static final String TAG = RoverObject.class.getSimpleName();
    protected String mObjectName;
    protected transient RoverNetworkManager mNetworkManager;
    
    //Constructor
    public RoverObject() {

        mNetworkManager = RoverNetworkManager.getInstance();
        mMeta = new HashMap<>();
    }
    
    //Getters
    public String getId() { return mId; }
    public String getObjectName() { return mObjectName; }
    public Map<String, Object> getMeta() { return mMeta; }
    
    //Setters
    public void setId(String id) { mId = id; }
    public void setMeta(Map<String, Object> meta) { mMeta = meta; }

    
    public void save(final RoverObjectSaveListener objectSaveListener) {
        
        final RoverObject self = this;
        
        mNetworkManager.sendRequest(this, new RoverNetworkListener() {
            
            @Override
            public void onNetworkCallSuccess(RoverObject object) {

                if(object != null) {
                    self.update(object);
                }
                Log.d(TAG, "Network call from " + self.getObjectName() + " ID " + self.getId() + " has succeeded");
                objectSaveListener.onSaveSuccess();
            }

            @Override
            public void onNetworkCallFailure() {
                
                Log.d(TAG, "Network call from " + self.getObjectName() + " ID " + self.getId() + " has failed");
                objectSaveListener.onSaveFailure();
            }
        });
    }
    
    public void update(RoverObject object) {
        
        mId = object.getId();
        mMeta = object.getMeta();
    }

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

        RoverObject roverObject = (RoverObject)o;

        if(mId != null ? !mId.equals(roverObject.getId()) : roverObject.getId() != null) {
            return false;
        }

        return true;
    }
}
