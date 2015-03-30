package co.roverlabs.sdk.networks;

import co.roverlabs.sdk.events.RoverEvent;
import co.roverlabs.sdk.models.RoverObject;
import co.roverlabs.sdk.models.RoverObjectWrapper;
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

    @POST("/visits/{id}/events")
    void sendEvent(@Header("Authorization") String authToken,
                   @Path("id") String objectId,
                   @Body RoverEvent event,
                   Callback<Object> callback);
}

