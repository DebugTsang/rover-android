package co.roverlabs.sdk.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.squareup.otto.Subscribe;

import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverNotificationEvent;
import co.roverlabs.sdk.ui.PicassoUtils;
import co.roverlabs.sdk.utilities.RoverConstants;

/**
 * Created by SherryYang on 2015-01-26.
 */
public class RoverNotificationManager {
    
    public static final String TAG = RoverNotificationManager.class.getSimpleName();
    private static RoverNotificationManager sNotificationManagerInstance;
    private Context mContext;
    private NotificationManager mNotificationManager;
    private int mNotificationIconId;
    
    private RoverNotificationManager(Context con) {

        mContext = con;
        mNotificationManager = (NotificationManager)con.getSystemService(Context.NOTIFICATION_SERVICE);
        RoverEventBus.getInstance().register(this);
    }

    public static RoverNotificationManager getInstance(Context con) {

        if(sNotificationManagerInstance == null) {
            sNotificationManagerInstance = new RoverNotificationManager(con);
        }
        return sNotificationManagerInstance;
    }
    
    public void setNotificationIconId(int id) { mNotificationIconId = id; }

    @Subscribe
    public void sendNotification(RoverNotificationEvent event) {

        if(event.getAction().equals(RoverConstants.NOTIFICATION_ACTION_CANCEL)) {
            return;
        }

        Intent intent = new Intent(mContext, event.getIntentClass());
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(mNotificationIconId)
                .setContentTitle(event.getTitle())
                .setContentText(event.getMessage())
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(event.getMessage()))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        mNotificationManager.notify(event.getId(), notificationBuilder.build());
    }

    @Subscribe
    public void cancelNotification(RoverNotificationEvent event) {

        if(event.getAction().equals(RoverConstants.NOTIFICATION_ACTION_SEND)) {
            return;
        }

        mNotificationManager.cancelAll();
    }
}
