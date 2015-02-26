package co.roverlabs.sdk.models;

import android.content.Context;
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
    protected RoverNetworkManager mNetworkManager;
    
    //Constructor
    public RoverModel() { mNetworkManager = new RoverNetworkManager(); }
    
    //Getters
    public String getId() { return mId; }
    public String getModelName() { return mModelName; }
    
    //Setters
    public void setId(String id) { mId = id; }
    
    public void save() {
        
        
        mNetworkManager.setNetworkListener(new RoverNetworkListener() {
            
            @Override
            public void onSuccess() {
                Log.d(TAG, "network call successful");
            }

            @Override
            public void onFailure() {
                Log.d(TAG, "network call failed");
            }
        });
        
        mNetworkManager.sendRequest("POST", mModelName, this);
    }
}
