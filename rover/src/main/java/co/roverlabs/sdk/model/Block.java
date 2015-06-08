package co.roverlabs.sdk.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import co.roverlabs.sdk.ui.TextStyle;
import co.roverlabs.sdk.ui.widgets.Border;
import co.roverlabs.sdk.ui.widgets.BoxModelDimens;
import co.roverlabs.sdk.ui.UiUtils;
import co.roverlabs.sdk.util.Constants;

/**
 * Created by SherryYang on 2015-04-30.
 */
public class Block {

    public static final String TAG = Block.class.getSimpleName();

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
    @SerializedName("barcodeString") private String mBarcodeString;
    @SerializedName("barcodeLabel") private String mBarcodeLabel;
    @SerializedName("barcodeFormat") private String mBarcodeFormat;
    //Text block text format
    //TODO: Remove hard coded values after testing
    //H1 format
    @SerializedName("h1Font") private String mH1Font;
    @SerializedName("h1FontSize") private Float mH1FontSize;
    @SerializedName("h1LineHeight") private Float mH1LineHeight;
    @SerializedName("h1TextAlign") private String mH1TextAlign;
    @SerializedName("h1Color") private List<Float> mH1Color;
    @SerializedName("h1Margin") private List<Integer> mH1Margin;
    //H2 format
    @SerializedName("h2Font") private String mH2Font;
    @SerializedName("h2FontSize") private Float mH2FontSize;
    @SerializedName("h2LineHeight") private Float mH2LineHeight;
    @SerializedName("h2TextAlign") private String mH2TextAlign;
    @SerializedName("h2Color") private List<Float> mH2Color;
    @SerializedName("h2Margin") private List<Integer> mH2Margin;
    //P format
    @SerializedName("pFont") private String mPFont;
    @SerializedName("pFontSize") private Float mPFontSize;
    @SerializedName("pLineHeight") private Float mPLineHeight;
    @SerializedName("pTextAlign") private String mPTextAlign;
    @SerializedName("pColor") private List<Float> mPColor;
    @SerializedName("pMargin") private List<Integer> mPMargin;
    //Button block text format
    @SerializedName("labelFont") private String mLabelFont;
    @SerializedName("labelFontSize") private Float mLabelFontSize;
    @SerializedName("labelLineHeight") private Float mLabelLineHeight;
    @SerializedName("labelTextAlign") private String mLabelTextAlign;
    @SerializedName("labelColor") private List<Float> mLabelFontColor;
    @SerializedName("labelMargin") private List<Integer> mLabelMargin;
    //Header block text format
    @SerializedName("titleFont") private String mHeaderFont;
    @SerializedName("titleFontSize") private Float mHeaderFontSize;
    @SerializedName("titleLineHeight") private Float mHeaderLineHeight;
    @SerializedName("titleTextAlign") private String mHeaderTextAlign;
    @SerializedName("titleColor") private List<Float> mHeaderFontColor;
    @SerializedName("titleMargin") private List<Integer> mHeaderMargin;
    //Barcode block text format
    @SerializedName("pluFont") private String mPluFont;
    @SerializedName("pluFontSize") private Float mPluFontSize;
    @SerializedName("pluLineHeight") private Float mPluLineHeight;
    @SerializedName("pluTextAlign") private String mPluTextAlign;
    @SerializedName("pluColor") private List<Float> mPluFontColor;
    @SerializedName("pluMargin") private List<Integer> mPluMargin;

    //Constructor
    public Block() { }

    //Getters
    public String getType() { return mType; }
    public String getBackgroundImageUrl() { return mBackgroundImageUrl; }
    public String getmBackgroundContentMode() { return mBackgroundContentMode; }

    public String getImageUrl() { return mImageUrl; }
    public String getImageUrl(int deviceWidth) {
        String imageUrl = null;

        String baseUrl = getImageUrl();
        if (baseUrl == null){ //there is no image for this block, so we return null
            return null;
        }

        //get the dimensions to generate the url
        Integer width = getImageWidth();
        Integer height = getImageHeight();
        Float offsetRatio = getImageOffsetRatio();
        Float aspectRatio = getImageAspectRatio();

        if (aspectRatio != null && aspectRatio > 0) {
            if (width != null && height != null) {
                imageUrl = baseUrl + "?w=" + deviceWidth + "&rect=0," + (int) ((-offsetRatio) * height) + "," + width + "," + (int) (width / aspectRatio);
            } else {
                imageUrl = baseUrl + "?w=" + deviceWidth + "&h" + (int) (deviceWidth / aspectRatio);
            }
        }

        return imageUrl;
    }

    public Integer getImageWidth() { return mImageWidth; }
    public Integer getImageHeight() { return mImageHeight; }
    public Float getImageOffsetRatio() { return mImageOffsetRatio; }
    public Float getImageAspectRatio() { return mImageAspectRatio; }
    public String getTextContent() { return mTextContent; }
    public String getUrl() { return mUrl; }
    public String getButtonLabel() { return mButtonLabel; }
    public String getHeaderTitle() { return mHeaderTitle; }
    public String getBarcodeLabel() { return mBarcodeLabel; }
    public String getBarcodeFormat() { return mBarcodeFormat; }
    public String getBarcodeString() { return mBarcodeString; }

