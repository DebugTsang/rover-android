package co.roverlabs.sdk.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.util.List;

import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-05-01.
 */
public class ImageUtils {

    public static final String TAG = ImageUtils.class.getSimpleName();

    public static int getARGBColor(List<Float> colorValues) {

        int red = colorValues.get(0).intValue();
        int green = colorValues.get(1).intValue();
        int blue = colorValues.get(2).intValue();
        int alpha = (int)(colorValues.get(3) * 255);
        return Color.argb(alpha, red, green, blue);
    }

    public static int convertDpToPx(Context con, int dp) {

        final float scale = con.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    public static int convertPxToDp(Context con, int px) {

        final float scale = con.getResources().getDisplayMetrics().density;
        return (int)(px / scale + 0.5f);
    }

    public static int getDeviceWidth(Context con) {

        return convertPxToDp(con, con.getResources().getDisplayMetrics().widthPixels);
    }

    public static int getDeviceHeight(Context con) {

        return convertPxToDp(con, con.getResources().getDisplayMetrics().heightPixels);
    }

    public static Bitmap getScaledBitmap(Context con, Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = convertDpToPx(con, width);
        int newHeight = convertDpToPx(con, height);
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