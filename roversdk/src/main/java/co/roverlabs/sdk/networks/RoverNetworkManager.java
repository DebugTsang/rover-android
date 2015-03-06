package co.roverlabs.sdk.networks;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.roverlabs.sdk.models.RoverObject;
import co.roverlabs.sdk.models.RoverObjectWrapper;
import co.roverlabs.sdk.utilities.RoverConstants;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by SherryYang on 2015-02-24.
 */
public class RoverNetworkManager {
    
    public static final String TAG = RoverNetworkManager.class.getName();
    private static RoverNetworkManager sNetworkManagerInstance;
    private String mAuthToken;
    private RoverNetworkListener.PostListener mPostListener;
    private RoverNetworkListener.PutListener mPutListener;


    private RoverNetworkManager() { }

    public static RoverNetworkManager getInstance() {

        if(sNetworkManagerInstance == null) {
            sNetworkManagerInstance = new RoverNetworkManager();
        }
        return sNetworkManagerInstance;
    }
    
    public void setAuthToken(String token) { mAuthToken = token; }
    
    public void setPostListener(RoverNetworkListener.PostListener postListener) {

        mPostListener = postListener;
    }
    
    public void setPutListener(RoverNetworkListener.PutListener putListener) {
        
        mPutListener = putListener;
    }
    
    public RoverNetworkInterface makeCall() {
        
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();
        
        RoverNetworkInterface call = new RestAdapter.Builder()
                .setEndpoint(RoverConstants.ROVER_URL)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(RoverNetworkInterface.class);
        
        return call;
    }
    
    public void sendRequest(String method, RoverObject object) {
        
        if(method.equals("POST")) {
            sendPostRequest(object);
        }
        else if(method.equals("PUT")) {
            sendPutRequest(object);
        }
    }
    
    public void sendPostRequest(RoverObject object) {
        
        RoverObjectWrapper wrapper = new RoverObjectWrapper();
        wrapper.set(object);
        
        makeCall().create(mAuthToken, object.getObjectName(), wrapper, new Callback<RoverObjectWrapper>() {

                    @Override
                    public void success(RoverObjectWrapper roverObjectWrapper, Response response) {

                        Log.d(TAG, "Retrofit POST call successful");
                        mPostListener.onSuccess(roverObjectWrapper.get());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        
                        Log.d(TAG, "Retrofit encountered an error during POST - " + error);
                        mPostListener.onFailure();
                    }
                }
        );
    }
    
    public void sendPutRequest(RoverObject object) {
        
        //TODO: Test PUT request
        RoverObjectWrapper wrapper = new RoverObjectWrapper();
        wrapper.set(object);
        
        makeCall().update(mAuthToken, object.getId(), object.getObjectName(), wrapper, new Callback() {

            @Override
            public void success(Object o, Response response) {

                Log.d(TAG, "Retrofit PUT call successful");
                mPutListener.onSuccess();
            }

            @Override
            public void failure(RetrofitError error) {

                Log.d(TAG, "Retrofit encountered an error during PUT - " + error);
                mPutListener.onFailure();
            }
        });
    }
}
