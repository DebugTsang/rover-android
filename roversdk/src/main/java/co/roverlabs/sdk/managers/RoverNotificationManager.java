package co.roverlabs.sdk.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.squareup.otto.Subscribe;

import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverNotificationEvent;
import co.roverlabs.sdk.ui.CardListActivity;
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


    public void showStickyNotification(int id, String title, String message) {

        Intent intent = new Intent(mContext, CardListActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(mNotificationIconId)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(false);

        Notification n;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            n = builder.build();
        } else {
            n = builder.getNotification();
        }

        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(id, n);
    }

    @Subscribe
    public void cancelNotification(RoverNotificationEvent event) {

        if(event.getAction().equals(RoverConstants.NOTIFICATION_ACTION_SEND)) {
            return;
        }

        //mNotificationManager.cancelAll();
    }
}
