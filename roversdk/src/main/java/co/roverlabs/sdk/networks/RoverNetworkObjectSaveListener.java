package co.roverlabs.sdk.network;

import co.roverlabs.sdk.model.RoverObject;

/**
 * Created by SherryYang on 2015-02-26.
 */
public interface RoverNetworkObjectSaveListener {
    
    public void onNetworkCallSuccess(RoverObject object);
    public void onNetworkCallFailure();
}
