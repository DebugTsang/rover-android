package co.roverlabs.sdk.managers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverNotificationEvent;
import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-01-26.
 */
public class RoverNotificationManager {
    
    public static final String TAG = RoverNotificationManager.class.getSimpleName();
    private static RoverNotificationManager sNotificationManagerInstance;
    private Context mContext;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;
    private int mNotificationIconId;
    private List<RoverNotificationEvent> mNotificationEvents;
    
    private RoverNotificationManager(Context con) {

        mContext = con;
        mNotificationManager = (NotificationManager)con.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationEvents = new ArrayList<>();
        RoverEventBus.getInstance().register(this);
    }

    public static RoverNotificationManager getInstance(Context con) {

        if(sNotificationManagerInstance == null) {
            sNotificationManagerInstance = new RoverNotificationManager(con);
        }
        return sNotificationManagerInstance;
    }
    
    public List<RoverNotificationEvent> getNotificationEvents() { return mNotificationEvents; }

    public void setNotificationIconId(int id) { mNotificationIconId = id; }

    public void addNotificationEvent(RoverNotificationEvent notificationEvent) { mNotificationEvents.add(notificationEvent); }

    public void clearNotificationEvents() { mNotificationEvents.clear(); }
    
    @Subscribe
    public void sendNotification(RoverNotificationEvent event) {

        Intent intent = new Intent(mContext, event.getIntentClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        mNotificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(mNotificationIconId)
                .setContentTitle(event.getTitle())
                .setContentText(event.getMessage())
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(event.getMessage()))
                .setGroup(RoverConstants.GROUP_KEY_TOUCHPOINTS)
                .setAutoCancel(true);
        
        if(mNotificationEvents.size() > 1) {
            String summaryText = String.valueOf(mNotificationEvents.size()) + " new messages";
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .setBigContentTitle("Rover Demo")
                    .setSummaryText(summaryText);
            for(RoverNotificationEvent notificationEvent : mNotificationEvents) {
                String title = notificationEvent.getTitle();
                String message = notificationEvent.getMessage();
                SpannableString notificationSpannable = new SpannableString(title + " " + message);
                notificationSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), 0);
                inboxStyle.addLine(notificationSpannable);
            }
            mNotificationBuilder.setStyle(inboxStyle);
        }
        
        mNotificationManager.notify(RoverConstants.TOUCHPOINT_NOTIFICATION_ID, mNotificationBuilder.build());
    }
}
