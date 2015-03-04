package co.roverlabs.sdk.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.models.RoverCard;

/**
 * Created by SherryYang on 2015-03-03.
 */
public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {

    private List<RoverCard> mCards;
    private Context mContext;

    public CardListAdapter(List<RoverCard> cards, Context con) { 
        
        mCards = cards;
        mContext = con;
    }

    @Override
    public int getItemCount() { return mCards.size(); }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        final int i = position;
        RoverCard card = mCards.get(position);
        holder.vCardTitle.setText(card.getTitle());
        holder.vCardImage. setImageResource(card.getImageResourceId());
        holder.vCardMessage.setText("This is card ID " + card.getId());
        holder.vCardDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCards.remove(i);
                notifyDataSetChanged();
                if(mCards.isEmpty()) {
                    ((Activity)mContext).finish();
                }
            }
        });
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card, parent, false);
        return new CardViewHolder(itemView);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        protected TextView vCardTitle;
        protected ImageView vCardImage;
        protected TextView vCardMessage;
        protected Button vCardDismissButton;

        public CardViewHolder(View v) {
            
            super(v);
            vCardTitle = (TextView)v.findViewById(R.id.cardTitleTest);
            vCardImage = (ImageView)v.findViewById(R.id.cardImageTest);
            vCardMessage = (TextView)v.findViewById(R.id.cardMessageTest);
            vCardDismissButton = (Button)v.findViewById(R.id.cardDismissButtonTest);
        }
    }
}
