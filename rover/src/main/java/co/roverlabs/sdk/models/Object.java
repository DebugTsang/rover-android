package co.roverlabs.sdk.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SherryYang on 2015-02-20.
 */
public abstract class Object {

    //JSON members
    @SerializedName("id") protected String mId;
    @SerializedName("meta") protected Map<String, java.lang.Object> mMeta;
    
    //Local members
    public static final String TAG = Object.class.getSimpleName();
    protected String mObjectName;
//    protected transient RoverNetworkManager mNetworkManager;
    
    //Constructor
    public Object() {

//        mNetworkManager = RoverNetworkManager.getInstance();
        mMeta = new HashMap<>();
    }
    
    //Getters
    public String getId() { return mId; }
    public String getObjectName() { return mObjectName; }
    public Map<String, java.lang.Object> getMeta() { return mMeta; }
    
    //Setters
    public void setId(String id) { mId = id; }
    public void setMeta(Map<String, java.lang.Object> meta) { mMeta = meta; }

    
//    public void save(final RoverObjectSaveListener objectSaveListener) {
//        // instead of this, broadcast event: RoverVisitNeedsValidation -> THIS ()RoverVisit)
//        final Object self = this;
//
//        mNetworkManager.sendObjectSaveRequest(this, new RoverNetworkObjectSaveListener() {
//
//            @Override
//            public void onNetworkCallSuccess(Object object) {
//
//                if (object != null) {
//                    self.update(object);
//                }
//                objectSaveListener.onSaveSuccess();
//            }
//
//            @Override
//            public void onNetworkCallFailure() {
//
//                objectSaveListener.onSaveFailure();
//            }
//        });
//    }
    
    public void update(Object object) {
        
        mId = object.getId();
        mMeta = object.getMeta();
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if(this == o) {
            return true;
        }
        if(o == null) {
            return false;
        }
        if(getClass() != o.getClass()) {
            return false;
        }

        Object roverObject = (Object)o;

        if(mId != null ? !mId.equals(roverObject.getId()) : roverObject.getId() != null) {
            return false;
        }

        return true;
    }
}
