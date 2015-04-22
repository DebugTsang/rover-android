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
    @SerializedName("backgroundColor") private List<Integer> mBackgroundColor;
    @SerializedName("blocks") private List<RoverBlock> mBlocks;

    //Constructor
    public RoverView() { mObjectName = "view"; }

    //Getters
    public String getType() { return mType; }
    public List<Integer> getMargin() { return mMargin; }
    public Integer getBorderRadius() { return mBorderRadius; }
    public List<Integer> getBackgroundColor() { return mBackgroundColor; }
    public List<RoverBlock> getBlocks() { return mBlocks; }

    public class RoverBlock {

        //JSON members
        @SerializedName("type") private String mType;
        @SerializedName("padding") private List<Integer> mPadding;
        @SerializedName("borderWidth") private List<Integer> mBorderWidth;
        @SerializedName("borderColor") private List<Integer> mBorderColor;
        @SerializedName("backgroundColor") private List<Integer> mBackgroundColor;
        @SerializedName("imageUrl") private String mImageUrl;
        @SerializedName("imageWidth") private Integer mImageWidth;
        @SerializedName("imageHeight") private Integer mImageHeight;
        @SerializedName("imageOffsetRatio") private Float mImageOffsetRatio;
        @SerializedName("imageAspectRatio") private Float mImageAspectRatio;
        @SerializedName("textContent") private String mTextContent;

        //Constructor
        public RoverBlock() { }

        //Getters
        public String getType() { return mType; }
        public List<Integer> getPadding() { return mPadding; }
        public List<Integer> getBorderWidth() { return mBorderWidth; }
        public List<Integer> getBorderColor() { return mBorderColor; }
        public List<Integer> getBackgroundColor() { return mBackgroundColor; }
        public String getImageUrl() { return mImageUrl; }
        public Integer getImageWidth() { return mImageWidth; }
        public Integer getImageHeight() { return mImageHeight; }
        public Float getImageOffsetRatio() { return mImageOffsetRatio; }
        public Float getImageAspectRatio() { return mImageAspectRatio; }
        public String getTextContent() { return mTextContent; }
    }

}
