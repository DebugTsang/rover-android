package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverOrganization extends RoverObject {
    
    //JSON members
    @SerializedName("title") private String mTitle;
    
    //Constructor
    public RoverOrganization() { mObjectName = "organization"; }
    
    //Getters
    public String getTitle() { return mTitle; }

    //Setters
    public void setTitle(String title) { mTitle = title; }
}
