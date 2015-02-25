package co.roverlabs.sdk.networks;

import org.json.JSONObject;

import co.roverlabs.sdk.models.RoverVisit;
import co.roverlabs.sdk.models.RoverVisitWrapper;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by SherryYang on 2015-02-24.
 */
public interface RoverNetworkInterface {
    
    @PUT("/visits/{id}")
    void updateVisit(@Header("Authorization") String appId,
                     @Path("id") String visitId,
                     @Body RoverVisitWrapper visit,
                     Callback<RoverVisitWrapper> callback);

    @POST("/visits")
    void createVisit(@Header("Authorization") String appId,
                     @Body RoverVisitWrapper visit,
                     Callback<RoverVisitWrapper> callback);
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

