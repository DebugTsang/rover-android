package co.roverlabs.sdk.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        holder.setIsRecyclable(false);

        final RoverCard card = mCards.get(position);
        final RoverView listView = card.getListView();

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

        for(RoverBlock block : listView.getBlocks()) {

            BoxModelDimens padding = block.getPadding(mContext);
            Border border = block.getBorder(mContext);
            int backgroundColor = block.getBackgroundColor();
            String blockBackgroundImageUrl = block.getBackgroundImageUrl();
            String blockBackgroundImageMode = block.getmBackgroundContentMode();

            FrameLayout blockLayout = null;

            switch(block.getType()) {

                case RoverConstants.VIEW_BLOCK_TYPE_TEXT:
                    blockLayout = holder.cardTextLayout;
                    holder.cardContentLayout.addView(holder.cardTextLayout);
                    holder.cardTextLayout.setBackgroundColor(backgroundColor);
                    setBackgroundImage(holder.cardTextBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    setBorder(holder.cardTextBorder, border);
                    setPadding(holder.cardTextContentLayout, padding, border);
                    setTextBlockText(holder.cardTextContentLayout, block.getTextContent(), block.getTextBlockStyles(mContext));
                    break;

                case RoverConstants.VIEW_BLOCK_TYPE_IMAGE:
                    blockLayout = holder.cardImageLayout;
                    holder.cardContentLayout.addView(holder.cardImageLayout);
                    holder.cardImageLayout.setBackgroundColor(backgroundColor);
                    setBackgroundImage(holder.cardImageBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    setBorder(holder.cardImageBorder, border);
                    setImage(holder.cardImage, block.getImageUrl(), block.getImageWidth(), block.getImageHeight(), block.getImageOffsetRatio(), block.getImageAspectRatio());
                    setPadding(holder.cardImage, padding, border);
                    break;

                case RoverConstants.VIEW_BLOCK_TYPE_BUTTON:
                    blockLayout = holder.cardButtonLayout;
                    holder.cardContentLayout.addView(holder.cardButtonLayout);
                    holder.cardButtonLayout.setBackgroundColor(backgroundColor);
                    setBackgroundImage(holder.cardButtonBackground, blockBackgroundImageUrl, blockBackgroundImageMode);
                    setBorder(holder.cardButtonBorder, border);
                    setButtonBlockText(holder.cardButton, block.getButtonLabel(), block.getLabelTextStyle(mContext));
                    setPadding(holder.cardButton, padding, border);
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

        }

        //Set background image if there is one
        String cardBackgroundImageUrl = listView.getBackgroundImageUrl();
        if(cardBackgroundImageUrl != null) {
            loadImage(holder.cardBackground, cardBackgroundImageUrl, listView.getBackgroundContentMode());
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
            cardTextContentLayout = (LinearLayout)view.findViewById(R.id.card_text_content_layout);
            //Button block
            cardButtonLayout = (FrameLayout)view.findViewById(R.id.card_button_layout);
            cardButtonBackground = (ImageView)view.findViewById(R.id.card_button_background);
            cardButtonBorder = (BorderedView)view.findViewById(R.id.card_button_border);
            cardButton = (TextView)view.findViewById(R.id.card_button);
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
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                break;

            //TODO: Tile mode
            //case RoverConstants.IMAGE_MODE_TILE:
            //    break;

            case RoverConstants.IMAGE_MODE_FILL:
                mPicasso.load(imageUrl).fit().centerCrop().into(imageView);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    public void setTextBlockText(LinearLayout layout, String text, List<TextStyle> styles) {

        layout.removeAllViews();

        Document textContent = Jsoup.parseBodyFragment(text);

        for(Element textElement : textContent.getAllElements()) {

            if(textElement.tag().equals(Tag.valueOf(RoverConstants.TEXT_H1))) {
                TextView textView = new TextView(mContext);
                layout.addView(textView);
                //TODO: Write helper function for this
                String original = textElement.toString();
                Matcher matcher = Pattern.compile("<h1>(.*?)</h1>").matcher(original);
                if(matcher.find()) {
                    setTextFormat(RoverConstants.VIEW_BLOCK_TYPE_TEXT, textView, matcher.group(1), styles.get(0));
                }
            }

            if(textElement.tag().equals(Tag.valueOf(RoverConstants.TEXT_H2))) {
                TextView textView = new TextView(mContext);
                layout.addView(textView);
                //TODO: Write helper function for this
                String original = textElement.toString();
                Matcher matcher = Pattern.compile("<h2>(.*?)</h2>").matcher(original);
                if(matcher.find()) {
                    setTextFormat(RoverConstants.VIEW_BLOCK_TYPE_TEXT, textView, matcher.group(1), styles.get(1));
                }
            }

            if(textElement.tag().equals(Tag.valueOf(RoverConstants.TEXT_P))) {
                TextView textView = new TextView(mContext);
                layout.addView(textView);
                //TODO: Write helper function for this
                String original = textElement.toString();
                Matcher matcher = Pattern.compile("<p>(.*?)</p>").matcher(original);
                if(matcher.find()) {
                    setTextFormat(RoverConstants.VIEW_BLOCK_TYPE_TEXT, textView, matcher.group(1), styles.get(2));
                }
            }
        }
    }

    public void setButtonBlockText(TextView textView, String text, TextStyle style) {

        Document textContent = Jsoup.parseBodyFragment(text);

        for (Element textElement : textContent.getAllElements()) {

            if(textElement.tag().equals(Tag.valueOf(RoverConstants.TEXT_DIV))) {
                //TODO: Write helper function for this
                String original = textElement.toString();
                Matcher matcher = Pattern.compile("<div>(.*?)</div>", Pattern.DOTALL).matcher(original);
                if(matcher.find()) {
                    setTextFormat(RoverConstants.VIEW_BLOCK_TYPE_BUTTON, textView, matcher.group(1), style);
                }
            }
        }
    }

    public void setTextFormat(String blockType, TextView textView, String text, TextStyle style) {

        textView.setText(Html.fromHtml(text));
        textView.setTextSize(style.size);

        if(style.type.equals(RoverConstants.TEXT_H1) || style.type.equals(RoverConstants.TEXT_H2)) {
            textView.setTypeface(Typeface.DEFAULT_BOLD);
        }
        else {
            textView.setTypeface(Typeface.DEFAULT);
        }

        if(style.align.equals(RoverConstants.TEXT_ALIGN_CENTER)) {
            textView.setGravity(Gravity.CENTER);
        }
        else if(style.align.equals(RoverConstants.TEXT_ALIGN_RIGHT)) {
            textView.setGravity(Gravity.RIGHT);
        }
        else {
            textView.setGravity(Gravity.LEFT);
        }

        textView.setTextColor(style.color);
        textView.setLineSpacing(style.lineHeight - textView.getLineHeight(), 1);

        if(blockType.equals(RoverConstants.VIEW_BLOCK_TYPE_BUTTON)) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(style.margin.left, style.margin.top, style.margin.right, style.margin.bottom);
            textView.setLayoutParams(layoutParams);
        }
        else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(style.margin.left, style.margin.top, style.margin.right, style.margin.bottom);
            textView.setLayoutParams(layoutParams);
        }
    }

    public void setImage(ImageView imageView, String url, Integer width, Integer height, Float offsetRatio, Float aspectRatio) {

        int deviceWidth = ImageUtils.getDeviceWidth(mContext);

        if(width != null && height != null) {
            url += "?w=" + deviceWidth + "&rect=0," + (int)((-offsetRatio) * height) + "," + width + ","+ (int)(width / aspectRatio);
        }
        else {
            url += "?w=" + deviceWidth + "&h" + (int)(deviceWidth / aspectRatio);
        }

        mPicasso.load(url).into(imageView);
    }
}

//        int fontSize3 = 14;
//        int fontWeight3 = Typeface.NORMAL;
//        int textAlign3 = View.TEXT_ALIGNMENT_VIEW_START;
//        int fontColor3 = Color.argb(1 * 255, 53, 107, 232);
//        int marginTop3 = ImageUtils.convertDpToPx(mContext, 0);
//        int marginRight3 = ImageUtils.convertDpToPx(mContext, 0);
//        int marginBottom3 = ImageUtils.convertDpToPx(mContext, 10);
//        int marginLeft3 = ImageUtils.convertDpToPx(mContext, 0);
//        int lineHeight = ImageUtils.convertDpToPx(mContext, 20);
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

