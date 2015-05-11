package co.roverlabs.sdk.ui;

import android.content.Context;

/**
 * Created by SherryYang on 2015-05-01.
 */
public class BoxModelDimens {

    public static final String TAG = BoxModelDimens.class.getSimpleName();
    public int top;
    public int right;
    public int bottom;
    public int left;

    public BoxModelDimens() { }

    public BoxModelDimens(Context con, Integer top, Integer right, Integer bottom, Integer left) {

        this.top = ImageUtils.convertDpToPx(con, top);
        this.right = ImageUtils.convertDpToPx(con, right);
        this.bottom = ImageUtils.convertDpToPx(con, bottom);
        this.left = ImageUtils.convertDpToPx(con, left);
    }

    @Override
    public String toString() {

        return "Dimensions are { top: " + top + " right: " + right + " bottom: " + bottom + " left: " + left + " }";
    }
}
