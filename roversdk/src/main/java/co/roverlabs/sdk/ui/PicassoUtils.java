package co.roverlabs.sdk.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

import co.roverlabs.sdk.managers.RoverVisitManager;
import co.roverlabs.sdk.models.RoverBlock;
import co.roverlabs.sdk.models.RoverCard;
import co.roverlabs.sdk.models.RoverView;
import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by arsent on 15-05-19.
 */
public class PicassoUtils {

    /**
     * Prefetch images so they won't use network when the user launches the CardActivity
     *
     * @param appContext
     */
    public static void prefetchImages(Context appContext) {
        List<RoverCard> cards = RoverVisitManager.getInstance(appContext).getLatestVisit().getAccumulatedCards();


        for (RoverCard card : cards) {
            RoverView listView = card.getListView();

            //load background images
            String imageUrl = listView.getBackgroundImageUrl();
            String imageMode = listView.getBackgroundContentMode();
            if (!TextUtils.isEmpty(imageUrl)) {
                getPicassoRequestCreator(appContext, imageUrl, imageMode).fetch();
            }

            //load block images
            for (RoverBlock block : listView.getBlocks()) {
                String blockImageUrl = getBlockImageUrl(appContext, block);

                if (blockImageUrl != null){
                    Picasso.with(appContext).load(blockImageUrl).fetch();
                }
            }
        }
    }

    public static void loadBlockImage(Context context, ImageView imageView, RoverBlock block) {
        String blockImageUrl = getBlockImageUrl(context, block);
        Picasso.with(imageView.getContext()).load(blockImageUrl).into(imageView);
    }

    /**
     *
     * Prefetch images into the view
     *
     * @param imageView
     * @param imageUrl
     * @param imageMode
     */
    public static void loadBackgroundImage(ImageView imageView, String imageUrl, String imageMode) {

        RequestCreator requestCreator = getPicassoRequestCreator(imageView.getContext(), imageUrl, imageMode);
        requestCreator.into(imageView);

        switch(imageMode) {

            case RoverConstants.IMAGE_MODE_STRETCH:
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                break;

            //TODO: Tile mode
            //case RoverConstants.IMAGE_MODE_TILE:
            //    break;

            case RoverConstants.IMAGE_MODE_FILL:
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;

            case RoverConstants.IMAGE_MODE_FIT:
                break;

            //TODO: Original size
            //case RoverConstants.IMAGE_MODE_ORIGINAL:
            //    imageView.setImageDrawable(backgroundDrawable);
            //    imageView.setScaleType(ImageView.ScaleType.CENTER);

            default:
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    /**
     *
     * Util function for creating Picasso RequestCreator so we can use for both pre-fetching and showing the image
     *
     * @param context
     * @param imageUrl
     * @param imageMode
     * @return
     */
    private static RequestCreator getPicassoRequestCreator(Context context, String imageUrl, String imageMode) {

        RequestCreator requestCreator = null;

        switch(imageMode) {

            case RoverConstants.IMAGE_MODE_STRETCH:
                requestCreator = Picasso.with(context).load(imageUrl).fit();
                break;

            //TODO: Tile mode
            //case RoverConstants.IMAGE_MODE_TILE:
            //    break;

            case RoverConstants.IMAGE_MODE_FILL:
                requestCreator = Picasso.with(context).load(imageUrl).fit().centerCrop();
                break;

            case RoverConstants.IMAGE_MODE_FIT:
                requestCreator = Picasso.with(context).load(imageUrl).fit().centerInside();
                break;

            //TODO: Original size
            //case RoverConstants.IMAGE_MODE_ORIGINAL:
            //    imageView.setImageDrawable(backgroundDrawable);
            //    imageView.setScaleType(ImageView.ScaleType.CENTER);

            default:
                requestCreator = Picasso.with(context).load(imageUrl).fit().centerCrop();
        }

        return requestCreator;
    }

    /**
     * Create image url based on the image size etc.
     *
     * @param context
     * @param block
     * @return
     */
    private static String getBlockImageUrl(Context context, RoverBlock block){
        String imageUrl = null;

        int deviceWidth = UiUtils.getDeviceWidth(context);

        String baseUrl = block.getImageUrl();

        Integer width = block.getImageWidth();
        Integer height = block.getImageHeight();
        Float offsetRatio = block.getImageOffsetRatio();
        Float aspectRatio = block.getImageAspectRatio();

        if (baseUrl != null && aspectRatio != null) {

            if (width != null && height != null) {
                imageUrl = baseUrl + "?w=" + deviceWidth + "&rect=0," + (int) ((-offsetRatio) * height) + "," + width + "," + (int) (width / aspectRatio);
            } else {
                imageUrl = baseUrl + "?w=" + deviceWidth + "&h" + (int) (deviceWidth / aspectRatio);
            }
        }

        return imageUrl;
    }

}
