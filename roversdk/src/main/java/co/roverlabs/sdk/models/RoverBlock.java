package co.roverlabs.sdk.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.roverlabs.sdk.ui.Border;
import co.roverlabs.sdk.ui.BoxModelDimens;
import co.roverlabs.sdk.ui.ImageUtils;

/**
 * Created by SherryYang on 2015-04-30.
 */
public class RoverBlock {

    //JSON members
    @SerializedName("type") private String mType;
    @SerializedName("padding") private List<Integer> mPadding;
    @SerializedName("borderWidth") private List<Integer> mBorderWidth;
    @SerializedName("borderColor") private List<Float> mBorderColor;
    @SerializedName("backgroundColor") private List<Float> mBackgroundColor;
    @SerializedName("backgroundImageUrl") private String mBackgroundImageUrl;
    @SerializedName("backgroundContentMode") private String mBackgroundContentMode;
    @SerializedName("imageUrl") private String mImageUrl;
    @SerializedName("imageWidth") private Integer mImageWidth;
    @SerializedName("imageHeight") private Integer mImageHeight;
    @SerializedName("imageOffsetRatio") private Float mImageOffsetRatio;
    @SerializedName("imageAspectRatio") private Float mImageAspectRatio;
    @SerializedName("textContent") private String mTextContent;
    @SerializedName("url") private String mUrl;
    @SerializedName("buttonLabel") private String mButtonLabel;
    @SerializedName("headerTitle") private String mHeaderTitle;

    //Constructor
    public RoverBlock() { }

    //Getters
    public String getType() { return mType; }
    public String getBackgroundImageUrl() { return mBackgroundImageUrl; }
    public String getmBackgroundContentMode() { return mBackgroundContentMode; }
    public String getImageUrl() { return mImageUrl; }
    public Integer getImageWidth() { return mImageWidth; }
    public Integer getImageHeight() { return mImageHeight; }
    public Float getImageOffsetRatio() { return mImageOffsetRatio; }
    public Float getImageAspectRatio() { return mImageAspectRatio; }
    public String getTextContent() { return mTextContent; }
    public String getUrl() { return mUrl; }
    public String getButtonLabel() { return mButtonLabel; }
    public String getHeaderTitle() { return mHeaderTitle; }

    public BoxModelDimens getPadding(Context con) {

        return new BoxModelDimens(con, mPadding.get(0), mPadding.get(1), mPadding.get(2), mPadding.get(3));
    }

    public BoxModelDimens getBorderWidth(Context con) {

        return new BoxModelDimens(con, mBorderWidth.get(0), mBorderWidth.get(1), mBorderWidth.get(2), mBorderWidth.get(3));
    }

    public int getBorderColor() {

        return ImageUtils.getARGBColor(mBorderColor);
    }

    public int getBackgroundColor() {

        return ImageUtils.getARGBColor(mBackgroundColor);
    }

    public Border getBorder(Context con) {

        return new Border(getBorderWidth(con), getBorderColor());
    }
}
