package co.roverlabs.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class Organization extends Object {
    
    //JSON members
    @SerializedName("title") private String mTitle;
    
    //Constructor
    public Organization() { mObjectName = "organization"; }
    
    //Getters
    public String getTitle() { return mTitle; }

    //Setters
    public void setTitle(String title) { mTitle = title; }
}