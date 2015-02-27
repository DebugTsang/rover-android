package co.roverlabs.sdk.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import co.roverlabs.sdk.networks.RoverNetworkListener;
import co.roverlabs.sdk.networks.RoverNetworkManager;

/**
 * Created by SherryYang on 2015-02-20.
 */
public abstract class RoverModel {

    //JSON members
    @SerializedName("id") protected String mId;
    
    //Local members
    private static final String TAG = RoverModel.class.getName();
    protected String mModelName;
    protected transient RoverNetworkManager mNetworkManager;
    
    //Constructor
    public RoverModel() { mNetworkManager = new RoverNetworkManager(); }
    
    //Getters
    public String getId() { return mId; }
    public String getModelName() { return mModelName; }
    
    //Setters
    public void setId(String id) { mId = id; }

    
    public void save() {
        
        String method;
        final RoverModel self = this;
        
        if(mId == null) {
            method = "POST";
        }
        else {
            method = "PUT";
        }
        
        mNetworkManager.setNetworkListener(new RoverNetworkListener() {
            
            @Override
            public void onSuccess(RoverModel object) {
                
                self.update(object);
                Log.d(TAG, "Network call from " + self.getModelName() + " ID " + self.getId() + " has succeeded");
            }

            @Override
            public void onFailure() {
                
                Log.d(TAG, "Network call from " + self.getModelName() + " ID " + self.getId() + " has failed");
            }
        });
        
        mNetworkManager.sendRequest(method, this);
    }
    
    public void update(RoverModel object) {
        
        mId = object.getId();
    }
}
