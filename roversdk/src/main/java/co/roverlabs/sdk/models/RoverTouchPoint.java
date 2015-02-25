package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverTouchPoint {
    
    @SerializedName("title") private String mTitle;
    @SerializedName("notification") private String mNotification;
    @SerializedName("minorNumber") private int mMinorNumber;
    @SerializedName("cards") private List<RoverCard> mCards;
    
    public String getTitle() { return mTitle; }
    public String getNotification() { return mNotification; }
    public int getMinorNumber() { return mMinorNumber; }
    public List<RoverCard> getCards() { return mCards; }
    
    public void setTitle(String title) { mTitle = title; }
    public void setNotification(String notification) { mNotification = notification; }
    public void setMinorNumber(int minorNumber) { mMinorNumber = minorNumber; }
    public void setCards(List<RoverCard> cards) { mCards = cards; }
}
