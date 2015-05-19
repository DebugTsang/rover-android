package co.roverlabs.sdk.events;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * Created by SherryYang on 2015-05-19.
 */
public class RoverCardDeliveredEvent extends RoverEvent {

    //JSON member
    @SerializedName("card") private String mCardId;

    //Local members
    public static final String TAG = RoverCardDeliveredEvent.class.getSimpleName();

    public RoverCardDeliveredEvent(String id, String cardId) {

        mId = id;
        mObjectName = "card";
        mAction = "deliver";
        mTimeStamp = Calendar.getInstance().getTime();
        mCardId = cardId;
    }

    public String getCardId() { return mCardId; }
}
