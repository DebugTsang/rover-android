package co.roverlabs.sdk.events;

import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-03-07.
 */
public class RoverNotificationEvent {

    private int mId;
    private String mTitle; 
    private String mMessage;
    private Class mIntentClass;
    private String mAction;
    
    public RoverNotificationEvent(int id, String title, String message, Class intentClass) {
        
        mId = id;
        mTitle = title;
        mIntentClass = intentClass;
        mAction = RoverConstants.NOTIFICATION_ACTION_SEND;
        if(message == null) {
            mMessage = "";
        }
        else {
            mMessage = message;
        }
    }

    public RoverNotificationEvent(String action) {

        mAction = action;
    }
    
    public int getId() { return mId; }
    public String getTitle() { return mTitle; }
    public String getMessage() { return mMessage; }
    public Class getIntentClass() { return mIntentClass; }
    public String getAction() { return mAction; }
}
