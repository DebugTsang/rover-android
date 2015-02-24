package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverTouchPoint {
    
    @SerializedName("title")
    private String mTitle;
    
    @SerializedName("notification")
    private String mNotification;
    
    @SerializedName("minorNumber")
    private int mMinorNumber;
    
    @SerializedName("cards")
    private List<RoverCard> mCards;
    
    public String getTitle() {
        
        return mTitle;
    }
    
    public void setTitle(String title) {
        
        mTitle = title;
    }
    
    public String getNotification() {
        
        return mNotification;
    }
    
    public void setNotification(String notification) {
        
        mNotification = notification;
    }
    
    public int getMinorNumber() {
        
        return mMinorNumber;
    }
    
    public void setMinorNumber(int minorNumber) {
        
        mMinorNumber = minorNumber;
    }
    
    public List<RoverCard> getCards() {
        
        return mCards;
    }
    
    public void setCards(List<RoverCard> cards) {
        
        mCards = cards;
    }
}
