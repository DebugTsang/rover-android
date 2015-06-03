package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.roverlabs.sdk.utils.Constants;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class Card extends Object {

    //JSON members
    @SerializedName("title") private String mTitle;
    @SerializedName("views") private List<View> mViews;

    //Local members
    public static final String TAG = Card.class.getSimpleName();
    private boolean mViewed;
    private boolean mDismissed;
    
    //Constructor
    public Card() {

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

    public View getListView() {

        if(mViews!= null) {
            for(View view : mViews) {
                if(view.getType().equals(Constants.VIEW_TYPE_LIST)) {
                    return view;
                }
            }
        }
        return null;
    }

    public View getDetailView(String id) {

        if(mViews!= null) {
            for(View view : mViews) {
                if(view.getType().equals(Constants.VIEW_TYPE_DETAIL)) {
                    if(view.getId().equals(id)) {
                        return view;
                    }
                }
            }
        }
        return null;
    }
}
