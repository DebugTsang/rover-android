package co.roverlabs.sdk.events;

/**
 * Created by SherryYang on 2015-03-07.
 */
public class RoverNotificationEvent {

    private Integer mId;
    private String mTitle; 
    private String mMessage;
    private Class mIntentClass;
    
    public RoverNotificationEvent(Integer id, String title, String message, Class intentClass) {
        
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
    
    public Integer getId() { return mId; }
    public String getTitle() { return mTitle; }
    public String getMessage() { return mMessage; }
    public Class getIntentClass() { return mIntentClass; }

    @Override
    public boolean equals(Object o) {

        if(this == o) {
            return true;
        }
        if(o == null) {
            return false;
        }
        if(getClass() != o.getClass()) {
            return false;
        }

        RoverNotificationEvent notificationEvent = (RoverNotificationEvent)o;

        if(mId != null ? !mId.equals(notificationEvent.getId()) : notificationEvent.getId() != null) {
            return false;
        }

        return true;
    }
}
