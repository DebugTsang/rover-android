package co.roverlabs.sdk.networks;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.roverlabs.sdk.Rover;
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
    private Context mContext;
    private RoverNetworkListener mNetworkListener;
    
    public RoverNetworkManager(Context con) {
        
        mContext = con;
    }

    public void setNetworkListener(RoverNetworkListener networkListener) {

        mNetworkListener = networkListener;
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
        
        makeCall().create(Rover.getInstance(mContext).getAuthToken(), object.getObjectName(), wrapper, new Callback<RoverObjectWrapper>() {

                    @Override
                    public void success(RoverObjectWrapper roverObjectWrapper, Response response) {

                        Log.d(TAG, "Retrofit call successful");
                        mNetworkListener.onSuccess(roverObjectWrapper.get());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        
                        Log.d(TAG, "Retrofit encountered an error - " + error);
                        mNetworkListener.onFailure();
                    }
                }
        );
    }
    
    public void sendPutRequest(RoverObject object) {
        
        //TODO: PUT request
    }
}
