package co.roverlabs.sdk.model;

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

    //Local members
    public static final String TAG = RoverCard.class.getSimpleName();
    private boolean mViewed;
    private boolean mDismissed;
    
    //Constructor
    public RoverCard() {

        mObjectName = "card";
        mViewed = false;
        mDismissed = false;
    }

    public boolean hasBeenViewed() { return mViewed; }
    public boolean hasBeenDismissed() { return mDismissed; }

    public void setViewed(boolean viewed) { mViewed = viewed; }
    public void setDismissed(boolean dismissed) { mDismissed = dismissed; }

    //Getters
    public String getTitle() { return mTitle; }

    public RoverView getListView() {

        if(mViews!= null) {
            for(RoverView view : mViews) {
                if(view.getType().equals(RoverConstants.VIEW_TYPE_LIST)) {
                    return view;
                }
            }
        }
        return null;
    }

    public RoverView getDetailView(String id) {

        if(mViews!= null) {
            for(RoverView view : mViews) {
                if(view.getType().equals(RoverConstants.VIEW_TYPE_DETAIL)) {
                    if(view.getId().equals(id)) {
                        return view;
                    }
                }
            }
        }
        return null;
    }
}
