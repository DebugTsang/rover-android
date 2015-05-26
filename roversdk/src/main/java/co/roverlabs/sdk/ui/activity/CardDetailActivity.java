package co.roverlabs.sdk.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.models.RoverBlock;
import co.roverlabs.sdk.models.RoverView;
import co.roverlabs.sdk.ui.widget.Border;
import co.roverlabs.sdk.ui.widget.BorderedView;
import co.roverlabs.sdk.ui.widget.BoxModelDimens;
import co.roverlabs.sdk.ui.ImageLoader;
import co.roverlabs.sdk.ui.TextStyle;
import co.roverlabs.sdk.utilities.UiUtils;
import co.roverlabs.sdk.utilities.FactoryUtils;
import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-05-14.
 */
public class CardDetailActivity extends BaseActivity {

    public static final String TAG = CardDetailActivity.class.getName();
    private RoverView mDetailView;
    //Header block
    private TextView mHeader;
    private View mHeaderBottomBorder;
    //Block body
    private FrameLayout mBlocksLayout;
    private ImageView mBlocksBackground;
    private LinearLayout mBlocksScrollLayout;
    //Image block
    private FrameLayout mImageBlockLayout;
    private ImageView mImageBlockBackground;
    private BorderedView mImageBlockBorder;
    private ImageView mImage;
    //Text block
    private FrameLayout mTextBlockLayout;
    private ImageView mTextBlockBackground;
    private BorderedView mTextBlockBorder;
    private LinearLayout mTextLayout;
    //Button block
    private FrameLayout mButtonBlockLayout;
    private ImageView mButtonBlockBackground;
    private BorderedView mButtonBlockBorder;
    private TextView mButton;
    //Sticky button block
    private FrameLayout mStickyButtonBlockLayout;
    private ImageView mStickyButtonBlockBackground;
    private BorderedView mStickyButtonBlockBorder;
    private TextView mStickyButton;
    //Barcode block
    private FrameLayout mBarcodeBlockLayout;
    private ImageView mBarcodeBlockBackground;
    private BorderedView mBarcodeBlockBorder;
    private LinearLayout mBarcodeContentLayout;
    private ImageView mBarcode128;
    private TextView mBarcodeLabel;

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_detail);

        mImageLoader = FactoryUtils.getDefaultImageLoader(getApplicationContext());

        String detailViewString = getIntent().getStringExtra(RoverConstants.VIEW_TYPE_DETAIL);
        mDetailView = new Gson().fromJson(detailViewString, RoverView.class);

        //Header block
        mHeader = (TextView)findViewById(R.id.detail_header_title);
        mHeaderBottomBorder = findViewById(R.id.detail_header_bottom_border);
        //Block body
        mBlocksLayout = (FrameLayout)findViewById(R.id.detail_blocks_layout);
        mBlocksBackground = (ImageView)findViewById(R.id.detail_background);
        mBlocksScrollLayout = (LinearLayout)findViewById(R.id.detail_blocks_scroll_layout);
        //Image block
        mImageBlockLayout = (FrameLayout)findViewById(R.id.detail_image_block_layout);
        mImageBlockBackground = (ImageView)findViewById(R.id.detail_image_block_background);
        mImageBlockBorder = (BorderedView)findViewById(R.id.detail_image_block_border);
        mImage = (ImageView)findViewById(R.id.detail_image_block_image);
        //Text block
        mTextBlockLayout = (FrameLayout)findViewById(R.id.detail_text_block_layout);
        mTextBlockBackground = (ImageView)findViewById(R.id.detail_text_block_background);
        mTextBlockBorder = (BorderedView)findViewById(R.id.detail_text_block_border);
        mTextLayout = (LinearLayout)findViewById(R.id.detail_text_block_text_layout);
        //Button block
        mButtonBlockLayout = (FrameLayout)findViewById(R.id.detail_button_block_layout);
        mButtonBlockBackground = (ImageView)findViewById(R.id.detail_button_block_background);
        mButtonBlockBorder = (BorderedView)findViewById(R.id.detail_button_block_border);
        mButton = (TextView)findViewById(R.id.detail_button_block_button);
        //Sticky button block
        mStickyButtonBlockLayout = (FrameLayout)findViewById(R.id.detail_sticky_button_block_layout);
        mStickyButtonBlockBackground = (ImageView)findViewById(R.id.detail_sticky_button_block_background);
        mStickyButtonBlockBorder = (BorderedView)findViewById(R.id.detail_sticky_button_block_border);
        mStickyButton = (TextView)findViewById(R.id.detail_sticky_button_block_button);
        //Barcode block
        mBarcodeBlockLayout = (FrameLayout)findViewById(R.id.detail_barcode_block_layout);
        mBarcodeBlockBackground = (ImageView)findViewById(R.id.detail_barcode_block_background);
        mBarcodeBlockBorder = (BorderedView)findViewById(R.id.detail_barcode_block_border);
        mBarcodeContentLayout = (LinearLayout)findViewById(R.id.detail_barcode_block_barcode_layout);
        mBarcode128 = (ImageView)findViewById(R.id.detail_barcode_block_barcode_128);
        mBarcodeLabel = (TextView)findViewById(R.id.detail_barcode_block_barcode_label);

        mImageLoader = FactoryUtils.getDefaultImageLoader(getApplicationContext());

        setHeaderBlock();
        setBackground();
        setContentBlocks();
    }

    private void setHeaderBlock() {

        RoverBlock headerBlock = mDetailView.getHeaderBlock();
        mHeader.setBackgroundColor(headerBlock.getBackgroundColor());
        UiUtils.setText(RoverConstants.VIEW_BLOCK_TYPE_HEADER, mHeader, headerBlock.getHeaderTitle(), headerBlock.getHeaderTextStyle(getApplicationContext()));
        mHeaderBottomBorder.getLayoutParams().height = headerBlock.getBorder(getApplicationContext()).bottom;
        mHeaderBottomBorder.setBackgroundColor(headerBlock.getBorderColor());
    }

    private void setBackground() {

        mBlocksLayout.setBackgroundColor(mDetailView.getBackgroundColor());
        mImageLoader.loadBackgroundImage(mBlocksBackground, mDetailView.getBackgroundImageUrl(), mDetailView.getBackgroundContentMode());
    }

    private void setContentBlocks() {

        mBlocksScrollLayout.removeAllViews();

        for(RoverBlock block : mDetailView.getBlocks()) {

            BoxModelDimens padding = block.getPadding(getApplicationContext());
            Border border = block.getBorder(getApplicationContext());
            int backgroundColor = block.getBackgroundColor();
            String blockBackgroundImageUrl = block.getBackgroundImageUrl();
            String blockBackgroundImageMode = block.getmBackgroundContentMode();

            FrameLayout blockLayout = null;

            switch(block.getType()) {
                case RoverConstants.VIEW_BLOCK_TYPE_TEXT:
                    blockLayout = mTextBlockLayout;
                    mBlocksScrollLayout.addView(mTextBlockLayout);
                    mTextBlockLayout.setBackgroundColor(backgroundColor);
                    mImageLoader.loadBackgroundImage(mTextBlockBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    UiUtils.setBorder(mTextBlockBorder, border);
                    UiUtils.setPadding(mTextLayout, padding, border);
                    UiUtils.setText(RoverConstants.VIEW_BLOCK_TYPE_TEXT, mTextLayout, block.getTextContent(), block.getTextBlockStyles(getApplicationContext()));
                    break;

                case RoverConstants.VIEW_BLOCK_TYPE_IMAGE:
                    blockLayout = mImageBlockLayout;
                    mBlocksScrollLayout.addView(mImageBlockLayout);
                    mImageBlockLayout.setBackgroundColor(backgroundColor);
                    mImageLoader.loadBackgroundImage(mImageBlockBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    UiUtils.setBorder(mImageBlockBorder, border);
                    mImageLoader.loadBlockImage(mImage, block);
                    UiUtils.setPadding(mImage, padding, border);
                    break;

                case RoverConstants.VIEW_BLOCK_TYPE_BUTTON:
                    if(mDetailView.isButtonBlockLast()) {
                        blockLayout = mStickyButtonBlockLayout;
                        mStickyButtonBlockLayout.setBackgroundColor(backgroundColor);
                        mImageLoader.loadBackgroundImage(mStickyButtonBlockBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                        UiUtils.setBorder(mStickyButtonBlockBorder, border);
                        UiUtils.setText(RoverConstants.VIEW_BLOCK_TYPE_BUTTON, mStickyButton, block.getButtonLabel(), block.getLabelTextStyle(getApplicationContext()));
                        UiUtils.setPadding(mStickyButton, padding, border);
                        mStickyButtonBlockLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        blockLayout = mButtonBlockLayout;
                        mBlocksScrollLayout.addView(mButtonBlockLayout);
                        mButtonBlockLayout.setBackgroundColor(backgroundColor);
                        mImageLoader.loadBackgroundImage(mButtonBlockBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                        UiUtils.setBorder(mButtonBlockBorder, border);
                        UiUtils.setText(RoverConstants.VIEW_BLOCK_TYPE_BUTTON, mButton, block.getButtonLabel(), block.getLabelTextStyle(getApplicationContext()));
                        UiUtils.setPadding(mButton, padding, border);
                    }
                    break;

                case RoverConstants.VIEW_BLOCK_TYPE_BARCODE:
                    blockLayout = mBarcodeBlockLayout;
                    mBlocksScrollLayout.addView(mBarcodeBlockLayout);
                    mBarcodeBlockLayout.setBackgroundColor(backgroundColor);
                    mImageLoader.loadBackgroundImage(mBarcodeBlockBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    UiUtils.setBorder(mBarcodeBlockBorder, border);
                    TextStyle barcodeLabelStyle = new TextStyle();
                    if(block.getBarcodeFormat().equals(RoverConstants.BARCODE_FORMAT_PLU)) {
                        barcodeLabelStyle = block.getPluTextStyle(getApplicationContext());
                        mBarcode128.setVisibility(View.GONE);
                    }
                    else if(block.getBarcodeFormat().equals(RoverConstants.BARCODE_FORMAT_128)) {
                        barcodeLabelStyle = block.getLabelTextStyle(getApplicationContext());
                        int barcodeWidth = UiUtils.getDeviceWidthInPx(getApplicationContext());
                        int barcodeHeight = UiUtils.convertDpToPx(getApplicationContext(), RoverConstants.BARCODE_HEIGHT);
                        int barcodeColor = Color.BLACK;
                        String barcodeContent = block.getBarcodeString();
                        mBarcode128.setImageBitmap(UiUtils.generateBarcode(barcodeContent, barcodeWidth, barcodeHeight, barcodeColor));
                        mBarcode128.setScaleType(ImageView.ScaleType.FIT_XY);
                        mBarcode128.setVisibility(View.VISIBLE);
                    }
                    UiUtils.setText(RoverConstants.VIEW_BLOCK_TYPE_BARCODE, mBarcodeLabel, block.getBarcodeLabel(), barcodeLabelStyle);
                    UiUtils.setPadding(mBarcodeContentLayout, padding, border);
                    break;
            }

            final String blockUrl = block.getUrl();

            if(blockLayout != null && blockUrl != null) {

                blockLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Uri blockUri = Uri.parse(blockUrl);
                        String blockScheme = blockUri.getScheme();
                        Intent intent = new Intent();

                        if(blockScheme != null) {
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setData(blockUri);
                            getApplicationContext().startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setShouldStartService(false);
    }
}
