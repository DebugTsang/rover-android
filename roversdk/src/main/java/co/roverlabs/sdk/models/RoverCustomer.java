package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverCustomer extends RoverModel {
    
    //JSON members
    @SerializedName("customerId") private String mId;
    
    //Constructor
    public RoverCustomer() { mModelName = "customer"; }
    
    //Getters
    public String getId() { return mId; }
    
    //Setters
    public void setId(String id) { mId = id; }
}
