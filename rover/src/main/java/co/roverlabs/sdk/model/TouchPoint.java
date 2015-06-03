package co.roverlabs.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class TouchPoint extends Object {
    
    //JSON members
    @SerializedName("title") private String mTitle;
    @SerializedName("notification") private String mNotification;
    @SerializedName("trigger") private String mTrigger;
    @SerializedName("minorNumber") private Integer mMinor;
    @SerializedName("cards") private List<Card> mCards;

    //Constructor
    public TouchPoint() { mObjectName = "touchpoint"; }
    
    //Getters
    public String getTitle() { return mTitle; }
    public String getNotification() { return mNotification; }
    public String getTrigger() { return mTrigger; }
    public Integer getMinor() { return mMinor; }
    public List<Card> getCards() { return mCards; }
    
    //Setters
    public void setTitle(String title) { mTitle = title; }
    public void setNotification(String notification) { mNotification = notification; }
    public void setTrigger(String trigger) { mTrigger = trigger; }
    public void setMinor(Integer minorNumber) { mMinor = minorNumber; }
    public void setCards(List<Card> cards) { mCards = cards; }

    @Override
    public boolean equals(java.lang.Object o) {

        if(this == o) {
            return true;
        }
        if(o == null) {
            return false;
        }
        if(getClass() != o.getClass()) {
            return false;
        }

        TouchPoint touchpoint = (TouchPoint)o;

        if(mId != null ? !mId.equals(touchpoint.getId()) : touchpoint.getId() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        return ((mId == null) ? 0 : mId.hashCode());
    }
}
