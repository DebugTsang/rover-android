package co.roverlabs.sdk.events;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * Created by SherryYang on 2015-05-21.
 */
public class RoverCardDiscardedEvent extends RoverEvent {

    //JSON member
    @SerializedName("card") private String mCardId;

    //Local members
    public static final String TAG = RoverCardDiscardedEvent.class.getSimpleName();

    public RoverCardDiscardedEvent(String id, String cardId) {

        mId = id;
        mObjectName = "card";
        mAction = "discard";
        mTimeStamp = Calendar.getInstance().getTime();
        mCardId = cardId;
    }

    public String getCardId() { return mCardId; }
}
