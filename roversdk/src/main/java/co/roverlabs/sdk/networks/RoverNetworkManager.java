package co.roverlabs.sdk.networks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.roverlabs.sdk.events.RoverEvent;
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
    
    public static final String TAG = RoverNetworkManager.class.getSimpleName();
    private static RoverNetworkManager sNetworkManagerInstance;
    private String mAuthToken;

    private RoverNetworkManager() { }

    public static RoverNetworkManager getInstance() {

        if(sNetworkManagerInstance == null) {
            sNetworkManagerInstance = new RoverNetworkManager();
        }
        return sNetworkManagerInstance;
    }
    
    public void setAuthToken(String token) { mAuthToken = token; }
    
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
    
    public void sendObjectSaveRequest(final RoverObject object, final RoverNetworkObjectSaveListener networkListener) {

        Callback<RoverObjectWrapper> networkCallback = new Callback<RoverObjectWrapper>() {
            
            @Override
            public void success(RoverObjectWrapper roverObjectWrapper, Response response) {
                
                networkListener.onNetworkCallSuccess(roverObjectWrapper.get());
            }

            @Override
            public void failure(RetrofitError error) {

                networkListener.onNetworkCallFailure();
            }
        };
        
        makeCall().createVisit(mAuthToken, object, networkCallback);
    }

    public void sendEventSaveRequest(final RoverEvent event, final RoverNetworkEventSaveListener networkListener) {

        Callback<Object> networkCallback = new Callback<Object>() {

            @Override
            public void success(Object o, Response response) {

                networkListener.onNetworkCallSuccess(response);
            }

            @Override
            public void failure(RetrofitError error) {

                networkListener.onNetworkCallFailure();
            }
        };

        makeCall().sendEvent(mAuthToken, event.getId(), event, networkCallback);
    }
}
