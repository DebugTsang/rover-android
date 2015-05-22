package co.roverlabs.sdk.ui;

import android.content.Context;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
public class PicassoImageLoader implements ImageLoader{

    private Context mContext;

    public PicassoImageLoader(Context context){
        this.mContext = context;
    }

    /**
     * Prefetch images so they won't use network when the user launches the CardActivity
     */
    public void fetchAll() {
        List<RoverCard> cards = RoverVisitManager.getInstance(this.mContext).getLatestVisit().getAccumulatedCards();


        for (RoverCard card : cards) {
            RoverView listView = card.getListView();

            //load background images
            String imageUrl = listView.getBackgroundImageUrl();
            if (!TextUtils.isEmpty(imageUrl)) {
                imageUrl = getBackgroundImageUrl(this.mContext, listView.getBackgroundImageUrl(), listView.getBackgroundContentMode());
                Picasso.with(this.mContext).load(imageUrl).fetch();
            }

            //load block images
            List<RoverBlock> blocks = listView.getBlocks();
            for (RoverBlock block : blocks) {
                String blockImageUrl = block.getImageUrl(UiUtils.getDeviceWidthInDp(this.mContext));
                if (blockImageUrl != null){
                    Picasso.with(this.mContext).load(blockImageUrl).fetch();
                }

                String blockBgImageUrl = getBackgroundImageUrl(this.mContext, block.getBackgroundImageUrl(), block.getmBackgroundContentMode());
                if (blockBgImageUrl != null){
                    Picasso.with(this.mContext).load(blockBgImageUrl).fetch();
                }
            }
        }
    }

    /**
     * Puts a placeholder until the image is loaded and loads the image using Picasso
     *
     * @param imageView
     * @param block
     */
    public void loadBlockImage(final ImageView imageView, final RoverBlock block) {
        //---- putting a placeholder
        //force the image to render the size we are setting
        imageView.setAdjustViewBounds(false);

        //calculating estimated height of the block using aspect ratio
        int estimatedWidth = UiUtils.getDeviceWidthInDp(this.mContext)
                - block.getPadding(this.mContext).left
                - block.getPadding(this.mContext).right
                - block.getBorderWidth(this.mContext).left
                - block.getBorderWidth(this.mContext).right;
        int estimatedHeight = (int) (estimatedWidth / block.getImageAspectRatio());

        imageView.setMinimumHeight(UiUtils.convertDpToPx(this.mContext, estimatedHeight));

        //---- we are now ready to load the image
        String blockImageUrl = block.getImageUrl(UiUtils.getDeviceWidthInDp(this.mContext));
        Picasso.with(imageView.getContext())
                .load(blockImageUrl)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.setMinimumHeight(0);
                        imageView.setAdjustViewBounds(true);
                    }

                    @Override
                    public void onError() {
                    }
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
    public void loadBackgroundImage(final ImageView imageView, final String imageUrl, final String imageMode) {
        if(imageUrl == null) {
            imageView.setVisibility(View.GONE);
            return;
        }

        imageView.setBackground(null);
        imageView.setImageDrawable(null);
        imageView.setImageBitmap(null);
        imageView.setVisibility(View.VISIBLE);

        String url = getBackgroundImageUrl(imageView.getContext(), imageUrl, imageMode);
        Picasso.with(imageView.getContext()).load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                switch (imageMode) {

                    case RoverConstants.IMAGE_MODE_STRETCH:
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        break;

                    case RoverConstants.IMAGE_MODE_TILE:
                        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                        drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                        imageView.setBackground(drawable);
                        imageView.setImageDrawable(null);
                        break;

                    case RoverConstants.IMAGE_MODE_FILL:
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        break;

                    case RoverConstants.IMAGE_MODE_FIT:
                        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        break;

                    case RoverConstants.IMAGE_MODE_ORIGINAL:
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    default:
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            }

            @Override
            public void onError() {
                Log.e(getClass().getCanonicalName(), "Error loading " + imageUrl + " with mode " + imageMode);
            }
         });
    }


    private static String getBackgroundImageUrl(Context context, String url, String mode){
        if (url == null){
            return null;
        }
        if (mode == null){
            mode = ""; //go with the default case
        }

        int deviceWidth = UiUtils.getDeviceWidthInDp(context);
        int deviceHeight = UiUtils.getDeviceHeightInDp(context);

        switch (mode) {
            case RoverConstants.IMAGE_MODE_STRETCH:
                return url + "?w=" + deviceWidth + "&h=" + deviceHeight;

            case RoverConstants.IMAGE_MODE_TILE:
                return url + "?fit=max&w=" + deviceWidth;

            case RoverConstants.IMAGE_MODE_FILL:
                return url + "?fit=crop&w=" + deviceWidth + "&h=" + deviceHeight;

            case RoverConstants.IMAGE_MODE_FIT:
                return url + "?w=" + deviceWidth + "&h=" + deviceHeight;

            case RoverConstants.IMAGE_MODE_ORIGINAL:
                return url + "?rect=0,0," + deviceWidth + "," + deviceHeight;
            default:
                return url + "?w=" + deviceWidth + "&h=" + deviceHeight;
        }
    }
}
