package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverOrganization extends RoverObject {
    
    //JSON members
    @SerializedName("meta") private Object mMetaData;
    @SerializedName("title") private String mTitle;
    
    //Constructor
    public RoverOrganization() { mObjectName = "organization"; }
    
    //Getters
    public Object getMetaData() { return mMetaData; }
    public String getTitle() { return mTitle; }
    
    //Setters
    public void setMetaData(Object metaData) { mMetaData = metaData; }
    public void setTitle(String title) { mTitle = title; }
}
