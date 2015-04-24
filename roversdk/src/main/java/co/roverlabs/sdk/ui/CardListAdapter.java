package co.roverlabs.sdk.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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

        RoverCard card = mCards.get(position);
        RoverView view = card.getListView();

        //Set margins
        int leftMargin = RoverUtils.convertDpToPx(mContext, view.getLeftMargin());
        int topMargin = RoverUtils.convertDpToPx(mContext, view.getTopMargin());
        int rightMargin = RoverUtils.convertDpToPx(mContext, view.getRightMargin());
        int bottomMargin = RoverUtils.convertDpToPx(mContext, view.getBottomMargin());
        CardView.LayoutParams cardLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2500);
        cardLayoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        holder.cardView.setLayoutParams(cardLayoutParams);

        //Set background color
        int alphaBackgroundValue = view.getAlphaBackgroundValue();
        int redBackgroundValue = view.getRedBackgroundValue();
        int greenBackgroundValue = view.getGreenBackgroundValue();
        int blueBackgroundValue = view.getBlueBackgroundValue();
        int backgroundColor = Color.argb(alphaBackgroundValue, redBackgroundValue, greenBackgroundValue, blueBackgroundValue);
        holder.cardView.setCardBackgroundColor(backgroundColor);

        //Set border radius
        int borderRadius = RoverUtils.convertDpToPx(mContext, view.getBorderRadius());
        holder.cardView.setRadius(borderRadius);

        //Set elevation
        holder.cardView.setCardElevation(0);

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
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new CardViewHolder(itemView);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        protected CardView cardView;

        public CardViewHolder(View view) {

            super(view);
            cardView = (CardView) view.findViewById(R.id.single_card_view);
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
