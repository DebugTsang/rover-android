package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverOrganization extends RoverModel {
    
    @SerializedName("meta") private Object mMetaData;
    @SerializedName("titla") private String mTitle;
    
    public Object getMetaData() { return mMetaData; }
    public String getTitle() { return mTitle; }
    
    public void setMetaData(Object metaData) { mMetaData = metaData; }
    public void setTitle(String title) { mTitle = title; }
}
