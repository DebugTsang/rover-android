package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverCard {

    @SerializedName("title")
    private String mTitle;

    public String getTitle() {

        return mTitle;
    }

    public void setTitle(String title) {

        mTitle = title;
    }
}
/*            "detailView":{  },
            "id":"54dc0ab206489ada572dc472",
            "listView":{  },
            "meta":{  },
            "title":"Back to School"*/
