package co.roverlabs.sdk.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.models.RoverBlock;
import co.roverlabs.sdk.models.RoverCard;
import co.roverlabs.sdk.models.RoverView;
import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-03-03.
 */
public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {

    public static final String TAG = CardListAdapter.class.getSimpleName();
    private List<RoverCard> mCards;
    private Context mContext;
    private Picasso mPicasso;

    public CardListAdapter(List<RoverCard> cards, Context con) {

        mCards = cards;
        mContext = con.getApplicationContext();
        mPicasso = Picasso.with(mContext);
        mPicasso.setLoggingEnabled(true);
    }

    @Override
    public int getItemCount() {

        return mCards.size();
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {

        RoverCard card = mCards.get(position);
        RoverView listView = card.getListView();

        //Set margins
        BoxModelDimens margin = listView.getMargin(mContext);
        CardView.LayoutParams cardLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ImageUtils.convertDpToPx(mContext, 600));
        cardLayoutParams.setMargins(margin.left, margin.top, margin.right, margin.bottom);
        holder.cardLayout.setLayoutParams(cardLayoutParams);

        //Set border radius
        holder.cardLayout.setRadius(listView.getBorderRadius(mContext));

        //Set elevation
        holder.cardLayout.setCardElevation(0);

        //Set background color
        holder.cardLayout.setCardBackgroundColor(listView.getBackgroundColor());

        //Set background image if there is one
        String cardBackgroundImageUrl = listView.getBackgroundImageUrl();
        if(cardBackgroundImageUrl != null) {
            holder.cardBackground.setBackground(null);
            holder.cardBackground.setImageDrawable(null);
            holder.cardBackground.setImageBitmap(null);
            loadImage(holder.cardBackground, cardBackgroundImageUrl, listView.getBackgroundContentMode());
            holder.cardBackground.setVisibility(View.VISIBLE);
        }
        else {
            holder.cardBackground.setVisibility(View.GONE);
        }

        //Arrange blocks
        holder.cardContentLayout.removeAllViews();

        for(RoverBlock block : listView.getBlocks()) {

            BoxModelDimens padding = block.getPadding(mContext);
            Border border = block.getBorder(mContext);
            int backgroundColor = block.getBackgroundColor();
            String blockBackgroundImageUrl = block.getBackgroundImageUrl();
            String blockBackgroundImageMode = block.getmBackgroundContentMode();

            switch(block.getType()) {

                case RoverConstants.VIEW_BLOCK_TYPE_TEXT:
                    holder.cardContentLayout.addView(holder.cardTextLayout);
                    holder.cardTextLayout.setBackgroundColor(backgroundColor);
                    setBackgroundImage(holder.cardTextBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    setBorder(holder.cardTextBorder, border);
                    setPadding(holder.cardText, padding, border);
                    //TODO: Remove after testing
                    holder.cardText.setText(block.getTextContent());
//                    ViewTreeObserver vto = holder.cardText.getViewTreeObserver();
//                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                        public boolean onPreDraw() {
//                            holder.cardText.getViewTreeObserver().removeOnPreDrawListener(this);
//                            int finalHeight = holder.cardText.getMeasuredHeight();
//                            int finalWidth = holder.cardText.getMeasuredWidth();
//                            Log.d(TAG, "Height: " + finalHeight + " Width: " + finalWidth);
//                            return true;
//                        }
//                    });
                    break;

                case RoverConstants.VIEW_BLOCK_TYPE_IMAGE:
                    holder.cardContentLayout.addView(holder.cardImageLayout);
                    holder.cardImageLayout.setBackgroundColor(backgroundColor);
                    setBackgroundImage(holder.cardImageBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    setBorder(holder.cardImageBorder, border);
                    //TODO: Remove after testing
                    mPicasso.load(R.drawable.test).fit().into(holder.cardImage);
                    holder.cardImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    setPadding(holder.cardImage, padding, border);
                    break;

                case RoverConstants.VIEW_BLOCK_TYPE_BUTTON:
                    holder.cardContentLayout.addView(holder.cardButtonLayout);
                    holder.cardButtonLayout.setBackgroundColor(backgroundColor);
                    setBackgroundImage(holder.cardButtonBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    setBorder(holder.cardButtonBorder, border);
                    //TODO: Remove after testing
                    mPicasso.load(R.drawable.button).fit().into(holder.cardButton);
                    holder.cardButton.setScaleType(ImageView.ScaleType.FIT_XY);
                    setPadding(holder.cardButton, padding, border);
                    break;

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
            }
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
        protected TextView cardText;
        //Button block
        protected FrameLayout cardButtonLayout;
        protected ImageView cardButtonBackground;
        protected BorderedView cardButtonBorder;
        protected ImageView cardButton;
        //Barcode block
        protected FrameLayout cardBarcodeLayout;
        protected ImageView cardBarcodeBackground;
        protected BorderedView cardBarcodeBorder;
        protected ImageView cardBarcode;

        public CardViewHolder(View view) {

            super(view);
            //Card
            cardLayout = (CardView)view.findViewById(R.id.card_layout);
            cardBackground = (ImageView)view.findViewById(R.id.card_background);
            cardContentLayout = (LinearLayout)view.findViewById(R.id.card_content_layout);
            //Image block
            cardImageLayout = (FrameLayout)view.findViewById(R.id.card_image_layout);
            cardImageBackground = (ImageView)view.findViewById(R.id.card_image_background);
            cardImageBorder = (BorderedView)view.findViewById(R.id.card_image_border);
            cardImage = (ImageView)view.findViewById(R.id.card_image);
            //Text block
            cardTextLayout = (FrameLayout)view.findViewById(R.id.card_text_layout);
            cardTextBackground = (ImageView)view.findViewById(R.id.card_text_background);
            cardTextBorder = (BorderedView)view.findViewById(R.id.card_text_border);
            cardText = (TextView)view.findViewById(R.id.card_text);
            //Button block
            cardButtonLayout = (FrameLayout)view.findViewById(R.id.card_button_layout);
            cardButtonBackground = (ImageView)view.findViewById(R.id.card_button_background);
            cardButtonBorder = (BorderedView)view.findViewById(R.id.card_button_border);
            cardButton = (ImageView)view.findViewById(R.id.card_button);
            //Barcode block
            cardBarcodeLayout = (FrameLayout)view.findViewById(R.id.card_barcode_layout);
            cardBarcodeBackground = (ImageView)view.findViewById(R.id.card_barcode_background);
            cardBarcodeBorder = (BorderedView)view.findViewById(R.id.card_barcode_border);
            cardBarcode = (ImageView)view.findViewById(R.id.card_barcode);
        }
    }

    public void setBackgroundImage(ImageView imageView, String imageUrl, String imageMode) {

        if(imageUrl != null) {
            imageView.setBackground(null);
            imageView.setImageDrawable(null);
            imageView.setImageBitmap(null);
            loadImage(imageView, imageUrl, imageMode);
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            imageView.setVisibility(View.GONE);
        }
    }

    public void setBorder(BorderedView borderView, Border border) {

        if(border.hasBorder()) {
            borderView.setBorder(border);
            borderView.setVisibility(View.VISIBLE);
        }
        else {
            borderView.setVisibility(View.GONE);
        }
    }

    public void setPadding(View view, BoxModelDimens padding, Border border) {

        if(border.hasBorder()) {
            padding.top += border.top;
            padding.right += border.right;
            padding.bottom += border.bottom;
            padding.left += border.left;
        }
        view.setPadding(padding.left, padding.top, padding.right, padding.bottom);
    }

    public void loadImage(ImageView imageView, String imageUrl, String imageMode) {

        switch(imageMode) {

            case RoverConstants.IMAGE_MODE_STRETCH:
                mPicasso.load(imageUrl).fit().into(imageView);
                break;

            //TODO: Tile mode
            //case RoverConstants.IMAGE_MODE_TILE:
            //    break;

            case RoverConstants.IMAGE_MODE_FILL:
                mPicasso.load(imageUrl).fit().centerCrop().into(imageView);
                break;

            case RoverConstants.IMAGE_MODE_FIT:
                mPicasso.load(imageUrl).fit().centerInside().into(imageView);
                break;

            //TODO: Original size
            //case RoverConstants.IMAGE_MODE_ORIGINAL:
            //    imageView.setImageDrawable(backgroundDrawable);
            //    imageView.setScaleType(ImageView.ScaleType.CENTER);

            default:
                mPicasso.load(imageUrl).fit().centerCrop().into(imageView);
        }
    }
}

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

//        RoverView.RoverBlock textBlock = view.getTextBlock();
//        if(textBlock != null) {
//            holder.webView.loadData(textBlock.getTextContent(), "text/html", "utf-8");
//            holder.webView.setVisibility(View.VISIBLE);
//        }
//        else {
//            holder.webView.setVisibility(View.GONE);
//        }

//        String text ="<style type=\"text/css\">" +
//                "h1{font-weight:normal;" +
//                "font-family: GillSans-Bold;" +
//                "font-size:22px;text-align:center;" +
//                "line-height:24px;" +
//                "min-height:24px;" +
//                "color:rgba(255,255,255,1);" +
//                "margin:0px 0px 10px 0px;}" +
//                "h2{font-weight:normal;" +
//                "font-family: GillSans;" +
//                "font-size:20px;" +
//                "text-align:center;" +
//                "line-height:22px;" +
//                "min-height:22px;" +
//                "color:rgba(255,255,255,1);" +
//                "margin:0px 0px 10px 0px;}" +
//
//                "p" +
//                //"{font-weight:normal;" +
//                //"font-family: GillSans;" + (Only certain ones are supported)
//                //"font-size:18px;" +
//                //"text-align:center;" +
//                //"line-height:20px;" +
//                //"min-height:20px;" +
//                //"color:rgba(255,255,255,0.7);" +
//                "margin:0px 0px 10px 0px;" +
//                "}</style>" +
//                "<p>They&#39;re in season and on sale: Fresh, local organic peaches.</p>";
//        String htmlText = //"<html>" +
//                //"<head>" +
//                "<style type=\"text/css\">" +
//                "p{" +
//                "margin:100px 100px 100px 100px;" +
//                "font-weight:bold;" +
//                "color:rgba(255, 84, 67, 1);" +
//                "font-family: Gill Sans;" +
//                "line-height:120px;" +
//                "text-align:center;" +
//                "font-size:20px;" +
//                "min-height:20px;" +
//                "}</style>" +
//                //"</head>" +
//                //"<body>" +
//                "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna</p>";
//                //"</body>" +
//                //"</html>";
//        holder.webView.loadData(htmlText, "text/html", "utf-8");

//        //Testing image loading
//        RoverView.RoverBlock imageBlock = view.getImageBlock();
//        if(imageBlock != null) {
//            holder.imageProgressBar.setVisibility(View.VISIBLE);
//            Picasso.with(mContext)
//                    .load(imageBlock.getImageUrl())
//                    .into(holder.cardImage, new com.squareup.picasso.Callback() {
//
//                        @Override
//                        public void onSuccess() {
//                            holder.imageProgressBar.setVisibility(View.GONE);
//                            holder.cardImage.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onError() {
//
//                        }
//                    });
//        }
//        else {
//            holder.cardImage.setVisibility(View.GONE);
//        }