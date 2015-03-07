package co.roverlabs.sdk.models;

import android.content.Context;
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

    
    public void save(/* successCallback, failureCallback */) {
        
        String method;
        final RoverObject self = this;
        
        if(mId == null) {
            method = "POST";
            //TODO: Should network callback be here or in RoverVisitManager?
            mNetworkManager.setPostListener(new RoverNetworkListener.PostListener() {

                @Override
                public void onSuccess(RoverObject object) {

                    self.update(object);
                    Log.d(TAG, "POST call from " + self.getObjectName() + " ID " + self.getId() + " has succeeded");
                    /*
                    * if successCallback
                    *  successCallback();
                    *  *  * 
                    * * * */
                }

                @Override
                public void onFailure() {

                    Log.d(TAG, "POST call from " + self.getObjectName() + " ID " + self.getId() + " has failed");
                    
                                        /*
                    * if failureCallback
                    *  failureCallback();
                    *  *  * 
                    * * * */
                }
            });
        }
        else {
            method = "PUT";
            mNetworkManager.setPutListener(new RoverNetworkListener.PutListener() {

                @Override
                public void onSuccess() {

                    Log.d(TAG, "PUT call from " + self.getObjectName() + " ID " + self.getId() + " has succeeded");
                }

                @Override
                public void onFailure() {

                    Log.d(TAG, "PUT call from " + self.getObjectName() + " ID " + self.getId() + " has failed");
                }
            });
        }
        
        mNetworkManager.sendRequest(method, this);
    }
    
    public void update(RoverObject object) {
        
        mId = object.getId();
    }
}
