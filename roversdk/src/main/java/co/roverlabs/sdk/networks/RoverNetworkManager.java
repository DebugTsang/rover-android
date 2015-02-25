package co.roverlabs.sdk.networks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.roverlabs.sdk.utilities.RoverConstants;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by SherryYang on 2015-02-24.
 */
public class RoverNetworkManager {
    
    public static final String TAG = RoverNetworkManager.class.getName();
    
    public RoverNetworkManager() {}
    
    public RoverNetworkInterface makeCall() {
        
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();
        
        RoverNetworkInterface call = new RestAdapter.Builder()
                .setEndpoint(RoverConstants.ROVER_URL)
                .setConverter(new GsonConverter(gson))
                .build()
                .create(RoverNetworkInterface.class);
        
        return call;
        
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
