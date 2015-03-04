package co.roverlabs.sdk.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverCard extends RoverObject {

    //JSON members
    @SerializedName("title") private String mTitle;
    
    //TODO: Remove these (used for testing)
    private String mMessage;
    private int mImageResourceId;
    
    //Constructor
    public RoverCard(Context con) { 
        
        super(con);
        mObjectName = "card"; 
    }

    //Getters
    public String getTitle() { return mTitle; }
    //TODO: Remove these (used for testing)
    public String getMessage() { return mMessage; }
    public int getImageResourceId() { return mImageResourceId; }

    //Setters
    public void setTitle(String title) { mTitle = title; }
    //TODO: Remove these (used for testing)
    public void setMessage(String message) { mMessage = message; }
    public void setImageResourceId(int imageResourceId) { mImageResourceId = imageResourceId; }
}
/*            "detailView":{  },
            "id":"54dc0ab206489ada572dc472",
            "listView":{  },
            "meta":{  },
            "title":"Back to School"*/
