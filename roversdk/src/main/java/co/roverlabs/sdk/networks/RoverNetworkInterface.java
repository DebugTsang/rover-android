package co.roverlabs.sdk.network;

import co.roverlabs.sdk.events.RoverEvent;
import co.roverlabs.sdk.model.RoverObject;
import co.roverlabs.sdk.model.RoverObjectWrapper;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by SherryYang on 2015-02-24.
 */
public interface RoverNetworkInterface {

    @POST("/visits")
    void createVisit(@Header("Authorization") String authToken,
                     @Body RoverObject object,
                     Callback<RoverObjectWrapper> callback);

}

