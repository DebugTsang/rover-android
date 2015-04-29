package co.roverlabs.sdk.events;

import java.util.ArrayList;
import java.util.List;

import co.roverlabs.sdk.models.RoverCard;

/**
 * Created by SherryYang on 2015-04-29.
 */
public class RoverCardsAddedEvent {

    public static final String TAG = RoverCardsAddedEvent.class.getSimpleName();
    private List<RoverCard> mCards;

    public RoverCardsAddedEvent(List<RoverCard> cards) {

        mCards = new ArrayList<>();
        for(RoverCard card : cards) {
            mCards.add(card);
        }
    }

    public List<RoverCard> getAddedCards() { return mCards; }
}
