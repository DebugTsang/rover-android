package co.roverlabs.sdk;


import co.roverlabs.sdk.model.Object;
import co.roverlabs.sdk.network.*;
import co.roverlabs.sdk.network.listeners.*;

/**
 * Created by ars on 15-06-02.
 */
public class NetworkHelper {
    public static void save(final RoverObjectSaveListener objectSaveListener, Object object) {
        // instead of this, broadcast event: RoverVisitNeedsValidation -> THIS ()RoverVisit)

        RoverNetworkManager.getInstance().sendObjectSaveRequest(object, new RoverNetworkObjectSaveListener() {

            @Override
            public void onNetworkCallSuccess(Object object) {
                objectSaveListener.onSaveSuccess();
            }

            @Override
            public void onNetworkCallFailure() {
                objectSaveListener.onSaveFailure();
            }
        });
    }
}
