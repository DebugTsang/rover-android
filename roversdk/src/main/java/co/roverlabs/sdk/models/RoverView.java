package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SherryYang on 2015-04-21.
 */
public class RoverView extends RoverObject {

    //JSON members
    @SerializedName("type") private String mType;
    @SerializedName("margin") private List<Integer> mMargin;
    @SerializedName("borderRadius") private Integer mBorderRadius;
    @SerializedName("backgroundColor") private List<Float> mBackgroundColor;
    @SerializedName("blocks") private List<RoverBlock> mBlocks;

    //Constructor
    public RoverView() { mObjectName = "view"; }

    //Getters
    public String getType() { return mType; }
    public List<Integer> getMargin() { return mMargin; }
    public Integer getBorderRadius() { return mBorderRadius; }
    public List<Float> getBackgroundColor() { return mBackgroundColor; }
    public List<RoverBlock> getBlocks() { return mBlocks; }
    public Integer getTopMargin() { return mMargin.get(0); }
    public Integer getRightMargin() { return mMargin.get(1); }
    public Integer getBottomMargin() { return mMargin.get(2); }
    public Integer getLeftMargin() { return mMargin.get(3); }
    public Integer getRedBackgroundValue() { return mBackgroundColor.get(0).intValue(); }
    public Integer getBlueBackgroundValue() { return mBackgroundColor.get(1).intValue(); }
    public Integer getGreenBackgroundValue() { return mBackgroundColor.get(2).intValue(); }
    public Integer getAlphaBackgroundValue() { return (int)(mBackgroundColor.get(3) * 255); }

    public class RoverBlock {

        //JSON members
        @SerializedName("type") private String mType;
        @SerializedName("padding") private List<Integer> mPadding;
        @SerializedName("borderWidth") private List<Integer> mBorderWidth;
        @SerializedName("borderColor") private List<Float> mBorderColor;
        @SerializedName("backgroundColor") private List<Float> mBackgroundColor;
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
        public List<Integer> getPadding() { return mPadding; }
        public List<Integer> getBorderWidth() { return mBorderWidth; }
        public List<Float> getBorderColor() { return mBorderColor; }
        public List<Float> getBackgroundColor() { return mBackgroundColor; }
        public String getImageUrl() { return mImageUrl; }
        public Integer getImageWidth() { return mImageWidth; }
        public Integer getImageHeight() { return mImageHeight; }
        public Float getImageOffsetRatio() { return mImageOffsetRatio; }
        public Float getImageAspectRatio() { return mImageAspectRatio; }
        public String getTextContent() { return mTextContent; }
    }
}
