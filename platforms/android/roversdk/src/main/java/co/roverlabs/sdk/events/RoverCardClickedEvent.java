package co.roverlabs.sdk.events;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * Created by SherryYang on 2015-05-19.
 */
public class RoverCardClickedEvent extends RoverEvent {

    //JSON member
    @SerializedName("card") private String mCardId;
    @SerializedName("url") private String mUrl;

    //Local members
    public static final String TAG = RoverCardClickedEvent.class.getSimpleName();

    public RoverCardClickedEvent(String id, String cardId, String url) {

        mId = id;
        mObjectName = "card";
        mAction = "click";
        mTimeStamp = Calendar.getInstance().getTime();
        mCardId = cardId;
        mUrl = url;
    }

    public String getCardId() { return mCardId; }
    public String getCardUrl() { return mUrl; }
}
