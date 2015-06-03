package co.roverlabs.sdk;

import android.content.Context;
import android.os.Handler;

/**
 * Created by ars on 15-06-02.
 */
public class FactoryUtils {

    private static ILocationHelper sLocationHelperInstance;
    public static ILocationHelper getDefaultLocationHelper(Context context, Handler handler){
        if (sLocationHelperInstance == null){
            sLocationHelperInstance = new EstimoteHelper(context, handler);
        }

        return sLocationHelperInstance;
    }
}
