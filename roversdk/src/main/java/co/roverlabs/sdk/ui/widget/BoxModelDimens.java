package co.roverlabs.sdk.ui.widget;

import android.content.Context;

import co.roverlabs.sdk.utilities.UiUtils;

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

        this.top = UiUtils.convertDpToPx(con, top);
        this.right = UiUtils.convertDpToPx(con, right);
        this.bottom = UiUtils.convertDpToPx(con, bottom);
        this.left = UiUtils.convertDpToPx(con, left);
    }

    @Override
    public String toString() {

        return "Dimensions are { top: " + top + " right: " + right + " bottom: " + bottom + " left: " + left + " }";
    }
}
