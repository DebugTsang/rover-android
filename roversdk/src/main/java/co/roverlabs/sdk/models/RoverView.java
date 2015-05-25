package co.roverlabs.sdk.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.roverlabs.sdk.ui.widget.BoxModelDimens;
import co.roverlabs.sdk.ui.UiUtils;
import co.roverlabs.sdk.utilities.RoverConstants;

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

        return UiUtils.convertDpToPx(con, mBorderRadius);
    }

    public int getBackgroundColor() {

        return UiUtils.getARGBColor(mBackgroundColor);
    }

    public RoverBlock getHeaderBlock() {

        if(mBlocks != null) {
            for(RoverBlock block : mBlocks) {
                if(block.getType().equals(RoverConstants.VIEW_BLOCK_TYPE_HEADER)) {
                    return block;
                }
            }
        }
        return null;
    }

    public boolean isButtonBlockLast() {

        if(mBlocks != null) {
            if(mBlocks.get(mBlocks.size() - 1).getType().equals(RoverConstants.VIEW_BLOCK_TYPE_BUTTON)) {
                return true;
            }
        }
        return false;
    }
}
