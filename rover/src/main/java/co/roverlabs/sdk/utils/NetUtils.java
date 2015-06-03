package co.roverlabs.sdk.utils;

import co.roverlabs.sdk.managers.networks.RoverNetworkManager;
import co.roverlabs.sdk.managers.networks.RoverNetworkObjectSaveListener;
import co.roverlabs.sdk.managers.networks.listeners.RoverObjectSaveListener;
import co.roverlabs.sdk.models.Object;

/**
 * Created by ars on 15-06-02.
 */
public class NetUtils {
    public void save(final RoverObjectSaveListener objectSaveListener, Object object) {
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