    public BoxModelDimens getPadding(Context con) {

        return new BoxModelDimens(con, mPadding.get(0), mPadding.get(1), mPadding.get(2), mPadding.get(3));
    }

    public BoxModelDimens getBorderWidth(Context con) {

        return new BoxModelDimens(con, mBorderWidth.get(0), mBorderWidth.get(1), mBorderWidth.get(2), mBorderWidth.get(3));
    }

    public int getBorderColor() {

        return UiUtils.getARGBColor(mBorderColor);
    }

    public int getBackgroundColor() {

        return UiUtils.getARGBColor(mBackgroundColor);
    }

    public Border getBorder(Context con) {

        return new Border(getBorderWidth(con), getBorderColor());
    }

    public boolean hasBackgroundImage() {

        return mBackgroundImageUrl != null;
    }

    public TextStyle getH1TextStyle(Context con) {

        TextStyle style = new TextStyle();
        style.type = Constants.TEXT_H1;
        style.font = mH1Font;
        style.size = mH1FontSize;
        style.lineHeight = UiUtils.convertDpToPx(con, mH1LineHeight.intValue());
        style.align = mH1TextAlign;
        style.color = UiUtils.getARGBColor(mH1Color);
        style.margin = new BoxModelDimens(con, mH1Margin.get(0), mH1Margin.get(1), mH1Margin.get(2), mH1Margin.get(3));
        return style;
    }

    public TextStyle getH2TextStyle(Context con) {

        TextStyle style = new TextStyle();
        style.type = Constants.TEXT_H2;
        style.font = mH2Font;
        style.size = mH2FontSize;
        style.lineHeight = UiUtils.convertDpToPx(con, mH2LineHeight.intValue());
        style.align = mH2TextAlign;
        style.color = UiUtils.getARGBColor(mH2Color);
        style.margin = new BoxModelDimens(con, mH2Margin.get(0), mH2Margin.get(1), mH2Margin.get(2), mH2Margin.get(3));
        return style;
    }

    public TextStyle getPTextStyle(Context con) {

        TextStyle style = new TextStyle();
        style.type = Constants.TEXT_P;
        style.font = mPFont;
        style.size = mPFontSize;
        style.lineHeight = UiUtils.convertDpToPx(con, mPLineHeight.intValue());
        style.align = mPTextAlign;
        style.color = UiUtils.getARGBColor(mPColor);
        style.margin = new BoxModelDimens(con, mPMargin.get(0), mPMargin.get(1), mPMargin.get(2), mPMargin.get(3));
        return style;
    }

    public List<TextStyle> getTextBlockStyles(Context con) {

        List<TextStyle> textStyles = new ArrayList<>();
        textStyles.add(getH1TextStyle(con));
        textStyles.add(getH2TextStyle(con));
        textStyles.add(getPTextStyle(con));
        return textStyles;
    }

    public TextStyle getLabelTextStyle(Context con) {

        TextStyle style = new TextStyle();
        style.type = Constants.TEXT_DIV;
        style.font = mLabelFont;
        style.size = mLabelFontSize;
        style.lineHeight = UiUtils.convertDpToPx(con, mLabelLineHeight.intValue());
        style.align = mLabelTextAlign;
        style.color = UiUtils.getARGBColor(mLabelFontColor);
        style.margin = new BoxModelDimens(con, mLabelMargin.get(0), mLabelMargin.get(1), mLabelMargin.get(2), mLabelMargin.get(3));
        return style;
    }

    public TextStyle getHeaderTextStyle(Context con) {

        TextStyle style = new TextStyle();
        style.type = Constants.TEXT_DIV;
        style.font = mHeaderFont;
        style.size = mHeaderFontSize;
        style.lineHeight = UiUtils.convertDpToPx(con, mHeaderLineHeight.intValue());
        style.align = mHeaderTextAlign;
        style.color = UiUtils.getARGBColor(mHeaderFontColor);
        style.margin = new BoxModelDimens(con, mHeaderMargin.get(0), mHeaderMargin.get(1), mHeaderMargin.get(2), mHeaderMargin.get(3));
        return style;
    }

    public TextStyle getPluTextStyle(Context con) {

        TextStyle style = new TextStyle();
        style.type = Constants.TEXT_DIV;
        style.font = mPluFont;
        style.size = mPluFontSize;
        style.lineHeight = UiUtils.convertDpToPx(con, mPluLineHeight.intValue());
        style.align = mPluTextAlign;
        style.color = UiUtils.getARGBColor(mPluFontColor);
        style.margin = new BoxModelDimens(con, mPluMargin.get(0), mPluMargin.get(1), mPluMargin.get(2), mPluMargin.get(3));
        return style;
    }
}
