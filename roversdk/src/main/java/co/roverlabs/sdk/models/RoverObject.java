package co.roverlabs.sdk.models;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import co.roverlabs.sdk.listeners.RoverObjectSaveListener;
import co.roverlabs.sdk.networks.RoverNetworkListener;
import co.roverlabs.sdk.networks.RoverNetworkManager;

/**
 * Created by SherryYang on 2015-02-20.
 */
public abstract class RoverObject {

    //JSON members
    @SerializedName("id") protected String mId;
    
    //Local members
    public static final String TAG = RoverObject.class.getSimpleName();
    protected String mObjectName;
    protected transient RoverNetworkManager mNetworkManager;
    
    //Should have instance of save successful/failed callback
    
    //Constructor
    public RoverObject(Context con) { 
        
        mNetworkManager = RoverNetworkManager.getInstance();
    }
    
    //Getters
    public String getId() { return mId; }
    public String getObjectName() { return mObjectName; }
    
    //Setters
    public void setId(String id) { mId = id; }

    
    public void save(final RoverObjectSaveListener objectSaveListener) {
        
        String method;
        final RoverObject self = this;
        
        if(mId == null) {
            method = "POST";
        }
        else {
            method = "PUT";
        }
        
        mNetworkManager.sendRequest(method, this, new RoverNetworkListener() {
            
            @Override
            public void onNetworkCallSuccess(String method, RoverObject object) {

                if(object != null) {
                    self.update(object);
                }
                Log.d(TAG, method + " call from " + self.getObjectName() + " ID " + self.getId() + " has succeeded");
                objectSaveListener.onSaveSuccess();
            }

            @Override
            public void onNetworkCallFailure(String method) {
                
                Log.d(TAG, method + " call from " + self.getObjectName() + " ID " + self.getId() + " has failed");
                objectSaveListener.onSaveFailure();
            }
        });
    }
    
    public void update(RoverObject object) {
        
        mId = object.getId();
    }
}
