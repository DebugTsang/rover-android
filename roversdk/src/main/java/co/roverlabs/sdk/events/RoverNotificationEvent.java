package co.roverlabs.sdk.events;

import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-03-07.
 */
public class RoverNotificationEvent {

    private int mId;
    private String mTitle; 
    private String mMessage;
    private String mAction;
    
    public RoverNotificationEvent(int id, String title, String message) {
        
        mId = id;
        mTitle = title;
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
    public String getAction() { return mAction; }
}
