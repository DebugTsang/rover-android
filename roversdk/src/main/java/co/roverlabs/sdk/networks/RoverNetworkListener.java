package co.roverlabs.sdk.networks;

import co.roverlabs.sdk.models.RoverObject;

/**
 * Created by SherryYang on 2015-02-26.
 */
public class RoverNetworkListener {
    
    public interface PostListener {

        public void onSuccess(RoverObject object);
        public void onFailure();
    }
    
    public interface PutListener {
        
        public void onSuccess();
        public void onFailure();
    }
   
}
