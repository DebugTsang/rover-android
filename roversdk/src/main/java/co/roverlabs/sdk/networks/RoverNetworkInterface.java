package co.roverlabs.sdk.networks;

import co.roverlabs.sdk.models.RoverObjectWrapper;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by SherryYang on 2015-02-24.
 */
public interface RoverNetworkInterface {
    
    @PUT("{object}s/{id}")
    void update(@Header("Authorization") String authToken,
                @Path("id") String objectId,
                @Path("object") String objectPath,
                @Body RoverObjectWrapper object,
                Callback<RoverObjectWrapper> callback);

    @POST("/{object}s")
    void create(@Header("Authorization") String authToken,
                @Path("object") String objectPath,
                @Body RoverObjectWrapper object,
                Callback<RoverObjectWrapper> callback);
}

/*    //Create
    public interface RoverPost {

        @POST("/visits")
        //@Headers("Content-Type: application/json")
        void createVisit(@Header("Authorization") String appId,
                         @Body RoverVisitWrapper visit,
                         Callback<RoverVisitWrapper> callback);
    }

    //Update
    public interface RoverPut {

        @PUT("/visits/{id}")
        //@Headers("Content-Type: application/json")
        public void updateVisit(@Header("Authorization") String appId,
                                @Path("id") String visitId,
                                @Body RoverVisitWrapper visit,
                                Callback<RoverVisitWrapper> callback);
    }*/

