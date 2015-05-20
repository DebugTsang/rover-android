package co.roverlabs.sdk.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
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
 *
 * PicassoUtils is responsible for image prefetching and image loading using Picasso library
 *
 */
public class PicassoUtils {

    /**
     * initialized the Picasso lib and makes sure it's done only ones
     */
    private static boolean isInitialized = false;
    private static void init(Context appContext){
        if (isInitialized) return;

        Picasso.with(appContext);
        isInitialized = true;
    }

    /**
     * Prefetch images so they won't use network when the user launches the CardActivity
     *
     * @param appContext
     */
    public static void prefetchImages(final Context appContext) {
        init(appContext);

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
                String blockImageUrl = block.getImageUrl(UiUtils.getDeviceWidthInDp(appContext));

                if (blockImageUrl != null){
                    Picasso.with(appContext).load(blockImageUrl).fetch();
                }
            }
        }
    }

    /**
     * Puts a placeholder until the image is loaded and loads the image using Picasso
     *
     * @param context
     * @param imageView
     * @param block
     */
    public static void loadBlockImage(final Context context, final ImageView imageView, final RoverBlock block) {
        init(context);

        //---- putting a placeholder
        //force the image to render the size we are setting
        imageView.setAdjustViewBounds(false);

        //calculating estimated height of the block using aspect ratio
        int estimatedWidth = UiUtils.getDeviceWidthInDp(context)
                - block.getPadding(context).left
                - block.getPadding(context).right
                - block.getBorderWidth(context).left
                - block.getBorderWidth(context).right;
        int estimatedHeight = (int) (estimatedWidth / block.getImageAspectRatio());

        imageView.setMinimumHeight(UiUtils.convertDpToPx(context, estimatedHeight));

        //---- we are now ready to load the image
        String blockImageUrl = block.getImageUrl(UiUtils.getDeviceWidthInDp(context));
        Picasso.with(imageView.getContext())
                .load(blockImageUrl)
                .into(imageView,  new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.setAdjustViewBounds(true);
                    }

                    @Override
                    public void onError() {}
                });
    }

    /**
     *
     * Loads background images using Picasso and sets the scale type depending on imageMode (RoverConstants.IMAGE_MODE_[X]) value
     *
     * @param imageView
     * @param imageUrl
     * @param imageMode
     */
    public static void loadBackgroundImage(final ImageView imageView, String imageUrl, String imageMode) {

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
     * Util function for creating Picasso RequestCreator so we can use for both pre-fetching and loading the image
     *
     * @param context
     * @param imageUrl
     * @param imageMode
     * @return
     */
    private static RequestCreator getPicassoRequestCreator(final Context context, String imageUrl, String imageMode) {
        init(context);

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


    public static Bitmap getScaledBitmap(Context con, Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = UiUtils.convertDpToPx(con, width);
        int newHeight = UiUtils.convertDpToPx(con, height);
        float scaleWidth = ((float)newWidth) / width;
        float scaleHeight = ((float)newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }

    public static void setImageMode(Context con, Bitmap bitmap, ImageView imageView, String imageMode) {

        Bitmap resizedBitmap = getScaledBitmap(con, bitmap);
        BitmapDrawable backgroundDrawable = new BitmapDrawable(con.getResources(), resizedBitmap);

        switch (imageMode) {

            case RoverConstants.IMAGE_MODE_STRETCH:
                imageView.setImageDrawable(backgroundDrawable);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                break;

            case RoverConstants.IMAGE_MODE_TILE:
                backgroundDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                imageView.setBackground(backgroundDrawable);
                break;

            case RoverConstants.IMAGE_MODE_FILL:
                imageView.setImageDrawable(backgroundDrawable);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;

            case RoverConstants.IMAGE_MODE_FIT:
                imageView.setImageDrawable(backgroundDrawable);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                break;

            default:
                imageView.setImageDrawable(backgroundDrawable);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
        }
    }
}
