package co.roverlabs.sdk.network;

import co.roverlabs.sdk.model.Object;
import co.roverlabs.sdk.model.ObjectWrapper;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by SherryYang on 2015-02-24.
 */
public interface RoverNetworkInterface {

    @POST("/visits")
    void createVisit(@Header("Authorization") String authToken,
                     @Body Object object,
                     Callback<ObjectWrapper> callback);

}

