package co.roverlabs.sdk.managers.networks;

import co.roverlabs.sdk.models.Object;

/**
 * Created by SherryYang on 2015-02-26.
 */
public interface RoverNetworkObjectSaveListener {
    
    public void onNetworkCallSuccess(Object object);
    public void onNetworkCallFailure();
}
