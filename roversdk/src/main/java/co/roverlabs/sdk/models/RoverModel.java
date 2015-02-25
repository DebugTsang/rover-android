package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public abstract class RoverModel {

    @SerializedName("id") protected String mId;
    
    public String getId() { return mId; }
    
    public void setId(String id) { mId = id; }
}
