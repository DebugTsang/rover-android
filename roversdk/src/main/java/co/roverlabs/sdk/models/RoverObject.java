package co.roverlabs.sdk.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import co.roverlabs.sdk.networks.RoverNetworkListener;
import co.roverlabs.sdk.networks.RoverNetworkManager;

/**
 * Created by SherryYang on 2015-02-20.
 */
public abstract class RoverObject {

    //JSON members
    @SerializedName("id") protected String mId;
    
    //Local members
    private static final String TAG = RoverObject.class.getName();
    protected String mObjectName;
    protected transient RoverNetworkManager mNetworkManager;
    
    //Constructor
    public RoverObject() { mNetworkManager = new RoverNetworkManager(); }
    
    //Getters
    public String getId() { return mId; }
    public String getObjectName() { return mObjectName; }
    
    //Setters
    public void setId(String id) { mId = id; }

    
    public void save() {
        
        String method;
        final RoverObject self = this;
        
        if(mId == null) {
            method = "POST";
        }
        else {
            method = "PUT";
        }
        
        mNetworkManager.setNetworkListener(new RoverNetworkListener() {
            
            @Override
            public void onSuccess(RoverObject object) {
                
                self.update(object);
                Log.d(TAG, "Network call from " + self.getObjectName() + " ID " + self.getId() + " has succeeded");
            }

            @Override
            public void onFailure() {
                
                Log.d(TAG, "Network call from " + self.getObjectName() + " ID " + self.getId() + " has failed");
            }
        });
        
        mNetworkManager.sendRequest(method, this);
    }
    
    public void update(RoverObject object) {
        
        mId = object.getId();
    }
}
