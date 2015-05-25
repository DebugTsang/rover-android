package co.roverlabs.sdk.networks;

import retrofit.client.Response;

/**
 * Created by SherryYang on 2015-03-30.
 */
public interface RoverNetworkEventSaveListener {

    public void onNetworkCallSuccess(Response response);
    public void onNetworkCallFailure();
}
