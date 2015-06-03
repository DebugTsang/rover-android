package co.roverlabs.sdk;

import android.content.Context;
import android.os.Handler;

import co.roverlabs.sdk.core.EstimoteManager;
import co.roverlabs.sdk.core.ILocationManager;
import co.roverlabs.sdk.ui.ImageLoader;
import co.roverlabs.sdk.ui.PicassoImageLoader;
import co.roverlabs.sdk.util.SharedPrefUtils;

/**
 * Created by ars on 15-06-02.
 */
public class Factory {

    private static ILocationManager sLocationManagerInstance;
    public static ILocationManager getDefaultLocationManager(Context context, Handler handler){
        if (sLocationManagerInstance == null){
            sLocationManagerInstance = new EstimoteManager(context, handler);
        }

        return sLocationManagerInstance;
    }

    private static UiHelper sUiHelper;
    public static UiHelper getUiHelper(){
        return sUiHelper;
    }
    public static void setUiHelper(UiHelper uiHelper){
        sUiHelper = uiHelper;
    }

    private static CoreHelper sCoreHelper;
    public static CoreHelper getCoreHelper(){
        return sCoreHelper;
    }
    public static void setCoreHelper(CoreHelper uiHelper){
        sCoreHelper = uiHelper;
    }


    private static ImageLoader sImageLoader;
    public static ImageLoader getDefaultImageLoader(Context context) {
        if (sImageLoader == null)
            sImageLoader = new PicassoImageLoader(context);
        return sImageLoader;
    }

    private static Config sConfig;
    public static Config getConfig() {
        return sConfig;
    }

    public static void setConfig(Context context, Config config){
        sConfig = config;
        SharedPrefUtils.writeObjectToSharedPrefs(context, config);
    }

}
