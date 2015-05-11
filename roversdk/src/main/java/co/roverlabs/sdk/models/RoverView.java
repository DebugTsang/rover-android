package co.roverlabs.sdk.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.roverlabs.sdk.ui.BoxModelDimens;
import co.roverlabs.sdk.ui.ImageUtils;

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
    @SerializedName("backgroundImageUrl") private String mBackgroundImageUrl;
    @SerializedName("backgroundContentMode") private String mBackgroundContentMode;

    //Constructor
    public RoverView() { mObjectName = "view"; }

    //Getters
    public String getType() { return mType; }
    public List<RoverBlock> getBlocks() { return mBlocks; }
    public String getBackgroundImageUrl() { return mBackgroundImageUrl; }
    public String getBackgroundContentMode() { return mBackgroundContentMode; }

    public BoxModelDimens getMargin(Context con) {

        return new BoxModelDimens(con, mMargin.get(0), mMargin.get(1), mMargin.get(2), mMargin.get(3));
    }

    public int getBorderRadius(Context con) {

        return ImageUtils.convertDpToPx(con, mBorderRadius);
    }

    public int getBackgroundColor() {

        return ImageUtils.getARGBColor(mBackgroundColor);
    }
}
