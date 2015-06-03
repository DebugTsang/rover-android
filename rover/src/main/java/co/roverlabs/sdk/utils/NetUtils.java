package co.roverlabs.sdk.utils;

import co.roverlabs.sdk.managers.networks.RoverNetworkManager;
import co.roverlabs.sdk.managers.networks.RoverNetworkObjectSaveListener;

/**
 * Created by ars on 15-06-02.
 */
public class NetUtils {
    public void save(final RoverNetworkObjectSaveListener objectSaveListener) {
        // instead of this, broadcast event: RoverVisitNeedsValidation -> THIS ()RoverVisit)
        final Object self = this;

        RoverNetworkManager.getInstance().sendObjectSaveRequest(this, new RoverNetworkObjectSaveListener() {

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
