package co.roverlabs.sdk.utilities;

import android.content.Context;

import co.roverlabs.sdk.RoverConfigs;
import co.roverlabs.sdk.managers.RoverNotificationManager;
import co.roverlabs.sdk.networks.RoverNetworkManager;
import co.roverlabs.sdk.ui.ImageLoader;
import co.roverlabs.sdk.ui.PicassoImageLoader;

/**
 * Created by arsent on 15-05-22.
 */
public class FactoryUtils {
    private static ImageLoader mImageLoader;
    private static RoverNotificationManager sNotifManager;
    private static RoverNetworkManager sNetworkManager;
    private static RoverConfigs sConfigs;

    private static boolean sIsInitialized;

    public static void initManagers(Context context){
        sConfigs = (RoverConfigs) SharedPrefsUtils.readObjectFromSharedPrefs(context, RoverConfigs.class, null);

        //init notification manager
        sNotifManager = RoverNotificationManager.getInstance(context);
        sNotifManager.setNotificationIconId(sConfigs.getNotificationIconId());
        sNotifManager.setHeadIconId(sConfigs.getRoverHeadIconId());


        sNetworkManager = RoverNetworkManager.getInstance();
        sNetworkManager.setAuthToken(sConfigs.getAuthToken());

        sIsInitialized = true;
    }

    public static ImageLoader getDefaultImageLoader(Context context) {
        if (mImageLoader == null)
            mImageLoader = new PicassoImageLoader(context);
        return mImageLoader;
    }

    public static RoverNotificationManager getNotificationManager(){
        return sNotifManager;
    }

    public static RoverNetworkManager getNetworkManager(){
        return sNetworkManager;
    }

    public static RoverConfigs getConfig(){
        return sConfigs;
    }
}
