package co.roverlabs.sdk.ui;

import android.widget.ImageView;

import co.roverlabs.sdk.models.RoverBlock;

/**
 * Created by arsent on 15-05-22.
 */
public interface ImageLoader {

    public void fetchAll();

    public void loadBlockImage(ImageView imageView, final RoverBlock block);

    public void loadBackgroundImage(ImageView imageView, final String imageUrl, final String imageMode);

}
