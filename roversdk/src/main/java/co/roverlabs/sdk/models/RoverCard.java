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
    @SerializedName("views") private List<RoverView> mViewDefinitions;
    
    //Constructor
    public RoverCard() { mObjectName = "card"; }

    //Getters
    public String getTitle() { return mTitle; }

    public RoverView getListViewDefinition() {

        if(mViewDefinitions!= null) {
            for(RoverView viewDefinition : mViewDefinitions) {
                if(viewDefinition.getType().equals(RoverConstants.VIEW_DEF_TYPE_LIST)) {
                    return viewDefinition;
                }
            }
        }
        return null;
    }
}
