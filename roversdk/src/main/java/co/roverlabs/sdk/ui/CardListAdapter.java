package co.roverlabs.sdk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.events.RoverCardClickedEvent;
import co.roverlabs.sdk.events.RoverCardViewedEvent;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.model.RoverBlock;
import co.roverlabs.sdk.model.RoverCard;
import co.roverlabs.sdk.model.RoverView;
import co.roverlabs.sdk.ui.activities.CardDetailActivity;
import co.roverlabs.sdk.ui.activities.CardListActivity;
import co.roverlabs.sdk.ui.widgets.Border;
import co.roverlabs.sdk.ui.widgets.BorderedView;
import co.roverlabs.sdk.ui.widgets.BoxModelDimens;
import co.roverlabs.sdk.utilities.Factory;
import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-03-03.
 */
public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {

    public static final String TAG = CardListAdapter.class.getSimpleName();
    private String mVisitId;
    private List<RoverCard> mCards;
    private Context mContext;
    private Activity mActivity;
    private ImageLoader mImageLoader;

    public CardListAdapter(String visitId, List<RoverCard> cards, Activity activity) {

        mVisitId = visitId;
        mCards = cards;
        mContext = activity.getApplicationContext();
        mActivity = activity;
        mImageLoader = Factory.getDefaultImageLoader(mContext);
    }

    @Override
    public int getItemCount() {

        return mCards.size();
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final RoverCard card = mCards.get(position);
        final RoverView listView = card.getListView();

        if(!card.hasBeenViewed()) {
            RoverEventBus.getInstance().post(new RoverCardViewedEvent(mVisitId, card.getId()));
        }

        card.setViewed(true);

        //Set margins
        BoxModelDimens margin = listView.getMargin(mContext);
        final CardView.LayoutParams cardLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        cardLayoutParams.setMargins(margin.left, margin.top, margin.right, margin.bottom);
        holder.cardLayout.setLayoutParams(cardLayoutParams);

        //Set border radius
        holder.cardLayout.setRadius(listView.getBorderRadius(mContext));

        //Set elevation
        holder.cardLayout.setCardElevation(0);

        //Set background color
        holder.cardLayout.setCardBackgroundColor(listView.getBackgroundColor());

        //Arrange blocks
        holder.cardContentLayout.removeAllViews();

        List<RoverBlock> blocks = listView.getBlocks();
        for (RoverBlock block : blocks) {

            BoxModelDimens padding = block.getPadding(mContext);
            Border border = block.getBorder(mContext);
            int backgroundColor = block.getBackgroundColor();
            String blockBackgroundImageUrl = block.getBackgroundImageUrl();
            String blockBackgroundImageMode = block.getmBackgroundContentMode();

            FrameLayout blockLayout = null;

            switch (block.getType()) {

                case RoverConstants.VIEW_BLOCK_TYPE_TEXT:
                    blockLayout = holder.cardTextLayout;
                    holder.cardContentLayout.addView(holder.cardTextLayout);
                    holder.cardTextLayout.setBackgroundColor(backgroundColor);
                    mImageLoader.loadBackgroundImage(holder.cardTextBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    UiUtils.setBorder(holder.cardTextBorder, border);
                    UiUtils.setPadding(holder.cardTextContentLayout, padding, border);
                    UiUtils.setText(RoverConstants.VIEW_BLOCK_TYPE_TEXT, holder.cardTextContentLayout, block.getTextContent(), block.getTextBlockStyles(mContext));
                    break;

                case RoverConstants.VIEW_BLOCK_TYPE_IMAGE:
                    blockLayout = holder.cardImageLayout;
                    holder.cardContentLayout.addView(holder.cardImageLayout);
                    holder.cardImageLayout.setBackgroundColor(backgroundColor);
                    mImageLoader.loadBackgroundImage(holder.cardImageBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    UiUtils.setBorder(holder.cardImageBorder, border);
                    mImageLoader.loadBlockImage(holder.cardImage, block);
                    UiUtils.setPadding(holder.cardImage, padding, border);
                    break;

                case RoverConstants.VIEW_BLOCK_TYPE_BUTTON:
                    blockLayout = holder.cardButtonLayout;
                    holder.cardContentLayout.addView(holder.cardButtonLayout);
                    holder.cardButtonLayout.setBackgroundColor(backgroundColor);

                    UiUtils.setText(RoverConstants.VIEW_BLOCK_TYPE_BUTTON, holder.cardButton, block.getButtonLabel(), block.getLabelTextStyle(mContext));
                    UiUtils.setBorder(holder.cardButtonBorder, border);
                    UiUtils.setPadding(holder.cardButton, padding, border);
                    mImageLoader.loadBackgroundImage(holder.cardButtonBackground, blockBackgroundImageUrl, blockBackgroundImageMode);

                    holder.cardButtonBackground.post(new Runnable() {
                        @Override public void run() {
                            int height = holder.cardButton.getHeight();
                            int width = holder.cardButton.getWidth();
                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
                            holder.cardButtonBackground.setLayoutParams(layoutParams);
                        }
                    });

                    break;

                case RoverConstants.VIEW_BLOCK_TYPE_BARCODE:
                    blockLayout = holder.cardBarcodeLayout;
                    holder.cardContentLayout.addView(holder.cardBarcodeLayout);
                    holder.cardBarcodeLayout.setBackgroundColor(backgroundColor);
                    mImageLoader.loadBackgroundImage(holder.cardBarcodeBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    UiUtils.setBorder(holder.cardBarcodeBorder, border);
                    TextStyle barcodeLabelStyle = new TextStyle();
                    if(block.getBarcodeFormat().equals(RoverConstants.BARCODE_FORMAT_PLU)) {
                        barcodeLabelStyle = block.getPluTextStyle(mContext);
                        holder.cardBarcode128.setVisibility(View.GONE);
                    }
                    else if(block.getBarcodeFormat().equals(RoverConstants.BARCODE_FORMAT_128)) {
                        barcodeLabelStyle = block.getLabelTextStyle(mContext);
                        int barcodeWidth = UiUtils.getDeviceWidthInPx(mContext);
                        int barcodeHeight = UiUtils.convertDpToPx(mContext, RoverConstants.BARCODE_HEIGHT);
                        int barcodeColor = Color.BLACK;
                        String barcodeContent = block.getBarcodeString();
                        holder.cardBarcode128.setImageBitmap(UiUtils.generateBarcode(barcodeContent, barcodeWidth, barcodeHeight, barcodeColor));
                        holder.cardBarcode128.setScaleType(ImageView.ScaleType.FIT_XY);
                        holder.cardBarcode128.setVisibility(View.VISIBLE);
                    }
                    UiUtils.setText(RoverConstants.VIEW_BLOCK_TYPE_BARCODE, holder.cardBarcodeLabel, block.getBarcodeLabel(), barcodeLabelStyle);
                    UiUtils.setPadding(holder.cardBarcodeContentLayout, padding, border);
                    break;
            }

            final String blockUrl = block.getUrl();

            if(blockLayout != null && blockUrl != null) {

                blockLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        RoverEventBus.getInstance().post(new RoverCardClickedEvent(mVisitId, card.getId(), blockUrl));

                        Uri blockUri = Uri.parse(blockUrl);
                        String blockScheme = blockUri.getScheme();
                        String blockUriString = blockUri.toString();
                        Intent intent = new Intent();

                        if (blockScheme != null) {
                            if(blockScheme.equals(RoverConstants.URL_SCHEME_ROVER)) {
                                ((CardListActivity)mActivity).setShouldStartService(false);

                                RoverView detailView = card.getDetailView(blockUriString.substring(blockUriString.lastIndexOf("/") + 1));
                                intent.setClass(mContext, CardDetailActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(RoverConstants.VIEW_TYPE_DETAIL, new Gson().toJson(detailView));
                                mContext.startActivity(intent);
                            }
                            else {
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setData(blockUri);
                                mContext.startActivity(intent);
                            }
                        }
                    }
                });
            }
        }

        //Set background image if there is one
        final String cardBackgroundImageUrl = listView.getBackgroundImageUrl();
        if(cardBackgroundImageUrl != null) {
            holder.cardBackground.post(new Runnable() {
                @Override
                public void run() {
                    int height = holder.cardLayout.getHeight();
                    int width = holder.cardLayout.getWidth();
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
                    holder.cardBackground.setLayoutParams(layoutParams);
                    mImageLoader.loadBackgroundImage(holder.cardBackground, cardBackgroundImageUrl, listView.getBackgroundContentMode());
                }
            });
            holder.cardBackground.setVisibility(View.VISIBLE);
        }
        else {
            holder.cardBackground.setVisibility(View.GONE);
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new CardViewHolder(itemView);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        //Card
        protected CardView cardLayout;
        protected ImageView cardBackground;
        protected LinearLayout cardContentLayout;
        //Image block
        protected FrameLayout cardImageLayout;
        protected ImageView cardImageBackground;
        protected BorderedView cardImageBorder;
        protected ImageView cardImage;
        //Text block
        protected FrameLayout cardTextLayout;
        protected ImageView cardTextBackground;
        protected BorderedView cardTextBorder;
        protected LinearLayout cardTextContentLayout;
        //Button block
        protected FrameLayout cardButtonLayout;
        protected ImageView cardButtonBackground;
        protected BorderedView cardButtonBorder;
        protected TextView cardButton;
        //Barcode block
        protected FrameLayout cardBarcodeLayout;
        protected ImageView cardBarcodeBackground;
        protected BorderedView cardBarcodeBorder;
        protected LinearLayout cardBarcodeContentLayout;
        protected ImageView cardBarcode128;
        protected TextView cardBarcodeLabel;

        public CardViewHolder(View view) {

            super(view);
            //Card
            cardLayout = (CardView)view.findViewById(R.id.card_list);
            cardBackground = (ImageView)view.findViewById(R.id.list_background);
            cardContentLayout = (LinearLayout)view.findViewById(R.id.list_blocks_layout);
            //Image block
            cardImageLayout = (FrameLayout)view.findViewById(R.id.list_image_block_layout);
            cardImageBackground = (ImageView)view.findViewById(R.id.list_image_block_background);
            cardImageBorder = (BorderedView)view.findViewById(R.id.list_image_block_border);
            cardImage = (ImageView)view.findViewById(R.id.list_image_block_image);
            //Text block
            cardTextLayout = (FrameLayout)view.findViewById(R.id.list_text_block_layout);
            cardTextBackground = (ImageView)view.findViewById(R.id.list_text_block_background);
            cardTextBorder = (BorderedView)view.findViewById(R.id.list_text_block_border);
            cardTextContentLayout = (LinearLayout)view.findViewById(R.id.list_text_block_text_layout);
            //Button block
            cardButtonLayout = (FrameLayout)view.findViewById(R.id.list_button_block_layout);
            cardButtonBackground = (ImageView)view.findViewById(R.id.list_button_block_background);
            cardButtonBorder = (BorderedView)view.findViewById(R.id.list_button_block_border);
            cardButton = (TextView)view.findViewById(R.id.list_button_block_button);
            //Barcode block
            cardBarcodeLayout = (FrameLayout)view.findViewById(R.id.list_barcode_block_layout);
            cardBarcodeBackground = (ImageView)view.findViewById(R.id.list_barcode_block_background);
            cardBarcodeBorder = (BorderedView)view.findViewById(R.id.list_barcode_block_border);
            cardBarcodeContentLayout = (LinearLayout)view.findViewById(R.id.list_barcode_block_barcode_layout);
            cardBarcode128 = (ImageView)view.findViewById(R.id.list_barcode_block_barcode_128);
            cardBarcodeLabel = (TextView)view.findViewById(R.id.list_barcode_block_barcode_label);
        }
    }
}

//        int fontSize3 = 14;
//        int fontWeight3 = Typeface.NORMAL;
//        int textAlign3 = View.TEXT_ALIGNMENT_VIEW_START;
//        int fontColor3 = Color.argb(1 * 255, 53, 107, 232);
//        int marginTop3 = UiUtils.convertDpToPx(mContext, 0);
//        int marginRight3 = UiUtils.convertDpToPx(mContext, 0);
//        int marginBottom3 = UiUtils.convertDpToPx(mContext, 10);
//        int marginLeft3 = UiUtils.convertDpToPx(mContext, 0);
//        int lineHeight = UiUtils.convertDpToPx(mContext, 20);
//
//        textView.setText(Html.fromHtml(text));
//        textView.setTextSize(fontSize3);
//        textView.setTypeface(Typeface.DEFAULT, fontWeight3);
//        textView.setTextAlignment(textAlign3);
//        textView.setTextColor(fontColor3);
//        //textView.setPadding(0, 0, 0, 0);
//        textView.setLineSpacing(lineHeight - textView.getLineHeight(), 1);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(marginLeft3, marginTop3, marginRight3, marginBottom3);
//        textView.setLayoutParams(layoutParams);


//    @Override
//    public void onBindViewHolder(CardViewHolder holder, int position) {
//
//        final int i = position;
//        RoverCard card = mCards.get(position);
//        RoverView view = card.getListView();
//        holder.vCardView.setCardBackgroundColor(Color.CYAN);
//        holder.vCardTitle.setText(card.getTitle());
//        int leftMargin = RoverUtils.convertDpToPx(mContext, view.getLeftMargin());
//        int topMargin = RoverUtils.convertDpToPx(mContext, view.getTopMargin());
//        int rightMargin = RoverUtils.convertDpToPx(mContext, view.getRightMargin());
//        int bottomMargin = RoverUtils.convertDpToPx(mContext, view.getBottomMargin());
//        CardView.LayoutParams cardLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2500);
//        cardLayoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
//        holder.vCardView.setLayoutParams(cardLayoutParams);
//        holder.vCardView.setRadius(RoverUtils.convertDpToPx(mContext, view.getBorderRadius()));
//        //holder.vCardImage. setImageResource(card.getImageResourceId());
//        //holder.vCardMessage.setText("This is card ID " + card.getId());
//        holder.vCardDismissButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCards.remove(i);
//                notifyDataSetChanged();
//                if(mCards.isEmpty()) {
//                    ((Activity)mContext).finish();
//                }
//            }
//        });
//    }

