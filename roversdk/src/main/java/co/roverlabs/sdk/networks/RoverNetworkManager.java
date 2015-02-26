package co.roverlabs.sdk.networks;

import android.util.Log;

import com.estimote.sdk.Region;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.roverlabs.sdk.managers.RoverNotificationManager;
import co.roverlabs.sdk.models.RoverModel;
import co.roverlabs.sdk.models.RoverObjectWrapper;
import co.roverlabs.sdk.models.RoverVisit;
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
    private RoverNetworkListener mNetworkListener;
    private String mAuthToken;

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
    
    public void sendRequest(String method, RoverModel object) {
        
        if(method.equals("POST")) {
            sendPostRequest(object);
        }
        else if(method.equals("PUT")) {
            sendPutRequest(object);
        }
    }
    
    public void sendPostRequest(RoverModel object) {
        
        RoverObjectWrapper wrapper = new RoverObjectWrapper();

        //if(object.getModelName().equals("visit")) {
            
            //Region region = new Region("ID", "B9407F30-F5F8-466E-AFF9-25556B57FE6D", null, null);
            //RoverVisit visit = new RoverVisit(region);
            //visit.customer_id ="1234";
            //visit.major = 52643;
            //visit.uuid = "F352DB29-6A05-4EA2-A356-9BFAC2BB3316";
            
        wrapper.set(object);


        makeCall().create("Bearer ff259b8f81ba2a2fd227445e2b3dbaca3e9552ff1663fa3f46e89a284bc9aaa0", object.getModelName(), wrapper, new Callback<RoverObjectWrapper>() {

                    @Override
                    public void success(RoverObjectWrapper roverObjectWrapper, Response response) {
                        Log.d(TAG, roverObjectWrapper.getVisit().toString());
                        Log.d(TAG, "response is good");
                        mNetworkListener.onSuccess();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, error.toString());
                        Log.d(TAG, "response is bad and you should feel bad");
                        mNetworkListener.onFailure();
                    }
                }
        );
        //}
    }
    
    public void sendPutRequest(RoverModel object) {
        
        //Callback for retrofit
    }
    
//    public RoverNetworkInterface.RoverPut makePutCall() {
//
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
//                .create();
//        
//        RoverNetworkInterface.RoverPut putCall = new RestAdapter.Builder()
//                .setEndpoint(RoverConstants.ROVER_URL)
//                .setConverter(new GsonConverter(gson))
//                .build()
//                .create(RoverNetworkInterface.RoverPut.class);
//        
//        return putCall;
//    }
//
//    public RoverNetworkInterface.RoverPost makePostCall() {
//
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
//                .create();
//
//        RoverNetworkInterface.RoverPost postCall = new RestAdapter.Builder()
//                .setEndpoint(RoverConstants.ROVER_URL)
//                .setConverter(new GsonConverter(gson))
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .build()
//                .create(RoverNetworkInterface.RoverPost.class);
//
//        return postCall;
//    }
}
