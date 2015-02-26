package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public abstract class RoverModel {

    //JSON members
    @SerializedName("id") protected String mId;
    
    //Local members
    protected String mModelName;
    
    //Getters
    public String getId() { return mId; }
    public String getModelName() { return mModelName; }
    
    //Setters
    public void setId(String id) { mId = id; }
}
