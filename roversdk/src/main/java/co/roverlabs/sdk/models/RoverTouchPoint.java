package co.roverlabs.sdk.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverTouchPoint extends RoverObject {
    
    //JSON members
    @SerializedName("title") private String mTitle;
    @SerializedName("notification") private String mNotification;
    @SerializedName("minorNumber") private int mMinorNumber;
    @SerializedName("cards") private List<RoverCard> mCards;
    
    //Constructor
    public RoverTouchPoint(Context con) {
        
        super(con);
        mObjectName = "touchpoint"; 
    }
    
    //Getters
    public String getTitle() { return mTitle; }
    public String getNotification() { return mNotification; }
    public int getMinorNumber() { return mMinorNumber; }
    public List<RoverCard> getCards() { return mCards; }
    
    //Setters
    public void setTitle(String title) { mTitle = title; }
    public void setNotification(String notification) { mNotification = notification; }
    public void setMinorNumber(int minorNumber) { mMinorNumber = minorNumber; }
    public void setCards(List<RoverCard> cards) { mCards = cards; }
    
    /*
    boolean isInRegion(region) {
        if this.minor == region.minor return true;
        else false;
    }
     */
}
