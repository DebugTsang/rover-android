package co.roverlabs.sdk.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.roverlabs.sdk.ui.widgets.BoxModelDimens;
import co.roverlabs.sdk.util.Constants;
import co.roverlabs.sdk.ui.UiUtils;

/**
 * Created by SherryYang on 2015-04-21.
 */
public class ViewModel extends Object {

    //JSON members
    @SerializedName("type") private String mType;
    @SerializedName("margin") private List<Integer> mMargin;
    @SerializedName("borderRadius") private Integer mBorderRadius;
    @SerializedName("backgroundColor") private List<Float> mBackgroundColor;
    @SerializedName("blocks") private List<Block> mBlocks;
    @SerializedName("backgroundImageUrl") private String mBackgroundImageUrl;
    @SerializedName("backgroundContentMode") private String mBackgroundContentMode;

    //Constructor
    public ViewModel() { mObjectName = "view"; }

    //Getters
    public String getType() { return mType; }
    public List<Block> getBlocks() { return mBlocks; }
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

    public Block getHeaderBlock() {

        if(mBlocks != null) {
            for(Block block : mBlocks) {
                if(block.getType().equals(Constants.VIEW_BLOCK_TYPE_HEADER)) {
                    return block;
                }
            }
        }
        return null;
    }

    public boolean isButtonBlockLast() {

        if(mBlocks != null) {
            if(mBlocks.get(mBlocks.size() - 1).getType().equals(Constants.VIEW_BLOCK_TYPE_BUTTON)) {
                return true;
            }
        }
        return false;
    }
}
