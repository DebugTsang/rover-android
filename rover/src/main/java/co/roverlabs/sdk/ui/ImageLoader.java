package co.roverlabs.sdk.ui;

import android.widget.ImageView;

import co.roverlabs.sdk.model.Block;

/**
 * Created by arsent on 15-05-22.
 */
public interface ImageLoader {

    void fetchAll();

    void loadBlockImage(ImageView imageView, final Block block);

    void loadBackgroundImage(ImageView imageView, final String imageUrl, final String imageMode);

}
