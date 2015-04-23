package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverCard extends RoverObject {

    //JSON members
    @SerializedName("title") private String mTitle;
    @SerializedName("views") private List<RoverView> mViews;
    
    //Constructor
    public RoverCard() { mObjectName = "card"; }

    //Getters
    public String getTitle() { return mTitle; }

    public RoverView getListView() {

        if(mViews!= null) {
            for(RoverView view : mViews) {
                if(view.getType().equals(RoverConstants.VIEW_DEF_TYPE_LIST)) {
                    return view;
                }
            }
        }
        return null;
    }
}
