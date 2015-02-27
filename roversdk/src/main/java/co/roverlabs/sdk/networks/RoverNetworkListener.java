package co.roverlabs.sdk.networks;

import co.roverlabs.sdk.models.RoverObject;

/**
 * Created by SherryYang on 2015-02-26.
 */
public interface RoverNetworkListener {
    
    public void onSuccess(RoverObject object);
    
    public void onFailure();
}
