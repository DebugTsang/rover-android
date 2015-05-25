package co.roverlabs.sdk.ui.widget;

/**
 * Created by SherryYang on 2015-05-01.
 */
public class Border {

    public static final String TAG = Border.class.getSimpleName();
    public int top;
    public int right;
    public int bottom;
    public int left;
    public int color;

    public Border() {}

    public Border(BoxModelDimens widths, int color) {

        this.top = widths.top;
        this.right = widths.right;
        this.bottom = widths.bottom;
        this.left = widths.left;
        this.color = color;
    }

    public boolean hasBorder() {

        return !(top == 0 && right == 0 && bottom == 0 && left == 0);
    }
}
