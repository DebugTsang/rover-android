package co.roverlabs.sdk.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.models.RoverBlock;
import co.roverlabs.sdk.models.RoverView;
import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-05-14.
 */
public class CardDetailActivity extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_detail);

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
        PicassoUtils.loadBackgroundImage(mBlocksBackground, mDetailView.getBackgroundImageUrl(), mDetailView.getBackgroundContentMode());
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
                    PicassoUtils.loadBackgroundImage(mTextBlockBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    UiUtils.setBorder(mTextBlockBorder, border);
                    UiUtils.setPadding(mTextLayout, padding, border);
                    UiUtils.setText(RoverConstants.VIEW_BLOCK_TYPE_TEXT, mTextLayout, block.getTextContent(), block.getTextBlockStyles(getApplicationContext()));
                    break;

                case RoverConstants.VIEW_BLOCK_TYPE_IMAGE:
                    blockLayout = mImageBlockLayout;
                    mBlocksScrollLayout.addView(mImageBlockLayout);
                    mImageBlockLayout.setBackgroundColor(backgroundColor);
                    PicassoUtils.loadBackgroundImage(mImageBlockBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    UiUtils.setBorder(mImageBlockBorder, border);
                    PicassoUtils.loadBlockImage(getApplicationContext(), mImage, block);
                    UiUtils.setPadding(mImage, padding, border);
                    break;

                case RoverConstants.VIEW_BLOCK_TYPE_BUTTON:
                    if(mDetailView.isButtonBlockLast()) {
                        blockLayout = mStickyButtonBlockLayout;
                        mStickyButtonBlockLayout.setBackgroundColor(backgroundColor);
                        PicassoUtils.loadBackgroundImage(mStickyButtonBlockBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                        UiUtils.setBorder(mStickyButtonBlockBorder, border);
                        UiUtils.setText(RoverConstants.VIEW_BLOCK_TYPE_BUTTON, mStickyButton, block.getButtonLabel(), block.getLabelTextStyle(getApplicationContext()));
                        UiUtils.setPadding(mStickyButton, padding, border);
                        mStickyButtonBlockLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        blockLayout = mButtonBlockLayout;
                        mBlocksScrollLayout.addView(mButtonBlockLayout);
                        mButtonBlockLayout.setBackgroundColor(backgroundColor);
                        PicassoUtils.loadBackgroundImage(mButtonBlockBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                        UiUtils.setBorder(mButtonBlockBorder, border);
                        UiUtils.setText(RoverConstants.VIEW_BLOCK_TYPE_BUTTON, mButton, block.getButtonLabel(), block.getLabelTextStyle(getApplicationContext()));
                        UiUtils.setPadding(mButton, padding, border);
                    }
                    break;

                /*
                case RoverConstants.VIEW_BLOCK_TYPE_BARCODE:
                    holder.cardContentLayout.addView(holder.cardBarcodeLayout);
                    holder.cardBarcodeLayout.setBackgroundColor(backgroundColor);
                    setBackgroundImage(holder.cardBarcodeBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    setBorder(holder.cardBarcodeBorder, border);
                    //TODO: Remove after testing
                    mPicasso.load(R.drawable.barcode).fit().into(holder.cardBarcode);
                    holder.cardBarcode.setScaleType(ImageView.ScaleType.FIT_XY);
                    setPadding(holder.cardBarcode, padding, border);
                    break;
                    */
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
}
