package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverTouchpoint extends RoverObject {
    
    //JSON members
    @SerializedName("title") private String mTitle;
    @SerializedName("notification") private String mNotification;
    @SerializedName("trigger") private String mTrigger;
    @SerializedName("minorNumber") private Integer mMinor;
    @SerializedName("cards") private List<RoverCard> mCards;

    //Constructor
    public RoverTouchpoint() { mObjectName = "touchpoint"; }
    
    //Getters
    public String getTitle() { return mTitle; }
    public String getNotification() { return mNotification; }
    public String getTrigger() { return mTrigger; }
    public Integer getMinor() { return mMinor; }
    public List<RoverCard> getCards() { return mCards; }
    
    //Setters
    public void setTitle(String title) { mTitle = title; }
    public void setNotification(String notification) { mNotification = notification; }
    public void setTrigger(String trigger) { mTrigger = trigger; }
    public void setMinor(Integer minorNumber) { mMinor = minorNumber; }
    public void setCards(List<RoverCard> cards) { mCards = cards; }

    @Override
    public boolean equals(Object o) {

        if(this == o) {
            return true;
        }
        if(o == null) {
            return false;
        }
        if(getClass() != o.getClass()) {
            return false;
        }

        RoverTouchpoint touchpoint = (RoverTouchpoint)o;

        if(mMinor != null ? !mMinor.equals(touchpoint.getMinor()) : touchpoint.getMinor() != null) {
            return false;
        }

        return true;
    }
}
