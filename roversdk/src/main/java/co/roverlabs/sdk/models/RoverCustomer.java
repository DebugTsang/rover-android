package co.roverlabs.sdk.models;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverCustomer extends RoverObject {
    
    //JSON members
    @SerializedName("customerId") private String mId;
    
    //Constructor
    public RoverCustomer(Context con) { 
        
        super(con);
        mObjectName = "customer"; 
    }
    
    //Getters
    public String getId() { return mId; }
    
    //Setters
    public void setId(String id) { mId = id; }
}