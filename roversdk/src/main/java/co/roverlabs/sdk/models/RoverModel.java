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
    protected transient String mModelName;
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
        
        if(mId == null) {
            method = "POST";
        }
        else {
            method = "PUT";
        }
        
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
        
        Log.d(TAG, "using the method " + method);
        mNetworkManager.sendRequest(method, this);
    }
}
