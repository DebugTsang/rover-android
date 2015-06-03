package co.roverlabs.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SherryYang on 2015-02-20.
 */
public class RoverCustomer extends RoverObject {
    
    //JSON members
    @SerializedName("customerId") private String mId;
    @SerializedName("name") private String mName;
    @SerializedName("email") private String mEmail;
    @SerializedName("traits") private Map<String, Object> mTraits;

    //Constructor
    public RoverCustomer() {

        mObjectName = "customer";
        mTraits = new HashMap<>();
    }
    
    //Getters
    public String getId() { return mId; }
    public String getName() { return mName; }
    public String getEmail() { return mEmail; }
    public Map<String, Object> getTraits() { return mTraits; }
    
    //Setters
    public void setId(String id) { mId = id; }
    public void setName(String name) { mName = name; }
    public void setEmail(String email) { mEmail = email; }
    public void setTraits(Map<String, Object> traits) { mTraits = traits; }

    public void addTraits(String key, Object value) {

        if(mTraits == null) {
            mTraits = new HashMap<>();
        }
        mTraits.put(key, value);
    }
}
