package co.roverlabs.sdk.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.models.RoverCard;
import co.roverlabs.sdk.models.RoverView;
import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-03-03.
 */
public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {

    public static final String TAG = CardListAdapter.class.getSimpleName();
    private List<RoverCard> mCards;
    private Context mContext;

    public CardListAdapter(List<RoverCard> cards, Context con) {

        mCards = cards;
        mContext = con;
    }

    @Override
    public int getItemCount() {

        return mCards.size();
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {

        final RoverCard card = mCards.get(position);
        final RoverView view = card.getListView();

        //Set margins
        int leftMargin = RoverUtils.convertDpToPx(mContext, view.getLeftMargin());
        int topMargin = RoverUtils.convertDpToPx(mContext, view.getTopMargin());
        int rightMargin = RoverUtils.convertDpToPx(mContext, view.getRightMargin());
        final int bottomMargin = RoverUtils.convertDpToPx(mContext, view.getBottomMargin());
        //TODO: Remove hard coded height
        CardView.LayoutParams cardLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2000);
        cardLayoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        holder.cardLayout.setLayoutParams(cardLayoutParams);

        //Set border radius
        int borderRadius = RoverUtils.convertDpToPx(mContext, view.getBorderRadius());
        holder.cardLayout.setRadius(borderRadius);

        //Set elevation
        holder.cardLayout.setCardElevation(0);

        //Set background color
        int alphaBackgroundValue = view.getAlphaBackgroundValue();
        int redBackgroundValue = view.getRedBackgroundValue();
        int greenBackgroundValue = view.getGreenBackgroundValue();
        int blueBackgroundValue = view.getBlueBackgroundValue();
        int backgroundColor = Color.argb(alphaBackgroundValue, redBackgroundValue, greenBackgroundValue, blueBackgroundValue);
        holder.cardLayout.setCardBackgroundColor(backgroundColor);

        //Set background image if there is one
        String backgroundImageUrl = view.getBackgroundImageUrl();
        if(backgroundImageUrl != null) {
            holder.cardBackgroundImage.setBackground(null);
            holder.cardBackgroundImage.setImageDrawable(null);
            Picasso.with(mContext)
                    .load(backgroundImageUrl)
                    .into(new Target() {

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                            RoverUtils.setImageMode(mContext, bitmap, holder.cardBackgroundImage, view.getBackgroundContentMode());
                            holder.cardBackgroundImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                            holder.cardBackgroundImage.setImageDrawable(errorDrawable);
                            Log.d(TAG, "Card background cannot be set - card ID " + card.getId());
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                            holder.cardBackgroundImage.setImageDrawable(placeHolderDrawable);
                        }
                    });
        }
        else {
            holder.cardBackgroundImage.setVisibility(View.GONE);
        }

        //TODO: Set card text, remove after testing
        holder.cardText.setText(card.getTitle());
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new CardViewHolder(itemView);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        protected CardView cardLayout;
        protected ImageView cardBackgroundImage;
        protected TextView cardText;

        public CardViewHolder(View view) {

            super(view);
            cardLayout = (CardView)view.findViewById(R.id.card_layout);
            cardBackgroundImage = (ImageView)view.findViewById(R.id.card_background);
            cardText = (TextView)view.findViewById(R.id.card_text);
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