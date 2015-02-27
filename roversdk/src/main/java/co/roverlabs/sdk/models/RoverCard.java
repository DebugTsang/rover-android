package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverCard extends RoverObject {

    //JSON members
    @SerializedName("title") private String mTitle;
    
    //Constructor
    public RoverCard() { mObjectName = "card"; }

    //Getters
    public String getTitle() { return mTitle; }

    //Setters
    public void setTitle(String title) { mTitle = title; }
}
/*            "detailView":{  },
            "id":"54dc0ab206489ada572dc472",
            "listView":{  },
            "meta":{  },
            "title":"Back to School"*/
