package co.roverlabs.sdk.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by SherryYang on 2015-05-04.
 */
public class BorderedView extends ImageView {

    public static final String TAG = BorderedView.class.getSimpleName();
    private Paint mPaint;
    private Border mBorder;

    public BorderedView(Context context) {

        super(context);
        initialize();
    }

    public BorderedView(Context context, AttributeSet attrs) {

        super(context, attrs);
        initialize();
    }

    public BorderedView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.TRANSPARENT);
    }

    public void setBorder(Border border) {

        mBorder = border;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if(mBorder == null) {
            return;
        }

        drawBorder(canvas);
    }

    public void drawBorder(Canvas canvas) {

        mPaint.setColor(mBorder.color);
        //Top
        canvas.drawRect(0, 0, getWidth(), mBorder.top, mPaint);
        //Right
        canvas.drawRect(getWidth() - mBorder.right, 0, getWidth(), getHeight(), mPaint);
        //Bottom
        canvas.drawRect(0, getHeight() - mBorder.bottom, getWidth(), getHeight(), mPaint);
        //Left
        canvas.drawRect(0, 0, mBorder.left, getHeight(), mPaint);
    }
}
