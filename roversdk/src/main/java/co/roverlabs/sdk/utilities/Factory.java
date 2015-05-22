package co.roverlabs.sdk.utilities;

import android.content.Context;

import co.roverlabs.sdk.ui.ImageLoader;
import co.roverlabs.sdk.ui.PicassoImageLoader;

/**
 * Created by arsent on 15-05-22.
 */
public class Factory {
    private static ImageLoader mImageLoader;

    public static ImageLoader getDefaultImageLoader(Context context) {
        if (mImageLoader == null)
            mImageLoader = new PicassoImageLoader(context);
        return mImageLoader;
    }
}
