package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverCustomer extends RoverModel {
    
    @SerializedName("customerId") private String mId;
    
    public String getId() { return mId; }
    
    public void setId(String id) { mId = id; }
}
