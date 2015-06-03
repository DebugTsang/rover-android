package co.roverlabs.sdk;

import android.content.Context;

import co.roverlabs.sdk.ui.ImageLoader;
import co.roverlabs.sdk.ui.PicassoImageLoader;

/**
 * Created by ars on 15-06-02.
 */
public class Factory {

    private static ImageLoader sImageLoader;
    public static ImageLoader getDefaultImageLoader(Context context) {
        if (sImageLoader == null)
            sImageLoader = new PicassoImageLoader(context);
        return sImageLoader;
    }

}
