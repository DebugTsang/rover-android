package co.roverlabs.sdk.events;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

import co.roverlabs.sdk.models.RoverCard;

/**
 * Created by SherryYang on 2015-05-19.
 */
public class RoverCardDeliveredEvent extends RoverEvent {

    //JSON member
    @SerializedName("card") private String mCardId;

    //Local members
    public static final String TAG = RoverCardDeliveredEvent.class.getSimpleName();
    private RoverCard mCard;

    public RoverCardDeliveredEvent(String id, RoverCard card) {

        mId = id;
        mObjectName = "card";
        mAction = "deliver";
        mTimeStamp = Calendar.getInstance().getTime();
        mCardId = card.getId();
        mCard = card;
    }

    public RoverCard getCard() { return mCard; }

    public String getCardId() { return mCardId; }
}
