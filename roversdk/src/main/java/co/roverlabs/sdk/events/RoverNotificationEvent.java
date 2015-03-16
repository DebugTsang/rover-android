package co.roverlabs.sdk.events;

/**
 * Created by SherryYang on 2015-03-07.
 */
public class RoverNotificationEvent {

    private int mId;
    private String mTitle; 
    private String mMessage;
    private Class mIntentClass;
    
    public RoverNotificationEvent(int id, String title, String message, Class intentClass) {
        
        mId = id;
        mTitle = title;
        mIntentClass = intentClass;
        if(message == null) {
            mMessage = "";
        }
        else {
            mMessage = message;
        }
    }
    
    public int getId() { return mId; }
    public String getTitle() { return mTitle; }
    public String getMessage() { return mMessage; }
    public Class getIntentClass() { return mIntentClass; }
}
